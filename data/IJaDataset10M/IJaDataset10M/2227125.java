package org.fao.waicent.xmap2D.symbols;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import org.fao.waicent.attributes.Context;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextMarkerSymbol extends BaseTextSymbol {

    public void load(Document doc, Element ele) throws IOException {
    }

    public void save(Document doc, Element ele) throws IOException {
    }

    public int getVAlignment() {
        return _valignment;
    }

    public void setVAlignment(int l) {
        _valignment = l;
    }

    public int getHAlignment() {
        return _halignment;
    }

    public void setHAlignment(int l) {
        _halignment = l;
    }

    public int getPrintMode() {
        return _printmode;
    }

    public void setPrintMode(int l) {
        _printmode = l;
    }

    public double getAngle() {
        return java.lang.Math.toDegrees(_angle);
    }

    public void setAngle(double a) {
        _angle = java.lang.Math.toRadians(a);
        return;
    }

    public double getInterval() {
        return _interval;
    }

    public void setInterval(double i) {
        _interval = i;
    }

    public boolean getOverlap() {
        return _overlap;
    }

    public void setOverlap(boolean f) {
        _overlap = f;
    }

    public void draw(Context c, double x, double y, double z, Graphics2D g2d) {
        String s = "my test";
        g2d.setComposite(AlphaComposite.getInstance(3, (float) getTransparency()));
        if (getAntialiasing()) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        Dimension dimension = getSizeTextWithEffect(s);
        double twidth = dimension.getWidth();
        double theight = dimension.getHeight();
        double xscale = g2d.getTransform().getScaleX();
        double yscale = g2d.getTransform().getScaleY();
        double xpos = x;
        double ypos = y;
        if (_halignment == 0 && _valignment == 0 && _angle != 0.0) {
            xpos = x;
            ypos = y;
        } else {
            double xoffset = (_interval + twidth / 2.0) / xscale;
            double yoffset = (_interval + theight / 2.0) / yscale;
            switch(_halignment) {
                case ALIGNMENT_LEFT:
                    xpos = x - xoffset;
                    break;
                case ALIGNMENT_RIGHT:
                    xpos = x + xoffset;
                    break;
                default:
                    xpos = x;
                    break;
            }
            switch(_valignment) {
                case ALIGNMENT_TOP:
                    ypos = y + yoffset;
                    break;
                case ALIGNMENT_BOTTOM:
                    ypos = y - yoffset;
                    break;
                default:
                    ypos = y;
                    break;
            }
        }
        drawTextWithEffect(g2d, s, xpos, ypos, _angle, getFont(), getColor(), getBlockOut(), getOutline(), getShadow(), getGlowing(), false);
        g2d.setComposite(AlphaComposite.SrcOver);
        return;
    }

    public TextMarkerSymbol() {
        _overlap = DEFAULT_OVERLAP;
        _interval = 0.0D;
        _angle = 0.0D;
        _printmode = 0;
        _halignment = 0;
        _valignment = 0;
    }

    private int _valignment;

    private int _halignment;

    private int _printmode;

    private double _angle;

    private double _interval;

    private boolean _overlap;

    public static final boolean DEFAULT_OVERLAP = true;

    public static final int ALIGNMENT_TOP = 4;

    public static final int ALIGNMENT_BOTTOM = 3;

    public static final int ALIGNMENT_RIGHT = 2;

    public static final int ALIGNMENT_LEFT = 1;

    public static final int ALIGNMENT_CENTER = 0;
}
