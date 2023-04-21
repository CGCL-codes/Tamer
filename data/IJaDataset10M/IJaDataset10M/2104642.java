package oclac.view.application.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import oclac.data.Pipeline;
import oclac.data.Project;
import oclac.data.ProjectSolution;
import oclac.io.CommandLineIO;
import oclac.io.CommandLineIO.ScriptFormats;
import oclac.util.I18N;
import oclac.view.ui.ExportDialog;
import oclac.view.ui.MainWindow;
import oclac.view.ui.data.ExportDialogData;
import oclac.view.ui.models.ProjectBrowserModel;

@SuppressWarnings("serial")
public class ExportAction extends AbstractAction implements TreeModelListener {

    /**
	 * Constructor for this action.
	 */
    public ExportAction() {
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/oclac/res/export.png")));
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.NAME, I18N.instance.getMessage("ExportAction.Label"));
        putValue(Action.SHORT_DESCRIPTION, I18N.instance.getMessage("ExportAction.ShortDescription"));
        update();
        ProjectBrowserModel.instance.addTreeModelListener(this);
    }

    /**
	 * Shows the export dialog and exports the wanted pipelines
	 * if the data that the user selected is valid and he confirmed to it.
	 */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (!ProjectBrowserModel.instance.hasPipeline()) return;
        ExportDialogData data = new ExportDialogData();
        Object selectedElement = MainWindow.instance.getProjectSolutionTree().getLastSelectedPathComponent();
        if (selectedElement != null) {
            data.setSelectionOnly(true);
        }
        ExportDialog exportDialog = new ExportDialog(MainWindow.instance, data);
        exportDialog.setVisible(true);
        data = exportDialog.getData();
        if (data == null) return;
        if (data.getFile() == null || data.getFile().getAbsolutePath().isEmpty()) {
            JOptionPane.showMessageDialog(MainWindow.instance, I18N.instance.getMessage("ExportAction.NoFile"), I18N.instance.getMessage("DefaultDialogs.Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        ScriptFormats format;
        String fileName = data.getFile().getAbsolutePath();
        if (fileName.indexOf('.') != -1 && fileName.substring(fileName.lastIndexOf('.') + 1).compareTo("bat") == 0) format = ScriptFormats.SF_BATCH; else if (fileName.indexOf('.') != -1 && fileName.substring(fileName.lastIndexOf('.') + 1).compareTo("sh") == 0) format = ScriptFormats.SF_SHELL; else return;
        CommandLineIO commandLineIO = new CommandLineIO(format, data.getPreferShortParameters());
        try {
            if (!data.getSelectionOnly() || (selectedElement instanceof ProjectSolution)) {
                commandLineIO.exportSolutionToFile(((ProjectSolution) ProjectBrowserModel.instance.getRoot()), data.getFile());
            }
            if (selectedElement instanceof Project) {
                commandLineIO.exportProjectToFile((Project) selectedElement, data.getFile());
            } else if (selectedElement instanceof Pipeline) {
                commandLineIO.exportPipelineToFile((Pipeline) selectedElement, data.getFile());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(MainWindow.instance, I18N.instance.getMessage("ExportAction.Error"), I18N.instance.getMessage("DefaultDialogs.Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(MainWindow.instance, I18N.instance.getMessage("ExportAction.SuccessMsg"), I18N.instance.getMessage("ExportAction.Success"), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
	 * This function updates the enabled status of the action.
	 * It should be called whenever something in the project browser changes.
	 */
    private void update() {
        setEnabled(ProjectBrowserModel.instance.hasPipeline());
    }

    @Override
    public void treeNodesChanged(TreeModelEvent arg0) {
        update();
    }

    @Override
    public void treeNodesInserted(TreeModelEvent arg0) {
        update();
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent arg0) {
        update();
    }

    @Override
    public void treeStructureChanged(TreeModelEvent arg0) {
        update();
    }
}
