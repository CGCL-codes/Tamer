package com.nexirius.framework.trend;

import java.awt.*;

public class TrendGridDouble extends TrendGrid {

    DoubleRange range;

    public TrendGridDouble(DoubleRange r) {
        r.round();
        range = r;
        xAchsis = false;
    }

    public LabelPositionVector getPositions(Rectangle rect) {
        LabelPositionVector v = new LabelPositionVector();
        double width = range.base() / 10.0;
        boolean useLong = true;
        double glitch = 0.0;
        if (width <= 1) {
            useLong = true;
            glitch = width / 1000000.0;
        }
        double value = range.min();
        while (value <= range.max()) {
            String label;
            if (useLong) {
                Long l = new Long((long) value);
                label = l.toString();
            } else {
                label = Double.toString(value);
            }
            v.append(new LabelPosition(getPosition(rect, value), label));
            value += width + glitch;
        }
        return v;
    }

    public int getPosition(Rectangle rect, double d) {
        return (int) (rect.getY() + rect.getHeight()) - (int) (rect.getHeight() * range.getRelativeLocation(d));
    }
}
