package com.purbon.search.fair.utils;


import com.fasterxml.jackson.databind.util.JSONPObject;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.apache.commons.math3.distribution.BinomialDistribution;

import java.util.Arrays;

public class MTableGenerator {

    private int[] mTable;
    private int n;
    private int k;
    private double p;
    private double alpha;

    /**
     * @param n     Total number of elements
     * @param k     The size of the ranking
     * @param p     The proportion of protected candidates in the top-k ranking
     * @param alpha the significance level
     */
    public MTableGenerator(int n, int k, double p, double alpha) {
        if (parametersAreValid(n, k, p, alpha)) {
            this.n = n;
            this.k = k;
            this.p = p;
            this.alpha = alpha;
        } else {
            throw new IllegalArgumentException("Invalid Input Parameters for MTable calculation.");
        }
    }

    private boolean parametersAreValid(int n, int k, double p, double alpha) {
        boolean alphaIsValid = false;

        return nIsValid(n) && kIsValid(k, n) && pIsValid(p) && alphaIsValid(alpha);
    }

    private boolean nIsValid(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Parameter n must be at least 1");
        } else {
            return true;
        }
    }

    private boolean kIsValid(int k, int n) {
        if (k < 1)
            throw new IllegalArgumentException("Parameter k must be at least 1");
        else if (k > n) {
            throw new IllegalArgumentException("Parameter k must be at most n");
        } else {
            return true;
        }
    }

    private boolean pIsValid(double p) {
        if (p >= 1d || p <= 0d) {
            throw new IllegalArgumentException("Parameter p must be in ]0.0, 1.0[");
        } else {
            return true;
        }
    }

    private boolean alphaIsValid(double alpha) {
        if (alpha <= 0d || alpha >= 1d) {
            throw new IllegalArgumentException("Parameter alpha must be in ]0.0, 1.0[");
        } else {
            return true;
        }
    }

    private int[] computeMTable() {
        int[] table = new int[this.n + 1];
        table[0] = 0;
        for (int i = 1; i < this.n + 1; i++) {
            table[i] = m(i);
        }
        return table;
    }

    private Integer m(int k) {

        BinomialDistribution dist = new BinomialDistribution(k, p);
        return dist.inverseCumulativeProbability(alpha);
    }

    public int[] getMTable() {
        if (this.mTable == null) {
            this.mTable = computeMTable();
        }
        return mTable;
    }

    public String toJSONObjectString(){
       String json ="{\"mTable\" : [";
        for(int i=0; i<this.getMTable().length; i++){
            json+="\"";
            json+=this.getMTable()[i];
            if(i+1<this.getMTable().length) {
                json += "\",";
            }else{
                json+="\"";
            }
        }
        json+="]}";

        return json;
    }

    public int getK() {
        return k;
    }

    public int getN() {
        return n;
    }

    public double getP() {
        return p;
    }

    public double getAlpha() {
        return alpha;
    }

}
