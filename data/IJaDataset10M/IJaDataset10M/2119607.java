package org.jquantlib.math.functions;

import org.jquantlib.math.Ops;

/**
 * A binary subtraction function
 *
 * @author Richard Gomes
 */
public final class Minus implements Ops.BinaryDoubleOp {

    @Override
    public double op(final double a, final double b) {
        return a - b;
    }
}
