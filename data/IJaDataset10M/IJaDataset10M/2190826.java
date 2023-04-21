package org.tn5250j.tools;

import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.font.*;
import java.awt.geom.AffineTransform;
import org.tn5250j.*;

public class GUIGraphicsUtils {

    private static final Insets GROOVE_INSETS = new Insets(2, 2, 2, 2);

    private static final Insets ETCHED_INSETS = new Insets(2, 2, 2, 2);

    public static final int RAISED = 1;

    public static final int INSET = 2;

    public static final int WINDOW_NORMAL = 3;

    public static final int WINDOW_GRAPHIC = 4;

    public static void draw3DLeft(Graphics2D g, int which, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        if (which == RAISED) {
            g.setColor(Color.white);
            g.drawLine(x, y, x + fmWidth, y);
            g.drawLine(x, y + 1, x + fmWidth, y + 1);
            g.drawLine(x, y, x, y + fmHeight - 2);
            g.drawLine(x + 1, y, x + 1, y + fmHeight - 2);
            g.setColor(Color.black);
            g.drawLine(x, y + fmHeight - 2, x + fmWidth, y + fmHeight - 2);
            g.setColor(Color.lightGray);
            g.drawLine(x + 1, y + fmHeight - 3, x + fmWidth, y + fmHeight - 3);
        }
        if (which == INSET) {
            g.setColor(Color.black);
            g.drawLine(x, y, x + fmWidth, y);
            g.drawLine(x, y + 1, x + fmWidth, y + 1);
            g.drawLine(x, y, x, y + fmHeight - 2);
            g.drawLine(x + 1, y, x + 1, y + fmHeight - 2);
            g.setColor(Color.white);
            g.drawLine(x, y + fmHeight - 2, x + fmWidth, y + fmHeight - 2);
            g.setColor(Color.lightGray);
            g.drawLine(x + 1, y + fmHeight - 3, x + fmWidth, y + fmHeight - 3);
        }
        g.setColor(oldColor);
    }

    public static void draw3DMiddle(Graphics2D g, int which, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        if (which == RAISED) {
            g.setColor(Color.white);
            g.drawLine(x, y, x + fmWidth, y);
            g.drawLine(x, y + 1, x + fmWidth, y + 1);
            g.setColor(Color.black);
            g.drawLine(x, y + fmHeight - 2, x + fmWidth, y + fmHeight - 2);
            g.setColor(Color.lightGray);
            g.drawLine(x, y + fmHeight - 3, x + fmWidth, y + fmHeight - 3);
        }
        if (which == INSET) {
            g.setColor(Color.black);
            g.drawLine(x, y, x + fmWidth, y);
            g.drawLine(x, y + 1, x + fmWidth, y + 1);
            g.setColor(Color.white);
            g.drawLine(x, y + fmHeight - 2, x + fmWidth, y + fmHeight - 2);
            g.setColor(Color.lightGray);
            g.drawLine(x, y + fmHeight - 3, x + fmWidth, y + fmHeight - 3);
        }
        g.setColor(oldColor);
    }

    public static void draw3DRight(Graphics2D g, int which, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        if (which == RAISED) {
            g.setColor(Color.white);
            g.drawLine(x, y, x + fmWidth - 2, y);
            g.drawLine(x, y + 1, x + fmWidth - 3, y + 1);
            g.setColor(Color.black);
            g.drawLine(x, y + fmHeight - 2, x + fmWidth - 2, y + fmHeight - 2);
            g.drawLine(x + fmWidth - 1, y, x + fmWidth - 1, y + fmHeight - 2);
            g.setColor(Color.lightGray);
            g.drawLine(x + fmWidth - 2, y + 1, x + fmWidth - 2, y + fmHeight - 3);
            g.drawLine(x, y + fmHeight - 3, x + fmWidth - 2, y + fmHeight - 3);
        }
        if (which == INSET) {
            g.setColor(Color.black);
            g.drawLine(x, y, x + fmWidth - 2, y);
            g.drawLine(x, y + 1, x + fmWidth - 3, y + 1);
            g.setColor(Color.white);
            g.drawLine(x, y + fmHeight - 2, x + fmWidth - 2, y + fmHeight - 2);
            g.drawLine(x + fmWidth - 1, y, x + fmWidth - 1, y + fmHeight - 2);
            g.setColor(Color.lightGray);
            g.drawLine(x + fmWidth - 2, y + 1, x + fmWidth - 2, y + fmHeight - 3);
            g.drawLine(x, y + fmHeight - 3, x + fmWidth - 2, y + fmHeight - 3);
        }
        g.setColor(oldColor);
    }

    public static void draw3DOne(Graphics2D g, int which, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        if (which == INSET) {
            g.setColor(Color.black);
            g.drawLine(x, y, x + fmWidth - 2, y);
            g.drawLine(x, y + 1, x + fmWidth - 3, y + 1);
            g.drawLine(x, y, x, y + fmHeight - 2);
            g.drawLine(x + 1, y, x + 1, y + fmHeight - 2);
            g.setColor(Color.white);
            g.drawLine(x, y + fmHeight - 2, x + fmWidth - 2, y + fmHeight - 2);
            g.drawLine(x + fmWidth - 1, y, x + fmWidth - 1, y + fmHeight - 2);
            g.setColor(Color.lightGray);
            g.drawLine(x + fmWidth - 2, y + 1, x + fmWidth - 2, y + fmHeight - 3);
            g.drawLine(x + 1, y + fmHeight - 3, x + fmWidth - 2, y + fmHeight - 3);
        }
        if (which == RAISED) {
            g.setColor(Color.white);
            g.drawLine(x, y, x + fmWidth - 2, y);
            g.drawLine(x, y + 1, x + fmWidth - 3, y + 1);
            g.drawLine(x, y, x, y + fmHeight - 2);
            g.drawLine(x + 1, y, x + 1, y + fmHeight - 2);
            g.setColor(Color.darkGray);
            g.drawLine(x, y + fmHeight - 2, x + fmWidth - 2, y + fmHeight - 2);
            g.drawLine(x + fmWidth - 1, y, x + fmWidth - 1, y + fmHeight - 2);
            g.setColor(Color.lightGray);
            g.drawLine(x + fmWidth - 2, y + 1, x + fmWidth - 2, y + fmHeight - 3);
            g.drawLine(x + 1, y + fmHeight - 3, x + fmWidth - 2, y + fmHeight - 3);
        }
        g.setColor(oldColor);
    }

    public static void drawScrollBar(Graphics2D g, int which, int direction, int x, int y, int fmWidth, int fmHeight, Color fg, Color bg) {
        Color oldColor = g.getColor();
        if (which == INSET) {
            g.setColor(bg);
            g.fillRect(x, y, fmWidth, fmHeight);
            g.setColor(fg);
            g.drawLine(x, y, x, y + fmHeight);
            g.drawLine(x + fmWidth - 1, y, x + fmWidth - 1, y + fmHeight);
        }
        if (which == RAISED) {
            g.setColor(bg);
            g.fillRect(x, y, fmWidth, fmHeight);
            g.setColor(fg);
            g.drawLine(x, y, x, y + fmHeight);
            g.drawLine(x + fmWidth - 1, y, x + fmWidth - 1, y + fmHeight);
        }
        if (direction == 1) {
            g.setColor(fg.brighter());
            g.drawLine(x + (fmWidth / 2), y + 2, x + 2, y + fmHeight - 4);
            g.setColor(fg.darker());
            g.drawLine(x + (fmWidth / 2), y + 2, x + fmWidth - 2, y + fmHeight - 4);
            g.drawLine(x + 2, y + fmHeight - 4, x + fmWidth - 2, y + fmHeight - 4);
            g.setColor(fg);
            g.drawLine(x, y, x + fmWidth - 1, y);
            g.drawLine(x, y + fmHeight - 1, x + fmWidth - 1, y + fmHeight - 1);
        }
        if (direction == 2) {
            g.setColor(fg.brighter());
            g.drawLine(x + (fmWidth / 2), y + fmHeight - 4, x + 2, y + 2);
            g.drawLine(x + 2, y + 2, x + fmWidth - 2, y + 2);
            g.setColor(fg.darker());
            g.drawLine(x + (fmWidth / 2), y + fmHeight - 4, x + fmWidth - 2, y + 2);
            g.setColor(fg);
            g.drawLine(x, y, x + fmWidth, y);
            g.drawLine(x, y + fmHeight - 1, x + fmWidth, y + fmHeight - 1);
        }
        if (direction == 3) {
            g.setColor(fg);
            g.fillRect(x + 2, y, fmWidth - 4, fmHeight);
        }
        g.setColor(oldColor);
    }

    public static void drawWinUpperLeft(Graphics2D g, int which, Color fill, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        g.setColor(fill);
        if (which == WINDOW_GRAPHIC) {
            g.fillRect(x, y, fmWidth, fmHeight);
            g.setColor(Color.white);
            g.drawLine(x, y, x + fmWidth, y);
            g.drawLine(x, y, x, y + fmHeight);
        }
        if (which == WINDOW_NORMAL) {
            g.drawLine(x + fmWidth / 2, y + fmHeight / 2, x + fmWidth, y + fmHeight / 2);
            g.drawLine(x + fmWidth / 2, y + fmHeight / 2, x + fmWidth / 2, y + fmHeight);
        }
        g.setColor(oldColor);
    }

    public static void drawWinUpper(Graphics2D g, int which, Color fill, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        g.setColor(fill);
        if (which == WINDOW_GRAPHIC) {
            g.fillRect(x, y, fmWidth, fmHeight);
            g.setColor(Color.white);
            g.drawLine(x, y, x + fmWidth, y);
            g.setColor(Color.black);
            g.drawLine(x, y + fmHeight - 1, x + fmWidth, y + fmHeight - 1);
            g.setColor(Color.white);
            g.drawLine(x, y + fmHeight, x + fmWidth, y + fmHeight);
        }
        if (which == WINDOW_NORMAL) {
            g.drawLine(x, y + fmHeight / 2, x + fmWidth, y + fmHeight / 2);
        }
        g.setColor(oldColor);
    }

    public static void drawWinUpperRight(Graphics2D g, int which, Color fill, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        g.setColor(fill);
        if (which == WINDOW_GRAPHIC) {
            g.fillRect(x, y, fmWidth, fmHeight);
            g.setColor(Color.white);
            g.drawLine(x, y, x + fmWidth, y);
            g.setColor(Color.black);
            g.drawLine(x + fmWidth, y, x + fmWidth, y + fmHeight);
        }
        if (which == WINDOW_NORMAL) {
            g.drawLine(x + fmWidth / 2, y + fmHeight / 2, x + fmWidth / 2, y + fmHeight);
            g.drawLine(x, y + fmHeight / 2, x + fmWidth / 2, y + fmHeight / 2);
            g.setColor(fill.darker());
            int w = 0;
            while (w < 3) {
                g.fillRect((x + fmWidth / 2) + (3 + w), y + ++w + fmHeight / 2, 1, (fmHeight / 2));
            }
        }
        g.setColor(oldColor);
    }

    public static void drawWinLeft(Graphics2D g, int which, Color fill, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        g.setColor(fill);
        if (which == WINDOW_GRAPHIC) {
            g.fillRect(x, y, fmWidth, fmHeight);
            g.setColor(Color.white);
            g.drawLine(x, y, x, y + fmHeight);
            g.setColor(Color.black);
            g.drawLine(x + fmWidth - 1, y, x + fmWidth - 1, y + fmHeight);
            g.setColor(Color.white);
            g.drawLine(x + fmWidth, y, x + fmWidth, y + fmHeight);
        }
        if (which == WINDOW_NORMAL) {
            g.drawLine(x + fmWidth / 2, y, x + fmWidth / 2, y + fmHeight);
        }
        g.setColor(oldColor);
    }

    public static void drawWinRight(Graphics2D g, int which, Color fill, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        g.setColor(fill);
        if (which == WINDOW_GRAPHIC) {
            g.fillRect(x, y, fmWidth, fmHeight);
            g.setColor(Color.black);
            g.drawLine(x + fmWidth, y, x + fmWidth, y + fmHeight);
            g.setColor(Color.white);
            g.drawLine(x, y, x, y + fmHeight);
            g.setColor(Color.black);
            g.drawLine(x + 1, y, x + 1, y + fmHeight);
        }
        if (which == WINDOW_NORMAL) {
            g.drawLine(x + fmWidth / 2, y, x + fmWidth / 2, y + fmHeight);
            g.setColor(fill.darker());
            g.fillRect((x + fmWidth / 2) + 3, y, 3, fmHeight);
        }
        g.setColor(oldColor);
    }

    public static void drawWinLowerLeft(Graphics2D g, int which, Color fill, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        g.setColor(fill);
        if (which == WINDOW_GRAPHIC) {
            g.fillRect(x, y, fmWidth, fmHeight);
            g.setColor(Color.black);
            g.drawLine(x, y + fmHeight - 1, x + fmWidth, y + fmHeight - 1);
            g.setColor(Color.white);
            g.drawLine(x, y, x, y + fmHeight - 1);
        }
        if (which == WINDOW_NORMAL) {
            g.drawLine(x + fmWidth / 2, y + fmHeight / 2, x + fmWidth / 2, y);
            g.drawLine(x + fmWidth / 2, y + fmHeight / 2, x + fmWidth, y + fmHeight / 2);
            g.setColor(fill.darker());
            int w = 0;
            while (w < 3) {
                g.fillRect((x + fmWidth / 2) + ++w, y + fmHeight / 2 + (2 + w), fmWidth / 2, 1);
            }
        }
        g.setColor(oldColor);
    }

    public static void drawWinBottom(Graphics2D g, int which, Color fill, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        g.setColor(fill);
        if (which == WINDOW_GRAPHIC) {
            g.fillRect(x, y, fmWidth, fmHeight);
            g.setColor(Color.black);
            g.drawLine(x, y + fmHeight - 1, x + fmWidth, y + fmHeight - 1);
            g.setColor(Color.white);
            g.drawLine(x, y, x + fmWidth, y);
            g.setColor(Color.black);
            g.drawLine(x, y + 1, x + fmWidth, y + 1);
        }
        if (which == WINDOW_NORMAL) {
            g.drawLine(x, y + fmHeight / 2, x + fmWidth, y + fmHeight / 2);
            g.setColor(fill.darker());
            g.fillRect(x, (y + fmHeight / 2) + 3, fmWidth, 3);
        }
        g.setColor(oldColor);
    }

    public static void drawWinLowerRight(Graphics2D g, int which, Color fill, int x, int y, int fmWidth, int fmHeight) {
        Color oldColor = g.getColor();
        g.setColor(fill);
        if (which == WINDOW_GRAPHIC) {
            g.fillRect(x, y, fmWidth, fmHeight);
            g.setColor(Color.black);
            g.drawLine(x, y + fmHeight - 1, x + fmWidth, y + fmHeight - 1);
            g.drawLine(x + fmWidth, y, x + fmWidth, y + fmHeight - 1);
        }
        if (which == WINDOW_NORMAL) {
            g.drawLine(x + fmWidth / 2, y, x + fmWidth / 2, y + fmHeight / 2);
            g.drawLine(x + fmWidth / 2, y + fmHeight / 2, x, y + fmHeight / 2);
            g.setColor(fill.darker());
            g.fillRect((x + fmWidth / 2) + 3, y, 3, (fmHeight / 2) + 3);
            g.fillRect(x, (y + fmHeight / 2) + 3, (fmWidth / 2) + 6, 3);
        }
        g.setColor(oldColor);
    }

    public static void drawEtchedRect(Graphics g, int x, int y, int w, int h, Color shadow, Color darkShadow, Color highlight, Color lightHighlight) {
        Color oldColor = g.getColor();
        g.setColor(shadow);
        g.drawLine(0, 0, w - 1, 0);
        g.drawLine(0, 1, 0, h - 2);
        g.setColor(darkShadow);
        g.drawLine(1, 1, w - 3, 1);
        g.drawLine(1, 2, 1, h - 3);
        g.setColor(lightHighlight);
        g.drawLine(w - 1, 0, w - 1, h - 1);
        g.drawLine(0, h - 1, w - 1, h - 1);
        g.setColor(highlight);
        g.drawLine(w - 2, 1, w - 2, h - 3);
        g.drawLine(1, h - 2, w - 2, h - 2);
        g.setColor(oldColor);
    }

    /**
     * Returns the amount of space taken up by a border drawn by
     * <code>drawEtchedRect()</code>
     *
     * @return  the inset of an etched rect
     */
    public static Insets getEtchedInsets() {
        return ETCHED_INSETS;
    }

    public static void drawGroove(Graphics g, int x, int y, int w, int h, Color shadow, Color highlight) {
        Color oldColor = g.getColor();
        g.setColor(shadow);
        g.drawRect(0, 0, w - 2, h - 2);
        g.setColor(highlight);
        g.drawLine(1, h - 3, 1, 1);
        g.drawLine(1, 1, w - 3, 1);
        g.drawLine(0, h - 1, w - 1, h - 1);
        g.drawLine(w - 1, h - 1, w - 1, 0);
        g.setColor(oldColor);
    }

    /**
     * Returns the amount of space taken up by a border drawn by
     * <code>drawGroove()</code>
     *
     * @return  the inset of a groove border
     */
    public static Insets getGrooveInsets() {
        return GROOVE_INSETS;
    }

    public static void drawBezel(Graphics g, int x, int y, int w, int h, boolean isPressed, boolean isDefault, Color shadow, Color darkShadow, Color highlight, Color lightHighlight) {
        Color oldColor = g.getColor();
        if (isPressed) {
            if (isDefault) {
                g.setColor(darkShadow);
                g.drawRect(0, 0, w - 1, h - 1);
            }
            g.setColor(shadow);
            g.drawRect(1, 1, w - 3, h - 3);
        } else {
            if (isDefault) {
                g.setColor(darkShadow);
                g.drawRect(0, 0, w - 1, h - 1);
                g.setColor(lightHighlight);
                g.drawLine(1, 1, 1, h - 3);
                g.drawLine(2, 1, w - 3, 1);
                g.setColor(highlight);
                g.drawLine(2, 2, 2, h - 4);
                g.drawLine(3, 2, w - 4, 2);
                g.setColor(shadow);
                g.drawLine(2, h - 3, w - 3, h - 3);
                g.drawLine(w - 3, 2, w - 3, h - 4);
                g.setColor(darkShadow);
                g.drawLine(1, h - 2, w - 2, h - 2);
                g.drawLine(w - 2, h - 2, w - 2, 1);
            } else {
                g.setColor(lightHighlight);
                g.drawLine(0, 0, 0, h - 1);
                g.drawLine(1, 0, w - 2, 0);
                g.setColor(highlight);
                g.drawLine(1, 1, 1, h - 3);
                g.drawLine(2, 1, w - 3, 1);
                g.setColor(shadow);
                g.drawLine(1, h - 2, w - 2, h - 2);
                g.drawLine(w - 2, 1, w - 2, h - 3);
                g.setColor(darkShadow);
                g.drawLine(0, h - 1, w - 1, h - 1);
                g.drawLine(w - 1, h - 1, w - 1, 0);
            }
            g.setColor(oldColor);
        }
    }

    public static void drawLoweredBezel(Graphics g, int x, int y, int w, int h, Color shadow, Color darkShadow, Color highlight, Color lightHighlight) {
        g.setColor(darkShadow);
        g.drawLine(0, 0, 0, h - 1);
        g.drawLine(1, 0, w - 2, 0);
        g.setColor(shadow);
        g.drawLine(1, 1, 1, h - 2);
        g.drawLine(1, 1, w - 3, 1);
        g.setColor(lightHighlight);
        g.drawLine(0, h - 1, w - 1, h - 1);
        g.drawLine(w - 1, h - 1, w - 1, 0);
        g.setColor(highlight);
        g.drawLine(1, h - 2, w - 2, h - 2);
        g.drawLine(w - 2, h - 2, w - 2, 1);
    }

    /** Draw a string with the graphics g at location (x,y) just like g.drawString() would.
   *  The first occurence of underlineChar in text will be underlined. The matching is
   *  not case sensitive.
   */
    public static void drawString(Graphics g, String text, int underlinedChar, int x, int y) {
        char lc, uc;
        int index = -1, lci, uci;
        if (underlinedChar != '\0') {
            uc = Character.toUpperCase((char) underlinedChar);
            lc = Character.toLowerCase((char) underlinedChar);
            uci = text.indexOf(uc);
            lci = text.indexOf(lc);
            if (uci == -1) index = lci; else if (lci == -1) index = uci; else index = (lci < uci) ? lci : uci;
        }
        g.drawString(text, x, y);
        if (index != -1) {
            FontMetrics fm = g.getFontMetrics();
            int underlineRectX = x + fm.stringWidth(text.substring(0, index));
            int underlineRectY = y;
            int underlineRectWidth = fm.charWidth(text.charAt(index));
            int underlineRectHeight = 1;
            g.fillRect(underlineRectX, underlineRectY + fm.getDescent() - 1, underlineRectWidth, underlineRectHeight);
        }
    }

    public static void drawDashedRect(Graphics g, int x, int y, int width, int height) {
        int vx, vy;
        for (vx = x; vx < (x + width); vx += 2) {
            g.drawLine(vx, y, vx, y);
            g.drawLine(vx, y + height - 1, vx, y + height - 1);
        }
        for (vy = y; vy < (y + height); vy += 2) {
            g.drawLine(x, vy, x, vy);
            g.drawLine(x + width - 1, vy, x + width - 1, vy);
        }
    }

    public static Font getDerivedFont(Font font, int width, int height, int numRows, int numCols, float scaleHeight, float scaleWidth, float pointSize) {
        int w = width / numCols;
        int h = height / (numRows + 2);
        int sw = 0;
        int sh = 0;
        float scw = 1.0f;
        float sch = 1.0f;
        Font k = null;
        LineMetrics l;
        FontRenderContext f = null;
        AffineTransform at = new AffineTransform();
        if (numCols == 132) {
            at.scale(scaleWidth, scaleHeight);
        } else {
            at.setToScale(1.0f, 1.0f);
            pointSize = 0;
        }
        float j = 1;
        if (pointSize == 0) {
            for (; j < 36; j++) {
                k = font.deriveFont(j);
                k = k.deriveFont(at);
                f = new FontRenderContext(k.getTransform(), true, true);
                l = k.getLineMetrics("Wy", f);
                sw = (int) k.getStringBounds("W", f).getWidth() + 2;
                sh = (int) (k.getStringBounds("y", f).getHeight() + l.getDescent() + l.getLeading());
                if (w < sw || h < sh) {
                    break;
                }
            }
        } else {
            k = font.deriveFont(pointSize);
        }
        if (j > 1) k = font.deriveFont(--j);
        k = k.deriveFont(at);
        return k;
    }
}
