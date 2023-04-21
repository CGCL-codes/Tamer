package fakegame.flow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: drchaj1
 * Date: Nov 8, 2009
 * Time: 2:29:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Projection2DContainer {

    private final double[] x;

    private final double[] y;

    private final int[] classIndices;

    private final double energy;

    public Projection2DContainer(double[] x, double[] y, int[] classIndices, double energy) {
        this.x = x;
        this.y = y;
        this.classIndices = classIndices;
        this.energy = energy;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public int[] getClassIndices() {
        return classIndices;
    }

    public double getEnergy() {
        return energy;
    }
}
