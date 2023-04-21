package com.chatcliente.lookandfeel.jtattoo;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;

/**
 * @author Michael Hagen
 */
public class BaseTextAreaUI extends BasicTextAreaUI {

    public static ComponentUI createUI(JComponent c) {
        return new BaseTextAreaUI();
    }

    public void installDefaults() {
        super.installDefaults();
        updateBackground();
    }

    protected void paintSafely(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        Object savedRenderingHint = null;
        if (AbstractLookAndFeel.getTheme().isTextAntiAliasingOn()) {
            savedRenderingHint = g2D.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
            g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, AbstractLookAndFeel.getTheme().getTextAntiAliasingHint());
        }
        super.paintSafely(g);
        if (AbstractLookAndFeel.getTheme().isTextAntiAliasingOn()) {
            g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, savedRenderingHint);
        }
    }

    protected void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("editable") || evt.getPropertyName().equals("enabled")) {
            updateBackground();
        }
        super.propertyChange(evt);
    }

    private void updateBackground() {
        JTextComponent c = getComponent();
        if (c.getBackground() instanceof UIResource) {
            if (!c.isEnabled() || !c.isEditable()) {
                c.setBackground(AbstractLookAndFeel.getDisabledBackgroundColor());
            } else {
                c.setBackground(AbstractLookAndFeel.getInputBackgroundColor());
            }
        }
    }
}
