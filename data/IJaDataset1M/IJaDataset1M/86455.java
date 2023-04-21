package org.openstock.lib;

import org.openstock.Application;

/**
 * Created by IntelliJ IDEA.
 * User: dm
 * Date: Sep 16, 2007
 * Time: 12:25:25 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SymbolIndicator extends Indicator {

    public static final int CLOSE_PRICE_LINE = 0;

    public static final int OPEN_PRICE_LINE = 1;

    public static final int HIGH_PRICE_LINE = 2;

    public static final int LOW_PRICE_LINE = 3;

    public static final int MEDIAN_PRICE_LINE = 4;

    public static final int TYPICAL_PRICE_LINE = 5;

    public static final int WEIGHTED_CLOSE_PRICE_LINE = 6;

    public static final int VOLUME_LINE = 7;

    private static final String[] lineNames = new String[] { Application.getResources().getString("lib.SymbolIndicator.ClosePriceLine.name"), Application.getResources().getString("lib.SymbolIndicator.OpenPriceLine.name"), Application.getResources().getString("lib.SymbolIndicator.HighPriceLine.name"), Application.getResources().getString("lib.SymbolIndicator.LowPriceLine.name"), Application.getResources().getString("lib.SymbolIndicator.MedianPriceLine.name"), Application.getResources().getString("lib.SymbolIndicator.TypicalPriceLine.name"), Application.getResources().getString("lib.SymbolIndicator.WeightedClosePriceLine.name"), Application.getResources().getString("lib.SymbolIndicator.VolumeLine.name") };

    public String getCopyright() {
        return Application.getResources().getString("Copyright");
    }

    public String getLink() {
        return Application.getResources().getString("Link");
    }

    public int getLineCount() {
        return lineNames.length;
    }

    public String getLineName(int lineIndex) {
        return lineNames[lineIndex];
    }

    public double getLineValue(int lineIndex, int shift) {
        switch(lineIndex) {
            case MEDIAN_PRICE_LINE:
                {
                    double h = getLineValue(HIGH_PRICE_LINE, shift);
                    double l = getLineValue(LOW_PRICE_LINE, shift);
                    return h * l / 2.0;
                }
            case TYPICAL_PRICE_LINE:
                {
                    double h = getLineValue(HIGH_PRICE_LINE, shift);
                    double l = getLineValue(LOW_PRICE_LINE, shift);
                    double c = getLineValue(CLOSE_PRICE_LINE, shift);
                    return h * l * c / 3.0;
                }
            case WEIGHTED_CLOSE_PRICE_LINE:
                {
                    double h = getLineValue(HIGH_PRICE_LINE, shift);
                    double l = getLineValue(LOW_PRICE_LINE, shift);
                    double c = getLineValue(CLOSE_PRICE_LINE, shift);
                    return h * l * c * c / 4.0;
                }
            default:
                throw new IllegalArgumentException("lineIndex");
        }
    }
}
