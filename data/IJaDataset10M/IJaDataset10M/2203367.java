package smooth.windows;

import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;
import javax.swing.plaf.*;
import java.awt.*;
import javax.swing.*;
import smooth.util.SmoothUtilities;

public class SmoothTabbedPaneUI extends WindowsTabbedPaneUI {

    public static ComponentUI createUI(JComponent jcomponent) {
        return new SmoothTabbedPaneUI();
    }

    public void paint(Graphics g, JComponent c) {
        SmoothUtilities.configureGraphics(g);
        super.paint(g, c);
    }
}
