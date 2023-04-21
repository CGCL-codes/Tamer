package ireval.stat;

import java.util.*;
import ireval.stat.Fmath;

public class Stat {

    public static final double FPMIN = 1e-300;

    private static int lgfN = 6;

    private static double[] lgfCoeff = { 1.000000000190015, 76.18009172947146, -86.50532032941677, 24.01409824083091, -1.231739572450155, 0.1208650973866179E-2, -0.5395239384953E-5 };

    private static double lgfGamma = 5.0;

    private static int igfiter = 1000;

    private static double igfeps = 1e-8;

    private static double histTol = 1.0001D;

    public static double mean(double[] aa) {
        int n = aa.length;
        double sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += aa[i];
        }
        return sum / ((double) n);
    }

    public static double mean(double[] aa, double[] ww) {
        int n = aa.length;
        if (n != ww.length) throw new IllegalArgumentException("length of variable array, " + n + " and length of weight array, " + ww.length + " are different");
        double sumx = 0.0D;
        double sumw = 0.0D;
        double weight = 0.0D;
        for (int i = 0; i < n; i++) {
            weight = 1.0D / (ww[i] * ww[i]);
            sumx += aa[i] * weight;
            sumw += weight;
        }
        return sumx / sumw;
    }

    public static double mean(float[] aa, float[] ww) {
        int n = aa.length;
        if (n != ww.length) throw new IllegalArgumentException("length of variable array, " + n + " and length of weight array, " + ww.length + " are different");
        float sumx = 0.0F;
        float sumw = 0.0F;
        float weight = 0.0F;
        for (int i = 0; i < n; i++) {
            weight = 1.0F / (ww[i] * ww[i]);
            sumx += aa[i] * weight;
            sumw += weight;
        }
        return sumx / sumw;
    }

    public static double geometricMean(double[] aa) {
        int n = aa.length;
        double product = 1.0D;
        for (int i = 0; i < n; i++) product *= Math.pow(aa[i], 1.0D / ((double) n));
        return product;
    }

    public static float geometricMean(float[] aa) {
        int n = aa.length;
        float product = 1.0F;
        for (int i = 0; i < n; i++) product *= (float) Math.pow(aa[i], 1.0F / ((float) n));
        return product;
    }

    public static double geometricMean(double[] aa, double[] ww) {
        int n = aa.length;
        double sumW = 0.0D;
        double[] weight = new double[n];
        for (int i = 0; i < n; i++) {
            weight[i] = 1.0D / (ww[i] * ww[i]);
            sumW += ww[i];
        }
        double product = 1.0D;
        for (int i = 0; i < n; i++) {
            product *= Math.pow(aa[i], weight[i] / sumW);
        }
        return product;
    }

    public static float geometricMean(float[] aa, float[] ww) {
        int n = aa.length;
        float sumW = 0.0F;
        float[] weight = new float[n];
        for (int i = 0; i < n; i++) {
            weight[i] = 1.0F / (ww[i] * ww[i]);
            sumW += ww[i];
        }
        float product = 1.0F;
        for (int i = 0; i < n; i++) {
            product *= (float) Math.pow(aa[i], weight[i] / sumW);
        }
        return product;
    }

    public static double harmonicMean(double[] aa) {
        int n = aa.length;
        double sum = 0.0D;
        for (int i = 0; i < n; i++) sum += 1.0D / aa[i];
        return (double) n / sum;
    }

    public static float harmonicMean(float[] aa) {
        int n = aa.length;
        float sum = 0.0F;
        for (int i = 0; i < n; i++) sum += 1.0F / aa[i];
        return (float) n / sum;
    }

    public static double harmonicMean(double[] aa, double[] ww) {
        int n = aa.length;
        double sum = 0.0D;
        double sumW = 0.0D;
        double[] weight = new double[n];
        for (int i = 0; i < n; i++) {
            sumW += ww[i];
            weight[i] = 1.0D / (ww[i] * ww[i]);
        }
        for (int i = 0; i < n; i++) sum += ww[i] / aa[i];
        return sumW / sum;
    }

    public static float harmonicMean(float[] aa, float[] ww) {
        int n = aa.length;
        float sum = 0.0F;
        float sumW = 0.0F;
        float[] weight = new float[n];
        for (int i = 0; i < n; i++) {
            sumW += ww[i];
            weight[i] = 1.0F / (ww[i] * ww[i]);
        }
        for (int i = 0; i < n; i++) sum += ww[i] / aa[i];
        return sumW / sum;
    }

    public static double generalisedMean(double[] aa, double m) {
        int n = aa.length;
        double sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += Math.pow(aa[i], m);
        }
        return Math.pow(sum / ((double) n), 1.0D / m);
    }

    public static float generalisedMean(float[] aa, float m) {
        int n = aa.length;
        float sum = 0.0F;
        for (int i = 0; i < n; i++) {
            sum += Math.pow(aa[i], m);
        }
        return (float) Math.pow(sum / ((float) n), 1.0F / m);
    }

    public static double interQuartileMean(double[] aa) {
        int n = aa.length;
        if (n < 4) throw new IllegalArgumentException("At least 4 array elements needed");
        double[] bb = Fmath.selectionSort(aa);
        double sum = 0.0D;
        for (int i = n / 4; i < 3 * n / 4; i++) sum += bb[i];
        return 2.0 * sum / (double) (n);
    }

    public static float interQuartileMean(float[] aa) {
        int n = aa.length;
        if (n < 4) throw new IllegalArgumentException("At least 4 array elements needed");
        float[] bb = Fmath.selectionSort(aa);
        float sum = 0.0F;
        for (int i = n / 4; i < 3 * n / 4; i++) sum += bb[i];
        return 2.0F * sum / (float) (n);
    }

    public static double rms(double[] aa) {
        int n = aa.length;
        double sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += aa[i] * aa[i];
        }
        return Math.sqrt(sum / ((double) n));
    }

    public static float rms(float[] aa) {
        int n = aa.length;
        float sum = 0.0F;
        for (int i = 0; i < n; i++) {
            sum += aa[i] * aa[i];
        }
        sum /= (float) n;
        return (float) Math.sqrt(sum);
    }

    public static float mean(float[] aa) {
        int n = aa.length;
        float sum = 0.0F;
        for (int i = 0; i < n; i++) {
            sum += aa[i];
        }
        return sum / ((float) n);
    }

    public static double mean(int[] aa) {
        int n = aa.length;
        double sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += (double) aa[i];
        }
        return sum / ((double) n);
    }

    public static double mean(long[] aa) {
        int n = aa.length;
        double sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += (double) aa[i];
        }
        return sum / ((double) n);
    }

    public static double median(double[] aa) {
        int n = aa.length;
        int nOverTwo = n / 2;
        double med = 0.0D;
        double[] bb = Fmath.selectionSort(aa);
        if (Fmath.isOdd(n)) {
            med = bb[nOverTwo];
        } else {
            med = (bb[nOverTwo - 1] + bb[nOverTwo]) / 2.0D;
        }
        return med;
    }

    public static float median(float[] aa) {
        int n = aa.length;
        int nOverTwo = n / 2;
        float med = 0.0F;
        float[] bb = Fmath.selectionSort(aa);
        if (Fmath.isOdd(n)) {
            med = bb[nOverTwo];
        } else {
            med = (bb[nOverTwo - 1] + bb[nOverTwo]) / 2.0F;
        }
        return med;
    }

    public static double median(int[] aa) {
        int n = aa.length;
        int nOverTwo = n / 2;
        double med = 0.0D;
        int[] bb = Fmath.selectionSort(aa);
        if (Fmath.isOdd(n)) {
            med = (double) bb[nOverTwo];
        } else {
            med = (double) (bb[nOverTwo - 1] + bb[nOverTwo]) / 2.0D;
        }
        return med;
    }

    public static double median(long[] aa) {
        int n = aa.length;
        int nOverTwo = n / 2;
        double med = 0.0D;
        long[] bb = Fmath.selectionSort(aa);
        if (Fmath.isOdd(n)) {
            med = (double) bb[nOverTwo];
        } else {
            med = (double) (bb[nOverTwo - 1] + bb[nOverTwo]) / 2.0D;
        }
        return med;
    }

    public static double standardDeviation(double[] aa) {
        return Math.sqrt(variance(aa));
    }

    public static float standardDeviation(float[] aa) {
        return (float) Math.sqrt(variance(aa));
    }

    public static double standardDeviation(int[] aa) {
        return Math.sqrt(variance(aa));
    }

    public static double standardDeviation(long[] aa) {
        return Math.sqrt(variance(aa));
    }

    public static double standardDeviation(double[] aa, double[] ww) {
        if (aa.length != ww.length) throw new IllegalArgumentException("length of variable array, " + aa.length + " and length of weight array, " + ww.length + " are different");
        return Math.sqrt(variance(aa, ww));
    }

    public static float standardDeviation(float[] aa, float[] ww) {
        if (aa.length != ww.length) throw new IllegalArgumentException("length of variable array, " + aa.length + " and length of weight array, " + ww.length + " are different");
        return (float) Math.sqrt(variance(aa, ww));
    }

    public static double volatilityLogChange(double[] array) {
        int n = array.length - 1;
        double[] change = new double[n];
        for (int i = 0; i < n; i++) change[i] = Math.log(array[i + 1] / array[i]);
        return Stat.standardDeviation(change);
    }

    public static float volatilityLogChange(float[] array) {
        int n = array.length - 1;
        float[] change = new float[n];
        for (int i = 0; i < n; i++) change[i] = (float) Math.log(array[i + 1] / array[i]);
        return Stat.standardDeviation(change);
    }

    public static double volatilityPerCentChange(double[] array) {
        int n = array.length - 1;
        double[] change = new double[n];
        for (int i = 0; i < n; i++) change[i] = (array[i + 1] - array[i]) * 100.0D / array[i];
        return Stat.standardDeviation(change);
    }

    public static double volatilityPerCentChange(float[] array) {
        int n = array.length - 1;
        float[] change = new float[n];
        for (int i = 0; i < n; i++) change[i] = (array[i + 1] - array[i]) * 100.0F / array[i];
        return Stat.standardDeviation(change);
    }

    public static double coefficientOfVariation(double[] array) {
        return 100.0D * Stat.standardDeviation(array) / Math.abs(Stat.mean(array));
    }

    public static float coefficientOfVariation(float[] array) {
        return 100.0F * Stat.standardDeviation(array) / Math.abs(Stat.mean(array));
    }

    public static double[] subtractMean(double[] array) {
        int n = array.length;
        double mean = Stat.mean(array);
        double[] arrayMinusMean = new double[n];
        for (int i = 0; i < n; i++) arrayMinusMean[i] = array[i] - mean;
        return arrayMinusMean;
    }

    public static float[] subtractMean(float[] array) {
        int n = array.length;
        float mean = Stat.mean(array);
        float[] arrayMinusMean = new float[n];
        for (int i = 0; i < n; i++) arrayMinusMean[i] = array[i] - mean;
        return arrayMinusMean;
    }

    public static double variance(double[] aa) {
        int n = aa.length;
        double sum = 0.0D, mean = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += aa[i];
        }
        mean = sum / ((double) n);
        sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += Fmath.square(aa[i] - mean);
        }
        return sum / ((double) (n - 1));
    }

    public static float variance(float[] aa) {
        int n = aa.length;
        float sum = 0.0F, mean = 0.0F;
        for (int i = 0; i < n; i++) {
            sum += aa[i];
        }
        mean = sum / ((float) n);
        sum = 0.0F;
        for (int i = 0; i < n; i++) {
            sum += Fmath.square(aa[i] - mean);
        }
        return sum / ((float) (n - 1));
    }

    public static double variance(int[] aa) {
        int n = aa.length;
        double sum = 0.0D, mean = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += (double) aa[i];
        }
        mean = sum / ((double) n);
        sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += Fmath.square((double) aa[i] - mean);
        }
        return sum / ((double) (n - 1));
    }

    public static double variance(long[] aa) {
        int n = aa.length;
        double sum = 0.0D, mean = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += (double) aa[i];
        }
        mean = sum / ((double) n);
        sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += Fmath.square((double) aa[i] - mean);
        }
        return sum / ((double) (n - 1));
    }

    public static double variance(double[] aa, double[] ww) {
        int n = aa.length;
        if (n != ww.length) throw new IllegalArgumentException("length of variable array, " + n + " and length of weight array, " + ww.length + " are different");
        double sumx = 0.0D, sumw = 0.0D, mean = 0.0D;
        double[] weight = new double[n];
        for (int i = 0; i < n; i++) {
            sumx += aa[i];
            weight[i] = 1.0D / (ww[i] * ww[i]);
            sumw += weight[i];
        }
        mean = sumx / sumw;
        sumx = 0.0D;
        for (int i = 0; i < n; i++) {
            sumx += weight[i] * Fmath.square(aa[i] - mean);
        }
        return sumx * (double) (n) / ((double) (n - 1) * sumw);
    }

    public static float variance(float[] aa, float[] ww) {
        int n = aa.length;
        if (n != ww.length) throw new IllegalArgumentException("length of variable array, " + n + " and length of weight array, " + ww.length + " are different");
        float sumx = 0.0F, sumw = 0.0F, mean = 0.0F;
        float[] weight = new float[n];
        for (int i = 0; i < n; i++) {
            sumx += aa[i];
            weight[i] = 1.0F / (ww[i] * ww[i]);
            sumw += weight[i];
        }
        mean = sumx / sumw;
        sumx = 0.0F;
        for (int i = 0; i < n; i++) {
            sumx += weight[i] * Fmath.square(aa[i] - mean);
        }
        return sumx * (float) (n) / ((float) (n - 1) * sumw);
    }

    public static double covariance(double[] xx, double[] yy) {
        int n = xx.length;
        if (n != yy.length) throw new IllegalArgumentException("length of x variable array, " + n + " and length of y array, " + yy.length + " are different");
        double sumx = 0.0D, meanx = 0.0D;
        double sumy = 0.0D, meany = 0.0D;
        for (int i = 0; i < n; i++) {
            sumx += xx[i];
            sumy += yy[i];
        }
        meanx = sumx / ((double) n);
        meany = sumy / ((double) n);
        double sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += (xx[i] - meanx) * (yy[i] - meany);
        }
        return sum / ((double) (n - 1));
    }

    public static float covariance(float[] xx, float[] yy) {
        int n = xx.length;
        if (n != yy.length) throw new IllegalArgumentException("length of x variable array, " + n + " and length of y array, " + yy.length + " are different");
        float sumx = 0.0F, meanx = 0.0F;
        float sumy = 0.0F, meany = 0.0F;
        for (int i = 0; i < n; i++) {
            sumx += xx[i];
            sumy += yy[i];
        }
        meanx = sumx / ((float) n);
        meany = sumy / ((float) n);
        float sum = 0.0F;
        for (int i = 0; i < n; i++) {
            sum += (xx[i] - meanx) * (yy[i] - meany);
        }
        return sum / ((float) (n - 1));
    }

    public static double covariance(int[] xx, int[] yy) {
        int n = xx.length;
        if (n != yy.length) throw new IllegalArgumentException("length of x variable array, " + n + " and length of y array, " + yy.length + " are different");
        double sumx = 0.0D, meanx = 0.0D;
        double sumy = 0.0D, meany = 0.0D;
        for (int i = 0; i < n; i++) {
            sumx += (double) xx[i];
            sumy += (double) yy[i];
        }
        meanx = sumx / ((double) n);
        meany = sumy / ((double) n);
        double sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += ((double) xx[i] - meanx) * ((double) yy[i] - meany);
        }
        return sum / ((double) (n - 1));
    }

    public static double covariance(long[] xx, long[] yy) {
        int n = xx.length;
        if (n != yy.length) throw new IllegalArgumentException("length of x variable array, " + n + " and length of y array, " + yy.length + " are different");
        double sumx = 0.0D, meanx = 0.0D;
        double sumy = 0.0D, meany = 0.0D;
        for (int i = 0; i < n; i++) {
            sumx += (double) xx[i];
            sumy += (double) yy[i];
        }
        meanx = sumx / ((double) n);
        meany = sumy / ((double) n);
        double sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += ((double) xx[i] - meanx) * ((double) yy[i] - meany);
        }
        return sum / ((double) (n - 1));
    }

    public static double covariance(double[] xx, double[] yy, double[] ww) {
        int n = xx.length;
        if (n != yy.length) throw new IllegalArgumentException("length of x variable array, " + n + " and length of y array, " + yy.length + " are different");
        if (n != ww.length) throw new IllegalArgumentException("length of x variable array, " + n + " and length of weight array, " + yy.length + " are different");
        double sumx = 0.0D, sumy = 0.0D, sumw = 0.0D, meanx = 0.0D, meany = 0.0D;
        double[] weight = new double[n];
        for (int i = 0; i < n; i++) {
            sumx += xx[i];
            sumy += yy[i];
            weight[i] = 1.0D / (ww[i] * ww[i]);
            sumw += weight[i];
        }
        meanx = sumx / sumw;
        meany = sumy / sumw;
        double sum = 0.0D;
        for (int i = 0; i < n; i++) {
            sum += weight[i] * (xx[i] - meanx) * (yy[i] - meany);
        }
        return sum * (double) (n) / ((double) (n - 1) * sumw);
    }

    public static double gamma(double x) {
        double xcopy = x;
        double first = x + lgfGamma + 0.5;
        double second = lgfCoeff[0];
        double fg = 0.0D;
        if (x >= 0.0) {
            if (x >= 1.0D && x - (int) x == 0.0D) {
                fg = Stat.factorial(x) / x;
            } else {
                first = Math.pow(first, x + 0.5) * Math.exp(-first);
                for (int i = 1; i <= lgfN; i++) second += lgfCoeff[i] / ++xcopy;
                fg = first * Math.sqrt(2.0 * Math.PI) * second / x;
            }
        } else {
            fg = -Math.PI / (x * Stat.gamma(-x) * Math.sin(Math.PI * x));
        }
        return fg;
    }

    public static double getLanczosGamma() {
        return Stat.lgfGamma;
    }

    public static int getLanczosN() {
        return Stat.lgfN;
    }

    public static double[] getLanczosCoeff() {
        int n = Stat.getLanczosN() + 1;
        double[] coef = new double[n];
        for (int i = 0; i < n; i++) {
            coef[i] = Stat.lgfCoeff[i];
        }
        return coef;
    }

    public static double getFpmin() {
        return Stat.FPMIN;
    }

    public static double logGamma(double x) {
        double xcopy = x;
        double fg = 0.0D;
        double first = x + lgfGamma + 0.5;
        double second = lgfCoeff[0];
        if (x >= 0.0) {
            if (x >= 1.0 && x - (int) x == 0.0) {
                fg = Stat.logFactorial(x) - Math.log(x);
            } else {
                first -= (x + 0.5) * Math.log(first);
                for (int i = 1; i <= lgfN; i++) second += lgfCoeff[i] / ++xcopy;
                fg = Math.log(Math.sqrt(2.0 * Math.PI) * second / x) - first;
            }
        } else {
            fg = Math.PI / (Stat.gamma(1.0D - x) * Math.sin(Math.PI * x));
            if (fg != 1.0 / 0.0 && fg != -1.0 / 0.0) {
                if (fg < 0) {
                    throw new IllegalArgumentException("\nThe gamma function is negative");
                } else {
                    fg = Math.log(fg);
                }
            }
        }
        return fg;
    }

    public static double incompleteGamma(double a, double x) {
        if (a < 0.0D || x < 0.0D) throw new IllegalArgumentException("\nFunction defined only for a >= 0 and x>=0");
        double igf = 0.0D;
        if (x < a + 1.0D) {
            igf = incompleteGammaSer(a, x);
        } else {
            igf = incompleteGammaFract(a, x);
        }
        return igf;
    }

    public static double incompleteGammaComplementary(double a, double x) {
        if (a < 0.0D || x < 0.0D) throw new IllegalArgumentException("\nFunction defined only for a >= 0 and x>=0");
        double igf = 0.0D;
        if (x != 0.0D) {
            if (x == 1.0D / 0.0D) {
                igf = 1.0D;
            } else {
                if (x < a + 1.0D) {
                    igf = 1.0D - incompleteGammaSer(a, x);
                } else {
                    igf = 1.0D - incompleteGammaFract(a, x);
                }
            }
        }
        return igf;
    }

    public static double incompleteGammaSer(double a, double x) {
        if (a < 0.0D || x < 0.0D) throw new IllegalArgumentException("\nFunction defined only for a >= 0 and x>=0");
        if (x >= a + 1) throw new IllegalArgumentException("\nx >= a+1   use Continued Fraction Representation");
        int i = 0;
        double igf = 0.0D;
        boolean check = true;
        double acopy = a;
        double sum = 1.0 / a;
        double incr = sum;
        double loggamma = Stat.logGamma(a);
        while (check) {
            ++i;
            ++a;
            incr *= x / a;
            sum += incr;
            if (Math.abs(incr) < Math.abs(sum) * Stat.igfeps) {
                igf = sum * Math.exp(-x + acopy * Math.log(x) - loggamma);
                check = false;
            }
            if (i >= Stat.igfiter) {
                check = false;
                igf = sum * Math.exp(-x + acopy * Math.log(x) - loggamma);
                System.out.println("\nMaximum number of iterations were exceeded in Stat.incompleteGammaSer().\nCurrent value returned.\nIncrement = " + String.valueOf(incr) + ".\nSum = " + String.valueOf(sum) + ".\nTolerance =  " + String.valueOf(igfeps));
            }
        }
        return igf;
    }

    public static double incompleteGammaFract(double a, double x) {
        if (a < 0.0D || x < 0.0D) throw new IllegalArgumentException("\nFunction defined only for a >= 0 and x>=0");
        if (x < a + 1) throw new IllegalArgumentException("\nx < a+1   Use Series Representation");
        int i = 0;
        double ii = 0;
        double igf = 0.0D;
        boolean check = true;
        double loggamma = Stat.logGamma(a);
        double numer = 0.0D;
        double incr = 0.0D;
        double denom = x - a + 1.0D;
        double first = 1.0D / denom;
        double term = 1.0D / FPMIN;
        double prod = first;
        while (check) {
            ++i;
            ii = (double) i;
            numer = -ii * (ii - a);
            denom += 2.0D;
            first = numer * first + denom;
            if (Math.abs(first) < Stat.FPMIN) {
                first = Stat.FPMIN;
            }
            term = denom + numer / term;
            if (Math.abs(term) < Stat.FPMIN) {
                term = Stat.FPMIN;
            }
            first = 1.0D / first;
            incr = first * term;
            prod *= incr;
            if (Math.abs(incr - 1.0D) < igfeps) check = false;
            if (i >= Stat.igfiter) {
                check = false;
                System.out.println("\nMaximum number of iterations were exceeded in Stat.incompleteGammaFract().\nCurrent value returned.\nIncrement - 1 = " + String.valueOf(incr - 1) + ".\nTolerance =  " + String.valueOf(igfeps));
            }
        }
        igf = 1.0D - Math.exp(-x + a * Math.log(x) - loggamma) * prod;
        return igf;
    }

    public static void setIncGammaMaxIter(int igfiter) {
        Stat.igfiter = igfiter;
    }

    public static int getIncGammaMaxIter() {
        return Stat.igfiter;
    }

    public static void setIncGammaTol(double igfeps) {
        Stat.igfeps = igfeps;
    }

    public static double getIncGammaTol() {
        return Stat.igfeps;
    }

    public static double beta(double z, double w) {
        return Math.exp(logGamma(z) + logGamma(w) - logGamma(z + w));
    }

    public static double incompleteBeta(double z, double w, double x) {
        if (x < 0.0D || x > 1.0D) throw new IllegalArgumentException("Argument x, " + x + ", must be lie between 0 and 1 (inclusive)");
        double ibeta = 0.0D;
        if (x == 0.0D) {
            ibeta = 0.0D;
        } else {
            if (x == 1.0D) {
                ibeta = 1.0D;
            } else {
                ibeta = Math.exp(Stat.logGamma(z + w) - Stat.logGamma(z) - logGamma(w) + z * Math.log(x) + w * Math.log(1.0D - x));
                if (x < (z + 1.0D) / (z + w + 2.0D)) {
                    ibeta = ibeta * Stat.contFract(z, w, x) / z;
                } else {
                    ibeta = 1.0D - ibeta * Stat.contFract(w, z, 1.0D - x) / w;
                }
            }
        }
        return ibeta;
    }

    public static double contFract(double a, double b, double x) {
        int maxit = 500;
        double eps = 3.0e-7;
        double aplusb = a + b;
        double aplus1 = a + 1.0D;
        double aminus1 = a - 1.0D;
        double c = 1.0D;
        double d = 1.0D - aplusb * x / aplus1;
        if (Math.abs(d) < Stat.FPMIN) d = FPMIN;
        d = 1.0D / d;
        double h = d;
        double aa = 0.0D;
        double del = 0.0D;
        int i = 1, i2 = 0;
        boolean test = true;
        while (test) {
            i2 = 2 * i;
            aa = i * (b - i) * x / ((aminus1 + i2) * (a + i2));
            d = 1.0D + aa * d;
            if (Math.abs(d) < Stat.FPMIN) d = FPMIN;
            c = 1.0D + aa / c;
            if (Math.abs(c) < Stat.FPMIN) c = FPMIN;
            d = 1.0D / d;
            h *= d * c;
            aa = -(a + i) * (aplusb + i) * x / ((a + i2) * (aplus1 + i2));
            d = 1.0D + aa * d;
            if (Math.abs(d) < Stat.FPMIN) d = FPMIN;
            c = 1.0D + aa / c;
            if (Math.abs(c) < Stat.FPMIN) c = FPMIN;
            d = 1.0D / d;
            del = d * c;
            h *= del;
            i++;
            if (Math.abs(del - 1.0D) < eps) test = false;
            if (i > maxit) {
                test = false;
                System.out.println("Maximum number of iterations (" + maxit + ") exceeded in Stat.contFract in Stat.incomplete Beta");
            }
        }
        return h;
    }

    public static double erf(double x) {
        double erf = 0.0D;
        if (x != 0.0) {
            if (x == 1.0D / 0.0D) {
                erf = 1.0D;
            } else {
                if (x >= 0) {
                    erf = Stat.incompleteGamma(0.5, x * x);
                } else {
                    erf = -Stat.incompleteGamma(0.5, x * x);
                }
            }
        }
        return erf;
    }

    public static double erfc(double x) {
        double erfc = 1.0D;
        if (x != 0.0) {
            if (x == 1.0D / 0.0D) {
                erfc = 0.0D;
            } else {
                if (x >= 0) {
                    erfc = 1.0D - Stat.incompleteGamma(0.5, x * x);
                } else {
                    erfc = 1.0D + Stat.incompleteGamma(0.5, x * x);
                }
            }
        }
        return erfc;
    }

    public static double normalProb(double mean, double sd, double upperlimit) {
        double arg = (upperlimit - mean) / (sd * Math.sqrt(2.0));
        return (1.0D + Stat.erf(arg)) / 2.0D;
    }

    public static double gaussianProb(double mean, double sd, double upperlimit) {
        double arg = (upperlimit - mean) / (sd * Math.sqrt(2.0));
        return (1.0D + Stat.erf(arg)) / 2.0D;
    }

    public static double normalProb(double mean, double sd, double lowerlimit, double upperlimit) {
        double arg1 = (lowerlimit - mean) / (sd * Math.sqrt(2.0));
        double arg2 = (upperlimit - mean) / (sd * Math.sqrt(2.0));
        return (Stat.erf(arg2) - Stat.erf(arg1)) / 2.0D;
    }

    public static double gaussianProb(double mean, double sd, double lowerlimit, double upperlimit) {
        double arg1 = (lowerlimit - mean) / (sd * Math.sqrt(2.0));
        double arg2 = (upperlimit - mean) / (sd * Math.sqrt(2.0));
        return (Stat.erf(arg2) - Stat.erf(arg1)) / 2.0D;
    }

    public static double normal(double mean, double sd, double x) {
        return Math.exp(-Fmath.square((x - mean) / sd) / 2.0) / (sd * Math.sqrt(2.0D * Math.PI));
    }

    public static double gaussian(double mean, double sd, double x) {
        return Math.exp(-Fmath.square((x - mean) / sd) / 2.0) / (sd * Math.sqrt(2.0D * Math.PI));
    }

    public static double[] normalRand(double mean, double sd, int n) {
        double[] ran = new double[n];
        Random rr = new Random();
        for (int i = 0; i < n; i++) {
            ran[i] = rr.nextGaussian();
            ran[i] = ran[i] * sd + mean;
        }
        return ran;
    }

    public static double[] gaussianRand(double mean, double sd, int n) {
        double[] ran = new double[n];
        Random rr = new Random();
        for (int i = 0; i < n; i++) {
            ran[i] = rr.nextGaussian();
            ran[i] = ran[i] * sd + mean;
        }
        return ran;
    }

    public static double[] normalRand(double mean, double sd, int n, long seed) {
        double[] ran = new double[n];
        Random rr = new Random(seed);
        for (int i = 0; i < n; i++) {
            ran[i] = rr.nextGaussian();
            ran[i] = ran[i] * sd + mean;
        }
        return ran;
    }

    public static double[] gaussianRand(double mean, double sd, int n, long seed) {
        double[] ran = new double[n];
        Random rr = new Random(seed);
        for (int i = 0; i < n; i++) {
            ran[i] = rr.nextGaussian();
            ran[i] = ran[i] * sd + mean;
        }
        return ran;
    }

    public static double logisticProb(double mu, double beta, double upperlimit) {
        return 0.5D * (1.0D + Math.tanh((upperlimit - mu) / (2.0D * beta)));
    }

    public static double logisticProb(double mu, double beta, double lowerlimit, double upperlimit) {
        double arg1 = 0.5D * (1.0D + Math.tanh((lowerlimit - mu) / (2.0D * beta)));
        double arg2 = 0.5D * (1.0D + Math.tanh((upperlimit - mu) / (2.0D * beta)));
        return arg2 - arg1;
    }

    public static double logistic(double mu, double beta, double x) {
        return Fmath.square(Fmath.sech((x - mu) / (2.0D * beta))) / (4.0D * beta);
    }

    public static double[] logisticRand(double mu, double beta, int n) {
        double[] ran = new double[n];
        Random rr = new Random();
        for (int i = 0; i < n; i++) {
            ran[i] = 2.0D * beta * Fmath.atanh(2.0D * rr.nextDouble() - 1.0D) + mu;
        }
        return ran;
    }

    public static double[] logisticRand(double mu, double beta, int n, long seed) {
        double[] ran = new double[n];
        Random rr = new Random(seed);
        for (int i = 0; i < n; i++) {
            ran[i] = 2.0D * beta * Fmath.atanh(2.0D * rr.nextDouble() - 1.0D) + mu;
        }
        return ran;
    }

    public static double logisticMean(double mu) {
        return mu;
    }

    public static double logisticStandDev(double beta) {
        return Math.sqrt(Fmath.square(Math.PI * beta) / 3.0D);
    }

    public static double logisticMode(double mu) {
        return mu;
    }

    public static double logisticMedian(double mu) {
        return mu;
    }

    public static double lorentzianProb(double mu, double gamma, double upperlimit) {
        double arg = (upperlimit - mu) / (gamma / 2.0D);
        return (1.0D / Math.PI) * (Math.atan(arg) + Math.PI / 2.0);
    }

    public static double lorentzianProb(double mu, double gamma, double lowerlimit, double upperlimit) {
        double arg1 = (upperlimit - mu) / (gamma / 2.0D);
        double arg2 = (lowerlimit - mu) / (gamma / 2.0D);
        return (1.0D / Math.PI) * (Math.atan(arg1) - Math.atan(arg2));
    }

    public static double poissonProb(int k, double mean) {
        if (k < 1) throw new IllegalArgumentException("k must be an integer greater than or equal to 1");
        return Stat.incompleteGammaComplementary((double) k, mean);
    }

    public static double poisson(int k, double mean) {
        if (k < 0) throw new IllegalArgumentException("k must be an integer greater than or equal to 0");
        return Math.pow(mean, k) * Math.exp(-mean) / Stat.factorial((double) k);
    }

    public static double[] poissonRand(double mean, int n) {
        Random rr = new Random();
        double[] ran = poissonRandCalc(rr, mean, n);
        return ran;
    }

    public static double[] poissonRand(double mean, int n, long seed) {
        Random rr = new Random(seed);
        double[] ran = poissonRandCalc(rr, mean, n);
        return ran;
    }

    private static double[] poissonRandCalc(Random rr, double mean, int n) {
        double[] ran = new double[n];
        double oldm = -1.0D;
        double expt = 0.0D;
        double em = 0.0D;
        double term = 0.0D;
        double sq = 0.0D;
        double lnMean = 0.0D;
        double yDev = 0.0D;
        if (mean < 12.0D) {
            for (int i = 0; i < n; i++) {
                if (mean != oldm) {
                    oldm = mean;
                    expt = Math.exp(-mean);
                }
                em = -1.0D;
                term = 1.0D;
                do {
                    ++em;
                    term *= rr.nextDouble();
                } while (term > expt);
                ran[i] = em;
            }
        } else {
            for (int i = 0; i < n; i++) {
                if (mean != oldm) {
                    oldm = mean;
                    sq = Math.sqrt(2.0D * mean);
                    lnMean = Math.log(mean);
                    expt = lnMean - Stat.logGamma(mean + 1.0D);
                }
                do {
                    do {
                        yDev = Math.tan(Math.PI * rr.nextDouble());
                        em = sq * yDev + mean;
                    } while (em < 0.0D);
                    em = Math.floor(em);
                    term = 0.9D * (1.0D + yDev * yDev) * Math.exp(em * lnMean - Stat.logGamma(em + 1.0D) - expt);
                } while (rr.nextDouble() > term);
                ran[i] = em;
            }
        }
        return ran;
    }

    public static double chiSquareProb(double chiSquare, int nu) {
        return Stat.incompleteGamma((double) nu / 2.0D, chiSquare / 2.0D);
    }

    public static double chiSquare(double[] observed, double[] expected, double[] variance) {
        int nObs = observed.length;
        int nExp = expected.length;
        int nVar = variance.length;
        if (nObs != nExp) throw new IllegalArgumentException("observed array length does not equal the expected array length");
        if (nObs != nVar) throw new IllegalArgumentException("observed array length does not equal the variance array length");
        double chi = 0.0D;
        for (int i = 0; i < nObs; i++) {
            chi += Fmath.square(observed[i] - expected[i]) / variance[i];
        }
        return chi;
    }

    public static double chiSquareFreq(double[] observedFreq, double[] expectedFreq) {
        int nObs = observedFreq.length;
        int nExp = expectedFreq.length;
        if (nObs != nExp) throw new IllegalArgumentException("observed array length does not equal the expected array length");
        double chi = 0.0D;
        for (int i = 0; i < nObs; i++) {
            chi += Fmath.square(observedFreq[i] - expectedFreq[i]) / expectedFreq[i];
        }
        return chi;
    }

    public static double chiSquareFreq(int[] observedFreq, int[] expectedFreq) {
        int nObs = observedFreq.length;
        int nExp = expectedFreq.length;
        if (nObs != nExp) throw new IllegalArgumentException("observed array length does not equal the expected array length");
        double[] observ = new double[nObs];
        double[] expect = new double[nObs];
        for (int i = 0; i < nObs; i++) {
            observ[i] = (int) observedFreq[i];
            expect[i] = (int) expectedFreq[i];
        }
        return chiSquareFreq(observ, expect);
    }

    public static double binomialProb(double p, int n, int k) {
        if (p < 0.0D || p > 1.0D) throw new IllegalArgumentException("\np must lie between 0 and 1");
        if (k < 0 || n < 0) throw new IllegalArgumentException("\nn and k must be greater than or equal to zero");
        if (k > n) throw new IllegalArgumentException("\nk is greater than n");
        return Stat.incompleteBeta(k, n - k + 1, p);
    }

    public static double binomial(double p, int n, int k) {
        if (k < 0 || n < 0) throw new IllegalArgumentException("\nn and k must be greater than or equal to zero");
        if (k > n) throw new IllegalArgumentException("\nk is greater than n");
        return Math.floor(0.5D + Math.exp(Stat.logFactorial(n) - Stat.logFactorial(k) - Stat.logFactorial(n - k))) * Math.pow(p, k) * Math.pow(1.0D - p, n - k);
    }

    public static double binomialCoeff(int n, int k) {
        if (k < 0 || n < 0) throw new IllegalArgumentException("\nn and k must be greater than or equal to zero");
        if (k > n) throw new IllegalArgumentException("\nk is greater than n");
        return Math.floor(0.5D + Math.exp(Stat.logFactorial(n) - Stat.logFactorial(k) - Stat.logFactorial(n - k)));
    }

    public double[] binomialRand(double prob, int nTrials, int n) {
        if (nTrials < n) throw new IllegalArgumentException("Number of deviates requested, " + n + ", must be less than the number of trials, " + nTrials);
        if (prob < 0.0D || prob > 1.0D) throw new IllegalArgumentException("The probablity provided, " + prob + ", must lie between 0 and 1)");
        double[] ran = new double[n];
        Random rr = new Random();
        double binomialDeviate = 0.0D;
        double deviateMean = 0.0D;
        double testDeviate = 0.0D;
        double workingProb = 0.0;
        double logProb = 0.0;
        double probOld = -1.0D;
        double probC = -1.0D;
        double logProbC = -1.0D;
        int nOld = -1;
        double enTrials = 0.0D;
        double oldGamma = 0.0D;
        double tanW = 0.0D;
        double hold0 = 0.0D;
        int jj;
        double probOriginalValue = prob;
        for (int i = 0; i < n; i++) {
            prob = probOriginalValue;
            workingProb = (prob <= 0.5D ? prob : 1.0 - prob);
            deviateMean = nTrials * workingProb;
            if (nTrials < 25) {
                binomialDeviate = 0.0D;
                for (jj = 1; jj <= nTrials; jj++) if (rr.nextDouble() < workingProb) ++binomialDeviate;
            } else if (deviateMean < 1.0D) {
                double expOfMean = Math.exp(-deviateMean);
                testDeviate = 1.0D;
                for (jj = 0; jj <= nTrials; jj++) {
                    testDeviate *= rr.nextDouble();
                    if (testDeviate < expOfMean) break;
                }
                binomialDeviate = (jj <= nTrials ? jj : nTrials);
            } else {
                if (nTrials != nOld) {
                    enTrials = (double) nTrials;
                    oldGamma = Stat.logGamma(enTrials + 1.0D);
                    nOld = nTrials;
                }
                if (workingProb != probOld) {
                    probC = 1.0 - workingProb;
                    logProb = Math.log(workingProb);
                    logProbC = Math.log(probC);
                    probOld = workingProb;
                }
                double sq = Math.sqrt(2.0 * deviateMean * probC);
                do {
                    do {
                        double angle = Math.PI * rr.nextDouble();
                        tanW = Math.tan(angle);
                        hold0 = sq * tanW + deviateMean;
                    } while (hold0 < 0.0D || hold0 >= (enTrials + 1.0D));
                    hold0 = Math.floor(hold0);
                    testDeviate = 1.2D * sq * (1.0D + tanW * tanW) * Math.exp(oldGamma - Stat.logGamma(hold0 + 1.0D) - Stat.logGamma(enTrials - hold0 + 1.0D) + hold0 * logProb + (enTrials - hold0) * logProbC);
                } while (rr.nextDouble() > testDeviate);
                binomialDeviate = hold0;
            }
            if (workingProb != prob) binomialDeviate = nTrials - binomialDeviate;
            ran[i] = binomialDeviate;
        }
        return ran;
    }

    public static double fTestProb(double fValue, int df1, int df2) {
        double ddf1 = (double) df1;
        double ddf2 = (double) df2;
        double x = ddf2 / (ddf2 + ddf1 * fValue);
        return Stat.incompleteBeta(df2 / 2.0D, df1 / 2.0D, x);
    }

    public static double fTestProb(double var1, int df1, double var2, int df2) {
        double fValue = var1 / var2;
        double ddf1 = (double) df1;
        double ddf2 = (double) df2;
        double x = ddf2 / (ddf2 + ddf1 * fValue);
        return Stat.incompleteBeta(df2 / 2.0D, df1 / 2.0D, x);
    }

    public static double fTestValueGivenFprob(double fProb, int df1, int df2) {
        int fTestsNum = 100;
        double[] fTestValues = new double[fTestsNum];
        fTestValues[0] = 0.0001D;
        fTestValues[fTestsNum - 1] = 10000.0D;
        double diff = (Fmath.log10(fTestValues[fTestsNum - 1]) - Fmath.log10(fTestValues[0])) / (fTestsNum - 1);
        for (int i = 1; i < fTestsNum - 1; i++) {
            fTestValues[i] = Math.pow(10.0D, (Fmath.log10(fTestValues[i - 1]) + diff));
        }
        double[] fTestProb = new double[fTestsNum];
        for (int i = 0; i < fTestsNum; i++) {
            fTestProb[i] = Stat.fTestProb(fTestValues[i], df1, df2);
        }
        double fTest0 = 0.0D;
        double fTest1 = 0.0D;
        double fTest2 = 0.0D;
        boolean test0 = true;
        boolean test1 = true;
        int i = 0;
        int endTest = 0;
        while (test0) {
            if (fProb == fTestProb[i]) {
                fTest0 = fTestValues[i];
                test0 = false;
                test1 = false;
            } else {
                if (fProb > fTestProb[i]) {
                    test0 = false;
                    if (i > 0) {
                        fTest1 = fTestValues[i - 1];
                        fTest2 = fTestValues[i];
                        endTest = -1;
                    } else {
                        fTest1 = fTestValues[i] / 10.0D;
                        fTest2 = fTestValues[i];
                    }
                } else {
                    i++;
                    if (i > fTestsNum - 1) {
                        test0 = false;
                        fTest1 = fTestValues[i - 1];
                        fTest2 = 10.0D * fTestValues[i - 1];
                        endTest = 1;
                    }
                }
            }
        }
        if (test1) fTest0 = fTestBisect(fProb, fTest1, fTest2, df1, df2, endTest);
        return fTest0;
    }

    private static double fTestBisect(double fProb, double fTestLow, double fTestHigh, int df1, int df2, int endTest) {
        double funcLow = fProb - Stat.fTestProb(fTestLow, df1, df2);
        double funcHigh = fProb - Stat.fTestProb(fTestHigh, df1, df2);
        double fTestMid = 0.0D;
        double funcMid = 0.0;
        int nExtensions = 0;
        int nIter = 1000;
        double check = fProb * 1e-6;
        boolean test0 = true;
        boolean test1 = true;
        while (test0) {
            if (funcLow * funcHigh > 0.0D) {
                if (endTest < 0) {
                    nExtensions++;
                    if (nExtensions > 100) {
                        System.out.println("Class: Stats\nMethod: fTestBisect\nProbability higher than range covered\nF-test value is less than " + fTestLow);
                        System.out.println("This value was returned");
                        fTestMid = fTestLow;
                        test0 = false;
                        test1 = false;
                    }
                    fTestLow /= 10.0D;
                    funcLow = fProb - Stat.fTestProb(fTestLow, df1, df2);
                } else {
                    nExtensions++;
                    if (nExtensions > 100) {
                        System.out.println("Class: Stats\nMethod: fTestBisect\nProbability lower than range covered\nF-test value is greater than " + fTestHigh);
                        System.out.println("This value was returned");
                        fTestMid = fTestHigh;
                        test0 = false;
                        test1 = false;
                    }
                    fTestHigh *= 10.0D;
                    funcHigh = fProb - Stat.fTestProb(fTestHigh, df1, df2);
                }
            } else {
                test0 = false;
            }
            int i = 0;
            while (test1) {
                fTestMid = (fTestLow + fTestHigh) / 2.0D;
                funcMid = fProb - Stat.fTestProb(fTestMid, df1, df2);
                if (Math.abs(funcMid) < check) {
                    test1 = false;
                } else {
                    i++;
                    if (i > nIter) {
                        System.out.println("Class: Stats\nMethod: fTestBisect\nmaximum number of iterations exceeded\ncurrent value of F-test value returned");
                        test1 = false;
                    }
                    if (funcMid * funcHigh > 0) {
                        funcHigh = funcMid;
                        fTestHigh = fTestMid;
                    } else {
                        funcLow = funcMid;
                        fTestLow = fTestMid;
                    }
                }
            }
        }
        return fTestMid;
    }

    public static double studentT(double tValue, int df) {
        double ddf = (double) df;
        double dfterm = (ddf + 1.0D) / 2.0D;
        return ((Stat.gamma(dfterm) / Stat.gamma(ddf / 2)) / Math.sqrt(ddf * Math.PI)) * Math.pow(1.0D + tValue * tValue / ddf, -dfterm);
    }

    public static double studentTProb(double tValue, int df) {
        double ddf = (double) df;
        double x = ddf / (ddf + tValue * tValue);
        return 0.5D * (1.0D + (Stat.incompleteBeta(ddf / 2.0D, 0.5D, 1) - Stat.incompleteBeta(ddf / 2.0D, 0.5D, x)) * Fmath.sign(tValue));
    }

    public static double probAtn(double tValue, int df) {
        double ddf = (double) df;
        double x = ddf / (ddf + tValue * tValue);
        return 1.0D - Stat.incompleteBeta(ddf / 2.0D, 0.5D, x);
    }

    public static double[][] histogramBins(double[] data, double binWidth, double binZero, double binUpper) {
        int n = 0;
        int m = data.length;
        for (int i = 0; i < m; i++) if (data[i] <= binUpper) n++;
        if (n != m) {
            double[] newData = new double[n];
            int j = 0;
            for (int i = 0; i < m; i++) {
                if (data[i] <= binUpper) {
                    newData[j] = data[i];
                    j++;
                }
            }
            System.out.println((m - n) + " data points, above histogram upper limit, excluded in Stat.histogramBins");
            return histogramBins(newData, binWidth, binZero);
        } else {
            return histogramBins(data, binWidth, binZero);
        }
    }

    public static double[][] histogramBins(double[] data, double binWidth, double binZero) {
        double dmax = Fmath.maximum(data);
        int nBins = (int) Math.ceil((dmax - binZero) / binWidth);
        if (binZero + nBins * binWidth > dmax) nBins++;
        int nPoints = data.length;
        int[] dataCheck = new int[nPoints];
        for (int i = 0; i < nPoints; i++) dataCheck[i] = 0;
        double[] binWall = new double[nBins + 1];
        binWall[0] = binZero;
        for (int i = 1; i <= nBins; i++) {
            binWall[i] = binWall[i - 1] + binWidth;
        }
        double[][] binFreq = new double[2][nBins];
        for (int i = 0; i < nBins; i++) {
            binFreq[0][i] = (binWall[i] + binWall[i + 1]) / 2.0D;
            binFreq[1][i] = 0.0D;
        }
        boolean test = true;
        for (int i = 0; i < nPoints; i++) {
            test = true;
            int j = 0;
            while (test) {
                if (j == nBins - 1) {
                    if (data[i] >= binWall[j] && data[i] <= binWall[j + 1] * (1.0D + Stat.histTol)) {
                        binFreq[1][j] += 1.0D;
                        dataCheck[i] = 1;
                        test = false;
                    }
                } else {
                    if (data[i] >= binWall[j] && data[i] < binWall[j + 1]) {
                        binFreq[1][j] += 1.0D;
                        dataCheck[i] = 1;
                        test = false;
                    }
                }
                if (test) {
                    if (j == nBins - 1) {
                        test = false;
                    } else {
                        j++;
                    }
                }
            }
        }
        int nMissed = 0;
        for (int i = 0; i < nPoints; i++) if (dataCheck[i] == 0) {
            nMissed++;
            System.out.println("p " + i + " " + data[i] + " " + binWall[0] + " " + binWall[nBins]);
        }
        if (nMissed > 0) System.out.println(nMissed + " data points, outside histogram limits, excluded in Stat.histogramBins");
        return binFreq;
    }

    public static double[][] histogramBins(double[] data, double binWidth) {
        double dmin = Fmath.minimum(data);
        double dmax = Fmath.maximum(data);
        double span = dmax - dmin;
        double binZero = dmin;
        int nBins = (int) Math.ceil(span / binWidth);
        double histoSpan = ((double) nBins) * binWidth;
        double rem = histoSpan - span;
        if (rem >= 0) {
            binZero -= rem / 2.0D;
        } else {
            if (Math.abs(rem) / span > histTol) {
                boolean testBw = true;
                double incr = histTol / nBins;
                int iTest = 0;
                while (testBw) {
                    binWidth += incr;
                    histoSpan = ((double) nBins) * binWidth;
                    rem = histoSpan - span;
                    if (rem < 0) {
                        iTest++;
                        if (iTest > 1000) {
                            testBw = false;
                            System.out.println("histogram method could not encompass all data within histogram\nContact Michael thomas Flanagan");
                        }
                    } else {
                        testBw = false;
                    }
                }
            }
        }
        return Stat.histogramBins(data, binWidth, binZero);
    }

    public static int factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("n must be a positive integer");
        if (n > 12) throw new IllegalArgumentException("n must less than 13 to avoid integer overflow\nTry long or double argument");
        int f = 1;
        for (int i = 1; i <= n; i++) f *= i;
        return f;
    }

    public static long factorial(long n) {
        if (n < 0) throw new IllegalArgumentException("n must be a positive integer");
        if (n > 20) throw new IllegalArgumentException("n must less than 21 to avoid long integer overflow\nTry double argument");
        long f = 1;
        for (int i = 1; i <= n; i++) f *= i;
        return f;
    }

    public static double factorial(double n) {
        if (n < 0 || (n - (int) n) != 0) throw new IllegalArgumentException("\nn must be a positive integer\nIs a Gamma funtion [Fmath.gamma(x)] more appropriate?");
        double f = 1.0D;
        int nn = (int) n;
        for (int i = 1; i <= nn; i++) f *= i;
        return f;
    }

    public static double logFactorial(int n) {
        if (n < 0 || (n - (int) n) != 0) throw new IllegalArgumentException("\nn must be a positive integer\nIs a Gamma funtion [Fmath.gamma(x)] more appropriate?");
        double f = 0.0D;
        for (int i = 2; i <= n; i++) f += Math.log(i);
        return f;
    }

    public static double logFactorial(double n) {
        if (n < 0 || (n - (int) n) != 0) throw new IllegalArgumentException("\nn must be a positive integer\nIs a Gamma funtion [Fmath.gamma(x)] more appropriate?");
        double f = 0.0D;
        int nn = (int) n;
        for (int i = 2; i <= nn; i++) f += Math.log(i);
        return f;
    }

    public static double corrCoeff(double[] xx, double[] yy) {
        double temp0 = 0.0D, temp1 = 0.0D;
        int nData = xx.length;
        if (yy.length != nData) throw new IllegalArgumentException("array lengths must be equal");
        int df = nData - 1;
        double mx = 0.0D;
        double my = 0.0D;
        for (int i = 0; i < nData; i++) {
            mx += xx[i];
            my += yy[i];
        }
        mx /= nData;
        my /= nData;
        double s2xx = 0.0D;
        double s2yy = 0.0D;
        double s2xy = 0.0D;
        for (int i = 0; i < nData; i++) {
            s2xx += Fmath.square(xx[i] - mx);
            s2yy += Fmath.square(yy[i] - my);
            s2xy += (xx[i] - mx) * (yy[i] - my);
        }
        s2xx /= df;
        s2yy /= df;
        s2xy /= df;
        double sampleR = s2xy / Math.sqrt(s2xx * s2yy);
        return sampleR;
    }

    public static float corrCoeff(float[] x, float[] y) {
        int nData = x.length;
        if (y.length != nData) throw new IllegalArgumentException("array lengths must be equal");
        int n = x.length;
        double[] xx = new double[n];
        double[] yy = new double[n];
        for (int i = 0; i < n; i++) {
            xx[i] = (double) x[i];
            yy[i] = (double) y[i];
        }
        return (float) Stat.corrCoeff(xx, yy);
    }

    public static double corrCoeff(int[] x, int[] y) {
        int n = x.length;
        if (y.length != n) throw new IllegalArgumentException("array lengths must be equal");
        double[] xx = new double[n];
        double[] yy = new double[n];
        for (int i = 0; i < n; i++) {
            xx[i] = (double) x[i];
            yy[i] = (double) y[i];
        }
        return Stat.corrCoeff(xx, yy);
    }

    public static double corrCoeff(double[] x, double[] y, double[] w) {
        int n = x.length;
        if (y.length != n) throw new IllegalArgumentException("x and y array lengths must be equal");
        if (w.length != n) throw new IllegalArgumentException("x and weight array lengths must be equal");
        double sxy = Stat.covariance(x, y, w);
        double sx = Stat.variance(x, w);
        double sy = Stat.variance(y, w);
        return sxy / Math.sqrt(sx * sy);
    }

    public static double corrCoeff(int element00, int element01, int element10, int element11) {
        return ((double) (element00 * element11 - element01 * element10)) / Math.sqrt((double) ((element00 + element01) * (element10 + element11) * (element00 + element10) * (element01 + element11)));
    }

    public static double corrCoeff(int[][] freqMatrix) {
        double element00 = (double) freqMatrix[0][0];
        double element01 = (double) freqMatrix[0][1];
        double element10 = (double) freqMatrix[1][0];
        double element11 = (double) freqMatrix[1][1];
        return ((element00 * element11 - element01 * element10)) / Math.sqrt(((element00 + element01) * (element10 + element11) * (element00 + element10) * (element01 + element11)));
    }

    public static double linearCorrCoeff(double rCoeff, int nu) {
        return Stat.corrCoeffPdf(rCoeff, nu);
    }

    public static double corrCoeffPdf(double rCoeff, int nu) {
        if (Math.abs(rCoeff) > 1.0D) throw new IllegalArgumentException("|Correlation coefficient| > 1 :  " + rCoeff);
        double a = ((double) nu - 2.0D) / 2.0D;
        double y = Math.pow((1.0D - Fmath.square(rCoeff)), a);
        double preterm = Math.exp(Stat.logGamma((nu + 1.0D) / 2.0) - Stat.logGamma(nu / 2.0D)) / Math.sqrt(Math.PI);
        return preterm * y;
    }

    public static double weibullProb(double mu, double sigma, double gamma, double upperlimit) {
        double arg = (upperlimit - mu) / sigma;
        double y = 0.0D;
        if (arg > 0.0D) y = 1.0D - Math.exp(-Math.pow(arg, gamma));
        return y;
    }

    public static double weibullProb(double mu, double sigma, double gamma, double lowerlimit, double upperlimit) {
        double arg1 = (lowerlimit - mu) / sigma;
        double arg2 = (upperlimit - mu) / sigma;
        double term1 = 0.0D, term2 = 0.0D;
        if (arg1 >= 0.0D) term1 = -Math.exp(-Math.pow(arg1, gamma));
        if (arg2 >= 0.0D) term2 = -Math.exp(-Math.pow(arg2, gamma));
        return term2 - term1;
    }

    public static double weibull(double mu, double sigma, double gamma, double x) {
        double arg = (x - mu) / sigma;
        double y = 0.0D;
        if (arg >= 0.0D) {
            y = (gamma / sigma) * Math.pow(arg, gamma - 1.0D) * Math.exp(-Math.pow(arg, gamma));
        }
        return y;
    }

    public static double weibullMean(double mu, double sigma, double gamma) {
        return mu + sigma * Stat.gamma(1.0D / gamma + 1.0D);
    }

    public static double weibullStandDev(double sigma, double gamma) {
        double y = Stat.gamma(2.0D / gamma + 1.0D) - Fmath.square(Stat.gamma(1.0D / gamma + 1.0D));
        return sigma * Math.sqrt(y);
    }

    public static double weibullMode(double mu, double sigma, double gamma) {
        double y = mu;
        if (gamma > 1.0D) {
            y = mu + sigma * Math.pow((gamma - 1.0D) / gamma, 1.0D / gamma);
        }
        return y;
    }

    public static double weibullMedian(double mu, double sigma, double gamma) {
        return mu + sigma * Math.pow(Math.log(2.0D), 1.0D / gamma);
    }

    public static double[] weibullRand(double mu, double sigma, double gamma, int n) {
        double[] ran = new double[n];
        Random rr = new Random();
        for (int i = 0; i < n; i++) {
            ran[i] = Math.pow(-Math.log(1.0D - rr.nextDouble()), 1.0D / gamma) * sigma + mu;
        }
        return ran;
    }

    public static double[] weibullRand(double mu, double sigma, double gamma, int n, long seed) {
        double[] ran = new double[n];
        Random rr = new Random(seed);
        for (int i = 0; i < n; i++) {
            ran[i] = Math.pow(-Math.log(1.0D - rr.nextDouble()), 1.0D / gamma) * sigma + mu;
        }
        return ran;
    }

    public static double frechetProb(double mu, double sigma, double gamma, double upperlimit) {
        double arg = (upperlimit - mu) / sigma;
        double y = 0.0D;
        if (arg > 0.0D) y = Math.exp(-Math.pow(arg, -gamma));
        return y;
    }

    public static double frechetProb(double mu, double sigma, double gamma, double lowerlimit, double upperlimit) {
        double arg1 = (lowerlimit - mu) / sigma;
        double arg2 = (upperlimit - mu) / sigma;
        double term1 = 0.0D, term2 = 0.0D;
        if (arg1 >= 0.0D) term1 = Math.exp(-Math.pow(arg1, -gamma));
        if (arg2 >= 0.0D) term2 = Math.exp(-Math.pow(arg2, -gamma));
        return term2 - term1;
    }

    public static double exponentialProb(double mu, double sigma, double upperlimit) {
        double arg = (upperlimit - mu) / sigma;
        double y = 0.0D;
        if (arg > 0.0D) y = 1.0D - Math.exp(-arg);
        return y;
    }

    public static double exponentialProb(double mu, double sigma, double lowerlimit, double upperlimit) {
        double arg1 = (lowerlimit - mu) / sigma;
        double arg2 = (upperlimit - mu) / sigma;
        double term1 = 0.0D, term2 = 0.0D;
        if (arg1 >= 0.0D) term1 = -Math.exp(-arg1);
        if (arg2 >= 0.0D) term2 = -Math.exp(-arg2);
        return term2 - term1;
    }

    public static double exponential(double mu, double sigma, double x) {
        double arg = (x - mu) / sigma;
        double y = 0.0D;
        if (arg >= 0.0D) {
            y = Math.exp(-arg) / sigma;
        }
        return y;
    }

    public static double exponentialMean(double mu, double sigma) {
        return mu + sigma;
    }

    public static double exponentialStandDev(double sigma) {
        return sigma;
    }

    public static double exponentialMode(double mu) {
        return mu;
    }

    public static double exponentialMedian(double mu, double sigma) {
        return mu + sigma * Math.log(2.0D);
    }

    public static double[] exponentialRand(double mu, double sigma, int n) {
        double[] ran = new double[n];
        Random rr = new Random();
        for (int i = 0; i < n; i++) {
            ran[i] = mu - Math.log(1.0D - rr.nextDouble()) * sigma;
        }
        return ran;
    }

    public static double[] exponentialRand(double mu, double sigma, int n, long seed) {
        double[] ran = new double[n];
        Random rr = new Random(seed);
        for (int i = 0; i < n; i++) {
            ran[i] = mu - Math.log(1.0D - rr.nextDouble()) * sigma;
        }
        return ran;
    }
}
