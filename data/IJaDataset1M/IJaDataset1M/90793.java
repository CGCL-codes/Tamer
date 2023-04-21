package phex.gui.common;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import javax.swing.JPanel;

/**
 *
 */
public class GradientPanel extends JPanel {

    protected Color fromColor;

    protected Color toColor;

    /**
     * 
     */
    public GradientPanel(Color fromColor, Color toColor) {
        super();
        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    private static Rectangle viewRect = new Rectangle();

    private static Rectangle textRect = new Rectangle();

    private static Rectangle iconRect = new Rectangle();

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        Insets i = getInsets();
        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = getWidth() - (i.right + viewRect.x);
        viewRect.height = getHeight() - (i.bottom + viewRect.y);
        Graphics2D g2 = (Graphics2D) g;
        Paint gradient = new GradientPaint(0, 0, fromColor, viewRect.width, viewRect.height, toColor);
        g2.setPaint(gradient);
        g2.fillRect(viewRect.x, viewRect.y, viewRect.width, viewRect.height);
    }
}
