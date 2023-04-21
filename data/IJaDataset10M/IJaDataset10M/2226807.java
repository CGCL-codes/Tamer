package tico.components;

import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * An implementation of a label for a tool bar with the format parameters for Tico
 * applications.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TToolBarLabel extends JLabel {

    /**
	 * Creates a <code>TToolBarLabel</code> with an <code>icon</code>.
	 * 
	 * @param icon The <code>icon</code> image to display on the button
	 */
    public TToolBarLabel(Icon icon) {
        super(icon);
        setPreferredSize(new Dimension(icon.getIconHeight() + 10, icon.getIconWidth() + 10));
    }
}
