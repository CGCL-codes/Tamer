package com.elibera.ccs.img;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import com.elibera.ccs.util.HelperStd;

/**
 * @author meisi
 * Resizen von Bildern
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ImageResizer {

    public static BufferedImage resizeImg(BufferedImage img, int w, int h) {
        int imgW = img.getWidth();
        int imgH = img.getHeight();
        if (w <= 0 && w <= 0) {
            if (w <= 0) w = imgW;
            if (h <= 0) h = imgH;
        } else if (w <= 0) {
            w = (h * imgW) / imgH;
        } else if (h <= 0) {
            h = (w * imgH) / imgW;
        }
        BufferedImage newBuff = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = newBuff.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(img, 0, 0, w, h, null);
        return newBuff;
    }

    public static BufferedImage getTextBox(int chars, String text) {
        int w = chars * 9 + 4;
        int h = 13;
        if (text != null) h++;
        BufferedImage newBuff = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic2D;
        if (newBuff == null) return null;
        try {
            graphic2D = newBuff.createGraphics();
            graphic2D.setColor(new java.awt.Color(255, 255, 255));
            graphic2D.fillRect(0, 0, w, h);
            graphic2D.setColor(new java.awt.Color(28, 81, 128));
            graphic2D.drawRect(1, 1, w - 2, h - 2);
            graphic2D.setColor(new java.awt.Color(0, 0, 0));
            if (text != null) graphic2D.drawString(text, 3, 12);
        } catch (Exception ee) {
        }
        return newBuff;
    }

    /**
	 * zeichnet den Grid auf das Bild
	 * inklusive der Markierungen
	 */
    public static BufferedImage drawGrid(BufferedImage buf, int gridX, int gridY, String[] gridSolutions) {
        if (buf == null) return null;
        try {
            int w = buf.getWidth();
            int h = buf.getHeight();
            BufferedImage newBuff = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic2D = newBuff.createGraphics();
            graphic2D.drawImage(buf, null, 0, 0);
            graphic2D.setColor(new java.awt.Color(0, 0, 0));
            graphic2D.drawRect(1, 1, w - 2, h - 2);
            int ws = w / gridX;
            for (int i = 0; i < gridX; i++) {
                graphic2D.drawLine(ws * i, 0, ws * i, h);
            }
            int hs = h / gridY;
            for (int i = 0; i < gridY; i++) {
                graphic2D.drawLine(0, hs * i, w, hs * i);
            }
            graphic2D.setColor(new java.awt.Color(255, 0, 0));
            for (int i = 0; i < gridSolutions.length; i++) {
                String[] grids = gridSolutions[i].split(",");
                if (grids.length < 2) continue;
                int gx = HelperStd.parseInt(grids[0], 1);
                int gy = HelperStd.parseInt(grids[1], 1);
                graphic2D.drawLine(ws * gx, hs * gy, ws * (gx + 1), hs * (gy + 1));
                graphic2D.drawLine(ws * gx, hs * (gy + 1), ws * (gx + 1), hs * gy);
            }
            return newBuff;
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return null;
    }

    /**
	 * zeichnet den Grid auf das Bild
	 * inklusive der Markierungen
	 */
    public static BufferedImage drawChars(StringBuffer chars, Font font) {
        if (chars == null || chars.length() <= 0) return getEmptyImage(10, 10);
        try {
            int w = chars.length() * 8;
            int h = (int) font.getSize2D();
            BufferedImage t = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic2D = t.createGraphics();
            if (font != null) graphic2D.setFont(font);
            w = graphic2D.getFontMetrics().stringWidth(chars.toString());
            BufferedImage newBuff = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            graphic2D = newBuff.createGraphics();
            if (font != null) graphic2D.setFont(font);
            graphic2D.setColor(new java.awt.Color(255, 255, 255));
            graphic2D.fillRect(0, 0, w, h);
            graphic2D.setColor(new java.awt.Color(0, 0, 0));
            graphic2D.drawString(chars.toString(), 0, h - 1);
            return newBuff;
        } catch (Exception ee) {
        }
        return null;
    }

    /**
	 * gibt ein leeres, weißes Bild zurück
	 * @param w
	 * @param h
	 * @return
	 */
    public static BufferedImage getEmptyImage(int w, int h) {
        try {
            BufferedImage newBuff = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic2D = newBuff.createGraphics();
            graphic2D.setColor(new java.awt.Color(255, 255, 255));
            graphic2D.fillRect(1, 1, w - 2, h - 2);
            return newBuff;
        } catch (Exception ee) {
        }
        return null;
    }
}
