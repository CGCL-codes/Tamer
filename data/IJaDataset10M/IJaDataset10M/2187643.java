package org.galab.saveableobject.fitness;

import org.galab.saveableobject.*;
import org.galab.saveableobject.world.*;
import org.galab.saveableobject.bot.*;
import org.galab.saveableobject.controller.*;
import org.galab.util.*;

public class HomeostaticSpikingConnectionFitnessMethod extends FitnessMethod {

    /** Creates new HomeostaticSpikingConnectionFitnessMethod */
    public HomeostaticSpikingConnectionFitnessMethod() {
        super();
        name = "Homeostatic Spiking Connection";
        nullFitness = false;
    }

    public void setup(VisualObject baseObject, World world, Population bots) {
        try {
            SpikingConnection connection = (SpikingConnection) baseObject;
            if (connection.getWeight() < 0) {
                nullFitness = true;
                return;
            }
        } catch (ClassCastException e) {
            System.out.println("HomeostaticSpikingConnectionFitnessMethod::augmentFitness - This fitness function isn't connected to the correct object");
            e.printStackTrace();
        }
        counter = 0;
        insideCounter = 0;
        weightDifference = 0;
    }

    public void end(VisualObject baseObject, World world, Population bots) {
        if (nullFitness == true) {
            return;
        }
        if (weightDifference < 1) {
            weightDifference = 1;
        }
        for (int i = 0; i < bots.getNumChildren(); i++) {
            Bot bot = (Bot) (bots.getChild(i));
            if (baseObject.isDescendant(bot)) {
                bot.augmentFitness(multiplier.doubleValue() / weightDifference);
            }
        }
    }

    public void augmentFitness(VisualObject baseObject, World world, Population bots) {
        if (nullFitness == true) {
            return;
        }
        counter++;
        try {
            SpikingConnection connection = (SpikingConnection) baseObject;
            weightDifference += Math.abs(connection.getCurrentWeight() - connection.getWeight());
        } catch (ClassCastException e) {
            System.out.println("HomeostaticSpikingConnectionFitnessMethod::augmentFitness - This fitness function isn't connected to the correct object");
            e.printStackTrace();
        }
    }

    int counter;

    int insideCounter;

    boolean nullFitness;

    double weightDifference;

    public Double multiplier = new Double(1.0);

    public static final double multiplier_MIN = -Util.maxInt;

    public static final double multiplier_MAX = Util.maxInt;

    public int multiplier_FIXED = SET_AND_FIXED;
}
