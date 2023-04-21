package ga;

import evaluator.FitnessFunction;

public class Fitness implements FitnessFunction {

    public Object getFitness(double bestlap, double topspeed, double distraced, double damage) {
        return new Double(topspeed);
    }
}
