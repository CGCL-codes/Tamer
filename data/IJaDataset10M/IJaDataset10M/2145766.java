package net.ar.webonswing.toolkit.lookandfeel;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;

public class DummyPanelUI extends PanelUI {

    public static ComponentUI createUI(JComponent c) {
        return new DummyPanelUI();
    }

    public void update(Graphics g, JComponent c) {
    }
}
