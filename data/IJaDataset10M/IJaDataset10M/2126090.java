package pl.softech.gpw.candles.patterns.impl;

import pl.softech.gpw.candles.Candle;
import pl.softech.gpw.candles.patterns.ICandlePattern;
import pl.softech.gpw.model.FInstrument;

/**
 * http://www.fxwords.com/b/bullish-belt-hold.html 
 * 
 * http://www.investopedia.com/terms/b/bullishbelthold.asp
 */
public class BullishBeltHold implements ICandlePattern {

    @Override
    public Type getType() {
        return Type.REVERSAL;
    }

    @Override
    public int getBarCount() {
        return 1;
    }

    @Override
    public Direction getDirection() {
        return Direction.BULLISH;
    }

    @Override
    public Reliability getReliability() {
        return Reliability.WEAK;
    }

    @Override
    public boolean test(FInstrument[] finstruments) {
        Candle[] candlesticks = Candle.create(finstruments, 3);
        if (candlesticks[2].isWhite() && !candlesticks[2].hasLowShadow() && candlesticks[2].hasHighShadow()) {
            Candle last = candlesticks[0];
            for (int i = 1; i < candlesticks.length - 1; i++) {
                if (!candlesticks[i].isAbove(last)) {
                    return false;
                }
                last = candlesticks[i];
            }
            return true;
        }
        return false;
    }
}
