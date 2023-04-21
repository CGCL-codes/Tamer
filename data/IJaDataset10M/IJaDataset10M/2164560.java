package org.mcisb.ui.util.data;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import org.mcisb.util.*;
import org.mcisb.util.data.*;

/**
 *
 * @author Neil Swainston
 */
public abstract class DataDisplayPanel extends JPanel implements Manipulatable, Disposable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    protected static final int AXES_BORDER = 50;

    /**
	 * 
	 */
    protected static final int TEXT_BORDER = 10;

    /**
	 * 
	 */
    protected static final int LABEL_BORDER = 2;

    /**
	 * 
	 */
    protected static final int MAJOR_MARK_LENGTH = 5;

    /**
	 * 
	 */
    protected static final int MINOR_MARK_LENGTH = 2;

    /**
	 * 
	 */
    protected static final String AXIS_PATTERN = "####0.0";

    /**
	 * 
	 */
    protected static final String DOUBLE_PATTERN = "####0.0000";

    /**
	 * 
	 */
    private static final String START_HIGHLIGHT = "0x00C000";

    /**
	 * 
	 */
    private static final String END_HIGHLIGHT = "0xFF0000";

    /**
	 * 
	 */
    protected java.util.List<Spectrum> spectra = new ArrayList<Spectrum>();

    /**
	 * 
	 */
    protected int spectrumIndex = 0;

    /**
	 * 
	 */
    protected double minX;

    /**
	 * 
	 */
    protected double maxY;

    /**
	 * 
	 */
    protected double scaleY = 1;

    /**
	 * 
	 */
    protected float labelThreshold = 0.0f;

    /**
	 * 
	 */
    protected double maxX;

    /**
	 * 
	 */
    protected boolean fixedMaxY = false;

    /**
	 * 
	 */
    protected boolean yAxisRelative = true;

    /**
	 * 
	 */
    protected int[] firstIndex;

    /**
	 * 
	 */
    protected int[] lastIndex;

    /**
	 * 
	 */
    protected String xAxisLabel = "";

    /**
	 * 
	 */
    protected String yAxisLabel = "";

    /**
	 * 
	 */
    protected String yAxisUnits = "";

    /**
	 *
	 * @param labelThreshold
	 */
    public DataDisplayPanel(final float labelThreshold) {
        setOpaque(false);
        setLabelThreshold(labelThreshold);
    }

    /**
	 * 
	 * @param maxY
	 */
    public void setMaxY(final double maxY) {
        this.maxY = maxY;
        fixedMaxY = true;
    }

    /**
	 * 
	 * @param scaleY
	 */
    public void setScaleY(final double scaleY) {
        this.scaleY = scaleY;
        repaint();
    }

    /**
	 *
	 * @param labelThreshold
	 */
    public void setLabelThreshold(final float labelThreshold) {
        this.labelThreshold = labelThreshold;
        repaint();
    }

    /**
	 *
	 * @param yAxisRelative
	 */
    public void setYAxisRelative(final boolean yAxisRelative) {
        this.yAxisRelative = yAxisRelative;
        repaint();
    }

    /**
	 * 
	 * @param spectra
	 * @throws Exception
	 */
    public void setSpectra(final java.util.List<Spectrum> spectra) throws Exception {
        this.spectra = spectra;
        firstIndex = new int[spectra.size()];
        lastIndex = new int[spectra.size()];
        setXRange(minX, maxX);
    }

    /**
	 * 
	 * @param spectrum
	 * @throws Exception
	 */
    public void setSpectrum(final Spectrum spectrum) throws Exception {
        final java.util.List<Spectrum> newSpectra = new ArrayList<Spectrum>();
        newSpectra.add(spectrum);
        setSpectra(newSpectra);
    }

    /**
	 *
	 * @param minX
	 * @param maxX
	 */
    public void setXRange(final double minX, final double maxX) {
        if (minX == NumberUtils.UNDEFINED && maxX == NumberUtils.UNDEFINED) {
            reset();
        } else {
            this.minX = minX;
            this.maxX = maxX;
        }
        if (!fixedMaxY) {
            maxY = 0.0;
        }
        for (int i = 0; i < spectra.size(); i++) {
            firstIndex[i] = Integer.MAX_VALUE;
            final Spectrum spectrum = spectra.get(i);
            final double[] xValues = spectrum.getXValues();
            final double[] yValues = spectrum.getYValues();
            for (lastIndex[i] = 0; lastIndex[i] < xValues.length; lastIndex[i]++) {
                final double xValue = xValues[lastIndex[i]];
                if (xValue > this.maxX) {
                    break;
                }
                if (xValue < this.minX) {
                    continue;
                }
                firstIndex[i] = Math.min(firstIndex[i], lastIndex[i]);
                if (!fixedMaxY) {
                    maxY = Math.max(maxY, yValues[lastIndex[i]]);
                }
            }
        }
        repaint();
    }

    @Override
    public void setXRangeByPosition(final double minXPosition, final double maxXPosition) {
        setXRange(convertXPosition(minXPosition), convertXPosition(maxXPosition));
    }

    @Override
    public void reset() {
        minX = Double.MAX_VALUE;
        maxX = Double.MIN_VALUE;
        for (Iterator<Spectrum> iterator = spectra.iterator(); iterator.hasNext(); ) {
            final double[] xValues = iterator.next().getXValues();
            minX = Math.min(minX, xValues.length > 0 ? xValues[0] : Double.MAX_VALUE);
            maxX = Math.max(maxX, xValues.length > 0 ? xValues[xValues.length - 1] : Double.MIN_VALUE);
        }
        setXRange(minX, maxX);
    }

    @Override
    public int getAxesBorder() {
        return AXES_BORDER;
    }

    @Override
    public void dispose() {
    }

    /**
	 *
	 * @param xAxisLabel
	 */
    public void setXAxisLabel(final String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
        repaint();
    }

    /**
	 *
	 * @param yAxisLabel
	 */
    public void setYAxisLabel(final String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
        repaint();
    }

    /**
	 *
	 * @param yAxisUnits
	 */
    public void setYAxisUnits(final String yAxisUnits) {
        this.yAxisUnits = yAxisUnits;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        if (g0 instanceof Graphics2D) {
            final Graphics2D g = (Graphics2D) g0;
            if (paintData(g)) {
                paintAxes(g);
            }
        }
    }

    /**
	 * 
	 */
    synchronized void incrementSpectrumIndex() {
        if (spectra != null && spectra.size() > 0) {
            spectrumIndex = (spectrumIndex + 1) % spectra.size();
        }
    }

    /**
	 * 
	 * @param g
	 * @param spectrum
	 */
    protected void paintSpectrum(final Graphics2D g, final Spectrum spectrum) {
        final double[] xValues = spectrum.getXValues();
        final double[] yValues = spectrum.getYValues();
        for (int j = firstIndex[spectrumIndex]; j < lastIndex[spectrumIndex]; j++) {
            final double xValue = xValues[j];
            final double yValue = yValues[j];
            final int xPosition = getXPosition(xValue);
            final int yPosition = getYPosition(yValue);
            g.setColor(getColor(xValue, yValue));
            paintData(g, xPosition, yPosition);
            if (yValue > maxY * labelThreshold) {
                paintLabel(g, xValue, xPosition, yPosition);
            }
            final Color color = g.getColor();
            if (spectrum.getXValuesHighlightFrom() > minX && spectrum.getXValuesHighlightFrom() < maxX) {
                g.setColor(Color.decode(START_HIGHLIGHT));
                final int xPositionHighlightFrom = getXPosition(spectrum.getXValuesHighlightFrom());
                g.drawLine(xPositionHighlightFrom, getHeight() - AXES_BORDER, xPositionHighlightFrom, getYPosition(maxY));
            }
            if (spectrum.getXValuesHighlightTo() > minX && spectrum.getXValuesHighlightTo() < maxX) {
                g.setColor(Color.decode(END_HIGHLIGHT));
                final int xPositionHighlightTo = getXPosition(spectrum.getXValuesHighlightTo());
                g.drawLine(xPositionHighlightTo, getHeight() - AXES_BORDER, xPositionHighlightTo, getYPosition(maxY));
            }
            g.setColor(color);
        }
    }

    /**
	 *
	 * @param x
	 * @param y
	 * @return Color
	 */
    protected Color getColor(final double x, final double y) {
        return Color.GRAY;
    }

    /**
	 * 
	 * @param xValue
	 * @return int
	 */
    protected int getXPosition(final double xValue) {
        final int xOffset = (int) ((getWidth() - (2 * AXES_BORDER)) * ((xValue - minX) / (maxX - minX)));
        return AXES_BORDER + xOffset;
    }

    /**
	 * 
	 * @param yValue
	 * @return int
	 */
    protected int getYPosition(final double yValue) {
        final int height = getHeight();
        final int yOffset = (int) ((height - (2 * AXES_BORDER)) * (yValue / maxY));
        return height - (AXES_BORDER + yOffset);
    }

    /**
	 *
	 * @param xPosition
	 * @return double
	 */
    protected double convertXPosition(final double xPosition) {
        return ((xPosition - AXES_BORDER) / (getWidth() - (2 * AXES_BORDER)) * (maxX - minX)) + minX;
    }

    /**
	 *
	 * @param yPosition
	 * @return double
	 */
    protected double convertYPosition(double yPosition) {
        final double axisLength = getHeight() - (2 * AXES_BORDER);
        return maxY * (axisLength - yPosition + AXES_BORDER) / axisLength;
    }

    /**
	 *
	 * @param g
	 * @param xPosition
	 * @param yPosition
	 */
    protected abstract void paintData(final Graphics2D g, final int xPosition, final int yPosition);

    /**
	 *
	 * @param g
	 */
    protected void paintAxes(final Graphics2D g) {
        final int height = getHeight();
        final int width = getWidth();
        g.setColor(Color.BLACK);
        g.drawLine(AXES_BORDER, AXES_BORDER, AXES_BORDER, height - AXES_BORDER + MAJOR_MARK_LENGTH);
        g.drawLine(AXES_BORDER - MAJOR_MARK_LENGTH, height - AXES_BORDER, width - AXES_BORDER, height - AXES_BORDER);
        g.drawLine(AXES_BORDER - MAJOR_MARK_LENGTH, AXES_BORDER, AXES_BORDER, AXES_BORDER);
        g.drawLine(width - AXES_BORDER, height - AXES_BORDER, width - AXES_BORDER, height - AXES_BORDER + MAJOR_MARK_LENGTH);
        final int xAxisLength = width - (2 * AXES_BORDER);
        final int yAxisLength = height - (2 * AXES_BORDER);
        final String LEFT_ANNOTATION = toString(minX, AXIS_PATTERN);
        final String RIGHT_ANNOTATION = toString(maxX, AXIS_PATTERN);
        Dimension dimension = getTextDimension(g, LEFT_ANNOTATION);
        g.drawBytes(LEFT_ANNOTATION.getBytes(), 0, LEFT_ANNOTATION.length(), AXES_BORDER - (dimension.width / 2), AXES_BORDER + yAxisLength + TEXT_BORDER + dimension.height / 2);
        dimension = getTextDimension(g, RIGHT_ANNOTATION);
        g.drawBytes(RIGHT_ANNOTATION.getBytes(), 0, RIGHT_ANNOTATION.length(), AXES_BORDER + xAxisLength - (dimension.width / 2), AXES_BORDER + yAxisLength + TEXT_BORDER + dimension.height / 2);
        dimension = getTextDimension(g, xAxisLabel);
        g.drawBytes(xAxisLabel.getBytes(), 0, xAxisLabel.length(), AXES_BORDER + ((xAxisLength - dimension.width) / 2), AXES_BORDER + yAxisLength + TEXT_BORDER + dimension.height / 2);
        final String TOP_ANNOTATION = toString((yAxisRelative) ? 100 : maxY * scaleY, AXIS_PATTERN);
        final String BOTTOM_ANNOTATION = toString(0, AXIS_PATTERN);
        final String Y_AXIS_ANNOTATION = yAxisLabel + ((yAxisRelative) ? " / %" : (yAxisUnits.length() > 0) ? " / " + yAxisUnits : "");
        dimension = getTextDimension(g, TOP_ANNOTATION);
        g.drawBytes(TOP_ANNOTATION.getBytes(), 0, TOP_ANNOTATION.length(), AXES_BORDER - dimension.width - TEXT_BORDER, AXES_BORDER + (dimension.height / 2));
        dimension = getTextDimension(g, BOTTOM_ANNOTATION);
        g.drawBytes(BOTTOM_ANNOTATION.getBytes(), 0, BOTTOM_ANNOTATION.length(), AXES_BORDER - dimension.width - TEXT_BORDER, height - AXES_BORDER + (dimension.height / 2));
        final AffineTransform defaultTransform = g.getTransform();
        dimension = getTextDimension(g, Y_AXIS_ANNOTATION);
        g.translate(AXES_BORDER - TEXT_BORDER, AXES_BORDER + ((yAxisLength + dimension.width) / 2));
        g.rotate(-Math.PI / 2.0);
        g.drawBytes(Y_AXIS_ANNOTATION.getBytes(), 0, Y_AXIS_ANNOTATION.length(), 0, 0);
        g.setTransform(defaultTransform);
        if (yAxisRelative) {
            final String COUNTS_ANNOTATION = "100% = " + StringUtils.getEngineeringNotation(maxY * scaleY) + " " + yAxisUnits;
            dimension = getTextDimension(g, COUNTS_ANNOTATION);
            g.drawBytes(COUNTS_ANNOTATION.getBytes(), 0, COUNTS_ANNOTATION.length(), AXES_BORDER, AXES_BORDER - TEXT_BORDER - dimension.height);
        }
        final int yAxisMinorMarks = 10;
        final int yAxisMinorMarkIntervals = yAxisLength / yAxisMinorMarks;
        for (int i = 0; i < yAxisMinorMarks; i++) {
            final int y = AXES_BORDER + (i * yAxisMinorMarkIntervals);
            g.drawLine(AXES_BORDER - MINOR_MARK_LENGTH, y, AXES_BORDER, y);
        }
    }

    /**
	 * 
	 *
	 * @param g
	 * @param text
	 * @return Dimension
	 */
    protected Dimension getTextDimension(final Graphics2D g, final String text) {
        final FontMetrics fontMetrics = g.getFontMetrics();
        return new Dimension(fontMetrics.stringWidth(text), fontMetrics.getHeight());
    }

    /**
	 *
	 * @param g
	 * @return boolean
	 */
    protected boolean paintData(final Graphics2D g) {
        boolean valid = false;
        for (int i = 0; i < spectra.size(); i++) {
            incrementSpectrumIndex();
            final Spectrum spectrum = spectra.get(spectrumIndex);
            final boolean currentValid = spectrum.getXValues().length > 0;
            if (currentValid) {
                paintSpectrum(g, spectrum);
            }
            valid |= currentValid;
        }
        return valid;
    }

    /**
	 *
	 * @param g
	 * @param xValue
	 * @param xPosition
	 * @param yPosition
	 */
    protected void paintLabel(final Graphics2D g, final double xValue, final int xPosition, final int yPosition) {
        final String LABEL = toString(xValue, DOUBLE_PATTERN);
        final Dimension dimension = getTextDimension(g, LABEL);
        g.drawBytes(LABEL.getBytes(), 0, LABEL.length(), xPosition - (dimension.width / 2), yPosition - LABEL_BORDER);
    }

    /**
	 *
	 * @param d
	 * @param pattern
	 * @return String
	 */
    protected String toString(final double d, final String pattern) {
        return new DecimalFormat(pattern).format(d);
    }
}
