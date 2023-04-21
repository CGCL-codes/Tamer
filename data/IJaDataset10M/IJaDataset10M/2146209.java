package org.mcisb.ui.util.edit;

import java.awt.event.*;
import javax.swing.*;

/**
 * 
 * @author Neil Swainston
 */
public class StartEditingAction extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof Editable) {
            ((Editable) source).edit();
        }
    }
}
