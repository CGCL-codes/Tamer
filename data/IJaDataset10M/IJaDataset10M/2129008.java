package net.sourceforge.javautil.developer.ui.command.set.ide;

import net.sourceforge.javautil.common.io.impl.SystemDirectory;
import net.sourceforge.javautil.developer.common.DeveloperContext;
import net.sourceforge.javautil.developer.common.ide.IIDEProject;
import net.sourceforge.javautil.developer.common.ide.IIDEWorkspace;
import net.sourceforge.javautil.developer.common.ide.IIntegratedDevelopmentEnvironment;
import net.sourceforge.javautil.developer.common.project.IProjectLayout;
import net.sourceforge.javautil.developer.common.project.workspace.IWorkspace;
import net.sourceforge.javautil.developer.ide.eclipse.EclipseIDE;

/**
 * {@link IIntegratedDevelopmentEnvironment} related context.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class IDEContext {

    private IIntegratedDevelopmentEnvironment<IIDEProject, IIDEWorkspace> ide = DeveloperContext.getInstance().find(IIntegratedDevelopmentEnvironment.class);

    private IIDEProject project;

    public IIntegratedDevelopmentEnvironment<IIDEProject, IIDEWorkspace> getIde() {
        return ide;
    }

    public void setIde(IIntegratedDevelopmentEnvironment<IIDEProject, IIDEWorkspace> ide) {
        this.ide = ide;
    }

    public IIDEWorkspace getWorkspace() {
        return ide.getSelectedWorkspace();
    }

    public void setWorkspace(IIDEWorkspace workspace) {
        ide.setSelectedWorkspace(workspace);
    }

    public IIDEProject getProject() {
        return project;
    }

    public void setProject(IIDEProject project) {
        this.project = project;
    }
}
