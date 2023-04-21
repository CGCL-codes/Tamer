package acide.gui.menuBar.projectMenu.listeners;

import acide.configuration.project.AcideProjectConfiguration;
import acide.files.AcideFileManager;
import acide.files.project.AcideProjectFile;
import acide.gui.mainWindow.AcideMainWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import acide.language.AcideLanguageManager;

/**
 * ACIDE -A Configurable IDE project menu remove file menu item listener.
 * 
 * @version 0.8
 * @see ActionListener
 */
public class AcideRemoveFileMenuItemListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int returnValue = JOptionPane.showConfirmDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s623"));
        if (returnValue == JOptionPane.OK_OPTION) {
            TreePath currentSelection = AcideMainWindow.getInstance().getExplorerPanel().getTree().getSelectionPath();
            if (currentSelection != null) {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
                AcideProjectFile currentFile = (AcideProjectFile) currentNode.getUserObject();
                if (!currentFile.isDirectory()) {
                    MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
                    if (parent != null) {
                        AcideMainWindow.getInstance().getExplorerPanel().getTreeModel().removeNodeFromParent(currentNode);
                        int fileIndex = -1;
                        for (int index = 0; index < AcideProjectConfiguration.getInstance().getNumberOfFilesFromList(); index++) {
                            if (AcideProjectConfiguration.getInstance().getFileAt(index).getAbsolutePath().equals(currentFile.getAbsolutePath())) {
                                fileIndex = index;
                            }
                        }
                        AcideProjectConfiguration.getInstance().removeFileAt(fileIndex);
                        AcideMainWindow.getInstance().getStatusBar().setStatusMessage(" ");
                        int fileEditorPanelIndex = -1;
                        for (int index = 0; index < AcideMainWindow.getInstance().getFileEditorManager().getNumberOfFileEditorPanels(); index++) {
                            if (AcideMainWindow.getInstance().getFileEditorManager().getFileEditorPanelAt(index).getAbsolutePath().equals(currentFile.getAbsolutePath())) fileEditorPanelIndex = index;
                        }
                        if (fileEditorPanelIndex != -1) {
                            if (AcideMainWindow.getInstance().getFileEditorManager().isRedButton(fileEditorPanelIndex)) {
                                returnValue = JOptionPane.showConfirmDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s643"), AcideLanguageManager.getInstance().getLabels().getString("s953"), JOptionPane.YES_NO_OPTION);
                                if (returnValue == JOptionPane.OK_OPTION) {
                                    boolean savingResult = AcideFileManager.getInstance().write(AcideMainWindow.getInstance().getFileEditorManager().getFileEditorPanelAt(fileEditorPanelIndex).getAbsolutePath(), AcideMainWindow.getInstance().getFileEditorManager().getFileEditorPanelAt(fileEditorPanelIndex).getTextEditionAreaContent());
                                    if (savingResult) {
                                        AcideMainWindow.getInstance().getFileEditorManager().setGreenButtonAt(fileEditorPanelIndex);
                                    }
                                }
                            }
                            AcideMainWindow.getInstance().getFileEditorManager().getTabbedPane().remove(fileEditorPanelIndex);
                            AcideMainWindow.getInstance().getFileEditorManager().getTabbedPane().validate();
                        }
                    }
                    AcideProjectConfiguration.getInstance().setIsModified(true);
                }
            }
        }
        if (AcideProjectConfiguration.getInstance().getNumberOfFilesFromList() > 0) {
            AcideMainWindow.getInstance().getFileEditorManager().updateRelatedComponentsAt(AcideMainWindow.getInstance().getFileEditorManager().getSelectedFileEditorPanelIndex());
            AcideMainWindow.getInstance().getExplorerPanel().getPopupMenu().getRemoveFileMenuItem().setEnabled(true);
            AcideMainWindow.getInstance().getExplorerPanel().getPopupMenu().getDeleteFileMenuItem().setEnabled(true);
        } else {
            AcideMainWindow.getInstance().getExplorerPanel().getPopupMenu().getRemoveFileMenuItem().setEnabled(false);
            AcideMainWindow.getInstance().getExplorerPanel().getPopupMenu().getDeleteFileMenuItem().setEnabled(false);
        }
    }
}
