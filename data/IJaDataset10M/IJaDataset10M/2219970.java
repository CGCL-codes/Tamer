package eplverb.firsttest;

public class Lit extends Exp {

    int value;

    public Lit setLit(int n) {
        value = n;
        return this;
    }

    public String toString() {
        return value + "";
    }

    public int eval() {
        return value;
    }
}
