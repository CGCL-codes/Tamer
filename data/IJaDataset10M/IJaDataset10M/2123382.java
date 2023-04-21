package org.plazmaforge.bsolution.base.client.swing.actions;

import java.awt.event.ActionEvent;
import org.plazmaforge.bsolution.base.client.swing.forms.SystemVariableList;
import org.plazmaforge.framework.client.swing.SwingFormManager;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * Created on 24.05.2006
 */
public class SystemVariableListAction extends GUIBaseAction {

    public void perform(ActionEvent e) throws ApplicationException {
        SwingFormManager.showListForm(SystemVariableList.class);
    }
}
