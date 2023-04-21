package com.mysolution.core.digitrecognizer.neuralnetwork;

import java.io.Serializable;

/**
 * @author Dmitry Kudryavtsev
 *         Date: 26.12.2009
 *         Time: 23:31:05
 */
public class NeuroDendriteWeight implements Serializable {

    public NeuroDendriteWeight(double value) {
        this.value = value;
    }

    public double value;

    public double error = 0;

    public double diagHessian = 0;
}
