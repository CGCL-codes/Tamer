package com.googlecode.sarasvati.visual.util;

import java.awt.Font;
import java.awt.Graphics;

public class FontUtil {

    public static void setSizedFont(final Graphics g, final String text, final float maxFontSize, final int maxWidth) {
        if (g.getFont().getSize() != maxFontSize) {
            Font newFont = g.getFont().deriveFont(maxFontSize);
            g.setFont(newFont);
        }
        float currentSize = maxFontSize;
        while (g.getFontMetrics().getStringBounds(text, g).getWidth() > maxWidth) {
            g.setFont(g.getFont().deriveFont(--currentSize));
        }
    }

    public static String[] split(final String text) {
        String[] lines = text.split(" ");
        if (lines.length < 3) {
            return lines;
        }
        String fst = lines[0];
        String snd = lines[lines.length - 1];
        for (int i = 1; i < lines.length - 1; i++) {
            String cur = lines[i];
            String tmp1 = fst + " " + cur;
            String tmp2 = concat(lines, i, " ");
            if (tmp1.length() < tmp2.length()) {
                fst = tmp1;
            } else {
                snd = tmp2;
                break;
            }
        }
        return new String[] { fst, snd };
    }

    public static String concat(final String[] str, final int start, final String middle) {
        StringBuilder buf = new StringBuilder();
        for (int i = start; i < str.length; i++) {
            buf.append(str[i]);
            if (i != str.length - 1) {
                buf.append(middle);
            }
        }
        return buf.toString();
    }
}
