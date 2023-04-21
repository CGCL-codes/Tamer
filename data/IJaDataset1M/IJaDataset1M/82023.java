package org.jgap.distr.grid;

import org.jgap.*;

/**
 * Default and simple implementation for IWorkerReturnStrategy.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class DefaultWorkerReturnStrategy implements IWorkerReturnStrategy {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.2 $";

    /**
   * Simple returns the fittest chromosome.
   *
   * @param a_req JGAPRequest
   * @param a_genotype Genotype
   * @return JGAPResult
   *
   * @author Klaus Meffert
   * @since 3.2
   */
    public JGAPResult assembleResult(JGAPRequest a_req, Genotype a_genotype) {
        try {
            IChromosome fittest = a_genotype.getFittestChromosome();
            Population pop = new Population(a_req.getConfiguration(), fittest);
            JGAPResult result = new JGAPResult(a_req.getSessionName(), a_req.getRID(), pop, 1);
            return result;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
