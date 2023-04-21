package vyborkiGeneration.functions;

import vyborkiGeneration.VyborkiGenerator;
import java.util.ArrayList;
import java.util.Random;

public class Reley extends VyborkiGenerator {

    public double PARAM_SIGMA = 0.5;

    public double generateElement() {
        double r = new Random().nextDouble();
        if ((r >= 0) && (r <= 1)) {
            return Math.sqrt(-2 * PARAM_SIGMA * PARAM_SIGMA * Math.log(1 - r));
        }
        return 0;
    }

    public double getFunctRaspr(double x) {
        if (x < 0) {
            return 0;
        } else {
            return (1 - Math.exp(-x * x / (2 * PARAM_SIGMA * PARAM_SIGMA)));
        }
    }
}
