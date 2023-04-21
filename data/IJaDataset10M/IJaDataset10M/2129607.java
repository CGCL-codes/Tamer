package net.kornr.canvaschart.client.core;

import net.kornr.canvaschart.client.annotation.AnnotationDisplay;
import net.kornr.canvaschart.client.annotation.MarkAnnotationDisplay;
import net.kornr.canvaschart.client.formatter.NumberFormatter;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class Annotation {

    private AnnotationDisplay display;

    private Point point;

    private int m_alignement;

    private AbsolutePanel m_parent;

    private Formatter m_formatter = new NumberFormatter(2);

    public static final int LEFT_MIDDLE = 1;

    public static final int LEFT_DOWN = 2;

    public static final int LEFT_UP = 3;

    public static final int RIGHT_MIDDLE = 4;

    public static final int RIGHT_DOWN = 5;

    public static final int RIGHT_UP = 6;

    public static final int CENTER_MIDDLE = 7;

    public static final int CENTER_DOWN = 8;

    public static final int CENTER_UP = 9;

    public AnnotationTimer m_timer = new AnnotationTimer();

    public class AnnotationTimer extends Timer {

        private boolean running = false;

        public void activate() {
            if (running == false) {
                running = true;
                this.schedule(1);
            }
        }

        @Override
        public void run() {
            if (Annotation.this.move()) {
                this.schedule(50);
            } else {
                running = false;
            }
        }
    }

    public Annotation(AnnotatedCanvas anncan, String html, Point p) {
        this(anncan, html, p, CENTER_MIDDLE);
    }

    public Annotation(AnnotatedCanvas anncan, String html, Point p, int align) {
        this.m_parent = anncan;
        this.display = new MarkAnnotationDisplay();
        this.point = p;
        m_alignement = align;
    }

    public boolean move() {
        if (this.display.getWidget().isAttached() == false) return false;
        double curx = this.m_parent.getWidgetLeft(this.display.getWidget());
        double cury = this.m_parent.getWidgetTop(this.display.getWidget());
        if (curx == point.getX() && cury == point.getY()) {
            return false;
        }
        double offx = (this.point.getX() - curx) / 4;
        double offy = (this.point.getY() - cury) / 4;
        double nx = curx + offx;
        double ny = cury + offy;
        if (Math.abs(offx) < 1) {
            nx = point.getX();
        }
        if (Math.abs(offy) < 1) {
            ny = point.getY();
        }
        m_parent.setWidgetPosition(this.display.getWidget(), (int) (nx), (int) (ny));
        return true;
    }

    public void setValue(double val) {
        String s = m_formatter.format(val);
        this.setText(s);
    }

    public void setText(String txt) {
        this.display.setData(txt);
    }

    public Widget getWidget() {
        return display.getWidget();
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
        Point offset = getAlignementOffset();
        this.point.add(offset);
    }

    public Point getAlignementOffset() {
        if (this.display.getWidget().isAttached()) {
            double width = this.display.getWidget().getOffsetWidth();
            double height = this.display.getWidget().getOffsetHeight();
            double offsetx = 0;
            double offsety = 0;
            switch(m_alignement) {
                case LEFT_UP:
                    offsety = -height;
                    break;
                case LEFT_MIDDLE:
                    offsety = -height / 2;
                    break;
                case LEFT_DOWN:
                    break;
                case CENTER_UP:
                    offsetx = -width / 2;
                    offsety = -height;
                    break;
                case CENTER_MIDDLE:
                    offsetx = -width / 2;
                    offsety = -height / 2;
                    break;
                case CENTER_DOWN:
                    offsetx = -width / 2;
                    break;
                case RIGHT_UP:
                    offsetx = -width;
                    offsety = -height;
                    break;
                case RIGHT_MIDDLE:
                    offsetx = -width;
                    offsety = -height / 2;
                    break;
                case RIGHT_DOWN:
                    offsetx = -width;
                    break;
            }
            return new Point(offsetx, offsety);
        }
        return new Point(0, 0);
    }

    public void setVisible(boolean b) {
        display.getWidget().setVisible(b);
    }

    public void setAlignement(int align) {
        if (align != m_alignement) {
            Point preoffset = getAlignementOffset();
            this.point.substract(preoffset);
            m_alignement = align;
            Point postoffset = getAlignementOffset();
            this.point.add(postoffset);
        }
    }

    public void update() {
        setVisible(true);
        m_timer.activate();
    }
}
