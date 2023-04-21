package jpatch.boundary.laf;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import java.awt.*;
import javax.swing.*;

public class SmoothDesktopIconUI extends MetalDesktopIconUI {

    public static ComponentUI createUI(JComponent jcomponent) {
        return new SmoothDesktopIconUI();
    }

    public void paint(Graphics g, JComponent c) {
        SmoothUtilities.configureGraphics(g);
        super.paint(g, c);
    }
}
