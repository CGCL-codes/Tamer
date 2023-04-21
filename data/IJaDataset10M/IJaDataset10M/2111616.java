package org.ungoverned.radical.action;

import java.awt.event.*;
import javax.swing.*;
import org.ungoverned.radical.Radical;

public class PropertyEditorsAction extends AbstractAction {

    public PropertyEditorsAction() {
        super("Property editors...");
    }

    public void actionPerformed(ActionEvent event) {
        Radical.managePropertyEditors();
    }
}
