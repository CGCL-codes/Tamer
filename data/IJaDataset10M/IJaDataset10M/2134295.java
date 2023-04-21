package pl.softech.gpw.stat;

/**
 * Average Directional Index (ADX)
 * http://en.wikipedia.org/wiki/Average_Directional_Index
 */
public class Adx {

    private interface Function {

        double[] compute(double[] arg);
    }

    private static double upMove(double highLast, double highNow) {
        double tmp = highNow - highLast;
        return tmp < 0 ? 0 : tmp;
    }

    private static double downMove(double lowLast, double lowNow) {
        double tmp = lowLast - lowNow;
        return tmp < 0 ? 0 : tmp;
    }

    private static double tr(double low, double high, double prevClose) {
        return Math.max(high, prevClose) - Math.min(low, prevClose);
    }

    private double[] plusDI;

    private double[] minusDI;

    private double[] adx;

    private double[] tr;

    public double[] getPlusDI() {
        return plusDI;
    }

    public double[] getAdx() {
        return adx;
    }

    public double[] getMinusDI() {
        return minusDI;
    }

    public double[] getTr() {
        return tr;
    }

    public double[] count(double[] low, double[] high, double[] close, final double weight, final int n) {
        return count(low, high, close, new Function() {

            @Override
            public double[] compute(double[] arg) {
                return AverageIndexes.ema(arg, weight, n);
            }
        });
    }

    public double[] count(double[] low, double[] high, double[] close, final int n) {
        return count(low, high, close, new Function() {

            @Override
            public double[] compute(double[] arg) {
                return AverageIndexes.sma(arg, n);
            }
        });
    }

    private double[] count(double[] low, double[] high, double[] close, Function function) {
        adx = new double[close.length];
        plusDI = new double[close.length];
        minusDI = new double[close.length];
        tr = new double[close.length];
        plusDI[0] = Double.NaN;
        minusDI[0] = Double.NaN;
        tr[0] = Double.NaN;
        for (int i = 1; i < adx.length; i++) {
            double upMove = upMove(high[i - 1], high[i]);
            double downMove = downMove(low[i - 1], low[i]);
            double plusDM = 0;
            double minusDM = 0;
            if (upMove > downMove && upMove > 0) {
                plusDM = upMove;
            }
            if (downMove > upMove && downMove > 0) {
                minusDM = downMove;
            }
            tr[i] = tr(low[i], high[i], close[i - 1]);
            plusDI[i] = plusDM;
            minusDI[i] = minusDM;
        }
        plusDI = function.compute(plusDI);
        minusDI = function.compute(minusDI);
        tr = function.compute(tr);
        for (int i = 0; i < adx.length; i++) {
            if (Double.isNaN(plusDI[i]) || Double.isNaN(minusDI[i])) {
                adx[i] = Double.NaN;
                continue;
            }
            plusDI[i] = plusDI[i] / tr[i] * 100;
            minusDI[i] = minusDI[i] / tr[i] * 100;
            double m = plusDI[i] + minusDI[i];
            adx[i] = 100 * Math.abs(plusDI[i] - minusDI[i]) / m;
        }
        return function.compute(adx);
    }
}
