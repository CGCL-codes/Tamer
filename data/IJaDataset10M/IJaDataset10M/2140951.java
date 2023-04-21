package de.jlab.boards;

public interface SweepListener {

    public abstract void setValue(double freq, double db);

    public abstract void finished();
}
