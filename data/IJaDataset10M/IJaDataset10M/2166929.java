package pl.softech.gpw.candles;

import java.io.Serializable;
import pl.softech.gpw.model.FInstrument;

public class Candle implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    public enum Color {

        BLACK, WHITE
    }

    private FInstrument fInstrument;

    public Candle(FInstrument fInstrument) {
        this.fInstrument = fInstrument;
    }

    public Color getColor() {
        if (fInstrument.getClose() > fInstrument.getOpen()) {
            return Color.WHITE;
        }
        return Color.BLACK;
    }

    public FInstrument instrument() {
        return fInstrument;
    }

    public boolean isWhite() {
        return Color.WHITE == getColor();
    }

    public boolean isBlack() {
        return Color.BLACK == getColor();
    }

    public boolean isBelow(Candle cand) {
        return maxBody() < cand.maxBody() && fInstrument.getHigh() < cand.instrument().getHigh();
    }

    public boolean isAbove(Candle cand) {
        return maxBody() > cand.maxBody() && fInstrument.getHigh() > cand.instrument().getHigh();
    }

    private double maxBody() {
        return Math.max(fInstrument.getClose(), fInstrument.getOpen());
    }

    private double minBody() {
        return Math.min(fInstrument.getClose(), fInstrument.getOpen());
    }

    public double getBodyLen() {
        return Math.abs(fInstrument.getClose() - fInstrument.getOpen());
    }

    public double getLen() {
        return fInstrument.getHigh() - fInstrument.getLow();
    }

    public boolean hasHighShadow() {
        return fInstrument.getHigh() > maxBody();
    }

    public boolean hasLowShadow() {
        return fInstrument.getLow() < minBody();
    }

    public double getLowShadowLen() {
        return minBody() - fInstrument.getLow();
    }

    public double getHighShadowLen() {
        return fInstrument.getHigh() - maxBody();
    }

    public boolean hasBothShadows() {
        return hasHighShadow() && hasLowShadow();
    }

    public boolean engulfsBody(Candle cand) {
        return maxBody() > cand.maxBody() && minBody() < cand.minBody();
    }

    public boolean isDoji() {
        return instrument().getClose() == instrument().getOpen();
    }

    public boolean hasRelativeShortHighShadow(Candle[] candles) {
        double max = Double.MIN_VALUE;
        for (Candle cand : candles) {
            if (cand == this) {
                continue;
            }
            if (cand.getLen() > max) {
                max = cand.getLen();
            }
        }
        return getHighShadowLen() / max < 0.25;
    }

    public boolean hasRelativeShortLowShadow(Candle[] candles) {
        double max = Double.MIN_VALUE;
        for (Candle cand : candles) {
            if (cand == this) {
                continue;
            }
            if (cand.getLen() > max) {
                max = cand.getLen();
            }
        }
        return getLowShadowLen() / max < 0.25;
    }

    public boolean hasRelativeShortShadows(Candle[] candles) {
        return hasRelativeShortHighShadow(candles) && hasRelativeShortLowShadow(candles);
    }

    public boolean hasLongBody() {
        return getBodyLen() / getLen() >= 0.75;
    }

    public boolean hasRelativeLongBody(Candle[] candles) {
        double minBody = Double.MAX_VALUE;
        for (Candle cand : candles) {
            if (cand == this) {
                continue;
            }
            if (cand.getBodyLen() < minBody) {
                minBody = cand.getBodyLen();
            }
        }
        return minBody / (0.0001 + getBodyLen()) < 0.25;
    }

    public boolean intersectBody(double value) {
        return value >= minBody() && value <= maxBody();
    }

    public static Candle[] create(FInstrument[] finstruments, int barCount) {
        Candle[] candlesticks = new Candle[barCount];
        for (int i = 0; i < candlesticks.length; i++) {
            candlesticks[i] = new Candle(finstruments[finstruments.length - candlesticks.length + i]);
        }
        return candlesticks;
    }

    @Override
    public Candle clone() {
        Candle clone;
        try {
            clone = (Candle) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        clone.fInstrument = fInstrument.clone();
        return clone;
    }
}
