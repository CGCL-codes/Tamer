package org.plazmaforge.bsolution.payroll.client.swing.actions;

import java.awt.event.ActionEvent;
import org.plazmaforge.bsolution.payroll.client.swing.forms.InputWorktimeList;
import org.plazmaforge.framework.client.swing.SwingFormManager;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * @author Oleh Hapon
 * Date: 29.10.2004
 * Time: 9:35:31
 * $Id: InputWorktimeListAction.java,v 1.3 2010/12/05 07:56:47 ohapon Exp $
 */
public class InputWorktimeListAction extends GUIPayrollAction {

    public void perform(ActionEvent e) throws ApplicationException {
        SwingFormManager.showListForm(InputWorktimeList.class);
    }
}
