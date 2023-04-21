package pl.softech.gpw.transaction;

import java.util.Date;
import pl.softech.gpw.candles.patterns.CandlePattern;
import pl.softech.gpw.candles.patterns.ICandlePattern;
import pl.softech.gpw.candles.patterns.ICandlePatternMetaData.Reliability;
import pl.softech.gpw.candles.patterns.ICandlePatternMetaData.Type;
import pl.softech.gpw.model.FIHelper;
import pl.softech.gpw.model.FInstrument;
import pl.softech.gpw.stat.Adx;
import pl.softech.gpw.stat.AverageIndexes;

public class SimpleTransactionSystem implements ITestable {

    private FInstrument[] instruments;

    private int nAdx;

    private int nSlowSma;

    private int nFastSma;

    private double[] low;

    private double[] high;

    private double[] close;

    private double[] adx;

    private double[] smaSlow;

    private double[] smaFast;

    private int cursor;

    private SimpleTransactionSystem(FInstrument[] instruments, int nAdx, int nSlowSma, int nFastSma) {
        this.instruments = instruments;
        this.nAdx = nAdx;
        this.nSlowSma = nSlowSma;
        this.nFastSma = nFastSma;
        init();
    }

    public SimpleTransactionSystem(FInstrument[] instruments) {
        this(instruments, 45, 100, 15);
    }

    @Override
    public String toString() {
        return instruments[0].getName();
    }

    private void init() {
        low = FIHelper.toLow(instruments);
        high = FIHelper.toHigh(instruments);
        close = FIHelper.toClose(instruments);
        adx = new Adx().count(low, high, close, nAdx);
        smaSlow = AverageIndexes.sma(close, nSlowSma);
        smaFast = AverageIndexes.sma(close, nFastSma);
        cursor = instruments.length - 1;
    }

    @Override
    public boolean start() {
        cursor = Math.max(nAdx, Math.max(nSlowSma, nFastSma));
        cursor = Math.min(cursor, instruments.length - 1);
        return cursor < instruments.length;
    }

    @Override
    public Date getDate() {
        return instruments[cursor].getDate();
    }

    public boolean next() {
        if (cursor < instruments.length - 1) {
            cursor++;
            return true;
        }
        return false;
    }

    @Override
    public Signal emit() {
        if (adx[cursor] >= 20) {
            if (close[cursor] > smaSlow[cursor]) {
                for (ICandlePattern p : CandlePattern.bullish()) {
                    if (p.getType() == Type.CONTINUATION && p.test(instruments)) {
                        return Signal.BUY;
                    }
                }
            }
        }
        if (smaFast[cursor] > close[cursor] && smaFast[cursor - 1] < close[cursor - 1]) {
            for (ICandlePattern p : CandlePattern.bullish()) {
                if (p.getType() == Type.REVERSAL && p.test(instruments)) {
                    if (p.getReliability() == Reliability.STRONG || p.getReliability() == Reliability.MODERATE) {
                    }
                }
            }
            return Signal.SELL;
        }
        return Signal.NONE;
    }

    @Override
    public double getOpen() {
        return instruments[cursor].getOpen();
    }

    @Override
    public double getClose() {
        return instruments[cursor].getClose();
    }
}
