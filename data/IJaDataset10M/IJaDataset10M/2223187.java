package com.peralex.utilities.ui.graphs.hopperHistogram.singleChannel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;
import com.peralex.utilities.ui.graphs.axisscale.AbstractDefaultAxisScale;
import com.peralex.utilities.ui.graphs.graphBase.PixelUnitConverter;

/**
 * FIXME this should probably be a subclass of GridDrawSurface. We should probably move the logarithmic feature into
 * there.
 * 
 * @author Andre
 */
class DrawSurface extends JPanel {

    /**
	 * Default minimum and maximum values of the Axis.
	 */
    protected double dMinX = 1000, dMaxX = 1000000, dMinY = 0, dMaxY = 3;

    /**
	 * These are the handles on the Axis.
	 */
    private AbstractDefaultAxisScale xAxis, yAxis;

    /**
	 * The step size between the vertical gridlines on the x axis.
	 */
    private double xStepSize_Hz;

    /**
	 * The step size between the horizontal gridlines on the y axis.
	 */
    private double yStepSize_Hz;

    /**
	 * Keep track of the current color
	 */
    private Color oBackgroundColor = new Color(0, 0, 0);

    /**
	 * Keep track of the current color
	 */
    private Color oGridColor = new Color(80, 80, 80);

    /**
	 * This flag indicates whether the Grid must be drawn or not.
	 */
    private boolean bGridVisible = true;

    /**
	 * Indicates whether the X-Axis has a logarithmic scale.
	 */
    private boolean bXAxisLogarithmic = true;

    /**
	 * The default constructor for DrawSurface.
	 */
    protected DrawSurface() {
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent evt) {
                resizeGrid();
            }
        });
        setBackgroundColor(oBackgroundColor);
        resizeGrid();
    }

    /**
	 * Draw horizontal grid lines
	 * 
	 * @param g2d The graphics area on which to draw the grid lines
	 */
    public void drawHorizontalGrid(Graphics2D g2d) {
        if (yAxis == null) return;
        yAxis.clear();
        final double firstValue_Hz = dMinY + ((dMinY % yStepSize_Hz) < 0 ? (Math.abs(dMinY % yStepSize_Hz)) : (dMinY % yStepSize_Hz));
        final int numYSteps = (int) ((dMaxY - firstValue_Hz) / yStepSize_Hz);
        g2d.setColor(oGridColor);
        int y1 = PixelUnitConverter.unitToPixel(false, firstValue_Hz, 0, getHeight(), dMinY, dMaxY);
        int y2 = y1;
        final int x1 = 0;
        final int x2 = getWidth();
        for (int i = 0; i < numYSteps + 1; i++) {
            final double dYLabel = firstValue_Hz + (i * yStepSize_Hz);
            yAxis.addLabel(y1 + 5, (float) dYLabel);
            if (bGridVisible) {
                g2d.drawLine(x1, y1, x2, y2);
            }
            y1 = y2 = PixelUnitConverter.unitToPixel(false, firstValue_Hz + ((i + 1) * yStepSize_Hz), 0, getHeight(), dMinY, dMaxY);
        }
        yAxis.repaint();
    }

    /**
	 * Draw vertical grid lines
	 * 
	 * @param g2d The graphics area on which to draw the grid lines
	 */
    public void drawVerticalGrid(Graphics2D g2d) {
        if (xAxis == null) return;
        if (bXAxisLogarithmic) {
            xAxis.clear();
            g2d.setColor(oGridColor);
            final int iPowerMax = (int) (Math.log10(dMaxX) + 0.5);
            final int iPowerMin = (int) (Math.log10(dMinX) + 0.5);
            final int iNumberOfGridLines = iPowerMax - iPowerMin;
            final double dGridStepSize = ((dMaxX - dMinX) / iNumberOfGridLines);
            for (int i = iPowerMin; i <= iPowerMax; i++) {
                final double dXLabel = Math.ceil((Math.pow(10, i)));
                final int iXCoordinate = PixelUnitConverter.unitToPixel(true, dMinX + (dGridStepSize * (i - iPowerMin)), 0, getWidth(), dMinX, dMaxX);
                if (i == iPowerMin) {
                    xAxis.addLabel(iXCoordinate + 30, (float) dXLabel);
                } else if (i == iPowerMax) {
                    xAxis.addLabel(iXCoordinate + 5, (float) dXLabel);
                } else {
                    xAxis.addLabel(iXCoordinate + 25, (float) dXLabel);
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.drawLine(iXCoordinate, 0, iXCoordinate, getHeight());
                    g2d.setStroke(new BasicStroke(1.0f));
                }
                if (i < iPowerMax) {
                    final double dStartValue = (dMinX + (dGridStepSize * (i - iPowerMin)));
                    for (int a = 1; a < 10; a++) {
                        final double dLineScaleFactor = Math.log10((Math.pow(10, i)) * a);
                        final int iXCoordinate2 = PixelUnitConverter.unitToPixel(true, dStartValue + (dGridStepSize * (dLineScaleFactor - i)), 0, getWidth(), dMinX, dMaxX);
                        g2d.drawLine(iXCoordinate2, 0, iXCoordinate2, getHeight());
                    }
                }
            }
            xAxis.repaint();
        } else {
            xAxis.clear();
            g2d.setColor(oGridColor);
            final double firstValue_Hz = Math.ceil(dMinX / xStepSize_Hz) * xStepSize_Hz;
            final int numXSteps = (int) ((dMaxX - firstValue_Hz) / xStepSize_Hz);
            int x1 = PixelUnitConverter.unitToPixel(true, firstValue_Hz, 0, getWidth(), dMinX, dMaxX);
            int x2 = x1;
            final int y1 = 0;
            final int y2 = getHeight();
            for (int i = 0; i < numXSteps + 1; i++) {
                final double dXLabel = firstValue_Hz + (i * xStepSize_Hz);
                if (dXLabel % 1 == 0) {
                    xAxis.addLabel(x1 + 35, (float) dXLabel);
                    if (bGridVisible) {
                        g2d.drawLine(x1, y1, x2, y2);
                    }
                }
                x1 = x2 = PixelUnitConverter.unitToPixel(true, firstValue_Hz + ((i + 1) * xStepSize_Hz), 0, getWidth(), dMinX, dMaxX);
            }
            xAxis.repaint();
        }
    }

    /**
	 * This will convert the given value to a pixel value on the graph's X-Axis.
	 */
    public int logUnitToPixel(double dValue, double dMinimumValue, double dMaximumValue) {
        double dPowerMin = Math.log10(dMinimumValue);
        double dPowerMax = Math.log10(dMaximumValue) - dPowerMin;
        double dPowerValue = Math.log10(dValue) - dPowerMin;
        return (int) (getWidth() * (dPowerValue / dPowerMax));
    }

    /**
	 * This will convert the given pixel value to a value on the graph's X-Axis.
	 */
    public double logXPixelToUnit(double dValue) {
        double dPowerMin = Math.log10(dMinX);
        double dPowerMax = Math.log10(dMaxX);
        double dPowerValue = ((dValue / getWidth()) * (dPowerMax - dPowerMin)) + dPowerMin;
        return Math.pow(10, dPowerValue);
    }

    /**
	 * Calculate the no of spaces along the axis.
	 * 
	 * @param approxSpaces The number of spaces that would look nice for a particular size of drawSurface.
	 * @param max The maximum value on the axis.
	 * @param min The minimum value on the axis.
	 * 
	 * @return The step size across the axis in the same unit as arguments min & max.
	 */
    private static double calculateStepSize(int approxSpaces, double min, double max) {
        double m = Math.log10((max - min) / approxSpaces);
        double floorM = Math.floor(m);
        double remainder = m - floorM;
        int f = 0;
        if (remainder <= 0.15) {
            f = 1;
        } else if (remainder <= 0.5) {
            f = 2;
        } else if (remainder <= 0.85) {
            f = 5;
        } else {
            f = 10;
        }
        return f * Math.pow(10.0, floorM);
    }

    /**
	 * This method resizes the current grid.
	 */
    public final void resizeGrid() {
        yStepSize_Hz = calculateStepSize(getHeight() / 50, dMinY, dMaxY);
        xStepSize_Hz = calculateStepSize(getWidth() / 100, dMinX, dMaxX);
        repaint();
    }

    /**
	 * This method sets the Background color of this graph.
	 */
    public final void setBackgroundColor(Color oBackgroundColor) {
        this.oBackgroundColor = oBackgroundColor;
        setBackground(oBackgroundColor);
        repaint();
    }

    /**
	 * This method sets the Grid Color of this graph.
	 */
    public void setGridColor(Color oGridColor) {
        this.oGridColor = oGridColor;
        repaint();
    }

    /**
	 * Sets the axes.
	 * 
	 * @param xAxis The axis to be displayed in the conventional x position (horizontal, bottom).
	 * @param yAxis The axis to be displayed in the conventional y position (vertical, left).
	 */
    public void setAxes(AbstractDefaultAxisScale xAxis, AbstractDefaultAxisScale yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        repaint();
    }

    /**
	 * This methods sets the XAxisRange.
	 * 
	 * @param dXAxisMinimum contains the minimum
	 * @param dXAxisMaximum contains the maximum
	 * @param dYAxisMinimum contains the minimum
	 * @param dYAxisMaximum contains the maximum
	 */
    public synchronized void setAxisRanges(double dXAxisMinimum, double dXAxisMaximum, double dYAxisMinimum, double dYAxisMaximum) {
        this.dMinX = dXAxisMinimum;
        this.dMaxX = dXAxisMaximum;
        this.dMinY = dYAxisMinimum;
        this.dMaxY = dYAxisMaximum;
        resizeGrid();
        repaint();
    }

    /**
	 * This methods sets the XAxisRange.
	 * 
	 * @param dXAxisMinimum contains the minimum
	 * @param dXAxisMaximum contains the maximum
	 */
    public synchronized void setXAxisRange(double dXAxisMinimum, double dXAxisMaximum) {
        this.dMinX = dXAxisMinimum;
        this.dMaxX = dXAxisMaximum;
        resizeGrid();
        repaint();
    }

    /**
	 * This methods sets the YAxisRange.
	 * 
	 * @param dYAxisMinimum contains the minimum
	 * @param dYAxisMaximum contains the maximum
	 */
    public synchronized void setYAxisRange(double dYAxisMinimum, double dYAxisMaximum) {
        this.dMinY = dYAxisMinimum;
        this.dMaxY = dYAxisMaximum;
        resizeGrid();
        repaint();
    }

    /**
	 * Get the XAxis Minimum.
	 * 
	 * @return dMinX.
	 */
    public double getXAxisMinimum() {
        return dMinX;
    }

    /**
	 * Get the XAxis Maximum.
	 * 
	 * @return dMaxX.
	 */
    public double getXAxisMaximum() {
        return dMaxX;
    }

    /**
	 * Get the YAxis Minimum.
	 * 
	 * @return dMinY.
	 */
    public double getYAxisMinimum() {
        return dMinY;
    }

    /**
	 * Get the YAxis Maximum.
	 * 
	 * @return dMaxY.
	 */
    public double getYAxisMaximum() {
        return dMaxY;
    }

    /**
	 * This method sets whether the grid must be drawn or not.
	 */
    public void setGridVisible(boolean _bShowGrid) {
        this.bGridVisible = _bShowGrid;
        repaint();
    }

    /**
	 * This method sets whether the XAxis is Logarithmic or not.
	 */
    public void setXAxisLogarithmic(boolean bXAxisLogarithmic) {
        this.bXAxisLogarithmic = bXAxisLogarithmic;
        resizeGrid();
        repaint();
    }

    /**
	 * This method returns whether the XAxis is Logarithmic or not.
	 * 
	 * @return bXAxisLogarithmic.
	 */
    public boolean isXAxisLogarithmic() {
        return bXAxisLogarithmic;
    }
}
