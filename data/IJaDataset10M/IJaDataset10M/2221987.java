package org.makagiga.commons.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import org.makagiga.commons.UI;
import org.makagiga.commons.annotation.Uninstantiable;

/**
 * @since 4.0 (org.makagiga.commons.swing package)
 */
public final class MSplashScreen {

    private static boolean closed;

    private static Color textColor = Color.BLACK;

    private static MLabel windowImage;

    private static MWindow window;

    private static Rectangle progressBarBounds;

    private static SimpleProgressBar progressBar;

    private static String previousText;

    /**
	 * Closes the splash screen.
	 */
    public static synchronized void close() {
        if (closed) return;
        if (window != null) {
            window.dispose();
            window = null;
        } else {
            SplashScreen splashScreen = SplashScreen.getSplashScreen();
            if (splashScreen != null) splashScreen.close();
        }
        progressBar = null;
        windowImage = null;
        closed = true;
    }

    /**
	 * @since 3.8.5
	 */
    public static synchronized SimpleProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new SimpleProgressBar();
            progressBar.setBorderPainted(false);
        }
        return progressBar;
    }

    public static synchronized boolean isAvailable() {
        return !closed && SplashScreen.getSplashScreen() != null;
    }

    public static synchronized void setImage(final Image value) {
        if (UI.isEmpty(value)) return;
        if (window == null) {
            window = new MWindow();
            windowImage = new MLabel();
            windowImage.setHorizontalAlignment(UI.CENTER);
            windowImage.setVerticalAlignment(UI.CENTER);
            window.addCenter(windowImage);
        }
        windowImage.setImage(value);
        Rectangle r = new Rectangle(0, 0, value.getWidth(null), value.getHeight(null));
        window.setSize(r.getSize());
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        windowImage.paintImmediately(r);
        closed = false;
    }

    /**
	 * @since 4.0
	 */
    public static void setProgressBarBounds(final int margin, final int height) {
        progressBarBounds = new Rectangle(margin, 1, 1, height);
    }

    public static void setText(final String text) {
        setText(text, Integer.MIN_VALUE);
    }

    /**
	 * @since 3.8.5
	 */
    public static synchronized void setText(final String text, final int progressValue) {
        Graphics graphics = null;
        Rectangle r = null;
        SplashScreen splashScreen = null;
        if (windowImage != null) {
            graphics = windowImage.getGraphics();
            r = windowImage.getBounds();
        } else {
            splashScreen = SplashScreen.getSplashScreen();
            if (splashScreen != null) {
                graphics = splashScreen.createGraphics();
                r = splashScreen.getBounds();
            }
        }
        if (!(graphics instanceof Graphics2D) || (r == null)) return;
        Graphics2D g = (Graphics2D) graphics;
        if ((previousText != null) && (windowImage != null)) {
            windowImage.paintImmediately(windowImage.getBounds());
        }
        previousText = text;
        int progressMargin = -1;
        int progressY = -1;
        if ((progressValue >= 0) && (progressBar != null)) {
            int progressHeight;
            progressBar.setValue(progressValue);
            if (progressBarBounds == null) {
                progressHeight = 2;
                progressMargin = 1;
            } else {
                progressHeight = progressBarBounds.height;
                progressMargin = progressBarBounds.x;
            }
            progressY = r.height - progressHeight - progressMargin;
            progressBar.paint(g, progressMargin, progressY, r.width - progressMargin * 2, progressHeight);
        }
        if ((text != null) && (progressY != -1)) {
            int fontSize = (progressBarBounds != null) ? (progressBarBounds.height - 2) : 0;
            if (fontSize < 8) fontSize = UI.getDefaultFontSize() - 1;
            g.setFont(new Font(Font.DIALOG, Font.PLAIN, fontSize));
            UI.setTextAntialiasing(g, null);
            FontMetrics fm = g.getFontMetrics();
            int textMargin = progressMargin + 1;
            int y = progressY + fm.getAscent();
            g.setColor(textColor);
            g.drawString(text, textMargin, y);
        }
        g.dispose();
        if (splashScreen != null) splashScreen.update();
    }

    public static void setTextColor(final Color value) {
        textColor = value;
    }

    @Uninstantiable
    private MSplashScreen() {
    }
}
