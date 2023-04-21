package org.processmining.mining.logabstraction;

import org.processmining.framework.log.LogEvents;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

/**
 * @author not attributable
 * @version 1.0
 */
public interface LogRelations {

    public DoubleMatrix2D getCausalFollowerMatrix();

    public DoubleMatrix2D getParallelMatrix();

    public DoubleMatrix1D getEndInfo();

    public DoubleMatrix1D getStartInfo();

    public DoubleMatrix1D getOneLengthLoopsInfo();

    public int getNumberElements();

    public LogEvents getLogEvents();
}
