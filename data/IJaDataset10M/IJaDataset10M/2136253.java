package org.maven.ide.eclipse.ext.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IObjectActionDelegate;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Action which executes a FindBugs goal <code>findbugs:findbugs</code></p>
 * 
 * @see AbstractMavenLifecycleAction
 * @see IObjectActionDelegate
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class FindBugsMavenAction extends AbstractMavenLifecycleAction implements IObjectActionDelegate {

    /**
	 * <p>Goal of FindBugs mojo <code>findbugs:findbugs</code></p>
	 * 
	 * @see org.maven.ide.eclipse.ext.actions.AbstractMavenLifecycleAction#getGoal()
	 */
    protected String getGoal() {
        return "findbugs:findbugs";
    }

    /**
	 * <p>Just an info for the task executed</p>
	 * 
	 * @see org.maven.ide.eclipse.ext.actions.AbstractMavenLifecycleAction#getName(org.eclipse.core.resources.IProject)
	 */
    protected String getName(final IProject p) {
        return "Find bugs in " + p.getName() + " project";
    }
}
