package uk.ac.shef.wit.aleph.algorithm.graphlab.util;

import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.AbstractIterationMonitor;
import no.uib.cipr.matrix.sparse.IterativeSolverNotConvergedException;

/**
 * Simple iteration monitor. Stops the iteration when the given residual is achieved
 */
public class IterationMonitorFixedResidualAndNumber extends AbstractIterationMonitor {

    private double _targetResidual;

    private int _targetIterations;

    public IterationMonitorFixedResidualAndNumber(final double residual, final int max) {
        _targetResidual = residual;
        _targetIterations = max;
    }

    protected boolean convergedI(final double r, final Vector x) throws IterativeSolverNotConvergedException {
        return convergedI(r);
    }

    protected boolean convergedI(final double r) throws IterativeSolverNotConvergedException {
        return r <= _targetResidual || iter >= _targetIterations;
    }
}
