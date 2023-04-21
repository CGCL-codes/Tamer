package epl.firsttest;

public class Neg extends Exp {

    Exp expr;

    public Neg setNeg(Exp a) {
        this.expr = a;
        return this;
    }

    public int eval() {
        return -(1) * this.expr.eval();
    }

    public String toString() {
        return "-" + this.expr.toString();
    }
}
