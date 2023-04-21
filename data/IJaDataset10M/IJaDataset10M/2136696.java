package org.objectstyle.wolips.datasets.listener;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.objectstyle.wolips.datasets.adaptable.Project;
import org.objectstyle.wolips.datasets.adaptable.ProjectPatternsets;

/**
 * @author ulrich
 */
public class PatternsetDeltaVisitor extends DefaultDeltaVisitor {

    private boolean needsFurtherInvestigation = true;

    public boolean visit(IResourceDelta delta) throws CoreException {
        if (!this.needsFurtherInvestigation) {
            return false;
        }
        if (!super.visit(delta)) {
            return false;
        }
        IResource resource = delta.getResource();
        if (resource.getType() == IResource.PROJECT) {
            return true;
        }
        if (resource.getType() == IResource.FOLDER && ProjectPatternsets.ANT_FOLDER_NAME.equals(resource.getName())) {
            return true;
        }
        if (ProjectPatternsets.EXTENSION.equals(resource.getFileExtension())) {
            IProject iProject = resource.getProject();
            Project project = (Project) iProject.getAdapter(Project.class);
            project.releasePatternsetCache();
            this.needsFurtherInvestigation = false;
            project.fullBuildRequired = true;
            return false;
        }
        return false;
    }

    /**
	 * 
	 */
    public void reset() {
        needsFurtherInvestigation = true;
    }
}
