package es.aeat.eett.infoRubik.reports;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import org.apache.log4j.Logger;

/**
 * @author f00992
 *
 * TODO Mover el grueso del codigo a Report o Reports y crear eventos de error
 *
 */
class ActionCreateReport extends AbstractAction {

    private static final long serialVersionUID = 7777533861353751911L;

    private static final ResourceBundle localizationResources = ResourceBundle.getBundle("es.aeat.eett.infoRubik.reports.locale.LocalizationBundle");

    private static Logger logger = Logger.getLogger(ActionCreateReport.class);

    private static final String NEW_REPORT = localizationResources.getString("NewReport");

    private final String[] ConnectOptionNames = { localizationResources.getString("OK"), localizationResources.getString("Cancel") };

    /**
     *
     */
    public ActionCreateReport() {
        super(NEW_REPORT);
        Icon icon = new ImageIcon(getClass().getResource("icons/cprj_obj.gif"));
        putValue(Action.SMALL_ICON, icon);
    }

    public void actionPerformed(ActionEvent e) {
        javax.swing.JPopupMenu jp = (JPopupMenu) ((Component) e.getSource()).getParent();
        try {
            createNewReport(jp.getInvoker());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void createNewReport(Component cParent) throws Exception {
        NewReporPanel reporPanel = new NewReporPanel();
        reporPanel.setNameReport(NEW_REPORT);
        File fDir = null;
        try {
            fDir = getValidNameDir();
            if (fDir == null) {
                logger.error("Error.");
                return;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return;
        }
        reporPanel.setNameDir(fDir.getName());
        if (JOptionPane.showOptionDialog(cParent, reporPanel, NEW_REPORT, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ConnectOptionNames, ConnectOptionNames[0]) == 0) {
            createNewReport(cParent, reporPanel, fDir);
        } else {
            fDir.delete();
        }
    }

    private void createNewReport(Component cParent, NewReporPanel reporPanel, File fDefaultDir) throws Exception {
        File fDir = fDefaultDir;
        if (reporPanel.getNameReport().trim().equals(new String())) {
            logger.error("Se necesita el nombre del informe.");
            return;
        }
        if (!fDefaultDir.getName().equals(reporPanel.getNameDir())) {
            fDefaultDir.delete();
            fDir = new File(ConfiResports.getInstance().getFileReportsSpace(), reporPanel.getNameDir());
            if (!fDir.mkdir()) {
                logger.error("Error creando: " + fDir.getAbsolutePath());
                return;
            }
        }
        try {
            createNewReport((JTree) cParent, reporPanel, fDir);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fDir.delete();
            return;
        }
    }

    private void createNewReport(JTree tree, NewReporPanel reporPanel, File fDir) throws Exception {
        Report report = Report.createReport(reporPanel.getNameReport(), fDir);
        addReport(tree, report);
    }

    private void addReport(JTree tree, Report report) {
        DefaultTreeModel tm = (DefaultTreeModel) tree.getModel();
        ReportMutableTreeNode nRoot = (ReportMutableTreeNode) tm.getRoot();
        int index = nRoot.getChildCount();
        ReportMutableTreeNode newChild = report.getMutableTreeNode();
        tm.insertNodeInto(newChild, nRoot, index);
        if (nRoot.getChildCount() == 1) {
            tree.updateUI();
        }
    }

    private File getValidNameDir() throws Exception {
        File fDirReports = ConfiResports.getInstance().getFileReportsSpace();
        File tmpFile = File.createTempFile(Report.getKEY_REPORT(), new String(), fDirReports);
        tmpFile.delete();
        if (!tmpFile.mkdir()) return null;
        return tmpFile;
    }
}
