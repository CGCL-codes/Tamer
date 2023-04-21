package pl.softech.gpw.candles.patterns.impl2;

import pl.softech.gpw.candles.Candle;

public class CandleRelativeShortLowShadowRule implements IRule {

    @Override
    public boolean match(Candle[] pattern, Candle[] candles) {
        for (int i = 0; i < pattern.length; i++) {
            if (pattern[i].hasRelativeShortLowShadow(pattern) != candles[i].hasRelativeShortLowShadow(candles)) {
                return false;
            }
        }
        return true;
    }
}
