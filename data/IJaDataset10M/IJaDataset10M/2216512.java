package org.frontburner.gui;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import org.log4j.Category;
import org.log4j.Priority;

/**
 *
 *
 * @author Marc Hedlund &lt;marc@precipice.org&gt;
 * @version $Revision: 1.4 $
 */
public class TabIcon extends JPanel implements Icon {

    private static Category LOG = Category.getInstance(TabIcon.class.getName());

    private int height = 0;

    private int width = 0;

    private String label = null;

    private Font font = new Font("Sanserif", Font.PLAIN, 12);

    private Color clear = new Color(1f, 1f, 1f, 0);

    public TabIcon(String label) {
        this.label = label;
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
        calculateBounds(frc);
    }

    public void calculateBounds(FontRenderContext frc) {
        LineMetrics lm = font.getLineMetrics(label, frc);
        Rectangle2D bounds = font.getStringBounds(label, frc);
        width = ((int) lm.getHeight());
        height = ((int) bounds.getWidth()) + 12;
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Image image = c.createImage(width, height - 2);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        try {
            JTabbedPane tabs = (JTabbedPane) c;
            int idx = tabs.indexOfTab(this);
            if (tabs.getSelectedIndex() == idx) {
                g2.setColor(new Color(204, 204, 204));
            } else {
                g2.setColor(new Color(153, 153, 153));
            }
            g2.fillRect(0, 0, width, height);
        } catch (ClassCastException e) {
        }
        AffineTransform rotator = new AffineTransform();
        rotator.translate(width - 5, (height - 4));
        rotator.rotate(-Math.PI / 2);
        g2.setTransform(rotator);
        g2.setFont(font);
        g2.setColor(c.getForeground());
        g2.drawString(label, 0, 0);
        g.drawImage(image, x, y, clear, null);
    }
}
