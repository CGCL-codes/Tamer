package edu.collablab.brenthecht.wikapidia.math;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import jsc.correlation.SpearmanCorrelation;
import jsc.datastructures.PairedData;

public class Correlation {

    private double[] x;

    private double[] y;

    public Correlation(Vector<Double> vX, Vector<Double> vY) {
        int i;
        int nonNulls = 0;
        for (i = 0; i < vX.size(); i++) {
            if (vX.get(i) != null && vY.get(i) != null) {
                nonNulls++;
            }
        }
        x = new double[vX.size()];
        y = new double[vY.size()];
        int curIndex = 0;
        for (i = 0; i < vX.size(); i++) {
            if (vX.get(i) != null && vY.get(i) != null) {
                x[curIndex] = vX.get(i);
                y[curIndex] = vY.get(i);
                curIndex++;
            }
        }
    }

    public HashMap<String, Double> getSpearmansCoefficient() {
        PairedData pd = new PairedData(x, y);
        HashMap<String, Double> rVal = new HashMap<String, Double>();
        SpearmanCorrelation sc = new SpearmanCorrelation(pd);
        rVal.put("s", sc.getS());
        rVal.put("p", sc.getSP());
        rVal.put("n", (double) x.length);
        return rVal;
    }
}
