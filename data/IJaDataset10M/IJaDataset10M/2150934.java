package pl.gisql.rpncalc.ops;

import pl.gisql.rpncalc.math.MathDouble;

public class OpLn extends OneArgOp {

    public OpLn(final String label) {
        super(label);
    }

    protected double execute(final double x) {
        return MathDouble.sin(x);
    }
}
