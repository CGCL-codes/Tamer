package org.openquant.backtest;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class Series {

    private double values[];

    private Core lib = new Core();

    MInteger outBegIdx = new MInteger();

    MInteger outNbElement = new MInteger();

    public Series(double[] vals) {
        values = vals;
    }

    public double getAt(int i) {
        return values[i];
    }

    public int size() {
        return values.length;
    }

    public double getLast() {
        return values[values.length];
    }

    private double[] adjust(double[] source, int lookback) {
        double rArray[] = new double[source.length];
        int i = 0, j = 0;
        for (double value : source) {
            if (i < lookback) {
                rArray[i] = 0.0;
            } else {
                rArray[i] = source[j++];
            }
            i++;
        }
        return rArray;
    }

    public Series EMA(int lookback) {
        double output[] = new double[values.length];
        lookback = lib.emaLookback(lookback);
        RetCode retCode = lib.ema(0, values.length - 1, values, lookback, outBegIdx, outNbElement, output);
        return new Series(adjust(output, lookback - 1));
    }

    public Series SMA(int lookback) {
        double output[] = new double[values.length];
        lookback = lib.smaLookback(lookback);
        RetCode retCode = lib.sma(0, values.length - 1, values, lookback, outBegIdx, outNbElement, output);
        return new Series(adjust(output, lookback - 1));
    }

    public Series WMA(int lookback) {
        double output[] = new double[values.length];
        lookback = lib.wmaLookback(lookback);
        RetCode retCode = lib.wma(0, values.length - 1, values, lookback, outBegIdx, outNbElement, output);
        return new Series(adjust(output, lookback - 1));
    }

    public Series RSI(int lookback) {
        double output[] = new double[values.length];
        lookback = lib.rsiLookback(lookback);
        RetCode retCode = lib.rsi(0, values.length - 1, values, lookback, outBegIdx, outNbElement, output);
        return new Series(adjust(output, lookback - 1));
    }

    public Series ROC(int lookback) {
        double output[] = new double[values.length];
        lookback = lib.rocLookback(lookback);
        RetCode retCode = lib.roc(0, values.length - 1, values, lookback, outBegIdx, outNbElement, output);
        return new Series(adjust(output, lookback - 1));
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (double value : values) {
            buffer.append(value);
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
