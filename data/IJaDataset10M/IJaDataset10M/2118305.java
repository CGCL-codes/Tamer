package net.sourceforge.javautil.developer.common.project.version.control;

import net.sourceforge.javautil.developer.common.project.IProject;
import net.sourceforge.javautil.developer.common.project.IProjectNature;
import net.sourceforge.javautil.developer.common.project.workspace.IWorkspaceNature;

/**
 * This will form the contract for natures that detect their use in relation to a {@link IProject}
 * and from which they can be used to interact with the {@link IVersionControlSystem}.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface IVersionControlNature extends IWorkspaceNature {

    /**
	 * @return The version control system related to this project.
	 */
    IVersionControlSystem getVCS();

    /**
	 * @return Information about this project/working copy
	 */
    IWorkingCopyInformation getWorkingCopyInformation();
}
