package com.jmex.bui.layout;

import com.jmex.bui.BComponent;
import com.jmex.bui.BContainer;
import com.jmex.bui.util.Dimension;
import com.jmex.bui.util.Insets;
import com.jmex.bui.util.Rectangle;

/**
 * Handles horizontally laid out groups.
 *
 * @see GroupLayout
 */
public class HGroupLayout extends GroupLayout {

    public Dimension computePreferredSize(BContainer target, int whint, int hhint) {
        DimenInfo info = computeDimens(target, true, whint, hhint);
        Dimension dims = new Dimension();
        if (_policy == STRETCH) {
            dims.width = info.maxfreewid * (info.count - info.numfix) + info.fixwid;
        } else if (_policy == EQUALIZE) {
            dims.width = info.maxwid * info.count;
        } else {
            dims.width = info.totwid;
        }
        dims.width += (info.count - 1) * _gap;
        dims.height = info.maxhei;
        return dims;
    }

    public void layoutContainer(BContainer target) {
        Rectangle b = target.getBounds();
        Insets insets = target.getInsets();
        b.width -= insets.getHorizontal();
        b.height -= insets.getVertical();
        DimenInfo info = computeDimens(target, true, b.width, b.height);
        int nk = target.getComponentCount();
        int sx, sy;
        int totwid, totgap = _gap * (info.count - 1);
        int freecount = info.count - info.numfix;
        int freefrac = 0;
        int defwid = 0;
        float conscale = 1f;
        if (_policy == STRETCH) {
            if (freecount > 0) {
                int freewid = b.width - info.fixwid - totgap;
                defwid = freewid / freecount;
                freefrac = freewid % freecount;
                totwid = b.width;
            } else {
                totwid = info.fixwid + totgap;
            }
        } else if (_policy == EQUALIZE) {
            defwid = info.maxwid;
            totwid = info.fixwid + defwid * freecount + totgap;
        } else if (_policy == CONSTRAIN) {
            totwid = info.totwid + totgap;
            if (totwid > b.width) {
                conscale = (b.width - totgap) / (float) info.totwid;
                totwid = b.width;
            }
        } else {
            totwid = info.totwid + totgap;
        }
        int defhei = 0;
        if (_offpolicy == STRETCH) {
            defhei = b.height;
        } else if (_offpolicy == EQUALIZE) {
            defhei = info.maxhei;
        }
        if (_justification == LEFT || _justification == TOP) {
            sx = insets.left;
        } else if (_justification == CENTER) {
            sx = insets.left + (b.width - totwid) / 2;
        } else {
            sx = insets.left + b.width - totwid;
        }
        for (int i = 0; i < nk; i++) {
            if (info.dimens[i] == null) {
                continue;
            }
            BComponent child = target.getComponent(i);
            int newwid, newhei;
            if (_policy == NONE || isFixed(child)) {
                newwid = info.dimens[i].width;
            } else if (_policy == CONSTRAIN) {
                newwid = Math.max(1, (int) (conscale * info.dimens[i].width));
            } else {
                newwid = defwid + freefrac;
                freefrac = 0;
            }
            if (_offpolicy == NONE) {
                newhei = info.dimens[i].height;
            } else if (_offpolicy == CONSTRAIN) {
                newhei = Math.min(info.dimens[i].height, b.height);
            } else {
                newhei = defhei;
            }
            if (_offjust == RIGHT || _offjust == TOP) {
                sy = insets.bottom + b.height - newhei;
            } else if (_offjust == LEFT || _offjust == BOTTOM) {
                sy = insets.bottom;
            } else {
                sy = insets.bottom + (b.height - newhei) / 2;
            }
            child.setBounds(sx, sy, newwid, newhei);
            sx += child.getWidth() + _gap;
        }
    }
}
