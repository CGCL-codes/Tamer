package org.plazmaforge.bsolution.partner.client.swing.actions;

import java.awt.event.ActionEvent;
import org.plazmaforge.bsolution.partner.client.swing.forms.ActivityTypeList;
import org.plazmaforge.framework.client.swing.SwingFormManager;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * Created on 24.05.2006
 */
public class ActivityTypeListAction extends GUIPartnerAction {

    public void perform(ActionEvent e) throws ApplicationException {
        SwingFormManager.showListForm(ActivityTypeList.class);
    }
}
