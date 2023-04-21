package com.sollink.sat.model;

import java.util.Vector;

public class Literal {

    public Vector<Clause> watches;

    public int literal;

    public Literal(int literal) {
        this.literal = literal;
        this.watches = new Vector<Clause>();
    }

    public int push(Clause c) {
        watches.add(c);
        return watches.size();
    }

    public Vector<Clause> moveTo() {
        Vector<Clause> tmp = new Vector<Clause>(watches);
        watches.clear();
        return tmp;
    }

    public int var() {
        return Math.abs(literal);
    }

    public int literal() {
        return literal;
    }

    public String toString() {
        return "" + literal;
    }

    public void remove(Clause clause) {
        watches.remove(clause);
    }
}
