package org.sourceforge.espro.elicitation;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The scale elicitation method.
 *
 * @author Martin Kaffanke
 * @version 1.1
 */
public class Scale extends AbstractScale {

    /**  */
    private static final long serialVersionUID = 7754526330114849395L;

    private static final Logger logger = Logger.getLogger(Scale.class.getName());

    /**
    * Holds the points of the scale.
    *
    * @see ScalePointSet
    */
    protected ScalePointSet points;

    protected boolean invLog = false;

    /** Base of the logarithm. */
    @Setting(name = "Log Base", help = "The base of the logarithm.", position = 21)
    protected double logBase = 10;

    /** The maximum of the scale. */
    @Setting(name = "Max", position = 2)
    protected double max = 1F;

    /** The minimum of the scale. */
    @Setting(name = "Min", position = 3)
    protected double min = 0F;

    /** Logarithmic scale */
    @Setting(name = "Logarithmic", position = 20)
    private boolean logarithmic = false;

    @Setting(name = "Digits", values = { 0, 10, 1 }, position = 6)
    private int digits = 2;

    /**
    * This constructs the 'scale' elicitation method.
    */
    public Scale() {
        super("Scale", "Point");
        initialize();
    }

    /**
    * This constructor is for subclasses, in order to create a new
    * method.
    *
    * @param name The name.
    * @param category The category.
    *
    * @see ElicitationMethod
    */
    protected Scale(final String name, final String category) {
        super(name, category);
    }

    /**
    * Returns the digits, which is the number of numbers behind the
    * coma-seperator.
    *
    * @return The number of digits.
    */
    public int getDigits() {
        return digits;
    }

    /**
    * Returns the logBase.
    *
    * @return The logBase.
    */
    public double getLogBase() {
        return logBase;
    }

    /**
    * Returns the maximum value of the scale.
    *
    * @return The maximum value of the scale.
    */
    public double getMax() {
        return max;
    }

    /**
    * Returns the minimum value of the scale.
    *
    * @return The minimum value of the scale.
    */
    public double getMin() {
        return min;
    }

    /**
    * DOCUMENT ME!
    *
    * @param d DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public int[] getPositionFromValue(double d) {
        int x = xRect + widthRect;
        int y = yPointerFromValue(d);
        int ret[] = { x, y };
        return ret;
    }

    /**
    * DOCUMENT ME!
    *
    * @return The invertlog.
    */
    public boolean isInvLog() {
        return invLog;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public boolean isLogarithmic() {
        return logarithmic;
    }

    /**
    * Sets the digits, which is the number of numbers behind the
    * coma-seperator.
    *
    * @param digits The number of numbers behind the coma-seperator.
    */
    public void setDigits(final int digits) {
        if (frozen) {
            return;
        }
        final int old = getDigits();
        this.digits = digits;
        pcs.firePropertyChange("digits", old, getDigits());
    }

    /**
    * DOCUMENT ME!
    *
    * @param invLog The invertlog to set.
    */
    public void setInvLog(final boolean invLog) {
        if (frozen) {
            return;
        }
        final boolean old = this.invLog;
        this.invLog = invLog;
        pcs.firePropertyChange("invLog", old, this.invLog);
    }

    /**
    * Sets the logBase
    *
    * @param logBase The logBase to set.
    */
    public void setLogBase(final double logBase) {
        if (frozen) {
            return;
        }
        final double old = this.logBase;
        this.logBase = logBase;
        pcs.firePropertyChange("logBase", old, this.logBase);
    }

    /**
    * DOCUMENT ME!
    *
    * @param logarithmic DOCUMENT ME!
    */
    public void setLogarithmic(boolean logarithmic) {
        if (frozen) {
            return;
        }
        boolean old = this.logarithmic;
        this.logarithmic = logarithmic;
        pcs.firePropertyChange("logarithmic", old, this.logarithmic);
    }

    /**
    * Sets a maximum value for the scale.
    *
    * @param max The maximum value.
    */
    public void setMax(final double max) {
        if (frozen) {
            return;
        }
        final double old = this.max;
        this.max = max;
        pcs.firePropertyChange("max", old, this.max);
    }

    /**
    * Sets the minimum value of the Scale.
    *
    * @param min The minimum value.
    */
    public void setMin(final double min) {
        if (frozen) {
            return;
        }
        final double old = this.min;
        this.min = min;
        pcs.firePropertyChange("min", old, this.min);
    }

    /**
    * Calculates sthe distances and lines between on the scale. This can
    * be easily overridden, to create a new scale.
    *
    * @return A list of ScalePoints.
    */
    protected ScalePointSet calcLinearPoints() {
        points = new ScalePointSet();
        final double interval = max - min;
        for (int i = 0; i < numberOfLinesRight; i++) {
            final double p1 = min + ((i * interval) / (numberOfLinesRight - 1));
            points.add(p1, 15, p1);
            if (p1 < (interval + min)) {
                for (int j = 1; j <= (numberOfBetweenLinesRight + 1); j++) {
                    final double p2 = p1 + ((j * interval) / ((numberOfLinesRight - 1) * (numberOfBetweenLinesRight + 1)));
                    points.add(p2, 9);
                }
            }
        }
        return points;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    protected ScalePointSet calcLogarithmicPoints() {
        points = new ScalePointSet();
        final double interval = max - min;
        double logBase = this.logBase;
        if (isInvLog()) {
            logBase = 1 / logBase;
        }
        for (int i = 0; i < numberOfLinesRight; i++) {
            final double p1 = (i * interval) / (numberOfLinesRight - 1);
            double pos1;
            pos1 = Math.pow(2, Math.log(p1) / Math.log(logBase));
            points.add(pos1, 15, p1);
            if (p1 < interval) {
                for (int j = 1; j <= (numberOfBetweenLinesRight + 1); j++) {
                    final double p2 = p1 + ((j * interval) / ((numberOfLinesRight - 1) * (numberOfBetweenLinesRight + 1)));
                    double pos2;
                    pos2 = Math.pow(2, Math.log(p2) / Math.log(logBase));
                    points.add(pos2, 9);
                }
            }
        }
        return points;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    protected ScalePointSet calcPoints() {
        if (logarithmic) {
            return calcLogarithmicPoints();
        }
        return calcLinearPoints();
    }

    /**
    * Draws a number on the right side.
    *
    * @param numForPos The number to draw.
    * @param yPos The y position.
    */
    protected void drawNumberRight(final double numForPos, double yPos) {
        final Font font = g2.getFont();
        final DecimalFormat threeDec = new DecimalFormat("0.000");
        threeDec.setMinimumFractionDigits(digits);
        threeDec.setMaximumFractionDigits(digits);
        final String text = (threeDec.format(new Float(numForPos)));
        final FontRenderContext frc = g2.getFontRenderContext();
        final Rectangle2D bounds = font.getStringBounds(text, frc);
        yPos += ((bounds.getHeight() / 2) - 2);
        g2.drawString(text, xRect + widthRect + 20, (float) yPos);
    }

    /**
    * Initializes the Scale.
    *
    * @see ElicitationMethod#initialize()
    */
    @Override
    protected void initialize() {
        verbalHintsList = new ArrayList<ScaleHint>();
        verbalHintsList.add(new ScaleHint(.0, "impossible"));
        verbalHintsList.add(new ScaleHint(.8, "probable"));
        verbalHintsList.add(new ScaleHint(.5, "fifty-fifty"));
        verbalHintsList.add(new ScaleHint(1, "certain"));
        setVerbalHintsList(verbalHintsList);
        addSelfListeners();
    }

    /**
    * Calculates the value from a given pointer. You don't have to use
    * the return value, because this will be automatically set to the
    * elicitation methods value.
    *
    * @param pointer The position to calculate the pointer from.
    *
    * @return The calculated value.
    *
    * @see #valueFromYPointer()
    */
    protected double valueFromYPointer(final double pointer) {
        double value = points.graphicsToValue(pointer - padding);
        if (isInvert()) {
            value = (max + min) - value;
        }
        if (logarithmic) {
            value = Math.pow(logBase, Math.log(value) / Math.log(2));
        }
        return value;
    }

    /**
    * Calculates the yPointer from the value of the elicitation method.
    * Note that you don't have to use the return value, because this will be
    * set automatically when you recalculate the yPointer.
    *
    * @param value The value.
    *
    * @return The yPointer.
    *
    * @see #valueFromYPointer()
    */
    protected int yPointerFromValue(double value) {
        if (logarithmic) {
            value = Math.pow(2, Math.log(value) / Math.log(logBase));
        }
        int yPointer = (int) points.valueToGraphics(value) + padding;
        if (isInvert()) {
            yPointer = (heightRect - yPointer) + (2 * padding);
        }
        return yPointer;
    }
}
