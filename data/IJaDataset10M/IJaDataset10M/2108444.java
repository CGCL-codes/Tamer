package org.jbox2d.dynamics;

/**
 * A holder for time step information.
 */
public class TimeStep {

    public float dt;

    public float inv_dt;

    public float dtRatio;

    public boolean warmStarting;

    public boolean positionCorrection;

    public int maxIterations;
}
