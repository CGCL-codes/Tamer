package net.ar.webonswing.toolkit.lookandfeel.delegate;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class DelegateTreeUI extends BasicTreeUI {

    public static ComponentUI createUI(JComponent c) {
        return new DelegateTreeUI();
    }

    public void update(Graphics g, JComponent c) {
    }

    public void paint(Graphics g, JComponent c) {
    }
}
