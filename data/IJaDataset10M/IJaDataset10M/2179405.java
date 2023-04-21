package net.sf.fir4j.view;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JComponent;

public class GraphicTextHelper {

    private JComponent parent;

    public GraphicTextHelper(JComponent parent) {
        this.parent = parent;
    }

    public int drawCenterLine(Font font, Graphics g, String s, int y) {
        final FontMetrics fm = g.getFontMetrics(font);
        final int SCREENWIDTH = parent.getWidth() - 20;
        int width = fm.stringWidth(s);
        if (width < SCREENWIDTH) {
            int xoff = (parent.getWidth() - width) / 2;
            g.drawString(s, xoff, y);
            return fm.getHeight();
        }
        String parts[] = s.split(" ");
        if (parts.length > 1) {
            for (int i = parts.length - 1; i >= 0; i--) {
                String s1 = parts[0];
                for (int p = 1; p < i; p++) s1 += " " + parts[p];
                width = fm.stringWidth(s1);
                if (width > SCREENWIDTH) continue;
                String s2 = parts[i];
                for (int p = i + 1; p < parts.length; p++) s2 += " " + parts[p];
                int adv1 = drawCenterLine(font, g, s1, y);
                int adv2 = drawCenterLine(font, g, s2, y + adv1);
                return adv1 + adv2;
            }
        }
        g.drawString(s, 0, y);
        return fm.getHeight();
    }
}
