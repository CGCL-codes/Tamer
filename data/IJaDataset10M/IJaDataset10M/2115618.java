package org.taak.captcha;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import org.taak.config.Config;
import org.taak.error.*;

/**
 * Simple implementation of Captcha interface.
 */
public class SimpleCaptcha implements Captcha {

    private static String CHARS = "abcdefghjkmnpqrstuwxyABCDEFGHJKMNPRSTUWXY3456789";

    private static final int CAPTCHA_LENGTH = 6;

    private static Color[] BG_COLORS = { Color.YELLOW, Color.LIGHT_GRAY, Color.WHITE };

    private static Color[] COLORS = { Color.BLACK, Color.DARK_GRAY, Color.MAGENTA, Color.BLUE, Color.RED };

    private static Font[] FONTS = { new Font("Arial", 1, 36), new Font("Courier", 1, 36), new Font("Dialog", 1, 36), new Font("Monospaced", 1, 36), new Font("SansSerif", 1, 36), new Font("Serif", 1, 36) };

    private int width;

    private int height;

    private int margin;

    private double skew;

    public SimpleCaptcha(Config config) {
        width = config.getInt("captcha.width", 200);
        height = config.getInt("captcha.height", 50);
        margin = config.getInt("captcha.margin", 5);
        skew = config.getDouble("captcha.skew", 0.5);
    }

    /**
     * Create captcha text.
     */
    public String makeText() {
        int CHARS_LEN = CHARS.length();
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            b.append(CHARS.charAt((int) (CHARS_LEN * Math.random())));
        }
        return b.toString();
    }

    /**
     * Return a BufferedImage given text.
     */
    public BufferedImage makeImage(String text) {
        int nchars = text.length();
        int char_width = (width - 2 * margin) / nchars;
        Color bgColor = BG_COLORS[rand(BG_COLORS.length)];
        Color color = COLORS[rand(COLORS.length)];
        Font font = FONTS[rand(FONTS.length)];
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(color);
        g2d.setFont(font);
        for (int i = 0; i < nchars; i++) {
            double angle = 0;
            if (rand(2) == 0) {
                angle = skew * Math.random();
            } else {
                angle = -skew * Math.random();
            }
            g2d.rotate(angle, char_width * i + char_width / 2 + margin, height / 2);
            g2d.drawString(text.substring(i, i + 1), i * char_width + margin, height - 2 * margin - rand(10));
            g2d.rotate(-angle, char_width * i + char_width / 2, height / 2);
        }
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < 3; i++) {
            g2d.setXORMode(BG_COLORS[rand(BG_COLORS.length)]);
            int x1 = 0;
            int y1 = rand(height);
            int x2 = width;
            int y2 = rand(height);
            g2d.drawLine(x1, y1, x2, y2);
        }
        return image;
    }

    private int rand(int n) {
        return (int) (Math.random() * n);
    }
}
