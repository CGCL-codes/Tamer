package detector;

import java.math.*;
import utilites.Matlab;

public class VoiceDetector {

    public VoiceDetector() {
        Eor = null;
        Cor = null;
    }

    private double[] Eor;

    private double[] Cor;

    /**
* @param e - ShortTimeEnergy
* @param c - SpectralCentroid
*/
    public void setFeatures(double[] e, double[] c) {
        Eor = e;
        Cor = c;
    }

    /**
* @param audioSamples - mono signal samples
* @return limits
*/
    public int[][] detectVoiced(double[] audioSamples, double fs, int numSamplesInWindow, int numSamplesInStep) throws Exception {
        if (Eor == null) {
            Eor = extractShortTimeEnergy(audioSamples, numSamplesInWindow, numSamplesInStep);
        }
        if (Cor == null) {
            Cor = extractSpectralCentroid(audioSamples, numSamplesInWindow, numSamplesInStep, fs);
        }
        double[] E = Matlab.medfilt1(Eor, 5);
        E = Matlab.medfilt1(E, 5);
        double[] C = Matlab.medfilt1(Cor, 5);
        C = Matlab.medfilt1(C, 5);
        Threshold th = new Threshold(Eor.length / 10);
        double T_E = th.process(E);
        double T_C = th.process(C);
        return Cutter.getLimits(E, T_E, C, T_C);
    }

    /**
* @param f - input 
* @param step - window width
* @param Maxima - output, should be prealocated by caller
* @return number of founded maxima
*/
    public static int findMaxima(final double[] f, int step, double[][] Maxima) {
        double meanf = 0.0;
        for (int i = 0; i < f.length; i++) {
            meanf += f[i];
        }
        meanf /= f.length;
        meanf *= 0.02;
        int countMaxima = 0;
        final int maxnmax = 32;
        double founded[] = new double[maxnmax];
        int indexes[] = new int[maxnmax];
        final int searched = f.length - step - 1;
        double leftsum = 0;
        double rightsum = 0;
        double middle;
        for (int i = 1; i < step; i++) {
        }
        leftsum = 0;
        rightsum = 0;
        for (int i = 0; i < step; i++) {
            leftsum += f[i];
            rightsum += f[i + step + 1];
        }
        final int mw = step;
        for (int i = step; i < searched; i++) {
            middle = mw * f[i];
            if (leftsum < middle && rightsum < middle) {
                if (f[i] > meanf) {
                    indexes[countMaxima] = i;
                    founded[countMaxima] = f[i];
                    countMaxima++;
                    if (countMaxima >= maxnmax) break;
                }
            }
            leftsum += f[i] - f[i - step];
            rightsum += f[i + step + 1] - f[i + 1];
        }
        int countNewMaxima = 0;
        double mergedM;
        int mergedI;
        int i = 0;
        while (i < countMaxima) {
            int stop = i + 1;
            int curMaxima = indexes[i];
            mergedM = founded[i];
            mergedI = i;
            while (stop < countMaxima && (indexes[stop] - curMaxima) < step / 2) {
                curMaxima = indexes[stop];
                if (founded[stop] > mergedM) {
                    mergedM = founded[stop];
                    mergedI = stop;
                }
                stop++;
            }
            Maxima[0][countNewMaxima] = mergedI;
            Maxima[1][countNewMaxima] = mergedM;
            countNewMaxima++;
            i = stop;
        }
        return countNewMaxima;
    }

    void testfindMaxima() {
    }

    private static double[] extractSpectralCentroid(double[] signal, int windowLength, int step, double fs) throws Exception {
        int L = signal.length;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < L; i++) {
            double t = Math.abs(signal[i]);
            if (t > max) max = t;
        }
        int numOfFrames = (L - windowLength) / step + 1;
        final double[] H = Matlab.hamming(windowLength);
        double[] m = new double[windowLength + 1];
        final double m0 = fs / (2 * windowLength);
        for (int j = 0; j < windowLength; j++) {
            m[j] = m0 * (double) (j + 1);
        }
        double[] C = new double[numOfFrames];
        double[] window = new double[windowLength];
        for (int i = 0; i < numOfFrames; i++) {
            double sumw2 = 0.0;
            for (int j = 0; j < windowLength; j++) {
                double val = window[j] = H[j] * signal[i * step + j];
                sumw2 += val * val;
            }
            if (sumw2 < 0.01) {
                C[i] = 0.0;
            } else {
                double[] FFT = Matlab.absFFT(window, windowLength);
                double maxFFT = Double.NEGATIVE_INFINITY;
                for (int j = 0; j < windowLength; j++) {
                    double val = FFT[j];
                    if (val > maxFFT) maxFFT = val;
                }
                double sumFFT = 0.0;
                double sumw = 0.0;
                for (int j = 0; j < windowLength; j++) {
                    double val = FFT[j] = FFT[j] / maxFFT;
                    sumFFT += val;
                    sumw += m[j] * val;
                }
                C[i] = 2.0 * sumw / sumFFT / fs;
            }
        }
        return C;
    }

    private static double[] extractShortTimeEnergy(final double[] signal, int windowLength, int step) {
        int L = signal.length;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < L; i++) {
            if (signal[i] > max) max = signal[i];
        }
        int numOfFrames = (L - windowLength) / step + 1;
        double[] E = new double[numOfFrames];
        double k = 1.0 / (double) windowLength;
        for (int i = 0; i < numOfFrames; i++) {
            double sum = 0.0;
            for (int j = 0; j < windowLength; j++) {
                double x = signal[i * step + j] / max;
                sum += x * x;
            }
            E[i] = k * sum;
        }
        return E;
    }

    private static void testShortTimeEnergy() {
        final int len = 16;
        double[] x = new double[len];
        for (int i = 0; i < len; i++) {
            x[i] = (double) (i + 1);
        }
        double[] res = extractShortTimeEnergy(x, 8, 4);
        final double[] testVector84 = new double[] { 0.099609375, 0.302734375, 0.630859375 };
        for (int i = 0; i < 3; i++) {
            if (Math.abs(res[i] - testVector84[i]) > 0.0001) {
                System.out.println(" failed");
                return;
            }
            System.out.println(res[i]);
        }
        System.out.println(" done");
    }
}
