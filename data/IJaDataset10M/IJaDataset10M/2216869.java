package com.jpatch.afw.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ImageUtils {

    public static Image createTextIcon(Font font, Color color, String text) {
        font = font.deriveFont(new AffineTransform(1.0, 0, 0, 0.8, 0, 0));
        BufferedImage image = new BufferedImage(100, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, font, frc);
        Rectangle2D bounds = textLayout.getBounds();
        float height = textLayout.getAscent() + textLayout.getDescent();
        image = new BufferedImage((int) (bounds.getWidth()) + 5, (int) height + 2, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setColor(color);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        textLayout.draw(g2, 1, textLayout.getAscent() + 1);
        return image;
    }

    public static Image createShadowIcon(Image source) {
        BufferedImage tmp = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        tmp.createGraphics().drawImage(source, 0, 0, null);
        int[] pixels = ((DataBufferInt) tmp.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = ((argb >> 24) & 0xff) / 8;
            int alpha = a;
            pixels[i] = (alpha << 24) | 0x000000;
        }
        BufferedImage image = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                g.drawImage(tmp, x, y, null);
            }
        }
        g.drawImage(source, 0, 0, null);
        return image;
    }

    public static Image createEtchedIcon(Image source) {
        BufferedImage tmp = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        tmp.createGraphics().drawImage(source, 0, 0, null);
        int[] pixels = ((DataBufferInt) tmp.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = ((argb >> 24) & 0xff) / 9;
            int alpha = a;
            pixels[i] = (alpha << 24) | 0xffffff;
        }
        BufferedImage image = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.drawImage(tmp, 0, 1, null);
        g.drawImage(source, 0, 0, null);
        return image;
    }

    public static Image createDisabledIcon(Image source) {
        BufferedImage image = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        image.createGraphics().drawImage(source, 0, 0, null);
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            int gray = (r + g + b) / 3;
            int alpha = a / 3;
            pixels[i] = (alpha << 24) | (gray << 16) | (gray << 8) | gray;
        }
        return image;
    }
}
