package com.jrefinery.chart;

import java.awt.*;
import java.awt.geom.*;
import com.jrefinery.data.*;

/**
 * A renderer for 3D-effect bars...
 */
public class VerticalBarRenderer3D extends VerticalBarRenderer {

    /**
     * Default constructor.
     */
    public VerticalBarRenderer3D() {
    }

    /**
     * Returns true, since there are (potentially) gaps between bars in this representation.
     */
    public boolean hasItemGaps() {
        return true;
    }

    /**
     * This will be a method in the renderer that tells whether there is one bar width per category
     * or onebarwidth per series per category.
     */
    public int barWidthsPerCategory(CategoryDataset data) {
        return data.getSeriesCount();
    }

    /**
     * Renders an individual bar...there are bug-fixes that have been applied to VerticalBarRenderer
     * that need to be applied here too.
     */
    public Shape drawBar(Graphics2D g2, Rectangle2D dataArea, BarPlot plot, ValueAxis valueAxis, CategoryDataset data, int series, Object category, int categoryIndex, double translatedZero, double itemWidth, double categorySpan, double categoryGapSpan, double itemSpan, double itemGapSpan) {
        Shape result = null;
        Number value = data.getValue(series, category);
        if (value != null) {
            double rectX = dataArea.getX() + dataArea.getWidth() * plot.getIntroGapPercent();
            int categories = data.getCategoryCount();
            int seriesCount = data.getSeriesCount();
            if (categories > 1) {
                rectX = rectX + categoryIndex * (categorySpan / categories) + (categoryIndex * (categoryGapSpan / (categories - 1)) + (series * itemSpan / (categories * seriesCount)));
                if (seriesCount > 1) {
                    rectX = rectX + (series * itemGapSpan / (categories * (seriesCount - 1)));
                }
            } else {
                rectX = rectX + (series * itemSpan / (categories * seriesCount));
                if (seriesCount > 1) {
                    rectX = rectX + (series * itemGapSpan / (categories * (seriesCount - 1)));
                }
            }
            double translatedValue = valueAxis.translateValueToJava2D(value.doubleValue(), dataArea);
            double rectY = Math.min(translatedZero, translatedValue);
            double rectWidth = itemWidth;
            double rectHeight = Math.abs(translatedValue - translatedZero);
            Rectangle2D bar = new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
            Paint seriesPaint = plot.getSeriesPaint(series);
            g2.setPaint(seriesPaint);
            g2.fill(bar);
            result = bar;
            GeneralPath bar3dRight = null;
            GeneralPath bar3dTop = null;
            double effect3d = 0.00;
            VerticalAxis vAxis = plot.getVerticalAxis();
            if (rectHeight != 0 && vAxis instanceof VerticalNumberAxis3D) {
                effect3d = ((VerticalNumberAxis3D) vAxis).getEffect3d();
                bar3dRight = new GeneralPath();
                bar3dRight.moveTo((float) (rectX + rectWidth), (float) rectY);
                bar3dRight.lineTo((float) (rectX + rectWidth), (float) (rectY + rectHeight));
                bar3dRight.lineTo((float) (rectX + rectWidth + effect3d), (float) (rectY + rectHeight - effect3d));
                bar3dRight.lineTo((float) (rectX + rectWidth + effect3d), (float) (rectY - effect3d));
                if (seriesPaint instanceof Color) {
                    g2.setPaint(((Color) seriesPaint).darker());
                }
                g2.fill(bar3dRight);
                bar3dTop = new GeneralPath();
                bar3dTop.moveTo((float) rectX, (float) rectY);
                bar3dTop.lineTo((float) (rectX + effect3d), (float) (rectY - effect3d));
                bar3dTop.lineTo((float) (rectX + rectWidth + effect3d), (float) (rectY - effect3d));
                bar3dTop.lineTo((float) (rectX + rectWidth), (float) (rectY));
                if (seriesPaint instanceof Color) {
                    g2.setPaint(((Color) seriesPaint));
                }
                g2.fill(bar3dTop);
            }
            if (itemWidth > 3) {
                g2.setStroke(plot.getSeriesOutlineStroke(series));
                g2.setPaint(plot.getSeriesOutlinePaint(series));
                g2.draw(bar);
                if (bar3dRight != null) {
                    g2.draw(bar3dRight);
                }
                if (bar3dTop != null) {
                    g2.draw(bar3dTop);
                }
            }
        }
        return result;
    }
}
