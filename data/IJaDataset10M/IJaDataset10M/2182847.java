package musite.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author gjj
 */
public final class ROC implements Serializable {

    private static final long serialVersionUID = 6204151244996530376L;

    public enum CompareCriteria {

        lt, le, gt, ge;

        private static final long serialVersionUID = -8000214294956007958L;
    }

    private List<Double> prediction_pos;

    private List<Double> prediction_neg;

    private double cachedAuc = -1.0;

    private CompareCriteria compPos;

    private static final double eps = 10e-5;

    public ROC(final List<Double> prediction) {
        this(null, prediction);
    }

    public ROC(final List<Double> label, final List<Double> prediction) {
        this(label, prediction, CompareCriteria.ge);
    }

    public ROC(final List<Double> label, final List<Double> prediction, CompareCriteria compPos) {
        if (prediction == null) {
            throw new IllegalArgumentException("Label and prediction must be non-empty array.");
        }
        if (label != null && label.size() != prediction.size()) {
            throw new NullPointerException();
        }
        prediction_pos = new ArrayList();
        prediction_neg = new ArrayList();
        if (label == null) {
            prediction_neg.addAll(prediction);
        } else {
            for (int i = 0; i < label.size(); i++) {
                Double l = label.get(i);
                if (l == 1) {
                    prediction_pos.add(prediction.get(i));
                } else {
                    prediction_neg.add(prediction.get(i));
                }
            }
        }
        if (prediction_neg.isEmpty()) {
            throw new IllegalArgumentException("Negative data cannot be empty");
        }
        Collections.sort(prediction_neg);
        Collections.sort(prediction_pos);
        this.compPos = compPos;
    }

    List<Double> getPredPos() {
        return prediction_pos;
    }

    List<Double> getPredNeg() {
        return prediction_neg;
    }

    CompareCriteria getCompPos() {
        return compPos;
    }

    public int getP() {
        return prediction_pos.size();
    }

    public int getN() {
        return prediction_neg.size();
    }

    /**
     *
     * @param threshold
     * @param exact 
     * @return
     */
    public double getTP(final double threshold) {
        return getNum(prediction_pos, threshold, compPos);
    }

    public double getTN(final double threshold) {
        return getNum(prediction_neg, threshold, negativePC(compPos));
    }

    public CompareCriteria negativePC(CompareCriteria comp) {
        if (comp == CompareCriteria.ge) {
            return CompareCriteria.lt;
        } else if (comp == CompareCriteria.gt) {
            return CompareCriteria.le;
        } else if (comp == CompareCriteria.le) {
            return CompareCriteria.gt;
        } else {
            return CompareCriteria.ge;
        }
    }

    private double getNum(List<Double> pred, final double threshold, CompareCriteria comp) {
        int n = pred.size();
        int idx = Collections.binarySearch(pred, threshold);
        if (comp == CompareCriteria.ge) {
            if (idx >= 0) {
                int ix = idx - 1;
                while (ix >= 0 && Double.compare(pred.get(ix), pred.get(idx)) == 0) ix--;
                return n - ix - 1;
            } else {
                return n + idx + 1;
            }
        } else if (comp == CompareCriteria.gt) {
            if (idx >= 0) {
                int ix = idx + 1;
                while (ix < n && Double.compare(pred.get(ix), pred.get(idx)) == 0) ix++;
                return n - ix;
            } else {
                return n + idx + 1;
            }
        } else if (comp == CompareCriteria.le) {
            if (idx >= 0) {
                int ix = idx + 1;
                while (ix < n && Double.compare(pred.get(ix), pred.get(idx)) == 0) ix++;
                return ix;
            } else {
                return -idx - 1;
            }
        } else {
            if (idx >= 0) {
                int ix = idx - 1;
                while (ix >= 0 && Double.compare(pred.get(ix), pred.get(idx)) == 0) ix--;
                return ix + 1;
            } else {
                return -idx - 1;
            }
        }
    }

    public double getFP(final double threshold) {
        return prediction_neg.size() - getTN(threshold);
    }

    public double getFN(final double threshold) {
        return prediction_pos.size() - getTP(threshold);
    }

    /**
     * True positive rate, hit rate, recall, sensitivity
     * @param threshold
     * @return
     */
    public double getTPR(final double threshold) {
        int n = getP();
        if (n == -1) return -1;
        return getTP(threshold) / n;
    }

    /**
     * False positive rate, false alarm rate, fall-out
     * @param threshold
     * @return
     */
    public double getFPR(final double threshold) {
        return 1 - getTNR(threshold);
    }

    /**
     * True negative rate, specificity
     * @param threshold
     * @return
     */
    public double getTNR(final double threshold) {
        int n = getN();
        return getTN(threshold) / n;
    }

    /**
     * Precesion, positive predictive value
     * @param threshold
     * @return
     */
    public double getPrecision(final double threshold) {
        double tp = getTP(threshold);
        return tp / (tp + getFP(threshold));
    }

    /**
     * Negative predictive value
     * @param threshold
     * @param exact
     * @return
     */
    public double getNPV(final double threshold) {
        double tn = getTN(threshold);
        return tn / (tn + getFN(threshold));
    }

    /**
     * False discovery rate
     * @param threshold
     * @param exact
     * @return
     */
    public double getFDR(final double threshold) {
        return 1 - getPrecision(threshold);
    }

    public double getAccuracy(final double threshold) {
        return (getTP(threshold) + getTN(threshold)) / (getP() + getN());
    }

    /**
     * Matthews correlation coefficient (MCC)
     * @param threshold
     * @param exact
     * @return
     */
    public double getMCC(final double threshold) {
        double tp = getTP(threshold);
        double tn = getTN(threshold);
        double fp = getFP(threshold);
        double fn = getFN(threshold);
        return (tp * tn - fp * fn) / Math.sqrt((tp + fp) * (tp + fn) * (tn + fp) * (tn + fn));
    }

    public double[] getDefaultThresholds() {
        int np = getP();
        int nn = getN();
        List<Double> list = new ArrayList(np + nn + 2);
        double max = prediction_neg.get(nn - 1);
        double min = prediction_neg.get(0);
        if (np > 0) {
            if (prediction_pos.get(np - 1) > max) {
                max = prediction_pos.get(np - 1);
            }
            if (prediction_pos.get(0) < min) {
                min = prediction_pos.get(0);
            }
        }
        list.add(min - eps * (max - min));
        list.add(max + eps * (max - min));
        list.addAll(prediction_pos);
        list.addAll(prediction_neg);
        TreeSet<Double> nr = new TreeSet<Double>(list);
        double[] res = new double[nr.size()];
        int i = 0;
        for (double d : nr) {
            res[i++] = d;
        }
        return res;
    }

    public double[][] roc() {
        return roc(getDefaultThresholds());
    }

    public double[][] roc(final double[] thresholds) {
        if (thresholds == null) {
            throw new NullPointerException();
        }
        int n = thresholds.length;
        double[][] ret = new double[2][n];
        int i = 0;
        for (double th : thresholds) {
            ret[0][i] = getTPR(th);
            ret[1][i] = getFPR(th);
            i++;
        }
        return ret;
    }

    public double auc() {
        if (cachedAuc < 0) {
            cachedAuc = auc(roc());
        }
        return cachedAuc;
    }

    public double auc(final double[] thresholds) {
        return auc(roc(thresholds));
    }

    public double auc(final double[][] tp_fps) {
        if (tp_fps == null) {
            throw new NullPointerException();
        }
        double area = 0;
        int n = tp_fps[0].length;
        for (int i = 0; i < n - 1; i++) {
            area += 0.5 * (tp_fps[0][i] + tp_fps[0][i + 1]) * (tp_fps[1][i] - tp_fps[1][i + 1]);
        }
        return area;
    }
}
