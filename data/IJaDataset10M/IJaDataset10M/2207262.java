package com.ezware.dialog.task.design;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.UIManager;
import com.ezware.dialog.task.ICommandLinkPainter;
import com.ezware.dialog.task.IContentDesign;

public class DefaultCommandLinkPainter implements ICommandLinkPainter {

    @Override
    public void intialize(JComponent source) {
        if (source instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) source;
            button.setOpaque(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
        }
    }

    @Override
    public void paint(Graphics g, JComponent source) {
        if (!(source instanceof AbstractButton)) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AbstractButton button = (AbstractButton) source;
        ButtonModel model = button.getModel();
        if (button.isSelected()) {
            drawButton(button, g2, Color.LIGHT_GRAY.brighter(), Color.GRAY.brighter(), Color.LIGHT_GRAY, 5);
        }
        Color messageBackground = normalize(UIManager.getColor(IContentDesign.COLOR_MESSAGE_BACKGROUND));
        Color instructionForeground = normalize(UIManager.getColor(IContentDesign.COLOR_INSTRUCTION_FOREGROUND));
        if (model.isArmed()) {
            drawButton(button, g2, messageBackground, instructionForeground, instructionForeground, 3);
        } else if (model.isRollover()) {
            drawButton(button, g2, messageBackground, instructionForeground, instructionForeground, 6);
        }
        g2.dispose();
    }

    private Color normalize(Color color) {
        return color == null ? Color.BLACK : color;
    }

    private static final int ARC_SIZE = 5;

    private void drawButton(AbstractButton button, Graphics2D g, Color startColor, Color endColor, Color borderColor, int gradientHeightFactor) {
        GradientPaint paint = new GradientPaint(0, 0, startColor, 0, button.getHeight() * gradientHeightFactor, endColor);
        g.setPaint(paint);
        g.fillRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, ARC_SIZE, ARC_SIZE);
        g.setColor(borderColor);
        g.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, ARC_SIZE, ARC_SIZE);
    }
}
