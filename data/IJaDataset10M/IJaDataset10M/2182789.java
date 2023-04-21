package org.argus.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

public class HelpAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public HelpAction() {
        super("Help");
    }

    public void actionPerformed(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "The author of JmxM, Dan Peder Eriksen, can be contacted on danpe@stud.cs.uit.no", "JmxM Help", JOptionPane.INFORMATION_MESSAGE);
    }
}
