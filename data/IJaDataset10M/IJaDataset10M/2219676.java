package org.adapit.wctoolkit.infrastructure.events.actions.xmi;

import java.awt.event.ActionEvent;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.events.actions.DefaultAbstractApplicationAction;

public class SaveGlobalConfigurationModelAction extends DefaultAbstractApplicationAction {

    @Override
    protected void doAction(ActionEvent evt) {
        try {
            DefaultApplicationFrame.getInstance().getConfigLoader().savePdmFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
