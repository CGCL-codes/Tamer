package genericAlgorithm.tsp.operators.bean;

import genericAlgorithm.framework.dataTypes.SolutionAVo;
import genericAlgorithm.framework.operators.Crossover;
import genericAlgorithm.tsp.utility.SolutionHelper;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jo
 */
public class CycleCrossover implements Crossover {

    @Override
    public SolutionAVo[] reproduction(SolutionAVo first, SolutionAVo second) {
        int[] tour1 = Arrays.copyOf(first.getTour(), first.getTour().length);
        int[] tour2 = Arrays.copyOf(second.getTour(), second.getTour().length);
        SolutionAVo newSolution1 = SolutionHelper.createSolution(first.getRelatedPopulation(), doCycle(tour1, tour2));
        SolutionAVo newSolution2 = SolutionHelper.createSolution(second.getRelatedPopulation(), doCycle(tour2, tour1));
        first = null;
        second = null;
        return new SolutionAVo[] { newSolution1, newSolution2 };
    }

    private int[] doCycle(int[] first, int[] second) {
        int[] offSpring = new int[first.length];
        int i = 0, j = 0;
        Set<Integer> offSpringPart = new HashSet<Integer>();
        Set<Integer> used = new HashSet<Integer>();
        ArrayList<Integer> u = new ArrayList<Integer>(first.length);
        for (int t = 0; t < first.length; t++) u.add((Integer) first[t]);
        offSpring[i] = first[i];
        offSpringPart.add(offSpring[i]);
        used.add(i);
        while (!offSpringPart.contains(second[i])) {
            j = u.indexOf((Integer) second[i]);
            offSpring[j] = first[j];
            offSpringPart.add(offSpring[j]);
            used.add(j);
            i = j;
        }
        for (i = 1; i < offSpring.length; i++) {
            if (!used.contains(i)) {
                offSpring[i] = first[i];
            }
        }
        return offSpring;
    }
}
