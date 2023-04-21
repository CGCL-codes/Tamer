package megamek.client.ui.AWT.widget;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A class for showing centered labels with desired value.
 */
public class PMValueLabel extends PMSimpleLabel {

    /**
     * Create the label.
     */
    PMValueLabel(FontMetrics fm, Color c) {
        super("", fm, c);
    }

    /**
     * Set/change the value displayed in the label.
     */
    void setValue(String v) {
        string = v;
        width = fm.stringWidth(string);
    }

    @Override
    public void setVisible(boolean v) {
        super.setVisible(v);
    }

    /**
     * Draw the label.
     */
    @Override
    public void drawInto(Graphics g) {
        if (!visible) return;
        Color temp = g.getColor();
        g.setColor(color);
        g.drawString(string, x - width / 2, y - fm.getMaxDescent());
        g.setColor(temp);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height, width, height + descent);
    }
}
