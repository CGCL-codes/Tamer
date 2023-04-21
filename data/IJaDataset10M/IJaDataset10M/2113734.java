package org.jfonia.view.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicCheckBoxUI;

/**
 * Credits to Ken (http://explodingpixels.wordpress.com/2009/02/01/creating-a-hud-style-check-box/)
 * @author Rik Bauwens
 */
public class HudCheckBoxUI extends BasicCheckBoxUI {

    public static final float FONT_SIZE = 11.0f;

    public static final Color FONT_COLOR = Color.WHITE;

    private static final Color TOP_COLOR = new Color(170, 170, 170, 50);

    private static final Color BOTTOM_COLOR = new Color(17, 17, 17, 50);

    private static final Color TOP_PRESSED_COLOR = new Color(249, 249, 249, 153);

    private static final Color BOTTOM_PRESSED_COLOR = new Color(176, 176, 176, 153);

    private static final Color LIGHT_SHADOW_COLOR = new Color(0, 0, 0, 145);

    private static final Color DARK_SHADOW_COLOR = new Color(0, 0, 0, 50);

    private static final Color BORDER_COLOR = new Color(0xc5c8cf);

    private static final int BORDER_WIDTH = 1;

    private static final int TOP_AND_BOTTOM_MARGIN = 2;

    private static final int LEFT_AND_RIGHT_MARGIN = 16;

    private static final Color PRESSED_CHECK_MARK_COLOR = new Color(0, 0, 0, 225);

    @Override
    protected void installDefaults(final AbstractButton b) {
        super.installDefaults(b);
        b.setIconTextGap((int) (FONT_SIZE / 2));
        icon = new CheckIcon();
        b.setFont(getHudFont());
        b.setForeground(FONT_COLOR);
        b.setOpaque(false);
        int bottomMargin = TOP_AND_BOTTOM_MARGIN + getHudControlShadowSize();
        b.setBorder(BorderFactory.createEmptyBorder(TOP_AND_BOTTOM_MARGIN, LEFT_AND_RIGHT_MARGIN, bottomMargin, LEFT_AND_RIGHT_MARGIN));
    }

    private static class CheckIcon implements Icon {

        private final int CHECK_BOX_SIZE = 12;

        public void paintIcon(Component c, Graphics g, int x, int y) {
            AbstractButton button = (AbstractButton) c;
            Graphics2D graphics = (Graphics2D) g.create();
            graphics.translate(x, y);
            paintHudControlBackground(graphics, button, CHECK_BOX_SIZE, CHECK_BOX_SIZE, Roundedness.CHECK_BOX);
            drawCheckMarkIfNecessary(graphics, button.getModel());
            graphics.dispose();
        }

        /**
         * Draws the check mark if {@link javax.swing.ButtonModel#isSelected}
         * returns true.
         */
        private void drawCheckMarkIfNecessary(Graphics2D graphics, ButtonModel model) {
            if (model.isSelected()) {
                drawCheckMark(graphics, model);
            }
        }

        /**
         * Draws the check in the check box using the appropriate color based on
         * the {@link ButtonModel#isPressed} state. Note that part of the check
         * will be drawn outside it's bounds. Because this icon is actually being
         * drawn inside a larger component (a {@link javax.swing.JCheckBox}), this
         * shouldn't be an issue.
         */
        private void drawCheckMark(Graphics2D graphics, ButtonModel model) {
            int x1 = CHECK_BOX_SIZE / 4;
            int y1 = CHECK_BOX_SIZE / 3;
            int x2 = x1 + CHECK_BOX_SIZE / 6;
            int y2 = y1 + CHECK_BOX_SIZE / 4;
            int x3 = CHECK_BOX_SIZE - 2;
            int y3 = -1;
            Color color = model.isPressed() ? PRESSED_CHECK_MARK_COLOR : FONT_COLOR;
            graphics.setStroke(new BasicStroke(1.65f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
            graphics.setColor(color);
            graphics.drawLine(x1, y1, x2, y2);
            graphics.drawLine(x2, y2, x3, y3);
        }

        public int getIconWidth() {
            return CHECK_BOX_SIZE;
        }

        public int getIconHeight() {
            return CHECK_BOX_SIZE;
        }
    }

    /**
     * Paints a HUD style button background onto the given {@link Graphics2D}
     * context. The background will be painted from 0,0 to width/height.
     * @param graphics the graphics context to paint onto.
     * @param button the button being painted.
     * @param width the width of the area to paint.
     * @param height the height of the area to paint.
     * @param roundedness the roundedness to use when painting the background.
     */
    private static void paintHudControlBackground(Graphics2D graphics, AbstractButton button, int width, int height, Roundedness roundedness) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(LIGHT_SHADOW_COLOR);
        int arcDiameter = roundedness.getRoundedDiameter(height);
        graphics.drawRoundRect(0, 0, width - 1, height, arcDiameter, arcDiameter);
        graphics.setColor(DARK_SHADOW_COLOR);
        int smallerShadowArcDiameter = height - 1;
        graphics.drawRoundRect(0, 0, width - 1, height + 1, smallerShadowArcDiameter, smallerShadowArcDiameter);
        graphics.setPaint(createButtonPaint(button, BORDER_WIDTH));
        graphics.fillRoundRect(0, 1, width, height - 1, arcDiameter, arcDiameter);
        graphics.setColor(BORDER_COLOR);
        graphics.drawRoundRect(0, 0, width - 1, height - 1, arcDiameter, arcDiameter);
    }

    /**
     * Creates a HUD style gradient paint for the given button offset from the top
     * and bottom of the button by the given line border size.
     */
    private static Paint createButtonPaint(AbstractButton button, int lineBorderWidth) {
        boolean isPressed = button.getModel().isPressed();
        Color topColor = isPressed ? TOP_PRESSED_COLOR : TOP_COLOR;
        Color bottomColor = isPressed ? BOTTOM_PRESSED_COLOR : BOTTOM_COLOR;
        int bottomY = button.getHeight() - lineBorderWidth * 2;
        return new GradientPaint(0, lineBorderWidth, topColor, 0, bottomY, bottomColor);
    }

    /**
     * Gets the number of pixels that a HUD style widget's shadow takes up. HUD
     * button's have a shadow directly below them, that is, there is no top, left
     * or right component to the shadow.
     * @return the number of pixels that a HUD style widget's shadow takes up.
     */
    private static int getHudControlShadowSize() {
        return 2;
    }

    /**
     * Gets the font used by HUD style widgets.
     * @return the font used by HUD style widgets.
     */
    private static Font getHudFont() {
        return UIManager.getFont("Button.font").deriveFont(Font.BOLD, FONT_SIZE);
    }

    /**
     * An enumeration representing the roundness styles of HUD buttons. Using this
     * enumeration will make it easier to transition this code to support more
     * HUD controls, like check boxes and combo buttons.
     */
    public enum Roundedness {

        /**
         * A roundedness of 40%, equates to slightly rounded edge.
         */
        CHECK_BOX(.4);

        private final double fRoundedPercentage;

        private Roundedness(double roundedPercentage) {
            fRoundedPercentage = roundedPercentage;
        }

        private int getRoundedDiameter(int controlHeight) {
            int roundedDiameter = (int) (controlHeight * fRoundedPercentage);
            int makeItEven = roundedDiameter % 2;
            return roundedDiameter - makeItEven;
        }
    }
}
