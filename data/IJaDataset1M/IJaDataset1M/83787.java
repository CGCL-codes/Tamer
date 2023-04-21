package org.dlib.gui.plaf;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;
import org.dlib.gui.GuiUtil;

public class WoodyMenuBarUI extends BasicMenuBarUI {

    public static ComponentUI createUI(JComponent c) {
        return new WoodyMenuBarUI();
    }

    public void update(Graphics g, JComponent c) {
        GuiUtil.setTextAntiAliasing(g, WoodyLookAndFeel.textAntialias);
        super.update(g, c);
    }
}
