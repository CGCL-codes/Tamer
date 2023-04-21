package org.openconcerto.erp.core.finance.accounting.action;

import org.openconcerto.erp.action.CreateFrameAbstractAction;
import org.openconcerto.erp.core.common.ui.PanelFrame;
import org.openconcerto.erp.core.finance.accounting.ui.ClotureManuellePanel;
import javax.swing.Action;
import javax.swing.JFrame;

public class NouveauClotureManuelleAction extends CreateFrameAbstractAction {

    public NouveauClotureManuelleAction() {
        super();
        this.putValue(Action.NAME, "Clôture manuelle");
    }

    @Override
    public JFrame createFrame() {
        return new PanelFrame(new ClotureManuellePanel(), "Clôture manuelle");
    }
}
