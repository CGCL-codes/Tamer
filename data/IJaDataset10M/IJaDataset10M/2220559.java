package jSimMacs.gromacsrun;

import jSimMacs.data.Project;
import jSimMacs.data.RemoteProject;
import jSimMacs.logic.ProjectLocation;

/**
 * @author Sanjit Roopra
 *
 */
public class GromacsRunFactory {

    public static GromacsRun getGromacsRun(ProjectLocation location, Project project) {
        switch(location) {
            case LOCALE:
                return new LocalGromacsRun(project);
            case SSH:
                return new SSHGromacsRuns((RemoteProject) project);
        }
        return null;
    }
}
