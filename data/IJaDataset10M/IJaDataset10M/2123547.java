package shellkk.qiq.math.ml.boost;

import org.apache.log4j.PropertyConfigurator;
import shellkk.qiq.math.StopException;
import shellkk.qiq.math.StopHandle;
import shellkk.qiq.math.kernel.DefaultKernel;
import shellkk.qiq.math.kernel.Kernel;
import shellkk.qiq.math.kernel.RBFKernel;
import shellkk.qiq.math.ml.ArraySampleSet;
import shellkk.qiq.math.ml.Classifier;
import shellkk.qiq.math.ml.Sample;
import shellkk.qiq.math.ml.SampleSet;
import shellkk.qiq.math.ml.TrainException;
import shellkk.qiq.math.ml.svm.BinaryPatternSVM;
import shellkk.qiq.math.ml.svm.KernelCache;
import shellkk.qiq.math.ml.svm.SVM;

public class BPBoostSVM extends AdaBoost {

    /**
	 * Whether log steps
	 */
    protected boolean logStep = true;

    /**
	 * C
	 */
    protected double[] C;

    /**
	 * KKT tolerance
	 */
    protected double[] tol;

    /**
	 * Alpha change rate tolerance
	 */
    protected double[] eps;

    /**
	 * Gap epsilon
	 */
    protected double[] gapEps;

    /**
	 * sum square norm of svms
	 */
    protected double sumSquareNorm;

    /**
	 * Kernel
	 */
    protected Kernel[] kernel;

    /**
	 * Kernel cache
	 */
    protected transient KernelCache[] kernelCache;

    /**
	 * Kernel cache size
	 */
    protected int cacheSize;

    protected long recomputeSteps = 100000;

    /**
	 * Constructor
	 * 
	 */
    public BPBoostSVM(Kernel[] kernel) {
        this.kernel = kernel;
        kernelCache = new KernelCache[kernel.length];
        level = kernel.length;
    }

    /**
	 * Used by constructor
	 * 
	 * @return
	 */
    public Kernel getDefaultKernel() {
        return new DefaultKernel();
    }

    public void train(SampleSet sampleSet, StopHandle stopHandle) throws StopException, TrainException {
        super.train(sampleSet, stopHandle);
        sumSquareNorm = 0;
        for (int i = 0; i < function.length; i++) {
            SVM svm = (SVM) function[i];
            sumSquareNorm += d[i] * svm.getSquareNorm();
        }
    }

    protected Classifier createFunction(int level) {
        BinaryPatternSVM svm = new BinaryPatternSVM(kernel[level]);
        svm.setLogStep(logStep);
        svm.setRecomputeSteps(recomputeSteps);
        svm.setCacheSize(cacheSize);
        svm.setKernelCache(kernelCache[level]);
        if (C != null && C.length > level) svm.setC(C[level]);
        if (eps != null && eps.length > level) svm.setEps(eps[level]);
        if (gapEps != null && gapEps.length > level) svm.setGapEps(gapEps[level]);
        if (tol != null && tol.length > level) svm.setTol(tol[level]);
        return svm;
    }

    public double[] getC() {
        return C;
    }

    public void setC(double[] c) {
        C = c;
    }

    public double[] getEps() {
        return eps;
    }

    public void setEps(double[] eps) {
        this.eps = eps;
    }

    public double[] getGapEps() {
        return gapEps;
    }

    public void setGapEps(double[] gapEps) {
        this.gapEps = gapEps;
    }

    public double[] getTol() {
        return tol;
    }

    public void setTol(double[] tol) {
        this.tol = tol;
    }

    public boolean isLogStep() {
        return logStep;
    }

    public void setLogStep(boolean logStep) {
        this.logStep = logStep;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public Kernel[] getKernel() {
        return kernel;
    }

    public void setKernel(Kernel[] kernel) {
        this.kernel = kernel;
    }

    public KernelCache[] getKernelCache() {
        return kernelCache;
    }

    public long getRecomputeSteps() {
        return recomputeSteps;
    }

    public void setRecomputeSteps(long recomputeSteps) {
        this.recomputeSteps = recomputeSteps;
    }

    public void setKernelCache(KernelCache[] kernelCache) {
        this.kernelCache = kernelCache;
    }

    public double getSumSquareNorm() {
        return sumSquareNorm;
    }

    public void setSumSquareNorm(double sumSquareNorm) {
        this.sumSquareNorm = sumSquareNorm;
    }

    public static void main(String[] args) {
        try {
            PropertyConfigurator.configure("log4j.properties");
            Sample[] samples = new Sample[250];
            for (int i = 0; i < samples.length; i++) {
                double xi1 = Math.random();
                double xi2 = Math.random();
                int yi = foo(xi1, xi2);
                double[] Xi = { xi1, xi2 };
                samples[i] = new Sample(Xi, yi);
            }
            Kernel[] kernel = new Kernel[10];
            double[] C = new double[10];
            for (int i = 0; i < kernel.length; i++) {
                kernel[i] = new RBFKernel(1.0 / (i + 1));
            }
            for (int i = 0; i < C.length; i++) {
                C[i] = 1;
            }
            BPBoostSVM svm = new BPBoostSVM(kernel);
            svm.setC(C);
            svm.setCacheSize(0);
            long begin = System.currentTimeMillis();
            svm.train(new ArraySampleSet(samples), null);
            long end = System.currentTimeMillis();
            System.out.println("seconds used by training:" + ((end - begin) / 1000));
            double err1 = 0;
            for (int i = 0; i < samples.length; i++) {
                int yi = (int) samples[i].y;
                int pi = (int) svm.getY(samples[i].x);
                err1 += yi == pi ? 0 : 1;
            }
            err1 = err1 / samples.length;
            System.out.println("emp risk:" + err1);
            double err = 0;
            for (int i = 0; i < 1000; i++) {
                double xi1 = Math.random();
                double xi2 = Math.random();
                int yi = foo(xi1, xi2);
                double[] Xi = { xi1, xi2 };
                int pi = (int) svm.getY(Xi);
                err += yi == pi ? 0 : 1;
            }
            err = err / 1000;
            System.out.println("expect risk:" + err);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int foo(double x1, double x2) {
        double v1 = Math.sin(x1 * Math.PI);
        return x2 > v1 ? 0 : 1;
    }
}
