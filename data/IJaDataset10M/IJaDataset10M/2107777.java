package shotgun.metrics;

import shotgun.Predictions;
import shotgun.Targets;

/**
 * Implementation of lift that can be parameterized independently of
 * other metrics.  Can efficiently compute multiple lift scores with
 * different threshold percentages.  Informally, lift measures how good
 * a classifier (or process) is at predicting which instances are
 * positive AND at placing the most likely instances at the top of the
 * score range.
 *
 * Example usage with shotgun to optimize lift with p = 25%:
 *   java <shotgun> -sfr 200 -custom lft_0.25 \
 *       -addmetrics shotgun.metrics.Lift ".05 .25" ...
 *
 * It is easiest to understand in the context of how it is used.  In
 * marketing, an advertiser might be able to afford sending flyers to
 * 25% of the market (or some p%).  The advertiser wants to get the
 * most bang for the buck, so they want to send the flyers to the
 * people who are most likely to buy.  In machine learning lingo,
 * these customers are the positive instances.  Lift measures how good
 * the classifier (or process) is at meeting these goals.
 *
 * Formally, given p in the range (0,1):
 *  lift = (% of all positives returned in top p%)/(% positives in data)
 *       = [a/(a+c)] / [(a+b)/(a+b+c+d)]
 *       = a/(a+b) * (a+b+c+d)/(a+c)
 * where a, b, c, and d refer to the confusion matrix:
 *
 *                        MODEL PREDICTION
 *
 *                   |       1       0       |
 *             - - - + - - - - - - - - - - - + - - - - -
 *  TRUE         1   |       A       B       |    A+B
 * OUTCOME           |                       |
 *               0   |       C       D       |    C+D
 *             - - - + - - - - - - - - - - - + - - - - -
 *                   |      A+C     B+D      |  A+B+C+D
 *
 *
 *              1 = POSITIVE
 *              0 = NEGATIVE
 */
public class Lift implements MetricBundle {

    private static final double EPS = 1e-99;

    private String[] labels = null;

    private double[] percentages = null;

    private PerfCache cache;

    public Lift() {
        cache = new PerfCache();
    }

    /**
   * Initializes the lift metrics.  Creates a lift metric for each
   * specified percentage parameter.
   * @param arg The percentage parameters to use.  Should be a quoted
   *    list of numbers in the range (0,1).  A lift metric will be
   *    computed for each parameter given.  E.g. ".05 .25"
   * @return True iff initialization succeeded.
   */
    public boolean init(String arg) {
        labels = null;
        percentages = null;
        String[] parms = arg.split(" ");
        if (parms.length <= 0) {
            System.err.println("missing percentage parameter to custom lift metric");
            return false;
        }
        labels = new String[parms.length];
        percentages = new double[parms.length];
        for (int i = 0; i < parms.length; ++i) {
            percentages[i] = Double.parseDouble(parms[i]);
            labels[i] = "LFT_" + percentages[i];
            if (percentages[i] > 1.0 || percentages[i] < 0.0) {
                System.err.println(i + " lift percentage not in range (0,1)");
                return false;
            }
        }
        return true;
    }

    public int count() {
        return labels.length;
    }

    public String name(int i) {
        return labels[i];
    }

    public double performance(int offset, Predictions pred) {
        double[] scores = cache.get(pred);
        if (scores == null) {
            scores = score(pred.getPredictions(), pred.getTargets());
            cache.put(pred, scores);
        }
        return scores[offset];
    }

    public double loss(int i, Predictions pred, double perf) {
        Targets targets = pred.getTargets();
        int totalPos = targets.getTotal_true_1();
        int numInstances = targets.getSize();
        double maxLift = ((double) numInstances) / ((double) totalPos + EPS);
        return (maxLift - perf) / (maxLift + EPS);
    }

    public boolean smallerIsBetter(int i) {
        return false;
    }

    public void invalidateCache(Predictions pred) {
        cache.remove(pred);
    }

    public boolean requiresStrictOrder(int i) {
        return false;
    }

    public boolean thresholdSensitive(int i) {
        return false;
    }

    private double[] score(double[] values, Targets targets) {
        double scores[] = new double[this.count()];
        int size = values.length;
        double[] pred = new double[size];
        int[] trueVal = new int[size];
        int[] origTrueVal = targets.getTargets();
        for (int i = 0; i < size; i++) {
            pred[i] = values[i];
            trueVal[i] = origTrueVal[i];
        }
        Predictions.quicksort(0, size - 1, pred, trueVal);
        int totalPos = targets.getTotal_true_1();
        for (int i = 0; i < labels.length; ++i) {
            scores[i] = Lift.lift(pred, trueVal, percentages[i], totalPos);
        }
        return scores;
    }

    /**
   * Computes lift score for predicted values.
   * @param pred The list of predictions, sorted in increasing order.
   * @param trueVals The list of target values, aligned with the values list.
   * @param percThresh The percentage of instances that should be considered positive.
   * @param totalPos The total number of positive instances in data set.
   * @return The computed lift score.
   */
    private static double lift(double[] pred, int[] trueVal, double percentage, double totalPos) {
        int size = pred.length;
        int data_split = size - (int) (size * percentage);
        double predThresh = 0.0;
        if (data_split <= 0) {
            predThresh = pred[0] - 1.0;
        } else if (data_split >= size) {
            predThresh = pred[size - 1] + 1.0;
        } else {
            predThresh = (pred[data_split] + pred[data_split - 1]) / 2.0;
        }
        double a = 0.0, b = 0.0, c = 0.0, d = 0.0;
        int item = size - 1;
        while (item >= 0 && pred[item] > predThresh) {
            if (trueVal[item] == 1) ++a; else ++c;
            --item;
        }
        int numRemaining = item - data_split + 1;
        int count = 0;
        int posCount = 0;
        while (item >= 0 && Math.abs(pred[item] - predThresh) <= EPS) {
            ++count;
            if (trueVal[item] == 1) ++posCount;
            --item;
        }
        if (count > 0) {
            a += (double) numRemaining * (double) posCount / (double) count;
            c += (double) numRemaining * (double) (count - posCount) / (double) count;
        }
        b = totalPos - a;
        d = size - totalPos - c;
        if (a + c < EPS) {
            return 0.0;
        }
        return (a / totalPos) * ((double) size / (a + c));
    }
}
