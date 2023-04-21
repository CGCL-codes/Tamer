package org.SCAraide.GUI.UI;

import java.awt.Image;
import javax.swing.JToolBar;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreePath;
import org.SCAraide.GUI.Model.ProjectsTreeModel.TreeNode;
import org.SCAraide.GUI.Model.SCAProject;
import org.SCAraide.GUI.SCAraideApp;
import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author jaydee
 */
public class ToolBarProject extends JToolBar {

    private JButton btnOpenProject;

    private JButton btnCloseProject;

    private JButton btnCreateProject;

    public ToolBarProject(String name, int orientation) {
        super(name, orientation);
        init();
    }

    public ToolBarProject(String name) {
        super(name);
        init();
    }

    public ToolBarProject(int orientation) {
        super(orientation);
        init();
    }

    public ToolBarProject() {
        init();
    }

    protected void init() {
        ApplicationActionMap actionMap = SCAraideApp.getApplication().getContext().getActionMap(ToolBarProject.class, this);
        ResourceMap resourceMap = SCAraideApp.getApplication().getContext().getResourceMap(ToolBarProject.class);
        btnOpenProject = new JButton(actionMap.get("loadProject"));
        btnOpenProject.setIcon(new ImageIcon(resourceMap.getImageIcon("loadProject.Action.icon").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        btnOpenProject.setHideActionText(true);
        btnOpenProject.setFocusable(false);
        add(btnOpenProject);
        btnCloseProject = new JButton(actionMap.get("closeProject"));
        btnCloseProject.setIcon(new ImageIcon(resourceMap.getImageIcon("closeProject.Action.icon").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        btnCloseProject.setHideActionText(true);
        btnCloseProject.setFocusable(false);
        add(btnCloseProject);
        btnCreateProject = new JButton(actionMap.get("createProject"));
        btnCreateProject.setIcon(new ImageIcon(resourceMap.getImageIcon("createProject.Action.icon").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        btnCreateProject.setFocusable(false);
        add(btnCreateProject);
    }

    @Action
    public void loadProject() {
        JFileChooser fileChooser = new JFileChooser("/home/jaydee/Projets/SCAraide/samples");
        fileChooser.setFileFilter(new FileNameExtensionFilter("SCA project file", "xml"));
        fileChooser.setMultiSelectionEnabled(true);
        if (fileChooser.showOpenDialog(SCAraideApp.getView().getComponent()) == JFileChooser.APPROVE_OPTION) {
            SCAraideApp.getController().loadProject(fileChooser.getSelectedFiles());
        }
    }

    @Action
    public void closeProject() {
        Vector<TreeNode> projectsToClose = new Vector<TreeNode>();
        TreePath selected[] = SCAraideApp.getView().getProjectsTree().getSelectionPaths();
        if (selected != null) {
            for (TreePath path : selected) {
                for (Object nodeObject : path.getPath()) {
                    TreeNode node = (TreeNode) nodeObject;
                    if (node.getUserObject() instanceof SCAProject) {
                        if (!projectsToClose.contains(node)) {
                            projectsToClose.add(node);
                        }
                    }
                }
            }
            SCAraideApp.getController().closeProject(projectsToClose.toArray(new TreeNode[0]));
        }
    }

    @Action
    public void createProject() {
    }
}
