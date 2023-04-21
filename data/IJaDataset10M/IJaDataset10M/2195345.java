package game.evolution.distance;

import game.evolution.ObjectEvolvable;

public class HammingDistanceComputingStrategy implements DistanceComputingStrategy {

    @Override
    public double computeDistance(ObjectEvolvable model1, ObjectEvolvable model2, double phenotypic) {
        return (model1.getGenome().distance(model2.getGenome()));
    }
}
