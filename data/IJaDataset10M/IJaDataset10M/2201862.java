package shellkk.qiq.math.ml.svm;

import java.util.Arrays;
import org.apache.log4j.PropertyConfigurator;
import shellkk.qiq.math.StopException;
import shellkk.qiq.math.StopHandle;
import shellkk.qiq.math.ml.ArraySampleSet;
import shellkk.qiq.math.ml.DefaultDensityKernel;
import shellkk.qiq.math.ml.DensityKernel;
import shellkk.qiq.math.ml.Sample;
import shellkk.qiq.math.ml.SampleSet;
import shellkk.qiq.math.ml.TrainException;
import shellkk.qiq.math.optim.QuadProgram;

/**
 * TODO bugs?
 * 
 * @author shell
 * 
 */
public class MixDensitySVM extends SVM {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected double sigma;

    protected double sumw;

    public MixDensitySVM() {
        this(null);
    }

    public MixDensitySVM(DensityKernel kernel) {
        if (kernel == null) {
            this.kernel = new DefaultDensityKernel();
        } else {
            this.kernel = kernel;
        }
        eps = 1.0E-4;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    protected double computeSquareNorm() {
        if (alpha == null) {
            return 0.0D;
        }
        double ww = 0;
        for (int i = 0; i < alpha.length; i++) {
            if (alpha[i] != 0.0D) {
                for (int j = 0; j < alpha.length; j++) {
                    if (alpha[j] != 0.0D) {
                        ww += alpha[i] * alpha[j] * kernel.product(samples[i].x, samples[j].x);
                    }
                }
            }
        }
        return ww;
    }

    public double getY(Object x) {
        if (alpha == null) {
            return 0;
        }
        double v = 0;
        for (int i = 0; i < alpha.length; i++) {
            if (alpha[i] != 0.0) {
                v += alpha[i] * kernel.product(samples[i].x, x);
            }
        }
        return v;
    }

    public void train(SampleSet sampleSet, StopHandle stopHandle) throws StopException, TrainException {
        samples = ArraySampleSet.toArraySampleSet(sampleSet, stopHandle).getSamples();
        int l = samples.length;
        DensityKernel dk = (DensityKernel) kernel;
        sumw = computeSumw();
        double[][] k = new double[l][l];
        for (int i = 0; i < l; i++) {
            for (int j = 0; j <= i; j++) {
                double p = kernel.product(samples[i].x, samples[j].x);
                k[i][j] = p;
                k[j][i] = p;
            }
        }
        double[] c = new double[l];
        double[][] a = new double[l * 3 + 1][l];
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                double ip = dk.integralProduct(samples[i].x, samples[j].x);
                a[2 * i][j] = ip;
                a[2 * i + 1][j] = -ip;
            }
        }
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                a[2 * l + i][j] = i == j ? -1 : 0;
            }
        }
        for (int j = 0; j < l; j++) {
            a[3 * l][j] = 1;
        }
        if (sigma <= 0) {
            sigma = autoSigma();
        }
        double[] b = new double[l * 3 + 1];
        for (int i = 0; i < l; i++) {
            double fl = getFl(samples[i].x);
            b[2 * i] = fl + sigma;
            b[2 * i + 1] = -fl + sigma;
        }
        for (int i = 0; i < l; i++) {
            b[2 * l + i] = 0;
        }
        b[3 * l] = 1;
        boolean[] equ = new boolean[3 * l + 1];
        for (int i = 0; i < equ.length; i++) {
            equ[i] = false;
        }
        equ[3 * l] = true;
        QuadProgram qp = new QuadProgram();
        qp.setLogStep(logStep);
        qp.setRecomputeSteps(recomputeSteps);
        qp.setTol(tol);
        qp.setEps(eps);
        alpha = qp.solve(k, c, a, b, equ, stopHandle);
        squareNorm = computeSquareNorm();
        supportVectorCount = computeSVCount();
        squareSupportVectorRange = computeSquareSVRange();
    }

    protected int computeSVCount() {
        int count = 0;
        for (int i = 0; i < alpha.length; i++) {
            if (alpha[i] != 0) {
                count++;
            }
        }
        return count;
    }

    protected double computeSumw() {
        double v = 0;
        for (int i = 0; i < samples.length; i++) {
            v += samples[i].weight;
        }
        return v;
    }

    protected double getFl(Object x) {
        DensityKernel dk = (DensityKernel) kernel;
        double w = 0;
        for (int i = 0; i < samples.length; i++) {
            if (dk.isBelong(samples[i].x, x)) {
                w += samples[i].weight;
            }
        }
        return w / sumw;
    }

    protected double autoSigma() {
        return Math.sqrt(1.0 / samples.length);
    }

    public static void main(String[] args) {
        try {
            PropertyConfigurator.configure("log4j.properties");
            Sample[] samples = new Sample[250];
            for (int i = 0; i < samples.length; i++) {
                double xi = Math.random();
                double yi = foo(xi);
                double[] Xi = { yi };
                samples[i] = new Sample(Xi, 0);
            }
            MixDensitySVM svm = new MixDensitySVM();
            svm.setEps(1.0E-5);
            svm.setTol(1.0E-4);
            long begin = System.currentTimeMillis();
            svm.train(new ArraySampleSet(samples), null);
            long end = System.currentTimeMillis();
            double[] a = new double[samples.length];
            System.arraycopy(svm.getAlpha(), 0, a, 0, a.length);
            Arrays.sort(a);
            for (int i = 0; i < a.length; i++) {
                System.out.println(a[i]);
            }
            System.out.println("seconds used by training:" + ((end - begin) / 1000));
            double err1 = 0;
            for (int i = 0; i < samples.length; i++) {
                double[] Xi = (double[]) samples[i].x;
                double yi = p(Xi[0]);
                double pi = svm.getY(Xi);
                double ei = Math.abs(pi - yi);
                err1 += ei;
            }
            err1 = err1 / samples.length;
            System.out.println("emp risk:" + err1);
            double err = 0;
            for (int i = 0; i < 1000; i++) {
                double xi = Math.random();
                double yi = p(xi);
                double[] X = { xi };
                double pi = svm.getY(X);
                double ei = Math.abs(pi - yi);
                err += ei;
            }
            err = err / 1000;
            System.out.println("expect risk:" + err);
            double R = svm.getSquareSupportVectorRange();
            double ww = svm.getSquareNorm();
            int count = svm.getSupportVectorCount();
            System.out.println("R:" + R + "\tw*w:" + ww + "\tcount:" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double p(double x) {
        return 1;
    }

    private static double foo(double x) {
        return x;
    }
}
