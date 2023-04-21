package org.plazmaforge.bsolution.bank.client.swing.actions;

import java.awt.event.ActionEvent;
import org.plazmaforge.bsolution.bank.client.swing.forms.BankList;
import org.plazmaforge.framework.client.swing.SwingFormManager;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * @author Oleh Hapon
 * Date: 13.07.2004
 * Time: 9:29:30
 * $Id: BankListAction.java,v 1.3 2010/12/05 07:56:44 ohapon Exp $
 */
public class BankListAction extends GUIBankAction {

    public void perform(ActionEvent e) throws ApplicationException {
        SwingFormManager.showListForm(BankList.class);
    }
}
