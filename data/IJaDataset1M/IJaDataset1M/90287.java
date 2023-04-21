package com.petersoft.white;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ScrollBarUI extends BasicScrollBarUI {

    public static final String FREE_STANDING_PROP = "JScrollBar.isFreeStanding";

    protected boolean isRollover = false;

    protected boolean wasRollover = false;

    Image trackVertical = new ImageIcon(ScrollBarUI.class.getResource("images/PScrollBar/PScrollBar_background_vertical.png")).getImage();

    Image thumbVertical = new ImageIcon(ScrollBarUI.class.getResource("images/PScrollBar/PScrollBar_thumb_vertical.png")).getImage();

    Image thumbBackgroundVertical = new ImageIcon(ScrollBarUI.class.getResource("images/PScrollBar/PScrollBar_thumb_background_vertical.png")).getImage();

    Image trackHorizontal = new ImageIcon(ScrollBarUI.class.getResource("images/PScrollBar/PScrollBar_background_horizontal.png")).getImage();

    Image thumbHorizontal = new ImageIcon(ScrollBarUI.class.getResource("images/PScrollBar/PScrollBar_thumb_horizontal.png")).getImage();

    Image thumbBackgroundHorizontal = new ImageIcon(ScrollBarUI.class.getResource("images/PScrollBar/PScrollBar_thumb_background_horizontal.png")).getImage();

    private final int MIN_THUMB_SIZE = 14;

    protected void installDefaults() {
        super.installDefaults();
    }

    public static ComponentUI createUI(JComponent c) {
        return new ScrollBarUI();
    }

    protected JButton createDecreaseButton(int orientation) {
        return new ScrollBarButton(orientation);
    }

    protected JButton createIncreaseButton(int orientation) {
        return new ScrollBarButton(orientation);
    }

    public Dimension getPreferredSize(JComponent c) {
        Insets insets = c.getInsets();
        int dx = insets.left + insets.right;
        int dy = insets.top + insets.bottom;
        return (scrollbar.getOrientation() == JScrollBar.VERTICAL) ? new Dimension(dx + 16, dy + 33) : new Dimension(dx + 33, dy + 11);
    }

    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(Color.red);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    protected void layoutVScrollbar(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;
        int decrButtonH = decrButton.getPreferredSize().height;
        int decrButtonY = sbInsets.top;
        int incrButtonH = incrButton.getPreferredSize().height;
        int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButtonH + incrButtonH;
        float trackH = sbSize.height - (sbInsetsH + sbButtonsH);
        float min = sb.getMinimum();
        float extent = sb.getVisibleAmount();
        float range = sb.getMaximum() - min;
        float value = sb.getValue();
        int thumbH = (range <= 0) ? getMaximumThumbSize().height : (int) (trackH * (extent / range));
        thumbH = Math.max(thumbH, getMinimumThumbSize().height);
        thumbH = Math.min(thumbH, getMaximumThumbSize().height);
        if (thumbH < MIN_THUMB_SIZE) thumbH = MIN_THUMB_SIZE;
        int thumbY = incrButtonY - thumbH;
        if (sb.getValue() < (sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - thumbH;
            thumbY = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
            thumbY += decrButtonY + decrButtonH;
        }
        int sbAvailButtonH = (sbSize.height - sbInsetsH);
        if (sbAvailButtonH < sbButtonsH) {
            incrButtonH = decrButtonH = sbAvailButtonH / 2;
            incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        }
        decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
        incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);
        int itrackY = decrButtonY + decrButtonH;
        int itrackH = incrButtonY - itrackY;
        trackRect.setBounds(itemX, itrackY, itemW, itrackH);
        if (thumbH >= (int) trackH) {
            setThumbBounds(0, 0, 0, 0);
        } else {
            if ((thumbY + thumbH) > incrButtonY) {
                thumbY = incrButtonY - thumbH;
            }
            if (thumbY < (decrButtonY + decrButtonH)) {
                thumbY = decrButtonY + decrButtonH + 1;
            }
            setThumbBounds(itemX, thumbY, itemW, thumbH);
        }
    }

    protected void layoutHScrollbar(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;
        boolean ltr = sb.getComponentOrientation().isLeftToRight();
        int leftButtonW = (ltr ? decrButton : incrButton).getPreferredSize().width;
        int rightButtonW = (ltr ? incrButton : decrButton).getPreferredSize().width;
        int leftButtonX = sbInsets.left;
        int rightButtonX = sbSize.width - (sbInsets.right + rightButtonW);
        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = leftButtonW + rightButtonW;
        float trackW = sbSize.width - (sbInsetsW + sbButtonsW);
        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = sb.getValue();
        int thumbW = (range <= 0) ? getMaximumThumbSize().width : (int) (trackW * (extent / range));
        thumbW = Math.max(thumbW, getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, getMaximumThumbSize().width);
        if (thumbW < MIN_THUMB_SIZE) thumbW = MIN_THUMB_SIZE;
        int thumbX = ltr ? rightButtonX - thumbW : leftButtonX + leftButtonW;
        if (sb.getValue() < (max - sb.getVisibleAmount())) {
            float thumbRange = trackW - thumbW;
            if (ltr) {
                thumbX = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
            } else {
                thumbX = (int) (0.5f + (thumbRange * ((max - extent - value) / (range - extent))));
            }
            thumbX += leftButtonX + leftButtonW;
        }
        int sbAvailButtonW = (sbSize.width - sbInsetsW);
        if (sbAvailButtonW < sbButtonsW) {
            rightButtonW = leftButtonW = sbAvailButtonW / 2;
            rightButtonX = sbSize.width - (sbInsets.right + rightButtonW);
        }
        (ltr ? decrButton : incrButton).setBounds(leftButtonX, itemY, leftButtonW, itemH);
        (ltr ? incrButton : decrButton).setBounds(rightButtonX, itemY, rightButtonW, itemH);
        int itrackX = leftButtonX + leftButtonW;
        int itrackW = rightButtonX - itrackX;
        trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        if (thumbW >= (int) trackW) {
            setThumbBounds(0, 0, 0, 0);
        } else {
            if (thumbX + thumbW > rightButtonX) {
                thumbX = rightButtonX - thumbW;
            }
            if (thumbX < leftButtonX + leftButtonW) {
                thumbX = leftButtonX + leftButtonW + 1;
            }
            setThumbBounds(thumbX, itemY, thumbW, itemH);
        }
    }

    public void paint(Graphics g, JComponent c) {
        Rectangle thumbBounds = getThumbBounds();
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            g.drawImage(trackVertical, 0, 0, c.getWidth(), c.getHeight(), null);
            g.setColor(new Color(178, 178, 178));
            g.drawRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 1, thumbBounds.height - 1);
            g.drawImage(thumbBackgroundVertical, thumbBounds.x + 1, thumbBounds.y + 1, thumbBounds.width - 2, thumbBounds.height - 2, null);
            g.drawImage(thumbVertical, (thumbBounds.width - 6) / 2 + thumbBounds.x, (thumbBounds.height - 8) / 2 + thumbBounds.y, 6, 8, null);
        } else {
            g.drawImage(trackHorizontal, 0, 0, c.getWidth(), c.getHeight(), null);
            g.setColor(new Color(178, 178, 178));
            g.drawRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 1, thumbBounds.height - 1);
            g.drawImage(thumbBackgroundHorizontal, thumbBounds.x + 1, thumbBounds.y + 1, thumbBounds.width - 2, thumbBounds.height - 2, null);
            g.drawImage(thumbHorizontal, (thumbBounds.width - 6) / 2 + thumbBounds.x, (thumbBounds.height - 8) / 2 + thumbBounds.y, 8, 6, null);
        }
    }
}
