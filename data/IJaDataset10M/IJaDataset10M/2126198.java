package examples.math;

import org.jgap.*;
import examples.math.ga.*;

/**
 * Main class to start the example.
 * The aim is to find a mathematical expression with simple operations (like
 * plus, minus, multiply, divide) that results in a given target number.
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public class MathRunner {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.1 $";

    public static void main(String[] args) throws Exception {
        int EVOLUTIONS = 1000;
        Genotype population = Genotype.randomInitialGenotype(new MathRunnerConfiguration());
        for (int i = 0; i < EVOLUTIONS; i++) {
            population.evolve();
            if (i % (EVOLUTIONS / 100) == 0) {
                System.err.println("Evolution (" + i + ")");
                System.err.println(population.getFittestChromosome());
            }
        }
        System.err.println(population.getFittestChromosome());
        System.err.println(population.getFittestChromosome().getFitnessValue());
    }
}
