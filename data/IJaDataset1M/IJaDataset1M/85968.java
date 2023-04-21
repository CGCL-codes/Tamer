package algs.chapter9.table8;

import java.text.NumberFormat;
import java.util.Random;
import algs.model.IMultiPoint;
import algs.model.kdtree.DimensionalNode;
import algs.model.kdtree.IVisitKDNode;
import algs.model.kdtree.KDFactory;
import algs.model.kdtree.KDTree;
import algs.model.nd.Hypercube;
import algs.model.nd.Hyperpoint;
import algs.model.problems.rangeQuery.BruteForceRangeQuery;
import algs.model.tests.common.TrialSuite;

class Counter implements IVisitKDNode {

    int ct;

    int numDrained;

    public void visit(DimensionalNode node) {
        ct++;
    }

    public void drain(DimensionalNode node) {
        ct++;
        numDrained++;
    }
}

public class Main {

    static Random rGen;

    /** 
	 * generate array of n d-dimensional points whose coordinates are
	 * values in the range 0 .. scale
	 */
    private static IMultiPoint[] randomPoints(int n, int d, int scale) {
        IMultiPoint points[] = new IMultiPoint[n];
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < d; j++) {
                sb.append(rGen.nextDouble() * scale);
                if (j < d - 1) {
                    sb.append(",");
                }
            }
            points[i] = new Hyperpoint(sb.toString());
        }
        return points;
    }

    public static void main(String[] args) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        rGen = new Random();
        rGen.setSeed(1);
        int numSearches = 128;
        int NUM_TRIALS = 100;
        int maxN = 131072;
        int scale = 4000;
        int maxD = 5;
        System.out.println("n\td=2 BF\td=3 BF\td=4 BF\td=5 BF\td=2 RQ\td=3 RQ\td=4 RQ\td=5 RQ");
        for (int n = 4096; n <= maxN; n *= 2) {
            double results_RQ[] = new double[maxD + 1];
            double results_BF[] = new double[maxD + 1];
            for (int d = 2; d <= maxD; d++) {
                TrialSuite kdSearch3 = new TrialSuite();
                TrialSuite bfSearch3 = new TrialSuite();
                Counter kd_count = new Counter();
                Counter bf_count = new Counter();
                for (int t = 1; t <= NUM_TRIALS; t++) {
                    long now, done;
                    IMultiPoint[] points = randomPoints(n, d, scale);
                    System.gc();
                    KDTree tree = KDFactory.generate(points);
                    double lows[] = new double[d], highs[] = new double[d];
                    for (int k = 0; k < d; k++) {
                        lows[k] = rGen.nextDouble();
                        highs[k] = lows[k];
                    }
                    Hypercube space3 = new Hypercube(lows, highs);
                    System.gc();
                    now = System.currentTimeMillis();
                    for (int ns = 0; ns < numSearches; ns++) {
                        tree.search(space3, kd_count);
                    }
                    done = System.currentTimeMillis();
                    kdSearch3.addTrial(n, now, done);
                    BruteForceRangeQuery bfrq = new BruteForceRangeQuery(points);
                    System.gc();
                    now = System.currentTimeMillis();
                    for (int ns = 0; ns < numSearches; ns++) {
                        bfrq.search(space3, bf_count);
                    }
                    done = System.currentTimeMillis();
                    bfSearch3.addTrial(n, now, done);
                    if (kd_count.ct != bf_count.ct) {
                        System.out.println("result1 fails");
                    }
                }
                results_BF[d] = Double.valueOf(bfSearch3.getAverage(n));
                results_RQ[d] = Double.valueOf(kdSearch3.getAverage(n));
            }
            System.out.print(n + "\t");
            for (int d = 2; d <= 5; d++) {
                System.out.print(nf.format(results_BF[d]));
                System.out.print("\t");
            }
            for (int d = 2; d <= 5; d++) {
                System.out.print(nf.format(results_RQ[d]));
                System.out.print("\t");
            }
            System.out.println();
        }
    }
}
