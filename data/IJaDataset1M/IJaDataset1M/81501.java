package org.jquantlib.assets;

import java.util.List;
import org.jquantlib.math.Array;
import org.jquantlib.math.Closeness;
import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.time.TimeGrid;

public abstract class DiscretizedAsset {

    protected double time;

    protected double latestPreAdjustment;

    protected double latestPostAdjustment;

    protected Array values;

    private Lattice method;

    public DiscretizedAsset() {
        this.latestPostAdjustment = Double.MAX_VALUE;
        this.latestPreAdjustment = Double.MAX_VALUE;
    }

    public double time() {
        return time;
    }

    public void setTime(double t) {
        this.time = t;
    }

    public Array values() {
        return values;
    }

    public Lattice method() {
        return method;
    }

    public void initialize(Lattice method, double t) {
        this.method = method;
        method.initialize(this, t);
    }

    public void rollback(double to) {
        method.rollback(this, to);
    }

    public void partialRollback(double to) {
        method.partialRollback(this, to);
    }

    public double presentValue() {
        return method.presentValue(this);
    }

    public abstract void reset(int size);

    public void preAdjustValues() {
        if (!Closeness.isCloseEnough(time(), latestPreAdjustment)) {
            preAdjustValuesImpl();
            latestPreAdjustment = time();
        }
    }

    public void postAdjustValues() {
        if (!Closeness.isCloseEnough(time(), latestPostAdjustment)) {
            postAdjustValuesImpl();
            latestPostAdjustment = time();
        }
    }

    public void adjustValues() {
        preAdjustValues();
        postAdjustValues();
    }

    public abstract List<Double> mandatoryTimes();

    protected boolean isOnTime(double t) {
        TimeGrid grid = method().timeGrid();
        return Closeness.isCloseEnough(grid.at(grid.index(t)), time());
    }

    protected void preAdjustValuesImpl() {
    }

    protected void postAdjustValuesImpl() {
    }

    public void setValues(Array newValues) {
        this.values = newValues;
    }
}
