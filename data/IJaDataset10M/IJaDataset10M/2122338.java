package de.ios.framework.gui;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Statische Funktionen zum Zeichnen von 3D-Frames f�r
 * Dialogelemente.
 */
public final class Frame3DTool {

    /** Richtungen f�r Pfeile */
    public static final int LEFT = 1;

    public static final int RIGHT = 2;

    public static final int UP = 3;

    public static final int DOWN = 4;

    /**
   * Zeichnen eines 3D-Pfeiles.
   * @param g      Graphics Instanz in die gezeichnet werden soll.
   * @param x0     Linker Rand.
   * @param y0     Oberer Rand.
   * @param w      Breite
   * @param h      H�he
   * @param inner  Farbe des Innenbereiches.
   * @param direction Richtung des Pfeils.
   */
    public static final void paint3DArrow(Graphics g, int x0, int y0, int w, int h, Color inner, int direction) {
        x0++;
        y0++;
        h -= 3;
        w -= 3;
        int x[] = { x0, x0, y0 };
        int y[] = { y0, y0, y0 };
        int c[] = { 1, 1, 1 };
        int h2, w2;
        int i, iDst;
        Color lC, sC;
        lC = getColorOffset(inner, lightOffset);
        sC = getColorOffset(inner, shadowOffset);
        h2 = h / 2;
        w2 = w / 2;
        switch(direction) {
            case UP:
                y[0] += h;
                y[1] += h;
                x[1] += w;
                x[2] += w2;
                c[0] = 0;
                c[1] = 0;
                break;
            case DOWN:
                x[0] += w;
                x[2] += w2;
                y[2] += h;
                c[2] = 0;
                break;
            case LEFT:
                x[0] += w;
                y[0] += h;
                x[1] += w;
                y[2] += h2;
                c[0] = 0;
                c[2] = 0;
                break;
            case RIGHT:
                y[1] += h;
                x[2] += w;
                y[2] += h2;
                c[1] = 0;
                break;
        }
        g.setColor(inner);
        g.fillPolygon(x, y, 3);
        for (i = 0; i < 3; i++) {
            iDst = (i >= 2) ? 0 : (i + 1);
            g.setColor((c[i] > 0) ? lC : sC);
            g.drawLine(x[i], y[i], x[iDst], y[iDst]);
        }
    }

    /**
   * Zeichnen einer 3D-Box.
   * @param g      Graphics Instanz in die gezeichnet werden soll.
   * @param x0     Linker Rand.
   * @param y0     Oberer Rand.
   * @param w      Breite
   * @param h      H�he
   * @param inner  Farbe des Innenbereiches.
   * @param raised Flag ob der Frame normal oder gedr�ckt
   *               gezeichnet werden soll.
   */
    public static final void paint3DBox(Graphics g, int x0, int y0, int w, int h, Color inner, boolean raised) {
        g.setColor(inner);
        g.fillRect(x0 + 2, y0 + 2, w - 4, h - 4);
        paint3DFrame(g, x0, y0, w, h, !raised);
    }

    /**
   * Zeichnen einer 3D-Box.
   * @param g      Graphics Instanz in die gezeichnet werden soll.
   * @param x0     Linker Rand.
   * @param y0     Oberer Rand.
   * @param w      Breite
   * @param h      H�he
   * @param thickness Dicke des Randed.
   * @param inner  Farbe des Innenbereiches.
   * @param raised Flag ob der Frame normal oder gedr�ckt
   *               gezeichnet werden soll.
   */
    public static final void paint3DBox(Graphics g, int x0, int y0, int w, int h, int thickness, Color inner, boolean raised) {
        g.setColor(inner);
        g.fillRect(x0 + thickness, y0 + thickness, w - (thickness * 2), h - (thickness * 2));
        paint3DFrame(g, x0, y0, w, h, thickness, !raised);
    }

    public static int lightOffset = 30;

    public static int shadowOffset = -100;

    /**
   * Zeichnen eines 3D-Randes.
   * Die Basisfarbe ist die aktuelle �ber setColor ggesetzte Farbe.
   * Die Randfarben werden �ber getColorOffset berechnet.
   * @param g      Graphics Instanz in die gezeichnet werden soll.
   * @param x0     Linker Rand.
   * @param y0     Oberer Rand.
   * @param w      Breite
   * @param h      H�he
   * @param active Flag ob der Frame normal oder f�r eine 
   *               aktive Komponente gezeichnet werden soll.
   */
    public static final void paint3DFrame(Graphics g, int x0, int y0, int w, int h, boolean active) {
        paint3DFrame(g, x0, y0, w, h, 2, active);
    }

    public static final void paintThin3DFrame(Graphics g, int x0, int y0, int w, int h, boolean active) {
        paint3DFrame(g, x0, y0, w, h, 1, active);
    }

    public static final void paint3DFrame(Graphics g, int x0, int y0, int w, int h, int thickness, boolean active) {
        int t, x1, y1;
        x1 = x0 + w - 1;
        y1 = y0 + h - 1;
        Color bkC = g.getColor();
        for (t = 0; t < thickness; t++) {
            g.setColor(getColorOffset(bkC, (active) ? shadowOffset : lightOffset));
            g.drawLine(x0 + t, y0 + t, x1 - t, y0 + t);
            g.drawLine(x0 + t, y0 + 1 + t, x0 + t, y1 - t);
            g.setColor(getColorOffset(bkC, (active) ? lightOffset : shadowOffset));
            g.drawLine(x1 - t, y0 + 1 + t, x1 - t, y1 - 1 - t);
            g.drawLine(x0 + 1 + t, y1 - t, x1 - t, y1 - t);
        }
    }

    /**
   * Zeichnen eines Focus-3D-Randes.
   * Die Basisfarbe ist die aktuelle �ber setColor ggesetzte Farbe.
   * Die Randfarben werden �ber getColorOffset berechnet.
   * @param g      Graphics Instanz in die gezeichnet werden soll.
   * @param x0     Linker Rand.
   * @param y0     Oberer Rand.
   * @param w      Breite
   * @param h      H�he
   * @param active Flag ob der Frame normal oder f�r eine 
   *               aktive Komponente gezeichnet werden soll.
   */
    public static final void paintFocusFrame(Graphics g, int x0, int y0, int w, int h, boolean active) {
        Color bk = g.getColor();
        paint3DFrame(g, x0 + 1, y0 + 1, w - 2, h - 2, true);
        g.setColor(active ? Color.black : bk);
        g.drawRect(x0, y0, w - 1, h - 1);
        if (active) g.setColor(bk);
    }

    /**
   * Berechnet eine dunklere oder hellere Farbe aus einer Quellfarbe.
   * Die RGB-Werte der Farben k�nnen nicht kleiner als 0, oder
   * gr��er als 255 werden. Maxima sind Weiss (255,255,255)
   * und Schwarz (0,0,0). �ber- bzw. Unterschreitungen werden
   * abgefangen.
   * @param c Basisfarbe.
   * @param offset Helligkeitsoffset. <0 f�r dunkler, >0 f�r heller.
   */
    public static final Color getColorOffset(Color c, int offset) {
        int r, g, b;
        r = c.getRed() + offset;
        g = c.getGreen() + offset;
        b = c.getBlue() + offset;
        if (r > 255) r = 255;
        if (g > 255) g = 255;
        if (b > 255) b = 255;
        if (r < 0) r = 0;
        if (g < 0) g = 0;
        if (b < 0) b = 0;
        return new Color(r, g, b);
    }
}

;
