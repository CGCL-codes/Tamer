package org.openconcerto.erp.core.sales.quote.action;

import org.openconcerto.erp.action.CreateFrameAbstractAction;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.view.EditFrame;
import javax.swing.Action;
import javax.swing.JFrame;

public class NouvellePropositionAction extends CreateFrameAbstractAction {

    public NouvellePropositionAction() {
        super();
        this.putValue(Action.NAME, "Proposition");
    }

    @Override
    public JFrame createFrame() {
        return new EditFrame(Configuration.getInstance().getDirectory().getElement("PROPOSITION"));
    }
}
