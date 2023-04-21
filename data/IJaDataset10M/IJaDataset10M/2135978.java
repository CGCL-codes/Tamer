package com.rapidminer.operator.features.transformation;

import java.io.Serializable;

/**
 * This class holds information about one eigenvector and eigenvalue.
 *  
 * @author Ingo Mierswa
 */
public interface ComponentVector extends Comparable<ComponentVector>, Serializable {

    public double[] getVector();

    public double getEigenvalue();
}
