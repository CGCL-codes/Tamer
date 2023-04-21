package gelations;

/**
 * @author conrada
 *
 */
public class ScalingTransformExp extends ScalingTransform {

    public ScalingTransformExp(long seed) {
        super(seed);
    }

    public Individual transformIndividual(Individual individual) {
        double newFitness = Math.sqrt(individual.getFitness() + 0.01);
        individual.setFitness(newFitness);
        return individual;
    }
}
