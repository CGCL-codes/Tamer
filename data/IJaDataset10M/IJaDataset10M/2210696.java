package net.sf.greengary.xynch;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/** Icon that changes it's color, on=green, off=red.
 */
public class OnOffIcon implements Icon {

    /** Current color of the icon. */
    private Color m_Color = Color.red;

    /** Set icon's color to green. */
    public void setOn() {
        m_Color = Color.green;
    }

    /** Set icon's color to red. */
    public void setOff() {
        m_Color = Color.red;
    }

    /** Returns the icon's height.
	*	@return an int specifying the fixed height of the icon. */
    public int getIconHeight() {
        return 16;
    }

    /** Returns the icon's width.
	*	@return an int specifying the fixed width of the icon. */
    public int getIconWidth() {
        return 16;
    }

    /**	Draw the icon at the specified location. 
	*	Icon implementations may use the Component argument 
	*	to get properties useful for painting, e.g. the foreground 
	*	or background color. 
	*	@param c Component to get properties useful for painting, 
	*	e.g. the foreground or background color. 
	*	@param g Graphics-object for painting.
	*	@param x x-position.
	*	@param y y-position. */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(m_Color);
        g.fillOval(x + 2, y + 2, 12, 12);
    }
}
