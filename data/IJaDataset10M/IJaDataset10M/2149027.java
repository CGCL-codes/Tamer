package com.memoire.dja;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * Independant graphics ops (1.2 implementation).
 */
public class DjaGraphics2 extends DjaGraphics0 {

    public static Stroke getStroke(DjaGraphics.BresenhamParams _bp) {
        float e = _bp.epaisseur_;
        float[] dash = null;
        if (_bp.vide_ > 0) dash = new float[] { _bp.trait_, 2.f + e * _bp.vide_ };
        return new BasicStroke(e, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 2.f * e, dash, 0.f);
    }

    public void drawLine(Graphics _g, int _x1, int _y1, int _x2, int _y2, DjaGraphics.BresenhamParams _bp) {
        Graphics2D g2d = (Graphics2D) _g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(getStroke(_bp));
        g2d.drawLine(_x1, _y1, _x2, _y2);
        g2d.setStroke(old);
    }

    public void drawRect(Graphics _g, int _x, int _y, int _w, int _h, DjaGraphics.BresenhamParams _bp) {
        Graphics2D g2d = (Graphics2D) _g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(getStroke(_bp));
        g2d.drawRect(_x, _y, _w, _h);
        g2d.setStroke(old);
    }

    public void drawRoundRect(Graphics _g, int _x, int _y, int _w, int _h, int _rh, int _rv, DjaGraphics.BresenhamParams _bp) {
        Graphics2D g2d = (Graphics2D) _g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(getStroke(_bp));
        g2d.drawRoundRect(_x, _y, _w, _h, _rh, _rv);
        g2d.setStroke(old);
    }

    public void fillRoundRect(Graphics _g, int _x, int _y, int _w, int _h, int _rh, int _rv) {
        _g.fillRoundRect(_x, _y, _w, _h, _rh, _rv);
    }

    public void drawOval(Graphics _g, int _x, int _y, int _w, int _h, DjaGraphics.BresenhamParams _bp) {
        Graphics2D g2d = (Graphics2D) _g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(getStroke(_bp));
        g2d.drawOval(_x, _y, _w, _h);
        g2d.setStroke(old);
    }

    public void fillOval(Graphics _g, int _x, int _y, int _w, int _h) {
        _g.fillOval(_x, _y, _w, _h);
    }

    public void drawPolyline(Graphics _g, int[] _xpoints, int[] _ypoints, int _npoints, DjaGraphics.BresenhamParams _bp) {
        Graphics2D g2d = (Graphics2D) _g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(getStroke(_bp));
        g2d.drawPolyline(_xpoints, _ypoints, _npoints);
        g2d.setStroke(old);
    }

    public void drawPolygon(Graphics _g, int[] _xpoints, int[] _ypoints, int _npoints, DjaGraphics.BresenhamParams _bp) {
        Graphics2D g2d = (Graphics2D) _g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(getStroke(_bp));
        g2d.drawPolygon(_xpoints, _ypoints, _npoints);
        g2d.setStroke(old);
    }
}
