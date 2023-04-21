package net.sf.javaml.filter.wavelet;

import net.sf.javaml.core.Complex;
import net.sf.javaml.core.ComplexInstance;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SimpleDataset;
import net.sf.javaml.core.SimpleInstance;
import net.sf.javaml.filter.FastFourierTransform;
import net.sf.javaml.filter.Filter;
import net.sf.javaml.utils.MathUtils;

public abstract class AbstractRealWaveletTransform implements Filter {

    private int nvoice, oct, scale;

    public AbstractRealWaveletTransform(int nvoice, int oct, int scale) {
        super();
        this.nvoice = nvoice;
        this.oct = oct;
        this.scale = scale;
    }

    public Dataset filterDataset(Dataset data) {
        Dataset out = new SimpleDataset();
        for (Instance i : data) out.add(filterInstance(i));
        return out;
    }

    public Instance filterInstance(Instance instance) {
        return transform(instance);
    }

    public Instance unfilterInstance(Instance instance) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private Instance transform(Instance x) {
        int n = x.size();
        FastFourierTransform fft = new FastFourierTransform();
        Instance xhat = fft.filterInstance(x);
        double[] xi = new double[n];
        double factor = 2 * Math.PI / n;
        for (int i = 0; i < n; i++) {
            if (i <= n / 2) xi[i] = i; else xi[i] = (i - n);
            xi[i] *= factor;
        }
        double omega0 = 5;
        int noctave = (int) (Math.floor(MathUtils.log2(n)) - oct);
        int nscale = nvoice * noctave;
        double[][] rwt = new double[n][nscale];
        int kscale = 1;
        for (int jo = 1; jo <= noctave; jo++) {
            for (double jv = 1; jv <= nvoice; jv++) {
                double qscale = scale * Math.pow(2, jv / nvoice);
                double[] omega = new double[xi.length];
                for (int i = 0; i < xi.length; i++) {
                    omega[i] = n * xi[i] / qscale;
                }
                Complex[] window = transform(omega, omega0);
                for (int i = 0; i < window.length; i++) {
                    window[i].re /= Math.sqrt(qscale);
                    window[i].im /= Math.sqrt(qscale);
                }
                Complex[] what = new Complex[window.length];
                for (int i = 0; i < what.length; i++) what[i] = Complex.multiply(window[i], xhat.getComplex(i));
                Instance t = new ComplexInstance(what);
                Instance w = fft.unfilterInstance(t);
                for (int i = 0; i < w.size(); i++) {
                    rwt[i][kscale - 1] = w.value(i);
                }
                kscale++;
            }
            scale *= 2;
        }
        double[] tmp = new double[n * nscale];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < nscale; j++) {
                tmp[j * n + i] = rwt[i][j];
            }
        }
        return new SimpleInstance(tmp);
    }

    abstract Complex[] transform(double[] omega, double omega0);
}
