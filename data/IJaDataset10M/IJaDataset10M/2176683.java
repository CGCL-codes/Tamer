package org.openconcerto.erp.core.finance.accounting.action;

import org.openconcerto.erp.action.CreateFrameAbstractAction;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.view.IListFrame;
import org.openconcerto.sql.view.ListeAddPanel;
import java.util.Calendar;
import javax.swing.Action;
import javax.swing.JFrame;

public class ListeDesEcrituresTestAction extends CreateFrameAbstractAction {

    public ListeDesEcrituresTestAction() {
        super();
        this.putValue(Action.NAME, "Liste des écritures");
    }

    public JFrame createFrame() {
        final long time = Calendar.getInstance().getTimeInMillis();
        final IListFrame frame = new IListFrame(new ListeAddPanel(Configuration.getInstance().getDirectory().getElement("ECRITURE")));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getPanel().setSearchFullMode(true);
        frame.getPanel().getListe().getModel().invokeLater(new Runnable() {

            public void run() {
                System.err.println("Load ecritures : " + (Calendar.getInstance().getTimeInMillis() - time) + " ms");
            }
        });
        return frame;
    }
}
