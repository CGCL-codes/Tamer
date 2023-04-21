package org.in4ama.editor.xui.dialog;

import java.util.Collection;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import net.xoetrope.optional.annotation.Find;
import net.xoetrope.swing.XList;
import net.xoetrope.xui.style.XStyleFactory;
import org.apache.log4j.Logger;
import org.in4ama.documentengine.exception.InitializationException;
import org.in4ama.documentengine.exception.ProjectException;
import org.in4ama.documentengine.generator.InforamaContext;
import org.in4ama.documentengine.generator.ProjectContext;
import org.in4ama.documentengine.project.ProjectMgr;
import org.in4ama.documentengine.project.ProjectMgrConfiguration;
import org.in4ama.editor.InforamaStudioContext;
import org.in4ama.editor.project.ProjectHandler;
import org.in4ama.editor.projectdesigner.ProjectDesigner;
import org.in4ama.editor.projectdesigner.ProjectDesignerMgr;
import org.in4ama.editor.projectdesigner.localfs.LocalFsProjectPage;
import org.in4ama.editor.ui.ProjectListRenderer;

/**
 * Dialog which allows users to enter the name of a new project along with
 * it's location.
 * 
 * See the newproject.xml file for the page declaration.
 * 
 * @author Val Cassidy, Jakub Jonik
 */
public class NewProjectDialog extends BaseDialogPage {

    private static final Logger logger = Logger.getLogger(NewProjectDialog.class);

    @Find
    private XList projectTypeList;

    @Override
    public void pageActivated() {
        loadProjectTypes();
    }

    /** Fills the internal data model with the available project types. */
    private void loadProjectTypes() {
        ProjectDesignerMgr projectDesignerMgr = InforamaStudioContext.getInstance().getProjectDesignerMgr();
        DefaultListModel model = new DefaultListModel();
        projectTypeList.setModel(model);
        Collection<ProjectDesigner> designers = projectDesignerMgr.getProjectDesigners();
        for (ProjectDesigner designer : designers) {
            if (designer.getName().equals(LocalFsProjectPage.NAME)) {
                model.add(0, designer);
            } else {
                model.addElement(designer);
            }
        }
        XStyleFactory styleFact = (XStyleFactory) this.getComponentFactory();
        projectTypeList.setCellRenderer(new ProjectListRenderer(styleFact));
        projectTypeList.setSelectedIndex(0);
        projectTypeList.updateUI();
    }

    public void next() {
        try {
            ProjectDesigner designer = getSelectedProjectDesigner();
            designer.newConfiguration();
            if (designer.hasNewUI()) {
                designer.updatePanelNew();
                ProjectConfigDialogNew projectConfigDialog = (ProjectConfigDialogNew) project.getPageManager().loadPage("projectconfigdialognew");
                projectConfigDialog.setProjectDesigner(designer);
                XPageDialog dlg = new XPageDialog(project.getAppFrame());
                dialog.setVisible(false);
                dlg.showDialog(projectConfigDialog, "DT_CREATE_PROJ", 400, 410, false);
            } else if (designer.saveConfigurationNew()) {
                createProject(designer);
            }
        } catch (Exception ex) {
            logger.error(ex);
            String msg1 = ex.getMessage();
            String msg2 = translate("ST_ERROR");
            JOptionPane.showMessageDialog(this, msg1, msg2, JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Creates a new project whose configuration
	 * is stored in the current designer. */
    private void createProject(ProjectDesigner designer) throws Exception {
        String projectName = designer.getProjectName();
        ProjectMgrConfiguration configuration = designer.getConfiguration();
        ProjectContext projectContext = InforamaContext.getInstance().createProject(projectName, configuration);
        ProjectMgr projectMgr = getDefaultProjectMgr();
        designer.projectCreated(projectMgr, projectContext.getProject());
        projectContext.refresh();
        projectMgr.saveProject(projectContext.getProject());
        ProjectHandler.getInstance().projectOpened(projectName, projectContext, configuration, designer.getMRUDescription());
    }

    protected ProjectContext getProjectContext() throws InitializationException, ProjectException {
        return getDefaultProjectContext();
    }

    private ProjectDesigner getSelectedProjectDesigner() {
        return (ProjectDesigner) projectTypeList.getSelectedObject();
    }

    public void cancel() {
        dialog.setVisible(false);
    }

    public void projectTypeClicked() {
        if (wasMouseDoubleClicked()) {
            next();
        }
    }
}
