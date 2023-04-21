package pl.softech.gpw.candles.patterns.impl;

import pl.softech.gpw.candles.Candle;
import pl.softech.gpw.candles.patterns.ICandlePattern;
import pl.softech.gpw.model.FInstrument;

/**
 * This pattern is a two bar pattern where the first bar is black and the second
 * bar is white. The second white bar engulfs the black bar. This means it is a
 * bigger bar from top to bottom. On the first day, the sellers have control of
 * the stock. As the first candle is only small, this shows that the sellers
 * only have a loose grip on the stock. The second bar shows a significant white
 * candle showing that the buyers have taken control of the stock. This can
 * often be a strong reversal signal.
 * 
 * http://www.howtotradestocks.org/candlestick-patterns.html
 * http://www.hotcandlestick.com/candles.htm
 * http://www.fxwords.com/b/bullish-engulfing-candlestick.html
 * 
 * Objecie Hossy
 * 
 */
public class BullishEngulfing implements ICandlePattern {

    @Override
    public int getBarCount() {
        return 2;
    }

    @Override
    public Type getType() {
        return Type.REVERSAL;
    }

    @Override
    public Direction getDirection() {
        return Direction.BULLISH;
    }

    @Override
    public boolean test(FInstrument[] finstruments) {
        Candle current = new Candle(finstruments[finstruments.length - 1]);
        Candle last = new Candle(finstruments[finstruments.length - 2]);
        if (current.isWhite() && current.hasBothShadows() && current.engulfsBody(last)) {
            if (last.isBlack() && last.hasBothShadows()) {
                FInstrument fcurr = current.instrument();
                FInstrument flast = last.instrument();
                if (flast.getLow() > fcurr.getLow() && flast.getHigh() < fcurr.getHigh()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Reliability getReliability() {
        return Reliability.MODERATE;
    }
}
