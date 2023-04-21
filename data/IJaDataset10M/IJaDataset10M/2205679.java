package ec.vector.breed;

import ec.BreedingPipeline;
import ec.EvolutionState;
import ec.Individual;
import ec.SelectionMethod;
import ec.util.Parameter;
import ec.vector.*;

/**
 * <p>GeneDuplicationPipeline is designed to duplicate a sequence of genes from the chromosome and append
 * them to the end of the chromosome.  The sequence of genes copied are randomly determined.  That is to
 * say a random begining index is selected and a random ending index is selected from the chromosome.  Then
 * this area is then copied (begining inclusive, ending exclusive) and appended to the end of the chromosome.
 * Since randomness is a factor several checks are performed to make sure the begining and ending indicies are
 * valid.  For example, since the ending index is exclusive, the ending index cannot equal the begining index (a
 * new ending index would be randomly seleceted in this case).  Likewise the begining index cannot be larger than the
 * ending index (they would be swapped in this case).</p>
 *
 * <p><b>Default Base</b><br>
 * ec.vector.breed.GeneDuplicationPipeline
 *
 * @author Sean Luke, Joseph Zelibor III, and Eric Kangas
 * @version 1.0
 */
public class GeneDuplicationPipeline extends BreedingPipeline {

    public static final String P_DUPLICATION = "duplicate";

    public static final int NUM_SOURCES = 1;

    public Parameter defaultBase() {
        return VectorDefaults.base().push(P_DUPLICATION);
    }

    public int numSources() {
        return NUM_SOURCES;
    }

    public int produce(int min, int max, int start, int subpopulation, Individual[] inds, EvolutionState state, int thread) {
        int n = sources[0].produce(min, max, start, subpopulation, inds, state, thread);
        if (!state.random[thread].nextBoolean(likelihood)) return reproduce(n, start, subpopulation, inds, state, thread, false);
        for (int q = start; q < n + start; q++) {
            if (sources[0] instanceof SelectionMethod) inds[q] = (Individual) (inds[q].clone());
            VectorIndividual ind = (VectorIndividual) (inds[q]);
            int len = ind.genomeLength();
            if (len == 0) {
                return n;
            }
            int end = 0;
            int begin = state.random[thread].nextInt(len + 1);
            do {
                end = state.random[thread].nextInt(len + 1);
            } while (begin == end);
            if (end < begin) {
                int temp = end;
                end = begin;
                begin = temp;
            }
            Object[] original = new Object[2];
            ind.split(new int[] { 0, len }, original);
            Object[] splice = new Object[3];
            ind.split(new int[] { begin, end }, splice);
            ind.cloneGenes(splice[1]);
            ind.join(new Object[] { original[1], splice[1] });
        }
        return n;
    }
}
