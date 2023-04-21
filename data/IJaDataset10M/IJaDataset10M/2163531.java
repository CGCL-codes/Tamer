package com.degani;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JToggleButton;

/**
 * A "color button" adapted from the Nimbus look and feel source code
 * @author ismaildegani
 *
 */
public class ColorButton extends JToggleButton {

    boolean mouseOver = false;

    private static final Color[] NORMAL_BG = new Color[] { getWebColor("FBFBFC"), getMidWebColor("FBFBFC", "F1F2F4"), getWebColor("F1F2F4"), getMidWebColor("FBFBFC", "D6D9DF"), getWebColor("D6D9DF"), getWebColor("D6D9DF"), getMidWebColor("D6D9DF", "F4F7FD"), getWebColor("F4F7FD"), getWebColor("FFFFFF") };

    private static final Color[] NORMAL_FG = new Color[] { getWebColor("95989E"), getWebColor("95989E"), getWebColor("55585E"), getWebColor("55585E") };

    private static final Color[] PRELIGHT_BG = new Color[] { getWebColor("FDFDFE"), getMidWebColor("FDFDFE", "F7F8FA"), getWebColor("F7F8FA"), getMidWebColor("F7F8FA", "E9ECF2"), getWebColor("E9ECF2"), getWebColor("E9ECF2"), getMidWebColor("E9ECF2", "FFFFFF"), getWebColor("FFFFFF"), getWebColor("FFFFFF") };

    private static final Color[] PRELIGHT_FG = new Color[] { getWebColor("7A7E86"), getWebColor("7A7E86"), getWebColor("2A2E36"), getWebColor("2A2E36") };

    private static final Color[] ACTIVE_BG = new Color[] { getWebColor("CDD1D8"), getMidWebColor("CDD1D8", "C2C7CF"), getWebColor("C2C7CF"), getMidWebColor("C2C7CF", "A4ABB8"), getWebColor("A4ABB8"), getWebColor("A4ABB8"), getMidWebColor("A4ABB8", "CCD3E0"), getWebColor("CCD3E0"), getWebColor("E7EDFB") };

    private static final Color[] ACTIVE_FG = new Color[] { getWebColor("000007"), getWebColor("000007"), getWebColor("60646C"), getWebColor("60646C") };

    private Color paintColor;

    private Color selectedPaintColor;

    public ColorButton() {
        addMouseListener(getHoverListener());
    }

    private MouseListener getHoverListener() {
        return new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOver = false;
                repaint();
            }
        };
    }

    private static final double FACTOR = 0.7;

    @Override
    public void setBackground(Color bg) {
        paintColor = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 70);
        selectedPaintColor = new Color((int) Math.max(bg.getRed() * FACTOR, 0), (int) Math.max(bg.getGreen() * FACTOR, 0), (int) Math.max(bg.getBlue() * FACTOR, 0), 70);
        super.setBackground(bg);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] fg, bg;
        if (isSelected()) {
            fg = ACTIVE_FG;
            bg = ACTIVE_BG;
        } else if (mouseOver) {
            fg = PRELIGHT_FG;
            bg = PRELIGHT_BG;
        } else {
            fg = NORMAL_FG;
            bg = NORMAL_BG;
        }
        int w = getWidth() - 2;
        int h = getHeight() - 2;
        int x = 0;
        int y = 0;
        int r = 2;
        g2.setPaint(new LinearGradientPaint(x, y, x, y + h, new float[] { 0, 0.024f, 0.06f, 0.276f, 0.6f, 0.7f, 0.856f, 0.9f, 1 }, bg));
        g2.fillRoundRect(x, y, w, h, h / r, h / r);
        if (isSelected()) {
            g2.setPaint(selectedPaintColor);
        } else {
            g2.setPaint(paintColor);
        }
        g2.fillRoundRect(x, y, w, h, h / r, h / r);
        g2.setPaint(new LinearGradientPaint(x, y, x, y + h, new float[] { 0, 0.05f, 0.95f, 1 }, fg));
        g2.drawRoundRect(x, y, w, h, h / r, h / r);
    }

    public static Color getWebColor(String c) {
        if (c.startsWith("#")) c = c.substring(1);
        return new Color(Integer.parseInt(c.substring(0, 2), 16), Integer.parseInt(c.substring(2, 4), 16), Integer.parseInt(c.substring(4, 6), 16));
    }

    /**
     * Get a Color that is 50% inbetween the two web colors given. The Web colors are of the form "FF00AB" or "#FF00AB".
     *
     * @param c1 The first color string
     * @param c2 The second color string
     * @return The Color middle color
     */
    public static Color getMidWebColor(String c1, String c2) {
        if (c1.startsWith("#")) c1 = c1.substring(1);
        if (c2.startsWith("#")) c2 = c2.substring(1);
        int rTop = Integer.parseInt(c1.substring(0, 2), 16);
        int gTop = Integer.parseInt(c1.substring(2, 4), 16);
        int bTop = Integer.parseInt(c1.substring(4, 6), 16);
        int rBot = Integer.parseInt(c2.substring(0, 2), 16);
        int gBot = Integer.parseInt(c2.substring(2, 4), 16);
        int bBot = Integer.parseInt(c2.substring(4, 6), 16);
        int rMid = rTop + ((rBot - rTop) / 2);
        int gMid = gTop + ((gBot - gTop) / 2);
        int bMid = bTop + ((bBot - bTop) / 2);
        return new Color(rMid, gMid, bMid);
    }
}
