package pl.softech.gpw.candles.editor.action;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import pl.softech.gpw.candles.Candle;
import pl.softech.gpw.candles.editor.CandlestickWidget;
import pl.softech.gpw.model.FInstrument;

public class CWidgetResizeHandler {

    public static final int HIGH_SHADOW = 0;

    public static final int MAX_BODY = 1;

    public static final int MIN_BODY = 2;

    public static final int LOW_SHADOW = 3;

    private boolean isSelected;

    private Rectangle2D[] resizeRects;

    private int resizeSource;

    private final CandlestickWidget widget;

    public CWidgetResizeHandler(CandlestickWidget widget) {
        this.widget = widget;
        isSelected = false;
        resizeRects = new Rectangle2D[4];
    }

    private Point loc;

    private Dimension dim;

    private double frac;

    private FInstrument fin;

    private void setMaxBody(Candle ca, float value) {
        if (ca.isBlack()) {
            ca.instrument().setOpen(value);
        } else {
            ca.instrument().setClose(value);
        }
    }

    private void setMinBody(Candle ca, float value) {
        if (ca.isBlack()) {
            ca.instrument().setClose(value);
        } else {
            ca.instrument().setOpen(value);
        }
    }

    public void resize(Point start, Point end) {
        double yDiff = start.getY() - end.getY();
        FInstrument fi = widget.getCandlestick().instrument();
        float diff = (float) (frac * yDiff);
        Dimension dim = new Dimension(this.dim);
        switch(resizeSource) {
            case HIGH_SHADOW:
                {
                    float min = Math.max(fin.getClose(), fin.getOpen());
                    if (fin.getHigh() + diff < min) {
                        diff = min - fin.getHigh();
                        yDiff = diff / frac;
                    }
                    fi.setHigh(fin.getHigh() + diff);
                    Point loc = new Point(this.loc);
                    loc.y -= yDiff;
                    widget.getWidget().setLocation(loc);
                    dim.height += yDiff;
                    break;
                }
            case LOW_SHADOW:
                {
                    float max = Math.min(fin.getClose(), fin.getOpen());
                    if (fin.getLow() + diff > max) {
                        diff = max - fin.getLow();
                        yDiff = diff / frac;
                    }
                    fi.setLow(fin.getLow() + diff);
                    dim.height += -yDiff;
                    break;
                }
            case MAX_BODY:
                {
                    float max = Math.max(fin.getClose(), fin.getOpen());
                    float min = Math.min(fin.getClose(), fin.getOpen());
                    if (max + diff < min) {
                        diff = min - max;
                    }
                    setMaxBody(widget.getCandlestick(), max + diff);
                    break;
                }
            case MIN_BODY:
                {
                    float min = Math.min(fin.getClose(), fin.getOpen());
                    float max = Math.max(fin.getClose(), fin.getOpen());
                    if (min + diff > max) {
                        diff = max - min;
                    }
                    setMinBody(widget.getCandlestick(), min + diff);
                    break;
                }
            default:
                return;
        }
        widget.getWidget().setSize(dim);
        widget.getWidget().repaint();
    }

    private int getResizeSource(Point point) {
        if (!isSelected) {
            return -1;
        }
        for (int i = 0; i < resizeRects.length; i++) {
            if (resizeRects[i].contains(point)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isResizeAction(Point point) {
        return getResizeSource(point) != -1;
    }

    public boolean shouldPerformResize(Point point) {
        int source = getResizeSource(point);
        if (source == -1) {
            return false;
        }
        this.resizeSource = source;
        loc = widget.getWidget().getLocation();
        dim = widget.getWidget().getSize();
        Rectangle bounds = widget.getCandleBounds();
        double len = widget.getCandlestick().getLen();
        frac = len / bounds.getHeight();
        fin = widget.getCandlestick().instrument().clone();
        return true;
    }

    private Rectangle2D drawResizeRect(Graphics2D g2, Point2D loc) {
        double a = 10;
        Rectangle2D rect = new Rectangle2D.Double(loc.getX() - a / 2, loc.getY() - a / 2, a, a);
        Color c = g2.getColor();
        g2.setColor(Color.GREEN);
        g2.fill(rect);
        g2.setColor(c);
        return rect;
    }

    public void handle(Graphics2D g2, Point2D[] resizePoints) {
        if (isSelected) {
            for (int i = 0; i < resizePoints.length; i++) {
                resizeRects[i] = drawResizeRect(g2, resizePoints[i]);
            }
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        widget.getWidget().repaint();
    }
}
