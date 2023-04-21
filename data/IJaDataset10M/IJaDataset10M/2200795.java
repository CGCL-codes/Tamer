package com.od.jtimeseries.ui.timeserious.action;

import com.od.jtimeseries.ui.config.ConfigAwareTreeManager;
import com.od.jtimeseries.ui.config.ConfigInitializer;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
* Created by IntelliJ IDEA.
* User: Nick Ebbutt
* Date: 08/04/11
* Time: 06:58
*/
public class ExitAction extends AbstractSaveConfigAction {

    public ExitAction(JFrame mainFrame, ConfigAwareTreeManager configTree, ConfigInitializer configInitializer) {
        super("Exit", null, mainFrame, configTree, configInitializer);
        super.putValue(SHORT_DESCRIPTION, "Exit and save config");
    }

    public void actionPerformed(ActionEvent e) {
        if (confirmAndSaveConfig("Exit TimeSerious", JOptionPane.YES_NO_CANCEL_OPTION)) {
            System.exit(0);
        }
    }
}
