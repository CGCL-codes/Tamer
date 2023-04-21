package org.openconcerto.erp.core.sales.invoice.action;

import org.openconcerto.erp.action.CreateFrameAbstractAction;
import org.openconcerto.erp.core.sales.invoice.ui.ListeDesEcheancesClientsFrame;
import javax.swing.Action;
import javax.swing.JFrame;

public class ListesFacturesClientsImpayeesAction extends CreateFrameAbstractAction {

    public ListesFacturesClientsImpayeesAction() {
        super();
        this.putValue(Action.NAME, "Factures clients non payées");
    }

    public JFrame createFrame() {
        return new ListeDesEcheancesClientsFrame();
    }
}
