package be.vds.jtbdive.client.actions.browser;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import be.smd.i18n.I18nResourceManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.view.components.DiveSelectionDialog;
import be.vds.jtbdive.client.view.core.browser.LogBookBrowserPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;

public class DeleteDivesAction extends AbstractAction {

    private static final long serialVersionUID = 886604030143517856L;

    private LogBookBrowserPanel logBookBrowserPanel;

    private LogBookManagerFacade logBookManagerFacade;

    public DeleteDivesAction(LogBookBrowserPanel logBookBrowserPanel, LogBookManagerFacade logBookManagerFacade) {
        super("dive.delete", UIAgent.getInstance().getIcon(UIAgent.ICON_DIVE_DOCUMENT_DELETE_16));
        this.logBookBrowserPanel = logBookBrowserPanel;
        this.logBookManagerFacade = logBookManagerFacade;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        I18nResourceManager i18n = I18nResourceManager.sharedInstance();
        List<Dive> dives = logBookBrowserPanel.getHighLightedDives();
        DiveSelectionDialog dlg = new DiveSelectionDialog(i18n.getString("dives.delete.confirm"), i18n.getString("dives.delete.confirm.message"), dives, WindowUtils.getParentFrame(logBookBrowserPanel));
        dlg.setMessageIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_TRASH_FULL_32));
        dlg.setLocationRelativeTo(null);
        int i = dlg.showSelection();
        if (i == DiveSelectionDialog.OPTION_OK) {
            dives = dlg.getSelectedDives();
            List<Dive> deletedDives = logBookManagerFacade.deleteDives(dives);
            if (deletedDives.size() != dives.size()) {
                dives.removeAll(deletedDives);
                annouceUndeletedDives(dives);
            }
        }
    }

    private void annouceUndeletedDives(List<Dive> dives) {
        StringBuilder sb = new StringBuilder("An error occured while deleting the following dives:");
        for (Dive dive : dives) {
            sb.append("\r\n- Dive ").append(dive.getNumber());
        }
        JOptionPane.showMessageDialog(WindowUtils.getParentFrame(logBookBrowserPanel), sb.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
