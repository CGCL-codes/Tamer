package edu.mit.lcs.haystack.server.extensions.infoextraction.rec;

import edu.mit.lcs.haystack.server.extensions.infoextraction.Utilities;

/**
 * @author yks
 */
public class FScore {

    double precision;

    double recall;

    public String toString() {
        return String.valueOf(this.getScore());
    }

    public String fullInfo() {
        return "precision: " + Utilities.round(this.getPrecision(), 2) + " recall: " + Utilities.round(this.getRecall(), 2) + " fscore: " + Utilities.round(this.getScore(), 2);
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getScore() {
        if (precision == 0 && recall == 0) {
            return 0;
        } else {
            return (2 * recall * precision) / (recall + precision);
        }
    }

    public FScore(int numMatching, int classSize, int clusterSize) {
        if (classSize > 0 && clusterSize > 0) {
            precision = ((double) numMatching / (double) clusterSize);
            recall = ((double) numMatching / (double) classSize);
        } else {
            precision = 0;
            recall = 0;
        }
    }
}
