package org.openconcerto.erp.core.humanresources.payroll.action;

import org.openconcerto.erp.action.CreateFrameAbstractAction;
import org.openconcerto.erp.core.common.ui.PanelFrame;
import org.openconcerto.erp.core.humanresources.payroll.ui.EditionFichePayePanel;
import javax.swing.Action;
import javax.swing.JFrame;

public class EditionFichePayeAction extends CreateFrameAbstractAction {

    public EditionFichePayeAction() {
        super();
        this.putValue(Action.NAME, "Edition des fiches de paye");
    }

    public JFrame createFrame() {
        return new PanelFrame(new EditionFichePayePanel(), "Edition des fiches de paye");
    }
}
