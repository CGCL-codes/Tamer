package com.jgoodies.looks.plastic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import com.jgoodies.looks.common.ExtBasicArrowButtonHandler;

/**
 * The JGoodies PlasticXP Look&amp;Feel implementation of <code>SpinnerUI</code>.
 * Configures the default editor to adjust font baselines and component
 * bounds. Also, changes the border of the buttons and the size of the arrows.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.6 $
 */
public final class PlasticXPSpinnerUI extends PlasticSpinnerUI {

    public static ComponentUI createUI(JComponent b) {
        return new PlasticXPSpinnerUI();
    }

    /**
     * The mouse/action listeners that are added to the spinner's
     * arrow buttons.  These listeners are shared by all
     * spinner arrow buttons.
     *
     * @see #createNextButton
     * @see #createPreviousButton
     */
    private static final ExtBasicArrowButtonHandler NEXT_BUTTON_HANDLER = new ExtBasicArrowButtonHandler("increment", true);

    private static final ExtBasicArrowButtonHandler PREVIOUS_BUTTON_HANDLER = new ExtBasicArrowButtonHandler("decrement", false);

    /**
     * Create a component that will replace the spinner models value
     * with the object returned by <code>spinner.getPreviousValue</code>.
     * By default the <code>previousButton</code> is a JButton
     * who's <code>ActionListener</code> updates it's <code>JSpinner</code>
     * ancestors model.  If a previousButton isn't needed (in a subclass)
     * then override this method to return null.
     *
     * @return a component that will replace the spinners model with the
     *     next value in the sequence, or null
     * @see #installUI
     * @see #createNextButton
     */
    protected Component createPreviousButton() {
        return new SpinnerXPArrowButton(SwingConstants.SOUTH, PREVIOUS_BUTTON_HANDLER);
    }

    /**
     * Create a component that will replace the spinner models value
     * with the object returned by <code>spinner.getNextValue</code>.
     * By default the <code>nextButton</code> is a JButton
     * who's <code>ActionListener</code> updates it's <code>JSpinner</code>
     * ancestors model.  If a nextButton isn't needed (in a subclass)
     * then override this method to return null.
     *
     * @return a component that will replace the spinners model with the
     *     next value in the sequence, or null
     * @see #installUI
     * @see #createPreviousButton
     */
    protected Component createNextButton() {
        return new SpinnerXPArrowButton(SwingConstants.NORTH, NEXT_BUTTON_HANDLER);
    }

    /**
     * It differs from its superclass in that it uses the same formula as JDK
     * to calculate the arrow height.
     */
    private static final class SpinnerXPArrowButton extends PlasticArrowButton {

        SpinnerXPArrowButton(int direction, ExtBasicArrowButtonHandler handler) {
            super(direction, UIManager.getInt("ScrollBar.width") - 1, false);
            addActionListener(handler);
            addMouseListener(handler);
        }

        protected int calculateArrowHeight(int height, int width) {
            int arrowHeight = Math.min((height - 4) / 3, (width - 4) / 3);
            return Math.max(arrowHeight, 3);
        }

        protected boolean isPaintingNorthBottom() {
            return true;
        }

        protected int calculateArrowOffset() {
            return 1;
        }

        protected void paintNorth(Graphics g, boolean leftToRight, boolean isEnabled, Color arrowColor, boolean isPressed, int width, int height, int w, int h, int arrowHeight, int arrowOffset, boolean paintBottom) {
            if (!isFreeStanding) {
                height += 1;
                g.translate(0, -1);
                if (!leftToRight) {
                    width += 1;
                    g.translate(-1, 0);
                } else {
                    width += 2;
                }
            }
            g.setColor(arrowColor);
            int startY = 1 + ((h + 1) - arrowHeight) / 2;
            int startX = w / 2;
            for (int line = 0; line < arrowHeight; line++) {
                g.fillRect(startX - line - arrowOffset, startY + line, 2 * (line + 1), 1);
            }
            paintNorthBorder(g, isEnabled, width, height, paintBottom);
            if (!isFreeStanding) {
                height -= 1;
                g.translate(0, 1);
                if (!leftToRight) {
                    width -= 1;
                    g.translate(1, 0);
                } else {
                    width -= 2;
                }
            }
        }

        private void paintNorthBorder(Graphics g, boolean isEnabled, int w, int h, boolean paintBottom) {
            if (isEnabled) {
                boolean isPressed = model.isPressed() && model.isArmed();
                if (isPressed) {
                    PlasticXPUtils.drawPressedButtonBorder(g, 0, 1, w - 2, h);
                } else {
                    PlasticXPUtils.drawPlainButtonBorder(g, 0, 1, w - 2, h);
                }
            } else {
                PlasticXPUtils.drawDisabledButtonBorder(g, 0, 1, w - 2, h + 1);
            }
            g.setColor(isEnabled ? PlasticLookAndFeel.getControlDarkShadow() : MetalLookAndFeel.getControlShadow());
            g.fillRect(0, 1, 1, 1);
            if (paintBottom) {
                g.fillRect(0, h - 1, w - 1, 1);
            }
        }

        protected void paintSouth(Graphics g, boolean leftToRight, boolean isEnabled, Color arrowColor, boolean isPressed, int width, int height, int w, int h, int arrowHeight, int arrowOffset) {
            if (!isFreeStanding) {
                height += 1;
                if (!leftToRight) {
                    width += 1;
                    g.translate(-1, 0);
                } else {
                    width += 2;
                }
            }
            g.setColor(arrowColor);
            int startY = (((h + 0) - arrowHeight) / 2) + arrowHeight - 2;
            int startX = w / 2;
            for (int line = 0; line < arrowHeight; line++) {
                g.fillRect(startX - line - arrowOffset, startY - line, 2 * (line + 1), 1);
            }
            paintSouthBorder(g, isEnabled, width, height);
            if (!isFreeStanding) {
                height -= 1;
                if (!leftToRight) {
                    width -= 1;
                    g.translate(1, 0);
                } else {
                    width -= 2;
                }
            }
        }

        private void paintSouthBorder(Graphics g, boolean isEnabled, int w, int h) {
            if (isEnabled) {
                boolean isPressed = model.isPressed() && model.isArmed();
                if (isPressed) {
                    PlasticXPUtils.drawPressedButtonBorder(g, 0, -2, w - 2, h + 1);
                } else {
                    PlasticXPUtils.drawPlainButtonBorder(g, 0, -2, w - 2, h + 1);
                }
            } else {
                PlasticXPUtils.drawDisabledButtonBorder(g, 0, -2, w - 2, h + 1);
            }
            g.setColor(isEnabled ? PlasticLookAndFeel.getControlDarkShadow() : MetalLookAndFeel.getControlShadow());
            g.fillRect(0, h - 2, 1, 1);
        }
    }
}
