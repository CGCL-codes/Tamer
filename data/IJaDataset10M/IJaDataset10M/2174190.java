package de.grogra.blocks.xFrogFileParser;

public class Transform extends Expr {

    public Transform(Expr a, Expr b, Expr c) {
        this.a = a;
        this.b = b;
        this.c = c;
        oldTransform = aktTransform;
        aktTransform = this;
        if (debug) System.out.println(getClass().getSimpleName() + " :: " + a);
    }

    @Override
    public String toString() {
        if (debugS) System.out.println("TS  " + getClass().getSimpleName());
        return "Transform {\n" + a.toString() + "\n" + b.toString() + "\n" + c.toString() + "\n" + "}\n";
    }
}
