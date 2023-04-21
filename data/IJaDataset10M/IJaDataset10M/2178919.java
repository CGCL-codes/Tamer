package shellkk.qiq.math.ml.hmm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import shellkk.qiq.math.StopException;
import shellkk.qiq.math.StopHandle;
import shellkk.qiq.math.ml.ArraySampleSet;
import shellkk.qiq.math.ml.LearnMachine;
import shellkk.qiq.math.ml.Sample;
import shellkk.qiq.math.ml.SampleSet;
import shellkk.qiq.math.ml.TrainException;

/**
 * Discrete hidden markov model compute with log function deal with long long
 * sequence
 * 
 * @author shellkk
 * 
 */
public class LogDHMM implements LearnMachine {

    /**
	 * log
	 */
    private static Log log = LogFactory.getLog(LogDHMM.class);

    protected boolean logStep = true;

    protected double eps = 1.0E-5;

    protected double[][] transProb;

    protected double[][] obProb;

    protected double[] stProb;

    protected Sample[] samples;

    public LogDHMM(int states, int obs) {
        transProb = new double[states][states];
        obProb = new double[states][obs];
        stProb = new double[states];
        randomInit();
    }

    public void init(double[] d, double v) {
        for (int i = 0; i < d.length; i++) {
            d[i] = v;
        }
    }

    public void init(double[][] dd, double v) {
        for (double[] d : dd) {
            init(d, v);
        }
    }

    protected void randomProb(double[] p) {
        double w = 0;
        for (int i = 0; i < p.length; i++) {
            p[i] = Math.random();
            w += p[i];
        }
        for (int i = 0; i < p.length; i++) {
            p[i] = p[i] > 0 ? Math.log(p[i] / w) : Double.NaN;
        }
    }

    public void randomInit() {
        for (int i = 0; i < transProb.length; i++) {
            randomProb(transProb[i]);
        }
        for (int i = 0; i < obProb.length; i++) {
            randomProb(obProb[i]);
        }
        randomProb(stProb);
    }

    /**
	 * y = log(sum[exp(a[i])])
	 * 
	 * @param a
	 * @param start
	 * @param end
	 * @return
	 */
    protected double logSumExp(double[] a, int start, int end) {
        double max = -Double.MAX_VALUE;
        for (int i = start; i < end; i++) {
            if (a[i] >= max) {
                max = a[i];
            }
        }
        double e = 0;
        for (int i = start; i < end; i++) {
            if (!Double.isNaN(a[i])) {
                e += Math.exp(a[i] - max);
            }
        }
        return e > 0 ? max + Math.log(e) : Double.NaN;
    }

    /**
	 * y[i][j] = log(P[ob[0-i],state[i]=j])
	 * 
	 * @param ob
	 * @return
	 */
    protected double[][] getAlpha(int[] ob) {
        int l = ob.length;
        int stateCount = stProb.length;
        double[] tmp = new double[stateCount];
        double[][] alpha = new double[l][stateCount];
        for (int i = 0; i < stateCount; i++) {
            alpha[0][i] = stProb[i] + obProb[i][ob[0]];
        }
        for (int t = 1; t < l; t++) {
            for (int i = 0; i < stateCount; i++) {
                for (int j = 0; j < stateCount; j++) {
                    tmp[j] = alpha[t - 1][j] + transProb[j][i];
                }
                alpha[t][i] = logSumExp(tmp, 0, tmp.length) + obProb[i][ob[t]];
            }
        }
        return alpha;
    }

    /**
	 * y[i][j] = log(P[ob[i+1-],state[i]=j])
	 * 
	 * @param ob
	 * @return
	 */
    protected double[][] getBeta(int[] ob) {
        int l = ob.length;
        int stateCount = stProb.length;
        double[] tmp = new double[stateCount];
        double[][] beta = new double[l][stateCount];
        for (int i = 0; i < stateCount; i++) {
            beta[l - 1][i] = 0;
        }
        for (int t = l - 2; t >= 0; t--) {
            for (int i = 0; i < stateCount; i++) {
                for (int j = 0; j < stateCount; j++) {
                    tmp[j] = transProb[i][j] + obProb[j][ob[t + 1]] + beta[t + 1][j];
                }
                beta[t][i] = logSumExp(tmp, 0, tmp.length);
            }
        }
        return beta;
    }

    /**
	 * top state sequence for the observer
	 * 
	 * @param ob
	 * @return
	 */
    public int[] getStateSequence(int[] ob) {
        int l = ob.length;
        int stateCount = stProb.length;
        int[][] top = new int[l][stateCount];
        double[][] d = new double[l][stateCount];
        for (int i = 0; i < stateCount; i++) {
            d[0][i] = stProb[i] + obProb[i][ob[0]];
        }
        for (int t = 1; t < l; t++) {
            for (int i = 0; i < stateCount; i++) {
                double max = -Double.MAX_VALUE;
                int top_state = -1;
                for (int j = 0; j < stateCount; j++) {
                    double v = d[t - 1][j] + transProb[j][i];
                    if (v > max) {
                        max = v;
                        top_state = j;
                    }
                }
                d[t][i] = max + obProb[i][ob[t]];
                top[t][i] = top_state;
            }
        }
        int[] seq = new int[l];
        double max = -Double.MAX_VALUE;
        int maxi = -1;
        for (int i = 0; i < stateCount; i++) {
            double v = d[l - 1][i];
            if (v > max) {
                max = v;
                maxi = i;
            }
        }
        seq[l - 1] = maxi;
        for (int t = l - 2; t >= 0; t--) {
            seq[t] = top[t + 1][seq[t + 1]];
        }
        return seq;
    }

    public double getLogProbability(int[] ob) {
        int l = ob.length;
        double[][] alpha = getAlpha(ob);
        return logSumExp(alpha[l - 1], 0, alpha[l - 1].length);
    }

    public double getProbability(int[] ob) {
        double e = getLogProbability(ob);
        return Double.isNaN(e) ? 0 : Math.exp(e);
    }

    public double getY(Object x) {
        return getProbability((int[]) x);
    }

    public void train(SampleSet sampleSet, StopHandle stopHandle) throws StopException, TrainException {
        samples = ArraySampleSet.toArraySampleSet(sampleSet, stopHandle).getSamples();
        int states = transProb.length;
        int obs = obProb[0].length;
        double[][] tmpTransProb = new double[states][states];
        double[][] tmpObProb = new double[states][obs];
        double[] tmpStProb = new double[states];
        double[] w = new double[samples.length];
        double total = 0;
        for (int k = 0; k < samples.length; k++) {
            w[k] = samples[k].weight;
            total += w[k];
        }
        for (int k = 0; k < samples.length; k++) {
            w[k] = w[k] / total;
        }
        double[] prob = new double[samples.length];
        for (int k = 0; k < samples.length; k++) {
            prob[k] = getLogProbability((int[]) samples[k].x);
        }
        double lastTarget = Double.NaN;
        double thisTarget = 0;
        for (int k = 0; k < samples.length; k++) {
            thisTarget += prob[k] * samples[k].weight;
        }
        while (Double.isNaN(lastTarget) || thisTarget - lastTarget >= eps) {
            if (stopHandle != null && stopHandle.isStoped()) {
                throw new StopException();
            }
            if (logStep) {
                logStep(thisTarget);
            }
            init(tmpTransProb, 0);
            init(tmpObProb, 0);
            init(tmpStProb, 0);
            for (int k = 0; k < samples.length; k++) {
                int[] ob = (int[]) samples[k].x;
                int l = ob.length;
                double[][] alpha = getAlpha(ob);
                double[][] beta = getBeta(ob);
                for (int i = 0; i < states; i++) {
                    double v = alpha[0][i] + beta[0][i] - prob[k];
                    tmpStProb[i] += Double.isNaN(v) ? 0 : Math.exp(v) * w[k];
                    double[] tmpgamma = new double[l];
                    double[][] tmpgo = new double[obs][l];
                    init(tmpgo, Double.NaN);
                    for (int t = 0; t < l; t++) {
                        tmpgamma[t] = alpha[t][i] + beta[t][i];
                        tmpgo[ob[t]][t] = tmpgamma[t];
                    }
                    double gama1 = logSumExp(tmpgamma, 0, l);
                    double gama2 = logSumExp(tmpgamma, 0, l - 1);
                    for (int b = 0; b < obs; b++) {
                        v = logSumExp(tmpgo[b], 0, l) - gama1;
                        tmpObProb[i][b] += Double.isNaN(v) ? 0 : Math.exp(v) * w[k];
                    }
                    for (int j = 0; j < states; j++) {
                        double[] kc = new double[l - 1];
                        for (int t = 0; t < l - 1; t++) {
                            kc[t] = alpha[t][i] + transProb[i][j] + obProb[j][ob[t + 1]] + beta[t + 1][j];
                        }
                        v = logSumExp(kc, 0, kc.length) - gama2;
                        tmpTransProb[i][j] += Double.isNaN(v) ? 0 : Math.exp(v) * w[k];
                    }
                }
            }
            for (int i = 0; i < states; i++) {
                for (int j = 0; j < states; j++) {
                    transProb[i][j] = Math.log(tmpTransProb[i][j]);
                }
                for (int b = 0; b < obs; b++) {
                    obProb[i][b] = Math.log(tmpObProb[i][b]);
                }
                stProb[i] = Math.log(tmpStProb[i]);
            }
            for (int k = 0; k < samples.length; k++) {
                prob[k] = getLogProbability((int[]) samples[k].x);
            }
            lastTarget = thisTarget;
            thisTarget = 0;
            for (int k = 0; k < samples.length; k++) {
                thisTarget += prob[k] * samples[k].weight;
            }
        }
    }

    protected void logStep(double p) {
        log.info("[p=" + p + "]");
    }

    public double[][] getObProb() {
        return obProb;
    }

    public void setObProb(double[][] obProb) {
        this.obProb = obProb;
    }

    public double[] getStProb() {
        return stProb;
    }

    public void setStProb(double[] stProb) {
        this.stProb = stProb;
    }

    public double[][] getTransProb() {
        return transProb;
    }

    public void setTransProb(double[][] transProb) {
        this.transProb = transProb;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public boolean isLogStep() {
        return logStep;
    }

    public void setLogStep(boolean logStep) {
        this.logStep = logStep;
    }

    public Sample[] getSamples() {
        return samples;
    }

    public void setSamples(Sample[] samples) {
        this.samples = samples;
    }

    public static void main(String[] args) {
        try {
            PropertyConfigurator.configure("log4j.properties");
            double[][] a = { { 0.1, 0.8, 0.1 }, { 0.1, 0.1, 0.8 }, { 0.8, 0.1, 0.1 } };
            double[][] b = { { 0.8, 0.1, 0.05, 0.05 }, { 0.1, 0.8, 0.05, 0.05 }, { 0.1, 0.1, 0.4, 0.4 } };
            double[] c = { 0.8, 0.1, 0.1 };
            Sample[] samples = new Sample[1000];
            for (int i = 0; i < samples.length; i++) {
                int[] seq = generate(50, a, b, c);
                samples[i] = new Sample(seq, 0);
            }
            LogDHMM hmm = new LogDHMM(3, 4);
            long begin = System.currentTimeMillis();
            hmm.train(new ArraySampleSet(samples), null);
            long end = System.currentTimeMillis();
            System.out.println("seconds used by training:" + ((end - begin) / 1000));
            hmm.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print() {
        double[][] aa = getTransProb();
        double[][] bb = getObProb();
        double[] cc = getStProb();
        printExp(aa);
        printExp(bb);
        printExp(cc);
    }

    private void printExp(double[][] dd) {
        for (int i = 0; i < dd.length; i++) {
            printExp(dd[i]);
        }
        System.out.println();
    }

    private void printExp(double[] d) {
        for (int i = 0; i < d.length; i++) {
            if (i > 0) {
                System.out.print(',');
            }
            System.out.print(Math.exp(d[i]));
        }
        System.out.println();
    }

    private static int[] generate(int length, double[][] a, double[][] b, double[] c) {
        int[] seq = new int[length];
        int state = randomIndex(c);
        for (int i = 0; i < length; i++) {
            int ob = randomIndex(b[state]);
            seq[i] = ob;
            state = randomIndex(a[state]);
        }
        return seq;
    }

    private static int randomIndex(double[] p) {
        double v = Math.random();
        double q = 0;
        for (int i = 0; i < p.length; i++) {
            q += p[i];
            if (v <= q) {
                return i;
            }
        }
        return p.length - 1;
    }
}
