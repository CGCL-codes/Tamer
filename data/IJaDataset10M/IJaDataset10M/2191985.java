package org.oors.ui.common;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BottomEtchedBorder extends EmptyBorder {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4879294079941432777L;

    public BottomEtchedBorder() {
        super(2, 2, 2, 2);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(c.getBackground().darker());
        g.drawLine(x, y + height - 2, x + width, y + height - 2);
        g.setColor(c.getBackground().brighter());
        g.drawLine(x, y + height - 1, x + width, y + height - 1);
    }
}
