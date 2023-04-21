package org.apache.harmony.awt.theme;

import org.apache.harmony.awt.state.TextState;
import com.google.code.appengine.awt.BasicStroke;
import com.google.code.appengine.awt.Dimension;
import com.google.code.appengine.awt.FontMetrics;
import com.google.code.appengine.awt.Graphics;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.Stroke;
import com.google.code.appengine.awt.SystemColor;

/**
 * Common functionality for default visual style
 */
public class DefaultStyle {

    static final int ABS_MARGIN = 4;

    static final double REL_MARGIN = 1. / 3.;

    static final int CB_SIZE = 12;

    /**
     * Draw dotted rectangle that indicates the focused component
     *
     */
    public static void drawFocusRect(Graphics g, int x, int y, int w, int h) {
        if ((w <= 0) || (h <= 0)) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1, new float[] { 1.0f, 1.0f }, 0.0f));
        int x1 = x + w;
        int y1 = y + h;
        g.drawLine(x, y, x, y1);
        g.drawLine(x, y1, x1, y1);
        g.drawLine(x, y, x1, y);
        g.drawLine(x1, y, x1, y1);
        g2d.setStroke(oldStroke);
    }

    /**
     * Draw text for disabled standard component.
     * It's assumed that desired font is already selected in passed Graphics
     * @param g
     * @param label
     * @param baseX
     * @param baseY
     */
    public static void drawDisabledString(Graphics g, String label, int baseX, int baseY) {
        g.setColor(SystemColor.controlHighlight);
        g.drawString(label, baseX + 1, baseY + 1);
        g.setColor(SystemColor.controlShadow);
        g.drawString(label, baseX, baseY);
    }

    /**
     * Calculate the size of the text
     * @param s
     */
    public static Dimension getTextSize(TextState s) {
        Dimension textSize = new Dimension(0, 0);
        String text = s.getText();
        if (text != null) {
            FontMetrics metrics = s.getFontMetrics();
            textSize.width = metrics.stringWidth(text);
            textSize.height = metrics.getHeight();
        }
        return textSize;
    }
}
