package com.platonov.network.core.input;

import java.io.Serializable;

/**
 * ����� ���������
 * <p/>
 * User: Platonov
 */
public class SumSqr extends SummingFunction implements Serializable {

    private static final long serialVersionUID = 2L;

    public double getOutput(double[] inputVector) {
        double sum = 0d;
        for (double input : inputVector) {
            sum += (input * input);
        }
        return sum;
    }
}
