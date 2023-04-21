package net.planetrenner.picit;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;

/**
 * <p>
 * Empty marker nature for PICit projects.
 * </p>
 * <p>
 * Concrete tool chain implementations should always add this nature to the project.
 * </p>
 */
public class PICitNature implements IProjectNature {

    public static final String NATURE_ID = "net.planetrenner.picit.PICitNature";

    private IProject project;

    public void configure() {
    }

    public void deconfigure() {
    }

    public IProject getProject() {
        return project;
    }

    public void setProject(IProject project) {
        this.project = project;
    }
}
