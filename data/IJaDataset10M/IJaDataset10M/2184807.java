package alice.tuprologx.pj.test;

import alice.tuprologx.pj.model.*;
import alice.tuprologx.pj.annotations.*;
import alice.tuprologx.pj.engine.*;

@Termifiable(predicate = "pair")
public class Pair {

    public Term x;

    public void setX(Term<?> x) {
        this.x = x;
    }

    public void setY(Term<?> y) {
        this.y = y;
    }

    public Term<?> getX() {
        return x;
    }

    public Term<?> getY() {
        return y;
    }

    public Term y;

    public Pair() {
    }

    public Pair(Term<?> x, Term<?> y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "Pair[x=" + x + ";y=" + y + "]";
    }
}
