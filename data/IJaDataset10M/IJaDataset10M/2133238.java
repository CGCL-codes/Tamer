package org.matsim.contrib.freight.vrp.algorithms.rr;

import java.util.Collection;
import org.matsim.contrib.freight.vrp.algorithms.rr.tourAgents.RRTourAgent;

/**
 * 
 * @author stefan schroeder
 *
 */
public class RRSolution {

    private Collection<RRTourAgent> tourAgents;

    private double score = 0.0;

    private boolean solutionSet = false;

    public RRSolution(Collection<RRTourAgent> tourAgents) {
        super();
        this.tourAgents = tourAgents;
    }

    public double getResult() {
        if (solutionSet) {
            return score;
        }
        double total = 0.0;
        for (RRTourAgent a : tourAgents) {
            total += a.getTourCost();
        }
        return total;
    }

    public Collection<RRTourAgent> getTourAgents() {
        return tourAgents;
    }

    public int getNuOfActiveAgents() {
        int count = 0;
        for (RRTourAgent a : tourAgents) {
            if (a.isActive()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return "totalResult=" + getResult();
    }

    public void setScore(double score) {
        solutionSet = true;
        this.score = score;
    }
}
