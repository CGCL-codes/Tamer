package smooth.metal;

import smooth.util.SmoothUtilities;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import java.awt.*;
import javax.swing.*;

public class SmoothTabbedPaneUI extends MetalTabbedPaneUI {

    public static ComponentUI createUI(JComponent jcomponent) {
        return new SmoothTabbedPaneUI();
    }

    public void paint(Graphics g, JComponent c) {
        SmoothUtilities.configureGraphics(g);
        super.paint(g, c);
    }
}
