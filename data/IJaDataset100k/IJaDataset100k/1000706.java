package de.muntjak.tinylookandfeel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicProgressBarUI;
import de.muntjak.tinylookandfeel.controlpanel.*;
import de.muntjak.tinylookandfeel.util.ColorRoutines;

/**
 * TinyProgressBarUI
 * 
 * @version 1.1
 * @author Hans Bickel
 */
public class TinyProgressBarUI extends BasicProgressBarUI {

    private static final HashMap cache = new HashMap();

    private static final Dimension PREFERRED_YQ_HORIZONTAL = new Dimension(146, 7);

    private static final Dimension PREFERRED_YQ_VERTICAL = new Dimension(7, 146);

    public static void clearCache() {
        cache.clear();
    }

    protected Dimension getPreferredInnerHorizontal() {
        return PREFERRED_YQ_HORIZONTAL;
    }

    protected Dimension getPreferredInnerVertical() {
        return PREFERRED_YQ_VERTICAL;
    }

    /**
	 * Creates the UI delegate for the given component.
	 *
	 * @param mainColor The component to create its UI delegate.
	 * @return The UI delegate for the given component.
	 */
    public static ComponentUI createUI(JComponent c) {
        return new TinyProgressBarUI();
    }

    protected void paintDeterminate(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets();
        int barRectWidth = progressBar.getWidth() - (b.right + b.left);
        int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);
        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
            drawXpHorzProgress(g, b.left, b.top, barRectWidth, barRectHeight, amountFull);
            if (progressBar.isStringPainted()) {
                g.setFont(c.getFont());
                paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
            }
        } else {
            int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
            drawXpVertProgress(g, b.left, b.top, barRectWidth, barRectHeight, amountFull);
            if (progressBar.isStringPainted()) {
                g.setFont(c.getFont());
                paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
            }
        }
    }

    private void drawXpHorzProgress(Graphics g, int x, int y, int w, int h, int amountFull) {
        g.translate(x, y);
        if (!progressBar.isOpaque()) {
            g.setColor(progressBar.getBackground());
            g.fillRect(0, 0, w, h);
        }
        ProgressKey key = new ProgressKey(progressBar.getForeground(), true, h);
        Object value = cache.get(key);
        if (value == null) {
            Image img = new BufferedImage(6, h, BufferedImage.TYPE_INT_ARGB);
            Graphics imgGraphics = img.getGraphics();
            Color c = progressBar.getForeground();
            Color c2 = ColorRoutines.lighten(c, 15);
            Color c3 = ColorRoutines.lighten(c, 35);
            Color c4 = ColorRoutines.lighten(c, 60);
            imgGraphics.setColor(c4);
            imgGraphics.drawLine(0, 0, 5, 0);
            imgGraphics.drawLine(0, h - 1, 5, h - 1);
            imgGraphics.setColor(c3);
            imgGraphics.drawLine(0, 1, 5, 1);
            imgGraphics.drawLine(0, h - 2, 5, h - 2);
            imgGraphics.setColor(c2);
            imgGraphics.drawLine(0, 2, 5, 2);
            imgGraphics.drawLine(0, h - 3, 5, h - 3);
            imgGraphics.setColor(c);
            imgGraphics.fillRect(0, 3, 6, h - 6);
            imgGraphics.dispose();
            cache.put(key, img);
            value = img;
            if (TinyLookAndFeel.PRINT_CACHE_SIZES) {
                System.out.println("TinyProgressBarUI.cache.size=" + cache.size());
            }
        }
        int mx = 0;
        while (mx < amountFull) {
            if (mx + 6 > w) {
                g.drawImage((Image) value, mx, 0, w - mx, h, progressBar);
            } else {
                g.drawImage((Image) value, mx, 0, progressBar);
            }
            mx += 8;
        }
        g.translate(-x, -y);
    }

    private void drawXpVertProgress(Graphics g, int x, int y, int w, int h, int amountFull) {
        g.translate(x, y);
        if (!progressBar.isOpaque()) {
            g.setColor(progressBar.getBackground());
            g.fillRect(0, 0, w, h);
        }
        ProgressKey key = new ProgressKey(progressBar.getForeground(), false, w);
        Object value = cache.get(key);
        if (value == null) {
            Image img = new BufferedImage(w, 6, BufferedImage.TYPE_INT_ARGB);
            Graphics imgGraphics = img.getGraphics();
            Color c = progressBar.getForeground();
            Color c2 = ColorRoutines.lighten(c, 15);
            Color c3 = ColorRoutines.lighten(c, 35);
            Color c4 = ColorRoutines.lighten(c, 60);
            imgGraphics.setColor(c4);
            imgGraphics.drawLine(0, 0, 0, 5);
            imgGraphics.drawLine(w - 1, 0, w - 1, 5);
            imgGraphics.setColor(c3);
            imgGraphics.drawLine(1, 0, 1, 5);
            imgGraphics.drawLine(w - 2, 0, w - 2, 5);
            imgGraphics.setColor(c2);
            imgGraphics.drawLine(2, 0, 2, 5);
            imgGraphics.drawLine(w - 3, 0, w - 3, 5);
            imgGraphics.setColor(c);
            imgGraphics.fillRect(3, 0, w - 6, 6);
            imgGraphics.dispose();
            cache.put(key, img);
            value = img;
            if (TinyLookAndFeel.PRINT_CACHE_SIZES) {
                System.out.println("TinyProgressBarUI.cache.size=" + cache.size());
            }
        }
        int my = 0;
        while (my < amountFull) {
            if (my + 6 > h) {
                g.drawImage((Image) value, 0, 0, w, h - my, progressBar);
            } else {
                g.drawImage((Image) value, 0, h - my - 6, progressBar);
            }
            my += 8;
        }
        g.translate(-x, -y);
    }

    protected void paintIndeterminate(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets();
        int barRectWidth = progressBar.getWidth() - (b.right + b.left);
        int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);
        Rectangle boxRect = new Rectangle();
        try {
            boxRect = getBox(boxRect);
        } catch (NullPointerException ignore) {
        }
        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            drawXpHorzProgress(g, b.left, b.top, barRectWidth, barRectHeight, boxRect);
        } else {
            drawXpVertProgress(g, b.left, b.top, barRectWidth, barRectHeight, boxRect);
        }
        if (progressBar.isStringPainted()) {
            if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
                paintString(g, b.left, b.top, barRectWidth, barRectHeight, boxRect.width, b);
            } else {
                paintString(g, b.left, b.top, barRectWidth, barRectHeight, boxRect.height, b);
            }
        }
    }

    private void drawXpHorzProgress(Graphics g, int x, int y, int w, int h, Rectangle boxRect) {
        if (!progressBar.isOpaque()) {
            g.setColor(progressBar.getBackground());
            g.fillRect(x, y, w, h);
        }
        g.translate(boxRect.x, boxRect.y);
        ProgressKey key = new ProgressKey(progressBar.getForeground(), true, h);
        Object value = cache.get(key);
        if (value == null) {
            Image img = new BufferedImage(6, h, BufferedImage.TYPE_INT_ARGB);
            Graphics imgGraphics = img.getGraphics();
            Color c = progressBar.getForeground();
            Color c2 = ColorRoutines.lighten(c, 15);
            Color c3 = ColorRoutines.lighten(c, 35);
            Color c4 = ColorRoutines.lighten(c, 60);
            imgGraphics.setColor(c4);
            imgGraphics.drawLine(0, 0, 5, 0);
            imgGraphics.drawLine(0, h - 1, 5, h - 1);
            imgGraphics.setColor(c3);
            imgGraphics.drawLine(0, 1, 5, 1);
            imgGraphics.drawLine(0, h - 2, 5, h - 2);
            imgGraphics.setColor(c2);
            imgGraphics.drawLine(0, 2, 5, 2);
            imgGraphics.drawLine(0, h - 3, 5, h - 3);
            imgGraphics.setColor(c);
            imgGraphics.fillRect(0, 3, 6, h - 6);
            imgGraphics.dispose();
            cache.put(key, img);
            value = img;
            if (TinyLookAndFeel.PRINT_CACHE_SIZES) {
                System.out.println("TinyProgressBarUI.cache.size=" + cache.size());
            }
        }
        int mx = 0;
        while (mx + 6 < boxRect.width) {
            g.drawImage((Image) value, mx, 0, progressBar);
            mx += 8;
        }
        g.translate(-boxRect.x, -boxRect.y);
    }

    private void drawXpVertProgress(Graphics g, int x, int y, int w, int h, Rectangle boxRect) {
        if (!progressBar.isOpaque()) {
            g.setColor(progressBar.getBackground());
            g.fillRect(x, y, w, h);
        }
        g.translate(boxRect.x, boxRect.y);
        ProgressKey key = new ProgressKey(progressBar.getForeground(), false, w);
        Object value = cache.get(key);
        if (value == null) {
            Image img = new BufferedImage(w, 6, BufferedImage.TYPE_INT_ARGB);
            Graphics imgGraphics = img.getGraphics();
            Color c = progressBar.getForeground();
            Color c2 = ColorRoutines.lighten(c, 15);
            Color c3 = ColorRoutines.lighten(c, 35);
            Color c4 = ColorRoutines.lighten(c, 60);
            imgGraphics.setColor(c4);
            imgGraphics.drawLine(0, 0, 0, 5);
            imgGraphics.drawLine(w - 1, 0, w - 1, 5);
            imgGraphics.setColor(c3);
            imgGraphics.drawLine(1, 0, 1, 5);
            imgGraphics.drawLine(w - 2, 0, w - 2, 5);
            imgGraphics.setColor(c2);
            imgGraphics.drawLine(2, 0, 2, 5);
            imgGraphics.drawLine(w - 3, 0, w - 3, 5);
            imgGraphics.setColor(c);
            imgGraphics.fillRect(3, 0, w - 6, 6);
            imgGraphics.dispose();
            cache.put(key, img);
            value = img;
            if (TinyLookAndFeel.PRINT_CACHE_SIZES) {
                System.out.println("TinyProgressBarUI.cache.size=" + cache.size());
            }
        }
        int my = 0;
        while (my + 6 < boxRect.height) {
            g.drawImage((Image) value, 0, my, progressBar);
            my += 8;
        }
        g.translate(-boxRect.x, -boxRect.y);
    }

    protected Color getSelectionForeground() {
        return Theme.progressSelectForeColor.getColor();
    }

    protected Color getSelectionBackground() {
        return Theme.progressSelectBackColor.getColor();
    }

    protected void installDefaults() {
        LookAndFeel.installBorder(progressBar, "ProgressBar.border");
        LookAndFeel.installColorsAndFont(progressBar, "ProgressBar.background", "ProgressBar.foreground", "ProgressBar.font");
    }

    private static class ProgressKey {

        private Color c;

        private boolean horizontal;

        private int size;

        ProgressKey(Color c, boolean horizontal, int size) {
            this.c = c;
            this.horizontal = horizontal;
            this.size = size;
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof ProgressKey)) return false;
            ProgressKey other = (ProgressKey) o;
            return size == other.size && horizontal == other.horizontal && c.equals(other.c);
        }

        public int hashCode() {
            return c.hashCode() * (horizontal ? 1 : 2) * size;
        }
    }
}
