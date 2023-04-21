package hdr_plugin.response.robertson;

import hdr_plugin.response.*;
import hdr_plugin.Exceptions.TypeNotSupportedException;
import hdr_plugin.helper.ImageJTools;
import hdr_plugin.helper.ResponseFunctionTools;
import hdr_plugin.radiance.RobertsonRadiance;
import hdr_plugin.response.weight.GaussianWeightFunction;
import hdr_plugin.response.weight.WeightFunction;
import ij.ImagePlus;

public class RobertsonCalculator implements ResponseFunctionCalculator {

    private ImagePlus imp;

    private ResponseFunctionCalculatorSettings settings;

    private WeightFunction w;

    private static final double MAX_DELTA = 1e-5;

    private static final int MAXIT = 500;

    public RobertsonCalculator(ImagePlus imp, ResponseFunctionCalculatorSettings settings) {
        this.imp = imp;
        this.settings = settings;
        this.w = new GaussianWeightFunction(settings.getZmin(), settings.getZmax(), 8.0);
    }

    public double[] calcResponse(int channel, int lambda) throws TypeNotSupportedException {
        int N = settings.getNoOfImages();
        int width = imp.getWidth();
        int height = imp.getHeight();
        int type = imp.getType();
        double[] out = new double[width * height];
        int saturated_pixels = 0;
        int M = settings.getLevels();
        double[] I[] = new double[3][M];
        I[channel] = ResponseFunctionTools.responseLinear(M);
        double[] Ip = new double[M];
        int m, i, j;
        I[channel] = normalizeI(I[channel], M);
        for (m = 0; m < M; m++) {
            Ip[m] = I[channel][m];
        }
        RobertsonRadiance r = new RobertsonRadiance(imp, new ResponseFunction(I, imp.getType(), w), settings.getExpTimes());
        out = r.calcRadiance(channel);
        boolean converged = false;
        long cardEm[] = new long[M];
        double[] sum = new double[M];
        int cur_it = 0;
        double pdelta = 0.0;
        while (!converged) {
            for (m = 0; m < M; m++) {
                cardEm[m] = 0;
                sum[m] = 0.0;
            }
            for (i = 0; i < N; i++) {
                Object pixels = imp.getImageStack().getPixels(i + 1);
                double ti = settings.getExpTimes()[i];
                for (j = 0; j < width * height; j++) {
                    m = ImageJTools.getPixelValue(pixels, j, type, channel);
                    if (m < M && m >= 0) {
                        sum[m] += ti * out[j];
                        cardEm[m]++;
                    } else {
                        System.out.println("robertson02: m out of range: " + m);
                    }
                }
            }
            for (m = 0; m < M; m++) {
                if (cardEm[m] != 0) {
                    I[channel][m] = sum[m] / cardEm[m];
                } else {
                    I[channel][m] = 0.0;
                }
            }
            I[channel] = normalizeI(I[channel], M);
            r.setR(new ResponseFunction(I, imp.getType(), w));
            out = r.calcRadiance(channel);
            double delta = 0.0;
            int hits = 0;
            for (m = 0; m < M; m++) {
                if (I[channel][m] != 0.0) {
                    double diff = I[channel][m] - Ip[m];
                    delta += diff * diff;
                    Ip[m] = I[channel][m];
                    hits++;
                }
            }
            delta /= hits;
            System.out.println(" # " + cur_it + " delta = " + delta + " coverage: " + 100 * hits / M + "%");
            if (delta < MAX_DELTA) {
                converged = true;
            } else if (Double.isNaN(delta) || (cur_it > MAXIT && pdelta < delta)) {
                System.out.println("algorithm failed to converge, too noisy data in range\n");
                break;
            }
            pdelta = delta;
            cur_it++;
        }
        for (int u = 0; u < I[channel].length; u++) {
            System.out.println(I[channel][u]);
        }
        return I[channel];
    }

    public WeightFunction getW() {
        return w;
    }

    public ResponseFunctionCalculatorSettings getResponseFunctionCalculatorSettings() {
        return settings;
    }

    public String getAlgorithm() {
        return "Robertson";
    }

    public String getAlgorithmReference() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SuppressWarnings("empty-statement")
    private double[] normalizeI(double[] I, int M) {
        int Mmin, Mmax;
        for (Mmin = 0; Mmin < M && I[Mmin] == 0; Mmin++) ;
        for (Mmax = M - 1; Mmax > 0 && I[Mmax] == 0; Mmax--) ;
        int Mmid = Mmin + (Mmax - Mmin) / 2;
        double mid = I[Mmid];
        if (mid == 0.0) {
            while (Mmid < Mmax && I[Mmid] == 0.0) {
                Mmid++;
            }
            mid = I[Mmid];
        }
        if (mid != 0.0) {
            for (int m = 0; m < M; m++) {
                I[m] /= mid;
            }
        }
        return I;
    }
}
