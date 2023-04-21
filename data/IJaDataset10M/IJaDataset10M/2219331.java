package de.grogra.blocks.xFrogFileParser;

public class Struct_Mut_Info extends Expr {

    public Struct_Mut_Info(Expr a, Expr b, Expr c) {
        this.a = a;
        this.b = b;
        this.c = c;
        if (debug) System.out.println(getClass().getSimpleName() + " :: " + a);
    }

    @Override
    public String toString() {
        if (debugS) System.out.println("TS  " + getClass().getSimpleName());
        return "Struct_Mut_Info {\n" + a.toString() + "\n" + b.toString() + "\n" + c.toString() + "\n" + "}\n";
    }
}
