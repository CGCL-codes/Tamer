package smooth.metal;

import smooth.util.SmoothUtilities;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import java.awt.*;
import javax.swing.*;

public class SmoothTextFieldUI extends MetalTextFieldUI {

    public static ComponentUI createUI(JComponent jcomponent) {
        return new SmoothTextFieldUI();
    }

    protected void paintSafely(Graphics g) {
        SmoothUtilities.configureGraphics(g);
        super.paintSafely(g);
    }
}
