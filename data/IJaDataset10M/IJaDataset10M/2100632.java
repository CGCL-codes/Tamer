package org.armedbear.j;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;

public final class ButtonUI extends MetalButtonUI {

    private static final ButtonUI buttonUI = new ButtonUI();

    public static ComponentUI createUI(JComponent c) {
        return buttonUI;
    }

    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
        Display.setRenderingHints(g);
        super.paintText(g, (JComponent) b, textRect, text);
    }
}
