package org.fudaa.fudaa.sig;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import org.fudaa.ctulu.CtuluLib;

/**
 * @author fred deniger
 * @version $Id: FSigImageIconFixeSize.java,v 1.3 2006-09-19 15:10:20 deniger Exp $
 */
public class FSigImageIconFixeSize implements Icon {

    final int maxSize_;

    int wImg_;

    int hImg_;

    BufferedImage img_;

    public FSigImageIconFixeSize(final int _maxSize, final BufferedImage _img) {
        super();
        img_ = _img;
        maxSize_ = _maxSize;
        update();
    }

    private void update() {
        if (img_ != null) {
            hImg_ = img_.getHeight();
            wImg_ = img_.getWidth();
            if (hImg_ > maxSize_ || wImg_ > maxSize_) {
                final double ratio = ((double) maxSize_) / Math.max(wImg_, hImg_);
                hImg_ = (int) (hImg_ * ratio);
                wImg_ = (int) (wImg_ * ratio);
            }
        }
    }

    public int getIconHeight() {
        return maxSize_;
    }

    public int getIconWidth() {
        return maxSize_;
    }

    public void paintIcon(final Component _c, final Graphics _g, final int _x, final int _y) {
        if (img_ == null) {
            _g.setFont(CtuluLib.getMiniFont());
            final String s = FSigLib.getS("Pas d'image");
            int wString = _g.getFontMetrics().stringWidth(s);
            if (wString < getIconWidth()) {
                wString = (getIconWidth() - wString) / 2;
            } else {
                wString = 0;
            }
            ((Graphics2D) _g).drawString(s, _x + wString, _y + getIconHeight() / 2);
        } else {
            ((Graphics2D) _g).drawImage(img_, _x, _y, wImg_, hImg_, null);
        }
    }

    public BufferedImage getImg() {
        return img_;
    }

    public void setImg(final BufferedImage _img) {
        img_ = _img;
        update();
    }
}
