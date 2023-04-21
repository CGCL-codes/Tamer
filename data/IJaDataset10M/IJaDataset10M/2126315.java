package net.sourceforge.cruisecontrol.events;

import java.util.EventObject;
import net.sourceforge.cruisecontrol.Project;
import net.sourceforge.cruisecontrol.ProjectState;

/**
 * The BuildProgressEvent is fired just before each step of the build loop.
 *
 * @author <a href="mailto:michael@insightfulminds.com">Michael Beauregard</a>
 * @author $Author: pauljulius $
 */
public class BuildProgressEvent extends EventObject {

    private final ProjectState state;

    /**
     * Constructs the event indicating the state of the build.
     *
     * @param project the project whose build loop has progressed
     * @param state indicates which step the build loop is in
     */
    public BuildProgressEvent(Project project, ProjectState state) {
        super(project);
        this.state = state;
    }

    public Project getProject() {
        return (Project) getSource();
    }

    public ProjectState getState() {
        return state;
    }

    public String toString() {
        return getProject().getName() + ": " + state.getName();
    }
}
