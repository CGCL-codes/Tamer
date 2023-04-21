package gem;

import gem.util.Binomial;
import gem.util.Histogram;
import gem.util.Progress;
import gem.util.Summary;
import java.io.File;
import java.util.*;

/**
 * @author Ozgun Babur
 */
public class ActivityPredicter2 implements Constants {

    public static final double THR = 0.05;

    public static final int ACTIVATED = 1;

    public static final int INACTIVATED = -1;

    public static final int STAY_ACTIVE = 2;

    public static final int STAY_INACTIVE = -2;

    public static final int UNDETERMINED = 0;

    static Map<Gene, Integer> expChMap;

    public static void main(String[] args) throws Throwable {
        String dir = "resource/expdata/AR-GR/";
        boolean[][] pos = StageAnalyzer.getPos(dir);
        boolean[] pos1 = pos[1];
        boolean[] pos2 = pos[2];
        System.out.print("Reading trips ... ");
        List<Triplet> trips = readTrips(dir);
        System.out.println("ok");
        Set<Gene> genes = Triplet.collectGenes(trips);
        Map<Gene, Mod> modMap = new HashMap<Gene, Mod>();
        for (Triplet t : trips) {
            if (!modMap.containsKey(t.M)) modMap.put(t.M, new Mod(t.M));
            if (!modMap.containsKey(t.F)) modMap.put(t.F, new Mod(t.F));
        }
        expChMap = new HashMap<Gene, Integer>();
        for (Gene gene : genes) {
            double pv = CellTypeMatcher.getChangePvalBetweenTissues(gene, pos1, pos2);
            if (pv < THR) {
                double mch = CellTypeMatcher.getMeanChange(gene, pos1, pos2);
                expChMap.put(gene, mch > 0 ? ACTIVATED : INACTIVATED);
            }
        }
        Progress p = new Progress(modMap.size());
        for (Mod mod : modMap.values()) {
            p.tick();
            for (Triplet t : trips) {
                mod.count(t);
            }
        }
        List<Mod> modList = new ArrayList<Mod>(modMap.values());
        Collections.sort(modList);
        int actch = 0;
        int expch = 0;
        int bothch = 0;
        int agree = 0;
        for (Mod mod : modList) {
            System.out.println(mod);
            if (mod.expression != 0) expch++;
            if (Math.abs(mod.activity) == 1) actch++;
            if (Math.abs(mod.activity) == 1 && mod.expression != 0) bothch++;
            if (mod.expression * mod.activity == 1) agree++;
        }
        System.out.println("expch = " + expch);
        System.out.println("actch = " + actch);
        System.out.println("bothch = " + bothch);
        System.out.println("agree = " + agree);
        h.print();
    }

    public static List<Triplet> readTrips(String dir) throws Throwable {
        List<Triplet> trips = Triplet.readTrips("result/Result_fdr0.01_var10.0_AR_allpairs_expo.xls");
        if ((new File(dir + "platform.txt")).exists()) {
            CrossPlatformMapper.associateAndClean(trips, dir + "data.txt", dir + "platform.txt");
        } else {
            CrossPlatformMapper.associateAndClean(trips, dir + "data.txt");
        }
        return trips;
    }

    private static class Mod implements Comparable {

        Gene gene;

        Map<Gene, int[]> count;

        int[] sum;

        int activity;

        int expression;

        public static final Set<Integer> INIT = new HashSet<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));

        public static final Set<Integer> FINAL = new HashSet<Integer>(Arrays.asList(0, 3, 6, 7, 9, 10));

        private Mod(Gene gene) {
            this.gene = gene;
            this.count = new HashMap<Gene, int[]>();
        }

        void count(Triplet t) {
            if (!expChMap.containsKey(t.T)) return;
            int[][][] c = multi(t.M == gene ? t.cnt : t.F == gene ? switchMods(t.cnt) : null);
            if (c == null) return;
            int tarch = expChMap.get(t.T);
            Gene other = t.M == gene ? t.F : t.M;
            if (!count.containsKey(other)) {
                count.put(other, new int[12]);
            }
            int[] c12 = count.get(other);
            for (int i = 0; i < c12.length; i++) {
                double ch = calcChangePvalDif(c, i);
                if (ch * tarch > 0) c12[i] += 1;
            }
        }

        void deleteSmallerCounts() {
            for (int[] cnt : count.values()) {
                discretize(cnt);
            }
        }

        void infer() {
            deleteSmallerCounts();
            List<int[]> cnts = new ArrayList<int[]>(count.values());
            sum = Summary.sum(cnts);
            sum = extractSingleFromDouble(sum);
            activity = getChangeFromSingleSum(sum);
            if (expChMap.containsKey(gene)) expression = expChMap.get(gene);
        }

        int getChangeFromSingleSum(int[] c) {
            double pv = Difference.calcPairwisePval(c[1], c[0] + c[1], c[3], c[2] + c[3]);
            if (pv < 0.01) {
                double p1 = c[1] / (double) c[0] + c[1];
                double p2 = c[3] / (double) c[2] + c[3];
                return (int) Math.signum(p2 - p1);
            } else return 0;
        }

        int[] extractSingleFromDouble(int[] cnt) {
            int initialAbsent = 0;
            int initialPresent = 0;
            int finalAbsent = 0;
            int finalPresent = 0;
            for (int i = 0; i < cnt.length; i++) {
                if (INIT.contains(i)) initialAbsent += cnt[i]; else initialPresent += cnt[i];
                if (FINAL.contains(i)) finalAbsent += cnt[i]; else finalPresent += cnt[i];
            }
            return new int[] { initialAbsent, initialPresent, finalAbsent, finalPresent };
        }

        @Override
        public String toString() {
            String s = gene.id;
            if (sum == null) infer();
            for (int i : sum) {
                s += "\t" + i;
            }
            s += "\t" + activity + "\t" + expression;
            return s;
        }

        public int compareTo(Object o) {
            Mod m = (Mod) o;
            return gene.id.compareTo(m.gene.id);
        }
    }

    private static int vectorToInd(int[] c) {
        int c1 = (c[0] * 2) + c[1];
        int c2 = (c[2] * 2) + c[3];
        int index = (c1 * 3) + c2;
        if (c2 > c1) index--;
        return index;
    }

    private static int[] indToVector(int index) {
        int c1 = index / 3;
        int c2 = index % 3;
        if (c2 >= c1) c2++;
        return new int[] { c1 / 2, c1 % 2, c2 / 2, c2 % 2 };
    }

    private static int[] switchMods(int[] cnt) {
        int[] c = new int[8];
        c[0] = cnt[0];
        c[1] = cnt[2];
        c[2] = cnt[1];
        c[3] = cnt[3];
        c[4] = cnt[4];
        c[5] = cnt[6];
        c[6] = cnt[5];
        c[7] = cnt[7];
        return c;
    }

    private static int[][][] multi(int[] cnt) {
        if (cnt == null) return null;
        int[][][] c = new int[2][2][2];
        c[0][0][0] = cnt[0];
        c[0][1][0] = cnt[1];
        c[1][0][0] = cnt[2];
        c[1][1][0] = cnt[3];
        c[0][0][1] = cnt[4];
        c[0][1][1] = cnt[5];
        c[1][0][1] = cnt[6];
        c[1][1][1] = cnt[7];
        return c;
    }

    private static double calcChangePvalDif(int[][][] cnt, int index) {
        int[] v = indToVector(index);
        int c1 = cnt[v[0]][v[1]][1];
        int n1 = cnt[v[0]][v[1]][0] + c1;
        int c2 = cnt[v[2]][v[3]][1];
        int n2 = cnt[v[2]][v[3]][0] + c2;
        double p1 = c1 / (double) n1;
        double p2 = c2 / (double) n2;
        double pval = Difference.calcPairwisePval(c1, n1, c2, n2);
        if (pval > THR) return 0;
        return p2 - p1;
    }

    static Histogram h = new Histogram(1);

    private static void discretize(int[] cnt) {
        int max = Summary.max(cnt);
        int min = Summary.min(cnt);
        if (Binomial.getPval(min, max) < THR) {
            for (int i = 0; i < cnt.length; i++) {
                if (cnt[i] < max) cnt[i] = 0; else cnt[i] = 1;
            }
        } else {
            for (int i = 0; i < cnt.length; i++) {
                cnt[i] = 0;
            }
        }
    }
}
