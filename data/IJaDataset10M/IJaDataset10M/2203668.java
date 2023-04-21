package org.jquantlib.math.functions;

import org.jquantlib.math.Ops;

/**
 * This method binds the 1st argument of a binary function to a scalar value, effectively enabling
 * a binary function to be called in a context intended for a unary function.
 *
 * @author Richard Gomes
 */
public final class Bind1st implements Ops.DoubleOp {

    private final double scalar;

    private final Ops.BinaryDoubleOp f;

    public Bind1st(final double scalar, final Ops.BinaryDoubleOp f) {
        this.scalar = scalar;
        this.f = f;
    }

    @Override
    public double op(final double a) {
        return f.op(scalar, a);
    }
}
