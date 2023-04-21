package pl.softech.gpw.candles.patterns.impl2;

import pl.softech.gpw.candles.Candle;

public class CandleLowShadowRule implements IRule {

    @Override
    public boolean match(Candle[] pattern, Candle[] candles) {
        for (int i = 0; i < pattern.length; i++) {
            if (pattern[i].hasLowShadow() != candles[i].hasLowShadow()) {
                return false;
            }
        }
        return true;
    }
}
