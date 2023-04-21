package hr.fer.zemris.ga_framework.algorithms.tsp.mutations;

import hr.fer.zemris.ga_framework.algorithms.tsp.IMutation;
import hr.fer.zemris.ga_framework.algorithms.tsp.Permutation;
import java.util.Random;

public class ShiftMutation implements IMutation {

    protected int len;

    protected Random rand;

    protected int[] xcharr;

    public ShiftMutation(int permlength) {
        len = permlength;
        rand = new Random();
        xcharr = new int[permlength];
    }

    public void mutate(Permutation tomutate, int sl) {
        int from = rand.nextInt(len);
        int intervlen = 0;
        if (sl == -1) {
            intervlen = rand.nextInt(len - 1);
        } else {
            if (sl >= len) sl = len - 1;
            intervlen = sl;
        }
        int to = (from + intervlen) % len;
        int dest = (to + rand.nextInt(len - intervlen - 1) + 1) % len;
        int ppos = dest;
        for (int i = 0, gpos = from; i <= intervlen; i++, gpos = (gpos + 1) % len, ppos = (ppos + 1) % len) {
            xcharr[ppos] = tomutate.field[gpos];
        }
        for (int gpos = (to + 1) % len; gpos != dest; gpos = (gpos + 1) % len) {
            xcharr[gpos] = tomutate.field[gpos];
        }
        for (int gpos = dest; gpos != from; gpos = (gpos + 1) % len, ppos = (ppos + 1) % len) {
            xcharr[ppos] = tomutate.field[gpos];
        }
        int[] tmp = tomutate.field;
        tomutate.field = xcharr;
        xcharr = tmp;
    }
}
