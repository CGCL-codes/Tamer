package org.radrails.rails.internal.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IStartup;
import org.radrails.rails.internal.core.RailsPlugin;

public class BadRailsNatureChecker implements IStartup {

    public void earlyStartup() {
        upgradeBadRailsNatures();
    }

    /**
	 * Fix existing rails projects who had their nature set to the bad value
	 */
    private void upgradeBadRailsNatures() {
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (int i = 0; i < projects.length; i++) {
            try {
                if (projects[i].hasNature("org.radrails.rails.ui.railsnature")) {
                    RailsPlugin.addRailsNature(projects[i], null);
                }
            } catch (CoreException e) {
            }
        }
    }
}
