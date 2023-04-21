package de.oklemenz.meta.ga.helper.mutation;

import java.util.LinkedList;
import java.util.Queue;
import de.oklemenz.meta.ga.GeneticAlgorithm;
import de.oklemenz.meta.ga.api.Gene;
import de.oklemenz.meta.ga.api.GenoType;
import de.oklemenz.meta.ga.api.Individual;
import de.oklemenz.meta.ga.helper.DefaultMutation;

public class BasicMutation<F extends Comparable<F>, L extends Comparable<L>, A extends Comparable<A>> extends DefaultMutation<F, L, A> {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2792907797650841473L;

    /**
     * Default constructor
     * 
     * @param geneticAlgorithm The genetic algorithm
     * @since MetaGA 1.0 
     */
    public BasicMutation(GeneticAlgorithm<F, L, A> geneticAlgorithm) {
        super(geneticAlgorithm);
    }

    public void execute(Individual<F, L, A> individual) {
        GenoType<F, L, A> genoType = individual.getChromosome().getGenoType();
        int mMin = (int) (minGenes.getDoubleValue() * geneCount.getIntValue());
        int mMax = (int) (maxGenes.getDoubleValue() * geneCount.getIntValue());
        if (subSection.getBooleanValue()) {
            int c = random.nextIntInc(genoType.getGeneCount() - mMax);
            int i = 0;
            Queue<Gene<F, L, A>> geneQueue = new LinkedList<Gene<F, L, A>>();
            for (Gene<F, L, A> gene : genoType) {
                if (i >= c) {
                    geneQueue.offer(gene);
                    if (i < c + mMax) {
                        if (i < c + mMin || mutationProbability.hasOccured()) {
                            execute(individual, geneQueue.poll());
                        }
                    } else {
                        break;
                    }
                }
                i++;
            }
        } else {
            for (int i = 0; i < mMax; i++) {
                if (i < mMin || mutationProbability.hasOccured()) {
                    execute(individual, genoType.getRandomGene());
                }
            }
        }
    }

    public String toString() {
        return "allelForwardedMutation:";
    }
}
