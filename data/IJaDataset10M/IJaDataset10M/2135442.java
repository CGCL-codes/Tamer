package org.maze.behaviours;

/**
 *
 * @author Thomas
 */
public abstract class AbstractFitnessBehaviour implements IFitnessBehaviour {

    protected double a;

    public void setA(double a) {
        assert a >= 0 && a <= 1;
        this.a = a;
    }
}
