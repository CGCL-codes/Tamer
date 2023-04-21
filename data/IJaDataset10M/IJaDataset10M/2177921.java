package org.modyna.modyna.gui.action;

import java.awt.event.ActionEvent;

/**
 * Triggers saving a model to a file
 * 
 * @author Dr. Rupert Rebentisch
 * 
 */
public class SaveAction extends ManagedAction {

    private static final long serialVersionUID = 5602357143116493411L;

    public SaveAction(ActionManager actionManager) {
        super("Save", "icons/save.gif", actionManager);
    }

    /**
	 * Triggers saving a model to a file
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent arg0) {
        getActionManager().saveToFile();
    }
}
