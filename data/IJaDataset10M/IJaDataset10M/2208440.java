package src.gui;

import src.parser.HeartRateLimit;
import src.util.UnitConverter;
import ewe.fx.BufferedGraphics;
import ewe.fx.Color;
import ewe.fx.Graphics;
import ewe.fx.Rect;
import ewe.sys.Vm;
import ewe.ui.Canvas;
import ewe.util.Vector;

/**
 * Shows the time in various heartrate zones.
 * 
 * @author Karl Neuhold
 */
public class AnalyseDiagram extends Canvas {

    /** Heartrate limits */
    private HeartRateLimit[] hrLimits;

    /**
     * Parameter Beschreibung
     * 
     * @param hrLimits
     *            Heartrate limits.
     */
    public AnalyseDiagram(HeartRateLimit[] hrLimits) {
        super();
        backGround = new Color(255, 255, 240);
        this.hrLimits = hrLimits;
        setPreferredSize(240, 280);
    }

    /**
     * Drawing statistical grafics.
     * 
     * @param g
     *            Graphics object.
     * @param r
     *            Graphics area.
     */
    public void doPaint(Graphics g, Rect r) {
        doBackground(g);
        if (hrLimits != null) {
            Vm.debug("#limits = " + hrLimits.length);
            drawDiagram(g, r);
        }
    }

    /**
     * 
     * Draw Diagram
     * 
     * @param g
     *            Graphics opject
     * @param r
     *            Graphic area
     */
    private void drawDiagram(Graphics g, Rect r) {
        BufferedGraphics bg = new BufferedGraphics(g, r);
        Graphics draw = bg.getGraphics();
        int rightBoder = 10;
        int textLine2 = 12;
        int height = r.height / hrLimits.length;
        int reductionFactor = getDiagramReductionFactor(r, rightBoder);
        for (int i = 0; i < hrLimits.length; i++) {
            int width = hrLimits[i].getTimeWithin() / reductionFactor;
            g.setColor(getColor(i));
            g.fillRoundRect(0, i * height, width, height, 5);
            g.setColor(Color.White);
            g.drawLine(0, i * height, r.width, i * height);
            drawText(g, textLine2, height, i);
        }
        bg.release();
        g.flush();
    }

    /**
     * @param r
     *            Area
     * @param rightBoder
     *            Right diagram border
     * @return Reduction Factor
     */
    private int getDiagramReductionFactor(Rect r, int rightBoder) {
        int reductionFactor = getMaximumValue() / (r.width - rightBoder);
        if (reductionFactor < 1) {
            reductionFactor = 1;
        }
        return reductionFactor;
    }

    /**
     * 
     * @param index
     *            Color for heartrate zone at index.
     * @return New Color for a HR zone.
     */
    private Color getColor(int index) {
        Vector availableColors = new Vector();
        availableColors.add(new Color(238, 92, 66));
        availableColors.add(new Color(255, 165, 0));
        availableColors.add(new Color(218, 165, 32));
        availableColors.add(new Color(100, 149, 237));
        availableColors.add(new Color(46, 139, 87));
        availableColors.add(new Color(240, 230, 140));
        availableColors.add(new Color(102, 205, 170));
        availableColors.add(new Color(175, 238, 238));
        availableColors.add(new Color(255, 218, 185));
        availableColors.add(new Color(240, 255, 255));
        availableColors.add(new Color(255, 0, 0));
        availableColors.add(new Color(0, 255, 0));
        availableColors.add(new Color(0, 0, 255));
        if (index > availableColors.getCount()) {
            index = 1;
        }
        return (Color) availableColors.get(index);
    }

    /**
     * @param g
     *            Graphics object.
     * @param textLine2
     *            Positon second text line.
     * @param height
     *            Height of graphic for a time zone.
     * @param index
     *            Current (number of) time zone.
     */
    private void drawText(Graphics g, int textLine2, int height, int index) {
        g.setColor(Color.Black);
        if (index == 0) {
            g.drawText(getNameOfHRzone(hrLimits[index]), 5, 0);
            g.drawText("hh:mm:ss = " + getTimeString(hrLimits[index].getTimeWithin()), 5, textLine2);
        } else {
            g.drawText(getNameOfHRzone(hrLimits[index]), 5, (index * height));
            g.drawText("hh:mm:ss = " + getTimeString(hrLimits[index].getTimeWithin()), 5, (index * height) + textLine2);
        }
        Vm.debug("name = " + getNameOfHRzone(hrLimits[index]));
        Vm.debug("limit = " + hrLimits[index].getLowerHeartRate() + " - " + hrLimits[index].getUpperHeartRate() + " time = " + hrLimits[index].getTimeWithin());
    }

    /**
     * 
     * @param hrLimit
     *            Current heartrate limit.
     * @return Name of HR limit or limits (upper and lower).
     */
    private String getNameOfHRzone(HeartRateLimit hrLimit) {
        if (hrLimit.getName() != null) {
            return hrLimit.getName();
        } else {
            return (hrLimit.getLowerHeartRate() + " - " + hrLimit.getLowerHeartRate());
        }
    }

    /**
     * Get time as hh:mm::ss string
     * 
     * @param seconds
     *            Duration in seconds
     * @return TimeString: hh:mm:ss
     */
    private String getTimeString(int seconds) {
        return UnitConverter.seconds2TimeString(seconds);
    }

    /**
     * 
     * Get maximum time in a heartrate zone
     * 
     * @return Maximum Time for a heartrate zone.
     */
    private int getMaximumValue() {
        int maxTime = 0;
        for (int i = 0; i < hrLimits.length; i++) {
            if (hrLimits[i].getTimeWithin() > maxTime) {
                maxTime = hrLimits[i].getTimeWithin();
            }
        }
        return maxTime;
    }

    /**
     * @param hrLimits
     *            The hrLimits to set.
     */
    public void setHrLimits(HeartRateLimit[] hrLimits) {
        this.hrLimits = hrLimits;
    }
}
