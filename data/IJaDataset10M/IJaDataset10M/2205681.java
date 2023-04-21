package org.plazmaforge.bsolution.payroll.client.swing.actions;

import java.awt.event.ActionEvent;
import org.plazmaforge.bsolution.payroll.client.swing.forms.EmployeeSicklistList;
import org.plazmaforge.framework.client.swing.SwingFormManager;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * @author Oleh Hapon
 * Date: 04.11.2004
 * Time: 9:24:07
 * $Id: EmployeeSicklistListAction.java,v 1.3 2010/12/05 07:56:47 ohapon Exp $
 */
public class EmployeeSicklistListAction extends GUIPayrollAction {

    public void perform(ActionEvent e) throws ApplicationException {
        SwingFormManager.showListForm(EmployeeSicklistList.class);
    }
}
