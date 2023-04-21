package Model;

/**
 * Class recording simulation statistics
 * - photon counter
 * - total recieved energy counter
 * @author Thomas
 */
public class Statistics {

    private int fotoneCount;

    private double emittedEnergy;

    private double recievedEnergy;

    public Statistics() {
        this.fotoneCount = 0;
        this.recievedEnergy = 0.0;
    }

    public static double normalDistribution(double x, double u, double o) {
        return 0.0;
    }

    public void increaseFotoneCount() {
        this.fotoneCount++;
    }

    public int getFotoneCount() {
        return this.fotoneCount;
    }

    public void addToRecievedEnergy(double energy) {
        this.recievedEnergy += energy;
    }

    public double getRecievedEnergy() {
        return this.recievedEnergy;
    }

    public void addToEmittedEnergy(double energy) {
        this.emittedEnergy += energy;
    }

    public double getEmittedEnergy() {
        return this.emittedEnergy;
    }
}
