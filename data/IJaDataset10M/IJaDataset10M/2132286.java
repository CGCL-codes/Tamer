package jgrx.iface.impl;

import jgrx.iface.Parametric;
import jgrx.iface.Term;

/**
 *
 * @author ellery
 */
public class MyParametric implements Parametric {

    Term x, y;

    double min, max, step;

    /** Creates a new instance of MyParametric */
    public MyParametric(Term x, Term y, double min, double max, double step) {
        this.x = x;
        this.y = y;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public Term x() {
        return x;
    }

    public Term y() {
        return y;
    }

    public double tMin() {
        return min;
    }

    public double tMax() {
        return max;
    }

    public double tStep() {
        return step;
    }
}
