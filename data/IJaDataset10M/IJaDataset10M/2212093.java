package GBP;

import java.util.*;

/**
 * This class contains methods for evolution:
 *  - Selection
 *  - Mutation/Recombination
 *  - Replacement
 *
 * @author T.S.Yo
**/
public class Evolution {

    /** Evolving the GBP solutions **/
    public void evolveGBP(final Individual[] pop) {
        Individual[] parents = randSelection(pop);
        Individual offspring = new Individual(parents[0]);
        offspring.setGenes(uniformCrossover(parents[0], parents[1]));
        replaceWorst(offspring, pop);
    }

    /** Randomly Select 2 parents from the given population **/
    public Individual[] randSelection(final Individual[] pop) {
        Random rand = new Random();
        int pSize = pop.length;
        int indIndex1 = rand.nextInt(pSize);
        int indIndex2 = rand.nextInt(pSize);
        while (indIndex2 == indIndex1) {
            indIndex2 = rand.nextInt(pSize);
        }
        Individual[] parents = new Individual[2];
        parents[0] = new Individual(pop[indIndex1]);
        parents[1] = new Individual(pop[indIndex2]);
        return parents;
    }

    /** Uniformcrossover **/
    public int[] uniformCrossover(final Individual ind1, final Individual ind2) {
        int genoSize = ind1.getArraySize();
        int[] newGenes = new int[genoSize];
        LinkedList<Integer> mismatch = new LinkedList<Integer>();
        checkHamming(ind1, ind2);
        if (genoSize != ind2.getArraySize()) {
            System.out.println("Parents with different length, return a random Individual");
        } else {
            for (int i = 0; i < genoSize; i++) {
                if (ind1.getGenes()[i] == ind2.getGenes()[i]) {
                    newGenes[i] = ind1.getGenes()[i];
                } else {
                    newGenes[i] = -1;
                    mismatch.add(i);
                }
            }
            int[] m = new int[mismatch.size()];
            for (int i = 0; i < (mismatch.size()); i++) {
                m[i] = mismatch.get(i);
            }
            randPermute(m);
            for (int i = 0; i < (m.length / 2); i++) {
                newGenes[m[i]] = 0;
            }
            for (int i = (m.length / 2); i < m.length; i++) {
                newGenes[m[i]] = 1;
            }
        }
        return newGenes;
    }

    /** Replace the worst individual **/
    public void replaceWorst(final Individual ind, final Individual[] pop) {
        Arrays.sort(pop, pop[0]);
        pop[0] = new Individual(ind);
    }

    /** Mutating from given individual **/
    public Individual mutation(final int mutOpt, Individual ind) {
        Individual mutInd = new Individual(ind);
        return mutInd;
    }

    /** Copy an integer array by values **/
    private int[] copyArray(int[] source) {
        int[] result = new int[source.length];
        for (int i = 0; i < result.length; i++) result[i] = source[i];
        return result;
    }

    /** check hamming distance **/
    private void checkHamming(final Individual ind1, final Individual ind2) {
        int hammingDistance = 0;
        int[] a1 = copyArray(ind1.getGenes());
        int[] a2 = copyArray(ind2.getGenes());
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i]) {
                hammingDistance++;
            }
        }
        if (hammingDistance > (a1.length / 2)) {
            int[] newA = flipArray(a2);
            ind2.setGenes(newA);
        }
    }

    /** Reverse the values of a bit string **/
    private int[] flipArray(int[] source) {
        int[] result = new int[source.length];
        for (int i = 0; i < result.length; i++) result[i] = (-1 * source[i]) + 1;
        return result;
    }

    /** Perform random permutation on an integer array **/
    private void randPermute(int[] a) {
        Random rand = new Random();
        for (int i = 0; i < (a.length - 1); i++) {
            int j = rand.nextInt((a.length - i)) + i;
            int tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
    }

    /** Show the gain-values **/
    public void showArray(int[] a) {
        String output = "Array Values: \n";
        for (int i = 0; i < a.length; i++) {
            output = output + " " + a[i];
            if ((i % 25) == 24) output = output + "\n";
        }
        System.out.println(output);
    }
}
