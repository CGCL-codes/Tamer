package de.grogra.blocks.xFrogFileParser;

public class AttractorGroup2 extends Expr {

    public AttractorGroup2(Expr a, Expr b, Expr c) {
        this.a = a;
        this.b = b;
        this.c = c;
        if (debug) System.out.println(getClass().getSimpleName() + " :: " + a);
    }

    @Override
    public String toString() {
        if (debugS) System.out.println("TS  " + getClass().getSimpleName());
        return a.toString() + "\n" + b.toString() + "\n" + c.toString() + "\n";
    }
}
