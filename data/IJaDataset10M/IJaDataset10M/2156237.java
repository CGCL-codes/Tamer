package iclab.filtering.discretization.unsupervised;

import iclab.core.ICAttribute.ICAttType;
import iclab.core.ICData;
import iclab.exceptions.ICParameterException;
import iclab.filtering.ICDataFilter;
import iclab.filtering.discretization.ICBlindDiscretizer;
import iclab.filtering.discretization.ICDiscretizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ICEqualFrequency extends ICDiscretizer implements ICDataFilter {

    private int _numIntervals;

    public ICEqualFrequency(int numIntervals, int[] variables) throws ICParameterException {
        int i;
        if (numIntervals <= 1) {
            throw new ICParameterException("Invalid number of intervals.");
        } else {
            _policy = new HashMap<Double, ArrayList<Double>>();
            _numIntervals = numIntervals;
            _Variables = new int[variables.length];
            for (i = 0; i < variables.length; i++) {
                _Variables[i] = variables[0];
            }
        }
    }

    double nextPoint(double[] data, int idx) {
        int i;
        int size;
        double point;
        double nPoint;
        boolean cont;
        point = data[idx];
        cont = true;
        size = data.length;
        i = idx + 1;
        while (i < size && cont == true) {
            if (data[idx] != data[i]) {
                cont = false;
            } else {
                i = i + 1;
            }
        }
        nPoint = data[i];
        return nPoint;
    }

    public ArrayList<Double> estimateCutPoints(double[] data) {
        ArrayList<Double> cutPoints;
        int numCutPoints;
        int delta;
        int i;
        int idx;
        double cp;
        int numInstances;
        double[] _data;
        idx = 0;
        cp = Double.MIN_VALUE;
        numInstances = data.length;
        numCutPoints = 0;
        cutPoints = new ArrayList<Double>();
        _data = new double[numInstances];
        for (i = 0; i < numInstances; i++) {
            _data[i] = data[i];
        }
        Arrays.sort(_data);
        numCutPoints = _numIntervals - 1;
        delta = numInstances / _numIntervals;
        for (i = 0; i < numCutPoints; i++) {
            idx = (i + 1) * delta;
            cp = _data[idx];
            if (!cutPoints.contains(new Double(cp))) {
                cutPoints.add(Double.valueOf(cp));
            }
        }
        return cutPoints;
    }

    public ICData filter(ICData data) throws ICParameterException {
        ICBlindDiscretizer discretizer;
        ICData discData;
        int numVariables;
        int numInstances;
        double[] vector;
        int i;
        int j;
        int k;
        ArrayList<Double> cutPointList;
        cutPointList = new ArrayList<Double>();
        numInstances = data.numInstances();
        if (!isValid(numInstances, _numIntervals)) {
            throw new ICParameterException("Number of instances is not enough!!");
        }
        numVariables = data.numAttributes();
        vector = new double[numInstances];
        for (i = 0; i < numVariables; i++) {
            if (isSelected(i)) {
                if (data.attribute(i).getType() == ICAttType.numeric) {
                    for (j = 0; j < numInstances; j++) {
                        vector[j] = data.get(i, j);
                    }
                    _policy.put(new Double(i), estimateCutPoints(vector));
                } else {
                    throw new ICParameterException("Only numeric variables can be discretized.");
                }
            }
        }
        discretizer = new ICBlindDiscretizer(_policy);
        discData = new ICData(discretizer.filter(data));
        return discData;
    }
}
