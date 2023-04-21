package kersoft.chart;

import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * The bar chart produces a bar graph of the data from the associated chart model. To set the data model call
 * setDataModel(ChartModel). See note below for information on using this bean with visual development environments.
 * 
 * NOTE: When using visual development environments the model for the chart will need to be added manually. This is done
 * by creating an instance of the ChartModel you require and then adding this to the chart before the chart is
 * displayed. This is done using the chart.setDataModel(ChartModel) method.
 * 
 * @author Andrew AJ Cain
 */
public class KSBarChart extends ScaledChart {

    /**
	 * The starting position for ticks on the x axis.
	 */
    private int startXtick = 0;

    /**
	 * The ending tick on the x axis.
	 */
    private int stopXtick = 0;

    /**
	 * The margin for the border around the x axis
	 */
    private int borderXmargin = 2;

    /**
	 * The width of a bar in the system.
	 */
    private int barSetWidth = 0;

    /**
	 * The width of the bar in the system.
	 */
    private int barWidth = 0;

    /**
	 * The constructor that builds the barchart with an associated chart model.
	 * 
	 * @param dataModel
	 *            The data model that will be used as the data for the chart.
	 */
    public KSBarChart(ChartModel dataModel) {
        this();
        setDataModel(dataModel);
    }

    /**
	 * The default constructor.
	 */
    public KSBarChart() {
        setZeroYscale(true);
    }

    /**
	 * This method draws the x Axis.
	 * 
	 * @param g
	 *            The graphics to use to draw the axis.
	 */
    protected void drawXaxis(Graphics g) {
        if (hasData() == false) return;
        g.setFont(getXaxisFont());
        FontMetrics metrics = g.getFontMetrics(getXaxisFont());
        borderXheight = getGraphMargin() + metrics.getHeight() * 2;
        startXtick = pointDisplay.x + borderXmargin;
        stopXtick = pointDisplay.x + pointDisplay.width;
        barSetWidth = (stopXtick - startXtick) / getDataPoints() - borderXmargin;
        barWidth = barSetWidth / getDataSets();
        for (int i = 0; i < getDataPoints(); i++) {
            g.setColor(getAxisColor());
            int sw = 0;
            String szLabel = new String("");
            if (getPointLabel(i) != null) szLabel = getPointLabel(i);
            sw = metrics.stringWidth(szLabel);
            int xRegion = startXtick + i * (barSetWidth + borderXmargin);
            int px = xRegion + barSetWidth / 2 - sw / 2;
            if (drawAt(i, metrics.stringWidth(szLabel))) {
                if (drawXaxisStringBelow(i)) {
                    g.drawString(szLabel, px, getValueYpos(0.0) + 2 + metrics.getHeight());
                } else {
                    g.drawString(szLabel, px, getValueYpos(0.0) - 3);
                }
            }
            px = xRegion + barSetWidth / 2;
            drawSets(i, xRegion, g);
        }
    }

    /**
	 * Determines if the label should be drawn at the specified point based on its width.
	 * 
	 * @param iPoint
	 *            The point to be drawn
	 * @param width
	 *            The width of the point label to be drawn.
	 * @return True if the label should be drawn.
	 */
    private boolean drawAt(int iPoint, int width) {
        boolean return_val = true;
        if (barSetWidth > 0) {
            if (barSetWidth < width) {
                int labelInterval = (width / barSetWidth) + 1;
                if ((iPoint % labelInterval) != 0) {
                    return_val = false;
                }
            }
        } else {
            return_val = false;
        }
        return return_val;
    }

    /**
	 * This method draws the sets for iPoint at xRegion using graphics g. This method draws the actual bars.
	 * 
	 * @param iPoint
	 *            The point to get the sets for
	 * @param xRedion
	 *            The region on the x axis that will be drawn at.
	 * @param g
	 *            The graphic to draw to.
	 */
    private void drawSets(int iPoint, int xRegion, Graphics g) {
        int barHeight = 0;
        for (int s = 0; s < getDataSets(); s++) {
            int px = xRegion + s * barWidth;
            double v = getPoint(s, iPoint);
            int py = getValueYpos(v);
            barHeight = 0;
            if (v > 0) {
                barHeight = getValueYpos(0.0) - py;
            } else {
                barHeight = py - getValueYpos(0.0);
                py = getValueYpos(0.0);
            }
            if (v != 0.0) {
                g.setColor(getLegendColor(s));
                g.fill3DRect(px, py, barWidth, barHeight, true);
            }
        }
    }

    /**
	 * Determins if the point label needs to be drawn above or below the line.
	 * 
	 * @param iPoint
	 *            The point to check for.
	 * @return true if the string should appear below the line.
	 */
    private boolean drawXaxisStringBelow(int iPoint) {
        boolean return_val = true;
        int iSet = getDataSets() / 2;
        double value = getPoint(iSet, iPoint);
        if (value < 0) return_val = false;
        return return_val;
    }

    /**
	 * This draws the chart.
	 * 
	 * @param g
	 *            The graphics to draw to.
	 */
    public void paint(Graphics g) {
        super.paint(g);
        drawXaxis(g);
    }
}
