package net.ar.webonswing.toolkit.lookandfeel;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class DummyViewportUI extends BasicViewportUI {

    public static ComponentUI createUI(JComponent c) {
        return new DummyViewportUI();
    }

    public void update(Graphics g, JComponent c) {
    }
}
