package com.memoire.bu;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Shape;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

/**
 * This is a border that will display only selected edges.
 */
public class BuCropBorder extends AbstractBorder {

    protected Border border_;

    protected boolean top_, left_, bottom_, right_;

    public BuCropBorder(Border _border, boolean _top, boolean _left, boolean _bottom, boolean _right) {
        border_ = _border;
        top_ = _top;
        left_ = _left;
        bottom_ = _bottom;
        right_ = _right;
    }

    public void paintBorder(Component _c, Graphics _g, int _x, int _y, int _w, int _h) {
        Shape old = _g.getClip();
        Insets r = border_.getBorderInsets(_c);
        _g.clipRect(_x, _y, _w, _h);
        int dx = left_ ? 0 : -r.left;
        int dy = top_ ? 0 : -r.top;
        int dw = right_ ? -dx : -dx + r.right;
        int dh = bottom_ ? -dy : -dy + r.bottom;
        border_.paintBorder(_c, _g, _x + dx, _y + dy, _w + dw, _h + dh);
        _g.setClip(old);
    }

    public Insets getBorderInsets(Component _c) {
        Insets r = border_.getBorderInsets(_c);
        return new Insets(top_ ? r.top : 0, left_ ? r.left : 0, bottom_ ? r.bottom : 0, right_ ? r.right : 0);
    }

    public boolean isBorderOpaque() {
        return border_.isBorderOpaque();
    }
}
