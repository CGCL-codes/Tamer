package de.grogra.blocks.xFrogFileParser;

public class FloatTripleRow1 extends Expr {

    public FloatTripleRow1(Expr a) {
        this.a = a;
        if (debug) System.out.println(getClass().getSimpleName() + " :: " + a);
    }

    @Override
    public String toString() {
        return a.toString();
    }
}
