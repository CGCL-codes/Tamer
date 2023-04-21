package jmetal.qualityIndicator;

import java.util.*;
import java.io.*;

/**
 * This class implements the hypervolume indicator. The code is the a Java version
 * of the original metric implementation by Eckart Zitzler.
 * It can be used also as a command line program just by typing
 * $java jmetal.qualityIndicator.Hypervolume <solutionFrontFile> <trueFrontFile> <numberOfOjbectives>
 * Reference: E. Zitzler and L. Thiele
 *           Multiobjective Evolutionary Algorithms: A Comparative Case Study 
 *           and the Strength Pareto Approach,
 *           IEEE Transactions on Evolutionary Computation, vol. 3, no. 4, 
 *           pp. 257-271, 1999.
 */
public class Hypervolume {

    jmetal.qualityIndicator.util.MetricsUtil utils_;

    /**
  * Constructor
  * Creates a new instance of MultiDelta
  */
    public Hypervolume() {
        utils_ = new jmetal.qualityIndicator.util.MetricsUtil();
    }

    boolean dominates(double point1[], double point2[], int noObjectives) {
        int i;
        int betterInAnyObjective;
        betterInAnyObjective = 0;
        for (i = 0; i < noObjectives && point1[i] >= point2[i]; i++) if (point1[i] > point2[i]) betterInAnyObjective = 1;
        return ((i >= noObjectives) && (betterInAnyObjective > 0));
    }

    void swap(double[][] front, int i, int j) {
        double[] temp;
        temp = front[i];
        front[i] = front[j];
        front[j] = temp;
    }

    int filterNondominatedSet(double[][] front, int noPoints, int noObjectives) {
        int i, j;
        int n;
        n = noPoints;
        i = 0;
        while (i < n) {
            j = i + 1;
            while (j < n) {
                if (dominates(front[i], front[j], noObjectives)) {
                    n--;
                    swap(front, j, n);
                } else if (dominates(front[j], front[i], noObjectives)) {
                    n--;
                    swap(front, i, n);
                    i--;
                    break;
                } else j++;
            }
            i++;
        }
        return n;
    }

    double surfaceUnchangedTo(double[][] front, int noPoints, int objective) {
        int i;
        double minValue, value;
        if (noPoints < 1) System.err.println("run-time error");
        minValue = front[0][objective];
        for (i = 1; i < noPoints; i++) {
            value = front[i][objective];
            if (value < minValue) minValue = value;
        }
        return minValue;
    }

    int reduceNondominatedSet(double[][] front, int noPoints, int objective, double threshold) {
        int n;
        int i;
        n = noPoints;
        for (i = 0; i < n; i++) if (front[i][objective] <= threshold) {
            n--;
            swap(front, i, n);
        }
        return n;
    }

    public double calculateHypervolume(double[][] front, int noPoints, int noObjectives) {
        int n;
        double volume, distance;
        volume = 0;
        distance = 0;
        n = noPoints;
        while (n > 0) {
            int noNondominatedPoints;
            double tempVolume, tempDistance;
            noNondominatedPoints = filterNondominatedSet(front, n, noObjectives - 1);
            tempVolume = 0;
            if (noObjectives < 3) {
                if (noNondominatedPoints < 1) System.err.println("run-time error");
                tempVolume = front[0][0];
            } else tempVolume = calculateHypervolume(front, noNondominatedPoints, noObjectives - 1);
            tempDistance = surfaceUnchangedTo(front, n, noObjectives - 1);
            volume += tempVolume * (tempDistance - distance);
            distance = tempDistance;
            n = reduceNondominatedSet(front, n, noObjectives - 1, distance);
        }
        return volume;
    }

    double[][] mergeFronts(double[][] front1, int sizeFront1, double[][] front2, int sizeFront2, int noObjectives) {
        int i, j;
        int noPoints;
        double[][] frontPtr;
        noPoints = sizeFront1 + sizeFront2;
        frontPtr = new double[noPoints][noObjectives];
        noPoints = 0;
        for (i = 0; i < sizeFront1; i++) {
            for (j = 0; j < noObjectives; j++) frontPtr[noPoints][j] = front1[i][j];
            noPoints++;
        }
        for (i = 0; i < sizeFront2; i++) {
            for (j = 0; j < noObjectives; j++) frontPtr[noPoints][j] = front2[i][j];
            noPoints++;
        }
        return frontPtr;
    }

    /** 
   * Returns the hypevolume value of the paretoFront. This method call to the
   * calculate hipervolume one
   * @param paretoFront The pareto front
   * @param paretoTrueFront The true pareto front
   * @param numberOfObjectives Number of objectives of the pareto front
   */
    public double hypervolume(double[][] paretoFront, double[][] paretoTrueFront, int numberOfObjectives) {
        double[] maximumValues;
        double[] minimumValues;
        double[][] normalizedFront;
        double[][] invertedFront;
        maximumValues = utils_.getMaximumValues(paretoTrueFront, numberOfObjectives);
        minimumValues = utils_.getMinimumValues(paretoTrueFront, numberOfObjectives);
        normalizedFront = utils_.getNormalizedFront(paretoFront, maximumValues, minimumValues);
        invertedFront = utils_.invertedFront(normalizedFront);
        return this.calculateHypervolume(invertedFront, invertedFront.length, numberOfObjectives);
    }

    /**
   * This class can be invoqued from the command line. Three params are required:
   * 1) the name of the file containing the front,  
   * 2) the name of the file containig the true Pareto front
   * 3) the number of objectives
   */
    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Error using delta. Type: \n java hypervolume " + "<SolutionFrontFile>" + "<TrueFrontFile> + <numberOfObjectives>");
            System.exit(1);
        }
        Hypervolume qualityIndicator = new Hypervolume();
        double[][] solutionFront = qualityIndicator.utils_.readFront(args[0]);
        double[][] trueFront = qualityIndicator.utils_.readFront(args[1]);
        double value = qualityIndicator.hypervolume(solutionFront, trueFront, (new Integer(args[2])).intValue());
        System.out.println(value);
    }
}
