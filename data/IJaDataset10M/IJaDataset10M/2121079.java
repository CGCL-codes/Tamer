package edu.stanford.genetics.treeview;

import java.awt.*;
import javax.swing.Icon;

/**
 *  A little icon with a changeable color.
 *
 * @author     Alok Saldanha <alok@genome.stanford.edu>
 * @version    @version $Revision: 1.5 $ $Date: 2004-12-21 03:28:14 $
 */
public class ColorIcon implements Icon {

    private int width, height;

    private Color color;

    /**
	 * @param  x  width of icon
	 * @param  y  height of icon
	 * @param  c  Initial color of icon.
	 */
    public ColorIcon(int x, int y, Color c) {
        width = x;
        height = y;
        color = c;
    }

    /**
	 *  Sets the color, but doesn't redraw or anything.
	 *
	 * @param  c  The new color
	 */
    public void setColor(Color c) {
        color = c;
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color old = g.getColor();
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(Color.black);
        g.drawRect(x, y, width, height);
        g.setColor(old);
    }
}
