package gem;

import gem.util.StudentsT;
import gem.util.Summary;
import java.util.Random;

/**
 * An implementation for MINDy.
 *
 * @author Ozgun Babur
 */
public class MINDy implements Constants {

    private static Random rand = new Random();

    /**
	 * Mutual information for F and T counts.
	 * @param ft F, T counts array
	 */
    public static double calcMI(int[][] ft) {
        double entT = calcEnt(new int[] { ft[0][0] + ft[1][0], ft[0][1] + ft[1][1] });
        double[] f = new double[] { ft[0][0] + ft[0][1], ft[1][0] + ft[1][1] };
        double total = f[0] + f[1];
        double[] pf = new double[] { f[0] / total, f[1] / total };
        double entTF = (pf[0] * calcEnt(new int[] { ft[0][0], ft[0][1] })) + (pf[1] * calcEnt(new int[] { ft[1][0], ft[1][1] }));
        double mi = entT - entTF;
        return mi;
    }

    /**
	 * Calculates information theoretic entropy of variable x. The counts array has two elements,
	 * showing counts for x = 0 and x = 1.
	 * @param x counts for x = 0 and x = 1
	 */
    public static double calcEnt(int[] x) {
        int tot = x[0] + x[1];
        double[] p = new double[2];
        p[0] = x[0] / (double) tot;
        p[1] = x[1] / (double) tot;
        double ent = -(p[0] * Math.log(p[0])) - (p[1] * Math.log(p[1]));
        return ent;
    }

    /**
	 * Delta CMI.
	 *
	 * @param ft0 F,T counts when M = 0
	 * @param ft1 F,T counts when M = 1
	 * @return delta CMI
	 */
    public static double calcDMI(int[][] ft0, int[][] ft1) {
        return calcMI(ft1) - calcMI(ft0);
    }

    /**
	 * Delta CMI.
	 *
	 * @param c complete count of M,F,T including nulls
	 * @return delta CMI
	 */
    public static double calcDMI(int[][][] c) {
        return calcDMI(conv9to4(c[0]), conv9to4(c[2]));
    }

    public static int[][] conv9to4(int[][] ft) {
        int[][] r = new int[2][2];
        r[0][0] = ft[0][0];
        r[0][1] = ft[0][2];
        r[1][1] = ft[2][2];
        r[1][0] = ft[2][0];
        return r;
    }

    /**
	 * Randomly permutes M in the counts. After permutaion, any dependency to M will
	 * vanish, thus we expect delta CMI to be distibuted with mean 0.
	 *
	 * @param m totals for m=0, m=1, and m=2
	 * @param cumft cumulative counts array for F,T status
	 * @return permuted complete counts int[3][3][3]
	 */
    public static int[][][] permuteM(int[] m, int[] cumft) {
        int[][] res = new int[m.length][cumft.length];
        for (int a = 0; a < 2; a++) {
            for (int b = 0; b < m[a]; b++) {
                int r = rand.nextInt(cumft[cumft.length - 1]) + 1;
                int cell = 0;
                while (cumft[cell] < r) cell++;
                res[a][cell]++;
                for (int i = cell; i < cumft.length; i++) {
                    cumft[i]--;
                }
            }
        }
        res[2][0] = cumft[0];
        for (int i = 1; i < cumft.length; i++) {
            res[m.length - 1][i] = cumft[i] - cumft[i - 1];
        }
        int[][][] result = new int[3][3][3];
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < cumft.length; i++) {
                result[j][i / 3][i % 3] = res[j][i];
            }
        }
        return result;
    }

    public static double calcPval(int[][][] c) {
        double dmi = Math.abs(calcDMI(c));
        int[] ft = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        int[] m = new int[] { 0, 0, 0 };
        for (int k = 0; k < 3; k++) {
            int x = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ft[(i * 3) + j] += c[k][i][j];
                    x += c[k][i][j];
                }
            }
            m[k] = x;
        }
        int[] cumft = new int[9];
        cumft[0] = ft[0];
        for (int i = 1; i < cumft.length; i++) {
            cumft[i] = cumft[i - 1] + ft[i];
        }
        assert cumft[cumft.length - 1] == m[0] + m[1] + m[2];
        int[] temp = new int[cumft.length];
        int trials = 1000000;
        int hit = 0;
        int i = 0;
        for (; i < trials && hit < 100; i++) {
            System.arraycopy(cumft, 0, temp, 0, cumft.length);
            int[][][] pc = permuteM(m, temp);
            double val = Math.abs(calcDMI(pc));
            if (val >= dmi) hit++;
        }
        return hit / (double) i;
    }

    /**
	 * MINDy classifies modulations with their biological activity, as activator and antagonist.
	 * This method implements this clasification.
	 *
	 * @param t triplet
	 * @return biological activity
	 */
    public static int getBiolActivity(Triplet t) {
        double corr = t.calcFTCorr();
        double[] t0 = extractConditionalT(t, ABSENT);
        double[] t1 = extractConditionalT(t, PRESENT);
        double m0 = Summary.mean(t0);
        double m1 = Summary.mean(t1);
        double pval = StudentsT.getPValOfMeanDifference(t0, t1);
        if (pval > 0.1) return UNDECIDED;
        if ((m1 - m0) * corr > 0) return ACTIVATOR; else return ANTAGONIST;
    }

    public static double[] extractConditionalT(Triplet t, int mCond) {
        int size = 0;
        for (int sta : t.M.status) {
            if (sta == mCond) size++;
        }
        double[] v = new double[size];
        int k = 0;
        for (int i = 0; i < t.M.status.length; i++) {
            if (t.M.status[i] == mCond) {
                v[k++] = t.T.value[i];
            }
        }
        return v;
    }

    public static void main(String[] args) {
    }

    public static final int ACTIVATOR = 0;

    public static final int ANTAGONIST = 1;

    public static final int UNDECIDED = 2;
}
