package org.in4ama.editor.xui.dialog;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.xoetrope.optional.annotation.Find;
import net.xoetrope.swing.XPanel;
import org.apache.log4j.Logger;
import org.in4ama.documentengine.compile.ProjectCompiler;
import org.in4ama.documentengine.generator.InforamaContext;
import org.in4ama.documentengine.project.ProjectMgr;
import org.in4ama.documentengine.project.ProjectMgrConfiguration;
import org.in4ama.editor.projectdesigner.ProjectDesigner;

/** Dialog for configuring projects. */
public class ProjectConfigDialogImport extends BaseDialogPage {

    private static final Logger logger = Logger.getLogger(ProjectConfigDialogNew.class);

    @Find
    private XPanel cardPanel;

    private ProjectDesigner designer;

    private File archiveFile;

    @Override
    public void pageActivated() {
        cardPanel.removeAll();
        if (designer != null) {
            JPanel panel = designer.getPanelNew();
            cardPanel.add(panel, designer.getType());
            panel.setVisible(true);
        }
    }

    public void importProject() {
        try {
            if (designer.saveConfigurationNew()) {
                importProject(designer);
                dialog.setVisible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String msg1 = ex.getMessage();
            String msg2 = translate("ST_ERROR");
            JOptionPane.showMessageDialog(this, msg1, msg2, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setArchiveFile(File archiveFile) {
        this.archiveFile = archiveFile;
    }

    /** Imports a new project whose configuration
	 * is stored in the current designer. */
    private void importProject(ProjectDesigner designer) throws Exception {
        InforamaContext ctx = InforamaContext.getInstance();
        ProjectMgrConfiguration configuration = designer.getConfiguration();
        ProjectMgr projectMgr = ctx.getProjectMgr(configuration);
        String projectName = designer.getProjectName();
        ProjectCompiler.importProject(ctx, projectMgr, archiveFile, projectName);
    }

    public void cancel() {
        dialog.setVisible(false);
    }

    public void setProjectDesigner(ProjectDesigner designer) {
        this.designer = designer;
    }

    public ProjectDesigner getProjectDesigner() {
        return designer;
    }
}
