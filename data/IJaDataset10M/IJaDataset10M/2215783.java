package org.fudaa.ctulu.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.SwingConstants;
import com.memoire.bu.BuHorizontalLayout;

/**
 * Lays out horizontally a set of components.
 */
public class CtuluHorizontalLayout extends BuHorizontalLayout {

    int hAligment_ = SwingConstants.TOP;

    public CtuluHorizontalLayout() {
        this(0, false, true);
    }

    public CtuluHorizontalLayout(final int _hgap) {
        this(_hgap, false, true);
    }

    public CtuluHorizontalLayout(final int _hgap, final boolean _hfilled, final boolean _vfilled) {
        super(_hgap, _hfilled, _vfilled);
    }

    public void layoutContainer(final Container _parent) {
        if (hAligment_ == SwingConstants.TOP || vfilled_) {
            super.layoutContainer(_parent);
        } else {
            synchronized (_parent.getTreeLock()) {
                final Insets insets = _parent.getInsets();
                final Rectangle bounds = new Rectangle(insets.left, insets.top, _parent.getSize().width - (insets.left + insets.right), _parent.getSize().height - (insets.top + insets.bottom));
                final int nbc = _parent.getComponentCount();
                int x = 0;
                boolean p = true;
                for (int i = 0; i < nbc; i++) {
                    final Component c = _parent.getComponent(i);
                    if (c.isVisible()) {
                        final Dimension d = c.getPreferredSize();
                        if (!p) {
                            x += hgap_;
                        }
                        p = false;
                        if (x > bounds.width) {
                            x = bounds.width;
                        }
                        int y = bounds.height - d.height;
                        if (y > bounds.height || y < 0) {
                            y = 0;
                        }
                        final Rectangle actual = new Rectangle(bounds.x + x, bounds.y + y, (hfilled_ && (i == nbc - 1) ? bounds.width - x : Math.min(bounds.width - x, d.width)), (vfilled_ ? bounds.height - y : Math.min(bounds.height - y, d.height)));
                        c.setBounds(actual.x, actual.y, actual.width, actual.height);
                        x += d.width;
                    }
                }
            }
        }
    }

    public int getHAligment() {
        return hAligment_;
    }

    public void setHAligment(final int _aligment) {
        hAligment_ = _aligment;
    }
}
