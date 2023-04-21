package org.stumeikle.NeuroCoSA.AutoCluster;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class NScalar implements NMeasurable, Cloneable {

    double v;

    public NScalar() {
    }

    public NScalar(double d) {
        v = d;
    }

    public double distanceTo(NMeasurable p) {
        return distanceTo((NScalar) p);
    }

    public double distanceSquaredTo(NMeasurable p) {
        return distanceSquaredTo((NScalar) p);
    }

    public void add(NMeasurable p) {
        add((NScalar) p);
        return;
    }

    public void subtract(NMeasurable p) {
        subtract((NScalar) p);
        return;
    }

    public double magnitude() {
        return v;
    }

    public double dotProduct(NMeasurable p) {
        return dotProduct((NScalar) p);
    }

    public NMeasurable createNewZero() {
        NScalar s = new NScalar();
        s.v = 0.0;
        return (NMeasurable) s;
    }

    public void divideByScalar(double d) {
        if (d > 0.0001 || d < -0.001) {
            v /= d;
        }
    }

    public void multiplyByScalar(double d) {
        v *= d;
    }

    public double distanceTo(NScalar p) {
        double dx;
        dx = p.v - v;
        return Math.sqrt(dx * dx);
    }

    public double distanceSquaredTo(NScalar p) {
        double dx;
        dx = p.v - v;
        return dx * dx;
    }

    public void add(NScalar p) {
        v += p.v;
    }

    public void subtract(NScalar p) {
        v -= p.v;
    }

    public double dotProduct(NScalar p) {
        return v * p.v;
    }

    public double getValue() {
        return v;
    }

    public void setValue(double d) {
        v = d;
    }

    public NMeasurable createCopy() {
        try {
            return (NMeasurable) this.clone();
        } catch (Exception e) {
            return null;
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (Exception e) {
            return null;
        }
    }
}
