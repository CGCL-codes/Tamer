package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.MathUnsupportedOperationException;

/**
 * A default concrete implementation of the abstract class
 * {@link IterativeLinearSolverEvent}.
 *
 * @version $Id: DefaultIterativeLinearSolverEvent.java 1244107 2012-02-14 16:17:55Z erans $
 */
public class DefaultIterativeLinearSolverEvent extends IterativeLinearSolverEvent {

    /** */
    private static final long serialVersionUID = 20120129L;

    /** The right-hand side vector. */
    private final RealVector b;

    /** The current estimate of the residual. */
    private final RealVector r;

    /** The current estimate of the norm of the residual. */
    private final double rnorm;

    /** The current estimate of the solution. */
    private final RealVector x;

    /**
     * Creates a new instance of this class. This implementation does
     * <em>not</em> deep copy the specified vectors {@code x}, {@code b},
     * {@code r}. Therefore the user must make sure that these vectors are
     * either unmodifiable views or deep copies of the same vectors actually
     * used by the {@code source}. Failure to do so may compromise subsequent
     * iterations of the {@code source}. If the residual vector {@code r} is
     * {@code null}, then {@link #getResidual()} throws a
     * {@link MathUnsupportedOperationException}, and
     * {@link #providesResidual()} returns {@code false}.
     *
     * @param source the iterative solver which fired this event
     * @param iterations the number of iterations performed at the time
     * {@code this} event is created
     * @param x the current estimate of the solution
     * @param b the right-hand side vector
     * @param r the current estimate of the residual (can be {@code null})
     * @param rnorm the norm of the current estimate of the residual
     */
    public DefaultIterativeLinearSolverEvent(final Object source, final int iterations, final RealVector x, final RealVector b, final RealVector r, final double rnorm) {
        super(source, iterations);
        this.x = x;
        this.b = b;
        this.r = r;
        this.rnorm = rnorm;
    }

    /**
     * Creates a new instance of this class. This implementation does
     * <em>not</em> deep copy the specified vectors {@code x}, {@code b}.
     * Therefore the user must make sure that these vectors are either
     * unmodifiable views or deep copies of the same vectors actually used by
     * the {@code source}. Failure to do so may compromise subsequent iterations
     * of the {@code source}. Callling {@link #getResidual()} on instances
     * returned by this constructor throws a
     * {@link MathUnsupportedOperationException}, while
     * {@link #providesResidual()} returns {@code false}.
     *
     * @param source the iterative solver which fired this event
     * @param iterations the number of iterations performed at the time
     * {@code this} event is created
     * @param x the current estimate of the solution
     * @param b the right-hand side vector
     * @param rnorm the norm of the current estimate of the residual
     */
    public DefaultIterativeLinearSolverEvent(final Object source, final int iterations, final RealVector x, final RealVector b, final double rnorm) {
        super(source, iterations);
        this.x = x;
        this.b = b;
        this.r = null;
        this.rnorm = rnorm;
    }

    /** {@inheritDoc} */
    @Override
    public double getNormOfResidual() {
        return rnorm;
    }

    /**
     * {@inheritDoc}
     *
     * This implementation throws an {@link MathUnsupportedOperationException}
     * if no residual vector {@code r} was provided at construction time.
     */
    public RealVector getResidual() {
        if (r != null) {
            return r;
        }
        throw new MathUnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public RealVector getRightHandSideVector() {
        return b;
    }

    /** {@inheritDoc} */
    @Override
    public RealVector getSolution() {
        return x;
    }

    /**
     * {@inheritDoc}
     *
     * This implementation returns {@code true} if a non-{@code null} value was
     * specified for the residual vector {@code r} at construction time.
     *
     * @return {@code true} if {@code r != null}
     */
    @Override
    public boolean providesResidual() {
        return r != null;
    }
}
