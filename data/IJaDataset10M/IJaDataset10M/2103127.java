package hr.fer.zemris.ga_framework.algorithms.tsp.crossovers;

import hr.fer.zemris.ga_framework.algorithms.tsp.ICrossover;
import hr.fer.zemris.ga_framework.algorithms.tsp.Permutation;

public class CycleCrossover implements ICrossover {

    private int len;

    private int tag;

    private int[] taken, rev1stpar;

    public CycleCrossover(int permlength) {
        tag = 0;
        len = permlength;
        taken = new int[len];
        rev1stpar = new int[len];
    }

    public void crossover(Permutation firstpar, Permutation secondpar, Permutation child) {
        if (++tag == 0) {
            taken = new int[len];
        }
        for (int i = 0; i < len; i++) {
            rev1stpar[firstpar.field[i]] = i;
        }
        int from = 1;
        for (int i = 0; i < len; i++) {
            if (taken[i] == tag) continue;
            from = (from == 1) ? 0 : 1;
            int pos = i;
            Permutation currParent = (from == 0) ? firstpar : secondpar;
            do {
                child.field[pos] = currParent.field[pos];
                taken[pos] = tag;
                pos = rev1stpar[secondpar.field[pos]];
            } while (pos != i);
        }
    }
}
