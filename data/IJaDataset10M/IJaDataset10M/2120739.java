package edu.upmc.opi.caBIG.caTIES.client.vr.query.actions;

import java.awt.event.ActionEvent;
import java.util.ConcurrentModificationException;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Desktop;
import edu.upmc.opi.caBIG.caTIES.client.vr.query.views.CaseSetPanel;
import edu.upmc.opi.caBIG.caTIES.client.vr.utils.progress.ProgressMonitor;
import edu.upmc.opi.caBIG.caTIES.client.vr.utils.progress.ProgressUtil;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_MiddleTierImpl;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_OrderSetImpl;

public class DeleteCaseSetAction extends AbstractAction {

    CaTIES_OrderSetImpl caseSet;

    private boolean success = false;

    private CaseSetPanel caseSetPanel;

    public DeleteCaseSetAction(CaseSetPanel caseSetPanel, CaTIES_OrderSetImpl caseSet) {
        super("Remove \"" + CaTIES_OrderSetImpl.getName(caseSet.obj) + "\"");
        this.caseSet = caseSet;
        this.caseSetPanel = caseSetPanel;
    }

    class OrderDeleter implements Runnable {

        public void run() {
            ProgressMonitor monitor = ProgressUtil.createModalProgressMonitor(CaTIES_Desktop.getInstance(), 2, true, 250);
            monitor.start("Removing case set");
            caseSet.prepareForDeletion();
            monitor.setCurrent("Removing case set", 1);
            CaTIES_MiddleTierImpl mt = CaTIES_MiddleTierImpl.getInstance();
            mt.deleteObject(caseSet.obj);
            try {
                success = mt.commitTransaction();
                if (monitor.getCurrent() != monitor.getTotal()) monitor.setCurrent(null, monitor.getTotal());
            } catch (Exception e) {
                if (e instanceof ConcurrentModificationException) ; else e.printStackTrace();
            }
            if (success) {
                CaTIES_Desktop.getInstance().preloadCaseSets();
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        caseSetPanel.loadCaseSets();
                        caseSetPanel.refresh();
                        JOptionPane.showMessageDialog(CaTIES_Desktop.getInstance(), "The case set\"" + caseSet.getName(caseSet.obj) + "\" was successfully removed.", "Remove Case Set", JOptionPane.PLAIN_MESSAGE);
                    }
                });
            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        JOptionPane.showMessageDialog(CaTIES_Desktop.getInstance(), "The case set\"" + caseSet.getName(caseSet.obj) + "\" was successfully removed.", "Remove Case Set", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }
    }

    ;

    public void actionPerformed(ActionEvent e) {
        int ret = JOptionPane.showConfirmDialog(CaTIES_Desktop.getInstance(), "Are you sure you want to remove the case set \"" + caseSet.getName(caseSet.obj) + "\"");
        if (ret == JOptionPane.YES_OPTION) {
            new Thread(new OrderDeleter()).start();
        }
    }

    public boolean isSuccessful() {
        return success;
    }
}
