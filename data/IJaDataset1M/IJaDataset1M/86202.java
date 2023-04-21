package examples.supergene;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Computes the optimal change with the same condition as
 * SupergeneTest, but without using supergenes. Implemented
 * to compare the performance.
 * To test the Supergene, we created the "makechange" version with
 * additional condition: the number of nickels and pennies must be
 * both even or both odd. The supergene encloses two genes
 * (nickels and pennies) and is valid if the condition above is
 * satisfied.
 *
 * @author Audrius Meskauskas
 * @author Klaus Meffert
 */
class WithoutSupergeneSample extends SupergeneSample {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "0.0.0 alpha explosive";

    /**
   * Executes the genetic algorithm to determine the minimum number of
   * coins necessary to make up the given target amount of change. The
   * solution will then be written to System.out.
   *
   * @param a_targetChangeAmount The target amount of change for which this
   * method is attempting to produce the minimum number of coins
   * @return absolute difference between the required and computed change
   * @throws Exception
   */
    public int makeChangeForAmount(int a_targetChangeAmount) throws Exception {
        Configuration conf = new DefaultConfiguration();
        WithoutSupergeneChangeFitFForTesting fitnessFunction = new WithoutSupergeneChangeFitFForTesting(a_targetChangeAmount);
        conf.setFitnessFunction(fitnessFunction);
        Gene[] sampleGenes = new Gene[4];
        sampleGenes[DIMES] = getDimesGene(conf);
        sampleGenes[NICKELS] = getNickelsGene(conf);
        sampleGenes[QUARTERS] = getQuartersGene(conf);
        sampleGenes[PENNIES] = getPenniesGene(conf);
        int s = solve(conf, a_targetChangeAmount, fitnessFunction, sampleGenes);
        return s;
    }

    public static void main(String[] args) {
        WithoutSupergeneSample test = new WithoutSupergeneSample();
        test.test();
        System.exit(0);
    }
}
