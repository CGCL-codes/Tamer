package org.outerj.pollo.action;

import org.outerj.pollo.PolloFrame;
import org.outerj.pollo.Pollo;
import org.outerj.pollo.util.ResourceManager;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action to close all open files.
 */
public class CloseAllAction extends AbstractAction {

    protected PolloFrame polloFrame;

    public CloseAllAction(PolloFrame polloFrame) {
        ResourceManager resMgr = ResourceManager.getManager(CloseAllAction.class);
        resMgr.configureAction(this);
        this.polloFrame = polloFrame;
    }

    public void actionPerformed(ActionEvent e) {
        Pollo.getInstance().closeAllFiles(polloFrame, null);
    }
}
