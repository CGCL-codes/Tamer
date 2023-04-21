package de.anormalmedia.touchui.component.slider;

import javax.microedition.lcdui.Graphics;
import de.anormalmedia.touchui.Dimension;
import de.anormalmedia.touchui.Insets;
import de.anormalmedia.touchui.Rectangle;
import de.anormalmedia.touchui.UIFactory;
import de.anormalmedia.touchui.UIUtils;
import de.anormalmedia.touchui.border.AbstractBorder;
import de.anormalmedia.touchui.border.LineBorder;
import de.anormalmedia.touchui.component.Component;
import de.anormalmedia.touchui.interfaces.IPointable;
import de.anormalmedia.touchui.paint.AbstractPaint;
import de.anormalmedia.touchui.paint.Color;
import de.anormalmedia.touchui.paint.ColorPaint;

public class Slider extends Component implements IPointable {

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    private int orientation = HORIZONTAL;

    private int indicatorWidth = 12;

    private int barHeight = 4;

    private AbstractPaint barHorizonalPaint = new ColorPaint(new Color(50, 50, 50));

    private AbstractPaint barVerticalPaint = new ColorPaint(new Color(50, 50, 50));

    private AbstractBorder barBorder = new LineBorder(new Color(100, 100, 100));

    private AbstractPaint indicatorHorizonalPaint = new ColorPaint(new Color(150, 150, 150));

    private AbstractPaint indicatorVerticalPaint = new ColorPaint(new Color(150, 150, 150));

    private AbstractBorder indicatorBorder = new LineBorder(new Color(100, 100, 100));

    private int maximum = 100;

    private int value = 0;

    boolean pressed = false;

    public Slider() {
        super();
        setInsets(new Insets(2, 2, 2, 2));
        UIFactory.styleSlider(this);
    }

    public Slider(int orientation) {
        this();
        setOrientation(orientation);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Rectangle lb = getBounds();
        AbstractBorder localBorder = getBorder();
        Insets insets = getInsets();
        Insets borderInsets = new Insets(0, 0, 0, 0);
        if (localBorder != null) {
            borderInsets = localBorder.getBorderInsets();
        }
        Insets bBorderInsets = new Insets(0, 0, 0, 0);
        if (getBarBorder() != null) {
            bBorderInsets = getBarBorder().getBorderInsets();
        }
        Insets iBorderInsets = new Insets(0, 0, 0, 0);
        if (getIndicatorBorder() != null) {
            iBorderInsets = getIndicatorBorder().getBorderInsets();
        }
        if (getOrientation() == HORIZONTAL) {
            int possibleHeight = lb.getHeight() - borderInsets.getTop() - borderInsets.getBottom() - insets.getTop() - insets.getBottom();
            int y1 = lb.getY() + borderInsets.getTop() + insets.getTop() + possibleHeight / 2 - getBarHeight() / 2;
            int x1 = lb.getX() + insets.getLeft() + borderInsets.getLeft();
            int width = lb.getWidth() - borderInsets.getLeft() - borderInsets.getRight() - insets.getLeft() - insets.getRight();
            getBarHorizonalPaint().paint(g, x1 + bBorderInsets.getLeft(), y1 + bBorderInsets.getTop(), width - bBorderInsets.getLeft() - bBorderInsets.getRight(), getBarHeight() - bBorderInsets.getTop() - bBorderInsets.getBottom());
            if (getBarBorder() != null) {
                getBarBorder().paint(g, x1, y1, width, getBarHeight());
            }
            int maxWidth = lb.getWidth() - borderInsets.getLeft() - borderInsets.getRight() - insets.getLeft() - insets.getRight() - getIndicatorWidth();
            double ratio = (double) maxWidth / (double) getMaximum();
            double location = ratio * value;
            int ix = (int) (x1 + location);
            int iy = lb.getY() + borderInsets.getTop() + insets.getTop();
            getIndicatorHorizonalPaint().paint(g, ix + iBorderInsets.getLeft(), iy + iBorderInsets.getTop(), getIndicatorWidth() - iBorderInsets.getLeft() - iBorderInsets.getRight(), possibleHeight - iBorderInsets.getTop() - iBorderInsets.getBottom());
            if (getIndicatorBorder() != null) {
                getIndicatorBorder().paint(g, ix, iy, getIndicatorWidth(), possibleHeight);
            }
        } else {
            int possibleWidth = lb.getWidth() - borderInsets.getLeft() - borderInsets.getRight() - insets.getLeft() - insets.getRight();
            int x1 = lb.getX() + borderInsets.getLeft() + insets.getLeft() + possibleWidth / 2 - getBarHeight() / 2;
            int y1 = lb.getY() + insets.getTop() + borderInsets.getTop();
            int height = lb.getHeight() - borderInsets.getTop() - borderInsets.getBottom() - insets.getTop() - insets.getBottom();
            getBarVerticalPaint().paint(g, x1 + bBorderInsets.getLeft(), y1 + bBorderInsets.getTop(), getBarHeight() - bBorderInsets.getLeft() - bBorderInsets.getRight(), height - bBorderInsets.getTop() - bBorderInsets.getBottom());
            if (getBarBorder() != null) {
                getBarBorder().paint(g, x1, y1, getBarHeight(), height);
            }
            int maxHeight = lb.getHeight() - borderInsets.getTop() - borderInsets.getBottom() - insets.getTop() - insets.getBottom() - getIndicatorWidth();
            double ratio = (double) maxHeight / (double) getMaximum();
            double location = ratio * value;
            int iy = (int) (y1 + location);
            int ix = lb.getX() + borderInsets.getLeft() + insets.getLeft();
            getIndicatorVerticalPaint().paint(g, ix + iBorderInsets.getLeft(), iy + iBorderInsets.getTop(), possibleWidth - iBorderInsets.getLeft() - iBorderInsets.getRight(), getIndicatorWidth() - iBorderInsets.getTop() - iBorderInsets.getBottom());
            if (getIndicatorBorder() != null) {
                getIndicatorBorder().paint(g, ix, iy, possibleWidth, getIndicatorWidth());
            }
        }
    }

    public Dimension getPreferredSize() {
        int prefWidth = 0;
        int prefHeight = 0;
        Insets insets = getInsets();
        if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        }
        AbstractBorder localBorder = getBorder();
        Insets borderInsets = new Insets(0, 0, 0, 0);
        if (localBorder != null) {
            borderInsets = localBorder.getBorderInsets();
        }
        int indBarMax = Math.max(getIndicatorWidth(), getBarHeight());
        prefWidth = indBarMax + insets.getLeft() + insets.getRight() + borderInsets.getLeft() + borderInsets.getRight();
        prefHeight = indBarMax + insets.getTop() + insets.getBottom() + borderInsets.getTop() + borderInsets.getBottom();
        return new Dimension(prefWidth, prefHeight);
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIndicatorWidth() {
        return indicatorWidth;
    }

    public void setIndicatorWidth(int indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
    }

    public int getBarHeight() {
        return barHeight;
    }

    public void setBarHeight(int barHeight) {
        this.barHeight = barHeight;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public AbstractPaint getBarHorizonalPaint() {
        return barHorizonalPaint;
    }

    public void setBarHorizonalPaint(AbstractPaint barHorizonalPaint) {
        this.barHorizonalPaint = barHorizonalPaint;
    }

    public AbstractPaint getIndicatorHorizonalPaint() {
        return indicatorHorizonalPaint;
    }

    public void setIndicatorHorizonalPaint(AbstractPaint indicatorHorizonalPaint) {
        this.indicatorHorizonalPaint = indicatorHorizonalPaint;
    }

    public AbstractBorder getBarBorder() {
        return barBorder;
    }

    public void setBarBorder(AbstractBorder barBorder) {
        this.barBorder = barBorder;
    }

    public AbstractBorder getIndicatorBorder() {
        return indicatorBorder;
    }

    public void setIndicatorBorder(AbstractBorder indicatorBorder) {
        this.indicatorBorder = indicatorBorder;
    }

    public AbstractPaint getBarVerticalPaint() {
        return barVerticalPaint;
    }

    public void setBarVerticalPaint(AbstractPaint barVerticalPaint) {
        this.barVerticalPaint = barVerticalPaint;
    }

    public AbstractPaint getIndicatorVerticalPaint() {
        return indicatorVerticalPaint;
    }

    public void setIndicatorVerticalPaint(AbstractPaint indicatorVerticalPaint) {
        this.indicatorVerticalPaint = indicatorVerticalPaint;
    }

    public boolean pointerDragged(int x, int y) {
        boolean contains = getVisibleRect().containsPoint(x, y);
        if (pressed && contains) {
            Rectangle b = getBounds();
            if (orientation == HORIZONTAL) {
                double ratio = (double) b.getWidth() / (double) getMaximum();
                setValue((int) ((x - b.getX()) / ratio));
            } else {
                double ratio = (double) b.getHeight() / (double) getMaximum();
                setValue((int) ((y - b.getY()) / ratio));
            }
            repaint();
        }
        return contains;
    }

    public boolean pointerPressed(int x, int y) {
        pressed = false;
        boolean containsPoint = getVisibleRect().containsPoint(x, y);
        if (containsPoint) {
            Rectangle b = getBounds();
            if (orientation == HORIZONTAL) {
                double ratio = (double) b.getWidth() / (double) getMaximum();
                double location = ratio * value;
                Rectangle indi = new Rectangle(b.getX() + (int) location - getIndicatorWidth() * 2, b.getY(), getIndicatorWidth() * 4, b.getHeight());
                if (indi.containsPoint(x, y)) {
                    UIUtils.vibrate(this, 30);
                    pressed = true;
                }
            } else {
                double ratio = (double) b.getHeight() / (double) getMaximum();
                double location = ratio * value;
                Rectangle indi = new Rectangle(b.getX(), b.getY() + (int) location - getIndicatorWidth() * 2, b.getWidth(), getIndicatorWidth() * 4);
                if (indi.containsPoint(x, y)) {
                    UIUtils.vibrate(this, 30);
                    pressed = true;
                }
            }
        }
        return containsPoint;
    }

    public boolean pointerReleased(int x, int y) {
        boolean containsPoint = getVisibleRect().containsPoint(x, y);
        pressed = false;
        return containsPoint;
    }
}
