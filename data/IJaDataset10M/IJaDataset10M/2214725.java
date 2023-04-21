package net.borderwars.util;

import javax.swing.*;
import java.awt.*;

/**
 * @author ehubbard
 *         Date: May 16, 2006
 *         Time: 10:20:04 AM
 */
public class GUIUtils {

    public static void centerFrame(JFrame frame, int w, int h) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int sx = (d.width - w) >> 1;
        int sy = (d.height - h) >> 1;
        frame.setLocation(sx, sy);
        frame.setSize(w, h);
    }
}
