package org.dcopolis.problem.sensor;

import java.util.*;
import java.io.*;
import org.sefirs.*;
import org.dcopolis.platform.*;

class PoissonSensor extends Sensor {

    double sleep_tolerance = 0.95;

    int NUM_PROBS = 1000;

    Random rand = null;

    boolean fire_flag = false;

    double lambda;

    Time tau;

    double eth;

    double[] probabilities = new double[NUM_PROBS];

    /**
	 * @lambda number of events occuring per tau time units
	 * @tau time (used for lambda) in ms
	 */
    public PoissonSensor(Platform platform, double lambda, Time tau) {
        super(platform);
        rand = new Random();
        this.tau = tau;
        this.lambda = lambda;
        this.eth = Math.pow(Math.E, -1 * lambda) * lambda;
    }

    /**
	*                (ln(-eth+1) + ln((-1 + rho) / eth))
	*     k*tau =  -------------------------------------------
	*                            ln(-eth + 1)
	**/
    public double computeK(double eth, double rho) {
        return ((Math.log1p(-eth) + Math.log(-1 * ((-1 + rho) / eth)))) / Math.log1p(-eth);
    }

    public void run() throws InterruptedException {
        while (true) {
            double rho = rand.nextDouble();
            double k = computeK(eth, rho);
            System.out.println("Sleeping for " + tau.multiply(k).toString() + " seconds");
            sleep(tau.multiply(k));
        }
    }

    public static void main(String[] args) {
        PoissonSensor ps = new PoissonSensor(null, 4.5, new Seconds(5));
        ps.start();
    }
}
