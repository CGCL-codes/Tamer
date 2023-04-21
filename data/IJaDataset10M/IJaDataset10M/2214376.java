package edu.unika.aifb.rules.combination;

import org.semanticweb.kaon2.api.owl.elements.DataProperty;
import org.semanticweb.kaon2.api.owl.elements.Individual;
import org.semanticweb.kaon2.api.owl.elements.OWLClass;
import org.semanticweb.kaon2.api.owl.elements.ObjectProperty;

/**
 * Weights have been set by an ontology expert. In detail not linear
 * weights are used, but a sigmoid function is applied. In general
 * this returns very good results.
 * 
 * @author Marc Ehrig
 */
public class ManualWeightsLinear implements Combination {

    private static final int MAX = 200;

    private double[] value = new double[MAX];

    private double result = 0;

    private int total = 0;

    double weights[] = new double[MAX];

    Object object1;

    Object object2;

    public ManualWeightsLinear(int totalT) {
        weights[0] = 2.0;
        weights[1] = 5.0;
        weights[2] = 1.0;
        weights[3] = 1.0;
        weights[4] = 1.0;
        weights[5] = 1.0;
        weights[6] = 1.0;
        weights[7] = 1.0;
        weights[8] = -1.0;
        weights[9] = -1.0;
        weights[10] = 1.0;
        weights[11] = 1.0;
        weights[12] = 1.0;
        weights[13] = 1.0;
        weights[14] = 1.0;
        weights[15] = 1.0;
        weights[16] = 1.0;
        weights[17] = 1.0;
        weights[18] = 1.0;
        weights[19] = 1.0;
        weights[20] = 1.0;
        weights[21] = 1.0;
        weights[22] = 1.0;
        total = totalT;
    }

    public double getValue(int index) {
        return value[index];
    }

    public void process() {
        if ((object1 instanceof OWLClass) && (object2 instanceof OWLClass)) {
            for (int i = 0; i <= 9; i++) {
                double calc = weights[i] * value[i];
                if (value[i] < 0) calc = 0.0;
                result = result + calc;
            }
            result = result / 6.0;
            if (result > 1) result = 1;
            if (result < 0) result = 0;
        } else if ((object1 instanceof DataProperty) && (object2 instanceof DataProperty)) {
            for (int i = 0; i < 2; i++) {
                double calc = weights[i] * value[i];
                if (value[i] < 0) calc = 0.0;
                result = result + calc;
            }
            for (int i = 10; i <= 13; i++) {
                double calc = weights[i] * value[i];
                if (value[i] < 0) calc = 0.0;
                result = result + calc;
            }
            result = result / 6.0;
            if (result > 1) result = 1;
            if (result < 0) result = 0;
        } else if ((object1 instanceof ObjectProperty) && (object2 instanceof ObjectProperty)) {
            for (int i = 0; i < 2; i++) {
                double calc = weights[i] * value[i];
                if (value[i] < 0) calc = 0.0;
                result = result + calc;
            }
            for (int i = 14; i <= 18; i++) {
                double calc = weights[i] * value[i];
                if (value[i] < 0) calc = 0.0;
                result = result + calc;
            }
            result = result / 6.0;
            if (result > 1) result = 1;
            if (result < 0) result = 0;
        } else if ((object1 instanceof Individual) && (object2 instanceof Individual)) {
            for (int i = 0; i < 2; i++) {
                double calc = weights[i] * value[i];
                if (value[i] < 0) calc = 0.0;
                result = result + calc;
            }
            for (int i = 19; i <= 22; i++) {
                double calc = weights[i] * value[i];
                if (value[i] < 0) calc = 0.0;
                result = result + calc;
            }
            result = result / 6.0;
            if (result > 1) result = 1;
            if (result < 0) result = 0;
        }
    }

    public double result() {
        return result;
    }

    public void reset() {
        for (int i = 0; i < MAX; i++) {
            value[i] = -1.0;
        }
        result = 0;
        object1 = null;
        object2 = null;
    }

    public void setValue(int index, double valueToSet) {
        value[index] = valueToSet;
    }

    public void setObjects(Object object1T, Object object2T) {
        object1 = object1T;
        object2 = object2T;
    }

    public double[] getAddInfo() {
        double[] addInfo = new double[total];
        for (int i = 0; i < total; i++) {
            addInfo[i] = value[i];
        }
        return addInfo;
    }
}
