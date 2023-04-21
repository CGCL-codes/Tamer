package com.memoire.vainstall.xui;

import com.memoire.vainstall.xui.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class XuiButtonBorder extends AbstractBorder {

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        AbstractButton button = (AbstractButton) c;
        ButtonModel model = button.getModel();
        Color color = Color.magenta;
        if (model.isEnabled()) {
            if (model.isPressed() && model.isArmed()) color = Color.red; else if (model.isSelected()) color = Color.green; else color = c.getForeground();
        } else color = Color.gray;
        int r = h / 2;
        g.setColor(color);
        g.drawArc(x, y, 2 * r, h - 2, 90, 180);
        g.drawArc(x + w - 1 - 2 * r, y, 2 * r, h - 2, 270, 180);
        g.drawArc(x + 1, y, 2 * r, h - 1, 90, 180);
        g.drawArc(x + w - 1 - 2 * r - 1, y, 2 * r, h - 2, 270, 180);
        g.drawArc(x, y + 1, 2 * r, h - 2, 90, 180);
        g.drawArc(x + w - 1 - 2 * r - 1, y + 1, 2 * r, h - 2, 270, 180);
        g.drawLine(x + r, y, x + w - 1 - r, y);
        g.drawLine(x + r, y + h - 1, x + w - 1 - r, y + h - 1);
        g.drawLine(x + r, y + 1, x + w - 1 - r, y + 1);
        g.drawLine(x + r, y + h - 2, x + w - 1 - r, y + h - 2);
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5);
    }

    public boolean isBorderOpaque() {
        return false;
    }
}
