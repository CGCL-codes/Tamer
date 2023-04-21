package nox.ui.common;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA. User: Jonathan Simon Date: Oct 5, 2004 Time:
 * 6:03:57 PM To change this template use File | Settings | File Templates.
 */
public class AngledLinesWindowsCornerIcon implements Icon {

    private static final Color WHITE_LINE_COLOR = new Color(255, 255, 255);

    private static final Color GRAY_LINE_COLOR = new Color(172, 168, 153);

    private static final int WIDTH = 13;

    private static final int HEIGHT = 13;

    public int getIconHeight() {
        return WIDTH;
    }

    public int getIconWidth() {
        return HEIGHT;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(WHITE_LINE_COLOR);
        g.drawLine(0, 12, 12, 0);
        g.drawLine(5, 12, 12, 5);
        g.drawLine(10, 12, 12, 10);
        g.setColor(GRAY_LINE_COLOR);
        g.drawLine(1, 12, 12, 1);
        g.drawLine(2, 12, 12, 2);
        g.drawLine(3, 12, 12, 3);
        g.drawLine(6, 12, 12, 6);
        g.drawLine(7, 12, 12, 7);
        g.drawLine(8, 12, 12, 8);
        g.drawLine(11, 12, 12, 11);
        g.drawLine(12, 12, 12, 12);
    }
}
