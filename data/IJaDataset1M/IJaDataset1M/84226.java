package org.xebra.client.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import org.xebra.client.events.ToolSelectionEvent.ToolStatus;
import org.xebra.client.util.ApplicationProperties;
import org.xebra.client.util.GUIMessages;
import org.xebra.client.util.ImageGenerator;
import org.xebra.client.util.ToolTracker;

/**
 * Toggles the window and level tooltip on and off.
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.3 $
 */
public class WindowLevelAction extends AbstractAction {

    private static final long serialVersionUID = 3555951964561780454L;

    public WindowLevelAction() {
        super();
        GUIMessages messages = GUIMessages.getSingletonInstance();
        putValue(Action.NAME, messages.getText("message.gui.title.toolbar.wl"));
        putValue(Action.SMALL_ICON, ImageGenerator.loadIcon(ImageGenerator.WINDOW_LEVEL_ICON));
        putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_W));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ApplicationProperties.getSingletonInstance().getCtrlShiftKey()));
    }

    /**
	 * @param e
	 *        The event.
	 *
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        Component comp = (Component) e.getSource();
        boolean isSelected = false;
        if (comp instanceof JToggleButton) {
            isSelected = ((JToggleButton) comp).isSelected();
        } else {
            isSelected = ((JCheckBoxMenuItem) comp).isSelected();
        }
        if (isSelected) {
            ToolTracker.setActiveTool(comp, ToolStatus.WINDOW_LEVEL_TOOL);
        } else {
            ToolTracker.setActiveTool(comp, ToolStatus.NO_TOOL);
        }
    }
}
