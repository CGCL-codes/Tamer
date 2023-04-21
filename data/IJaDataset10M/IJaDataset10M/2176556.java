package org.gaea.ui.utilities;

import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.gaea.common.GaeaLogger;
import org.gaea.common.GaeaLoggerFactory;
import org.gaea.ui.language.Messages;

/**
 * Utilities used by tool bar.
 * 
 * @author mtremblay
 */
public class ToolBarUtilities {

    /**
	 * Makes the button with the specified information.
	 * 
	 * @param actionCommand
	 *            String of the action command related to this button.
	 * @param toolTipText
	 *            Tool tip of this button.
	 * @param altText
	 *            Alternative text in case the image is unavailable.
	 * @param imgLocation
	 *            Location of the image of this button.
	 * @param eventActionListener
	 *            Action listener to be used when an action is done on this
	 *            button.
	 * @return JButton Button made.
	 */
    public static JButton makeButton(String actionCommand, String toolTipText, String altText, String imgLocation, ActionListener eventActionListener) {
        ImageIcon imageIcon = UtilityImage.getImageIcon(imgLocation);
        JButton button = new JButton();
        button.setFocusable(false);
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(eventActionListener);
        button.setMargin(new Insets(0, 0, 0, 0));
        if (imageIcon != null) {
            button.setIcon(imageIcon);
        } else {
            button.setText(altText);
            GaeaLogger logger = GaeaLoggerFactory.getLogger("org.gaea.ui.utilities.ToolBarUtilities", ErrorUtilities.getErrorHandlers());
            logger.error(Messages.getString("ToolBarUtilities.ResourceNotFound", imgLocation));
        }
        return button;
    }
}
