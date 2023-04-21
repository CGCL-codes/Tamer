package ircam.jmax.guiobj;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.Math;
import java.awt.image.*;
import ircam.jmax.*;
import ircam.jmax.fts.*;
import ircam.jmax.editors.patcher.*;
import ircam.jmax.editors.patcher.objects.*;
import ircam.jmax.editors.patcher.actions.*;
import ircam.jmax.editors.patcher.menus.*;
import ircam.jmax.editors.patcher.interactions.*;
import ircam.jmax.toolkit.*;

public class Slider extends GraphicObject implements FtsIntValueListener {

    static final int THROTTLE_LATERAL_OFFSET = 3;

    static final int THROTTLE_HEIGHT = 3;

    protected static final int BOTTOM_OFFSET = 5;

    protected static final int UP_OFFSET = 5;

    private static final int MINIMUM_DIMENSION = 15;

    private static final int DEFAULT_WIDTH = 20;

    private static final int DEFAULT_RANGE = 127;

    private static final int DEFAULT_HEIGHT = BOTTOM_OFFSET + UP_OFFSET + DEFAULT_RANGE;

    static final int VERTICAL_OR = 0;

    static final int HORIZONTAL_OR = 1;

    private int value = 0;

    private int rangeMax;

    private int rangeMin;

    private int orientation;

    private BufferedImage buff;

    private Graphics2D buffG;

    public Slider(FtsGraphicObject theFtsObject) {
        super(theFtsObject);
        rangeMin = ((FtsSliderObject) ftsObject).getMinValue();
        rangeMax = ((FtsSliderObject) ftsObject).getMaxValue();
        if (value < rangeMin) value = rangeMin; else if (value > rangeMax) value = rangeMax;
        orientation = ((FtsSliderObject) ftsObject).getOrientation();
        if ((orientation != HORIZONTAL_OR) && (orientation != VERTICAL_OR)) setOrientation(VERTICAL_OR);
        updateOffScreenBuffer();
    }

    void updateOffScreenBuffer() {
        int w = getWidth() - 4;
        if (w <= 0) w = DEFAULT_WIDTH - 4;
        int h = getHeight() - 4;
        if (h <= 0) h = DEFAULT_HEIGHT - 4;
        buff = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        buffG = buff.createGraphics();
    }

    public void setDefaults() {
        setWidth(DEFAULT_WIDTH);
        int h = BOTTOM_OFFSET + (rangeMax - rangeMin) + UP_OFFSET;
        setHeight(h);
    }

    public void setMinValue(int theValue) {
        rangeMin = theValue;
        ((FtsSliderObject) ftsObject).setMinValue(rangeMin);
    }

    public void setCurrentMinValue(int value) {
        rangeMin = value;
        if (value < rangeMin) value = rangeMin;
    }

    public int getMinValue() {
        return rangeMin;
    }

    public void setMaxValue(int theValue) {
        rangeMax = theValue;
        ((FtsSliderObject) ftsObject).setMaxValue(rangeMax);
    }

    public void setCurrentMaxValue(int value) {
        rangeMax = value;
        if (value > rangeMax) value = rangeMax;
        redraw();
    }

    public int getMaxValue() {
        return rangeMax;
    }

    public void setWidth(int w) {
        if (w <= 0) w = DEFAULT_WIDTH; else if (w < MINIMUM_DIMENSION) w = MINIMUM_DIMENSION;
        super.setWidth(w);
        updateOffScreenBuffer();
    }

    public void setHeight(int h) {
        if (h <= 0) h = DEFAULT_RANGE; else if (h < MINIMUM_DIMENSION) h = MINIMUM_DIMENSION;
        super.setHeight(h);
        updateOffScreenBuffer();
    }

    public void setCurrentBounds(int x, int y, int w, int h) {
        super.setCurrentBounds(x, y, w, h);
        updateOffScreenBuffer();
    }

    public void setRange(int theMaxInt, int theMinInt) {
        if ((theMaxInt == rangeMax) && (theMinInt == rangeMin)) return;
        setMaxValue(theMaxInt);
        setMinValue(theMinInt);
        if (value < rangeMin) value = rangeMin; else if (value > rangeMax) value = rangeMax;
        redraw();
    }

    public void setOrientation(int or) {
        orientation = or;
        ((FtsSliderObject) ftsObject).setOrientation(orientation);
        updateOffScreenBuffer();
    }

    public void setCurrentOrientation(int or) {
        orientation = or;
        itsSketchPad.repaint();
    }

    public int getOrientation() {
        return orientation;
    }

    public void valueChanged(int v) {
        value = (v < rangeMin) ? rangeMin : ((v >= rangeMax) ? rangeMax : v);
        drawContent(buffG, 0, 0, getWidth() - 4, getHeight() - 4);
        updateRedraw();
    }

    public void redefined() {
        setDefaults();
    }

    public void gotSqueack(int squeack, Point mouse, Point oldMouse) {
        int newValue;
        if (orientation == VERTICAL_OR) newValue = (((getY() + getHeight() - mouse.y - BOTTOM_OFFSET) * (rangeMax - rangeMin)) / (getHeight() - BOTTOM_OFFSET - UP_OFFSET - THROTTLE_HEIGHT)) + rangeMin; else newValue = (((mouse.x - (getX() + UP_OFFSET)) * (rangeMax - rangeMin)) / (getWidth() - BOTTOM_OFFSET - UP_OFFSET - THROTTLE_HEIGHT)) + rangeMin;
        if (newValue > rangeMax) newValue = rangeMax; else if (newValue < rangeMin) newValue = rangeMin;
        if (newValue != value) ((FtsSliderObject) ftsObject).setValue(newValue);
    }

    public void paint(Graphics g) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();
        int range = (rangeMax - rangeMin);
        if (!isSelected()) g.setColor(Settings.sharedInstance().getUIColor()); else g.setColor(Settings.sharedInstance().getUIColor().darker());
        g.fill3DRect(x + 1, y + 1, w - 2, h - 2, true);
        g.setColor(Color.black);
        int pixels, pos;
        if (orientation == VERTICAL_OR) {
            pixels = h - BOTTOM_OFFSET - UP_OFFSET - THROTTLE_HEIGHT;
            pos = y + BOTTOM_OFFSET + pixels - (pixels * (value - rangeMin)) / range;
            g.drawRect(x + THROTTLE_LATERAL_OFFSET, pos, w - 2 * THROTTLE_LATERAL_OFFSET - 1, THROTTLE_HEIGHT - 1);
        } else {
            pixels = w - BOTTOM_OFFSET - UP_OFFSET - THROTTLE_HEIGHT;
            pos = x + UP_OFFSET + (pixels * (value - rangeMin)) / range;
            g.drawRect(pos, y + THROTTLE_LATERAL_OFFSET, THROTTLE_HEIGHT - 1, h - 2 * THROTTLE_LATERAL_OFFSET - 1);
        }
        super.paint(g);
    }

    public void updatePaint(Graphics g) {
        g.drawImage(buff, getX() + 2, getY() + 2, itsSketchPad);
    }

    public void drawContent(Graphics g, int x, int y, int w, int h) {
        int range = rangeMax - rangeMin;
        g.setColor(Settings.sharedInstance().getUIColor());
        g.fillRect(x, y, w, h);
        g.setColor(Color.black);
        int pixels, pos;
        if (orientation == VERTICAL_OR) {
            pixels = h + 4 - BOTTOM_OFFSET - UP_OFFSET - THROTTLE_HEIGHT;
            pos = y - 2 + BOTTOM_OFFSET + pixels - (pixels * (value - rangeMin)) / range;
            g.drawRect(x - 2 + THROTTLE_LATERAL_OFFSET, pos, w + 4 - 2 * THROTTLE_LATERAL_OFFSET - 1, THROTTLE_HEIGHT - 1);
        } else {
            pixels = w + 4 - BOTTOM_OFFSET - UP_OFFSET - THROTTLE_HEIGHT;
            pos = x - 2 + UP_OFFSET + (pixels * (value - rangeMin)) / range;
            g.drawRect(pos, y - 2 + THROTTLE_LATERAL_OFFSET, THROTTLE_HEIGHT - 1, h + 4 - 2 * THROTTLE_LATERAL_OFFSET - 1);
        }
    }

    protected SensibilityArea findSensibilityArea(int mouseX, int mouseY) {
        if ((mouseY >= getY() + getHeight() - ObjectGeometry.V_RESIZE_SENSIBLE_HEIGHT) && (mouseX >= getX() + getWidth() / 2)) {
            return SensibilityArea.get(this, Squeack.VRESIZE_HANDLE);
        } else return super.findSensibilityArea(mouseX, mouseY);
    }

    public ObjectControlPanel getControlPanel() {
        return new SliderControlPanel(this);
    }

    public boolean isInspectable() {
        return true;
    }

    void updateDimension() {
        int w = getWidth();
        int h = getHeight();
        setWidth(h);
        setHeight(w);
        itsSketchPad.repaint();
    }
}
