package com.jeantessier.dependencyfinder.gui;

import java.awt.event.*;
import javax.swing.*;

public class AdvancedQueryPanelAction extends AbstractAction {

    private DependencyFinder model = null;

    public AdvancedQueryPanelAction(DependencyFinder model) {
        this.model = model;
        putValue(Action.LONG_DESCRIPTION, "Select advanced query panel");
        putValue(Action.NAME, "Advanced");
    }

    public void actionPerformed(ActionEvent e) {
        if (model != null) {
            model.setAdvancedMode(true);
            model.buildQueryPanel();
        }
    }
}
