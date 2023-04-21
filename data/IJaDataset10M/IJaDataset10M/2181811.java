package com.digitprop.tonic;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;

public class ScrollButton extends BasicArrowButton {

    /**	The shadow color */
    private static Color shadowColor;

    /**	The highlight color */
    private static Color highlightColor;

    /**	If true, this button is free standing, i.e. does not touch any edge of
	 * 	the parent component.
	 */
    private boolean isFreeStanding = false;

    /**	Width of the button */
    private int buttonWidth;

    /**	Icon to be displayed on the button */
    private Icon icon;

    /**	Creates an instance.
	 * 
	 * 	@param	direction		The direction into which the arrow on the button
	 * 									is to point. This must be a SwingConstants constant.
	 *		@param	width				The width of the button
	 *		@param	freeStanding	If true, the button is freestanding
	 */
    public ScrollButton(int direction, int width, boolean freeStanding) {
        super(direction);
        shadowColor = UIManager.getColor("ScrollBar.darkShadow");
        highlightColor = UIManager.getColor("ScrollBar.highlight");
        buttonWidth = width;
        isFreeStanding = freeStanding;
    }

    /**	Sets whether the button is free standing, i.e. does not touch any
	 * 	edge of the parent component.
	 */
    public void setFreeStanding(boolean freeStanding) {
        isFreeStanding = freeStanding;
    }

    /**	Paints this button */
    public void paint(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(UIManager.getColor("Button.borderColor"));
        Rectangle shadowRect = new Rectangle(0, 0, getWidth(), getHeight());
        boolean isPressed = model.isPressed();
        int delta = (isPressed ? 1 : 0);
        if (isPressed) {
            g.setColor(UIManager.getColor("Button.highlight"));
            g.drawRect(shadowRect.x, shadowRect.y, shadowRect.width - 1, shadowRect.height - 1);
            g.setColor(UIManager.getColor("Button.shadow"));
            g.drawLine(shadowRect.x, shadowRect.y, shadowRect.x + shadowRect.width - 1, shadowRect.y);
            g.drawLine(shadowRect.x, shadowRect.y, shadowRect.x, shadowRect.y + shadowRect.height - 1);
        }
        if (icon == null) {
            switch(direction) {
                case EAST:
                    icon = UIManager.getIcon("Arrow.right");
                    break;
                case WEST:
                    icon = UIManager.getIcon("Arrow.left");
                    break;
                case NORTH:
                    icon = UIManager.getIcon("Arrow.up");
                    break;
                case SOUTH:
                    icon = UIManager.getIcon("Arrow.down");
                    break;
            }
        }
        if (icon != null) icon.paintIcon(this, g, getWidth() / 2 - icon.getIconWidth() / 2 + delta, getHeight() / 2 - icon.getIconHeight() / 2 + delta);
    }

    /**	Returns the preferred size for this button */
    public Dimension getPreferredSize() {
        if (getDirection() == NORTH) {
            return new Dimension(buttonWidth, buttonWidth);
        } else if (getDirection() == SOUTH) {
            return new Dimension(buttonWidth, buttonWidth);
        } else if (getDirection() == EAST) {
            return new Dimension(buttonWidth, buttonWidth);
        } else if (getDirection() == WEST) {
            return new Dimension(buttonWidth, buttonWidth);
        } else {
            return new Dimension(0, 0);
        }
    }

    /**	Returns the minimum size for this button */
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**	Returns the maximum size for this button */
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**	Returns the button width */
    public int getButtonWidth() {
        return buttonWidth;
    }
}
