package com.dhb.interfaces;

/**
 * This type was created in VisualAge.
 */
public interface ParametrizedOneVariableFunction extends OneVariableFunction {

    /**
 * @return double[]	array containing the parameters
 */
    double[] parameters();

    /**
 * @param p double[]	assigns the parameters
 */
    void setParameters(double[] p);

    /**
 * This method was created in VisualAge.
 * @return double[]
 * @param x double
 */
    double[] valueAndGradient(double x);
}
