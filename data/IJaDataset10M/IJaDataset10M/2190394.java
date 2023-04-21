package org.galab.saveableobject.fitness;

import org.galab.saveableobject.*;
import org.galab.saveableobject.world.*;
import org.galab.saveableobject.bot.*;
import org.galab.util.*;

/**
 *
 * @author  Administrator
 * @version 
 */
public class ProportionalDistanceFitnessMethod extends FitnessMethod {

    /** Creates new ProportionalDistanceFitnessMethod */
    public ProportionalDistanceFitnessMethod() {
        super();
        name = "Proportional Distance";
    }

    public void setup(VisualObject baseObject, World world, Population bots, double fadeFactor) {
        startDistance = getAverageDistance(baseObject, bots);
        counter = 0;
        currentSum = 0;
    }

    public void end(VisualObject baseObject, World world, Population bots, double fadeFactor) {
        for (int i = 0; i < bots.getNumChildren(); i++) {
            Bot bot = (Bot) bots.getChild(i);
            double ans = (currentSum * multiplier.doubleValue()) / counter;
            if (ans < 0) {
                ans = 0;
            }
            bot.augmentFitness(fadeFactor * ans);
        }
    }

    private double getAverageDistance(VisualObject baseObject, Population bots) {
        double dist = 0;
        for (int i = 0; i < bots.getNumChildren(); i++) {
            Bot bot = (Bot) bots.getChild(i);
            dist += bot.getDistance(baseObject);
        }
        dist /= bots.getNumChildren();
        return dist;
    }

    public void augmentFitness(VisualObject baseObject, World world, Population bots, double fadeFactor) {
        currentSum += 1 - (getAverageDistance(baseObject, bots) / startDistance);
        counter++;
    }

    int counter;

    double currentSum;

    double startDistance;

    public Double multiplier = new Double(1.0);

    public static final double multiplier_MIN = -Util.maxInt;

    public static final double multiplier_MAX = Util.maxInt;

    public int multiplier_FIXED = SET_AND_FIXED;
}
