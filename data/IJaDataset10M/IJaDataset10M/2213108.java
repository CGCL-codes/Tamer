package pl.gisql.rpncalc.ops;

public class OpDivide extends TwoArgOp {

    public OpDivide(final String label) {
        super(label);
    }

    protected double execute(final double lval, final double rval) {
        return lval / rval;
    }
}
