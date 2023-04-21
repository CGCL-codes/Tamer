package smooth.windows;

import smooth.util.SmoothUtilities;
import javax.swing.plaf.*;
import java.awt.*;
import javax.swing.*;
import com.sun.java.swing.plaf.windows.WindowsRadioButtonMenuItemUI;

public class SmoothRadioButtonMenuItemUI extends WindowsRadioButtonMenuItemUI {

    public static ComponentUI createUI(JComponent jcomponent) {
        return new SmoothRadioButtonMenuItemUI();
    }

    public void paint(Graphics g, JComponent c) {
        SmoothUtilities.configureGraphics(g);
        super.paint(g, c);
    }
}
