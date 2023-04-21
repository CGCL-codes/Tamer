package net.sourceforge.cruisecontrol.dashboard;

import java.io.File;
import net.sourceforge.cruisecontrol.dashboard.utils.functors.CCProjectFolderFilter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Projects {

    private File logRoot;

    private File artifacts;

    private String[] projectNames;

    public Projects(File logRoot, File artifacts, String[] projectNames) {
        this.projectNames = projectNames;
        this.logRoot = logRoot;
        this.artifacts = artifacts;
    }

    public File getArtifactRoot(String projectName) {
        return new File(artifacts, projectName);
    }

    public File getLogRoot(String projectName) {
        return new File(logRoot, projectName);
    }

    public File getLogRoot() {
        return logRoot;
    }

    public File[] getProjectsFromFileSystem() {
        return getLogRoot().listFiles(new CCProjectFolderFilter());
    }

    public boolean hasProject(String projectName) {
        return ArrayUtils.indexOf(projectNames, projectName) > -1;
    }

    public File[] getProjectsRegistedInBuildLoop() {
        File[] projects = new File[projectNames.length];
        for (int j = 0; j < projectNames.length; j++) {
            projects[j] = getLogRoot(projectNames[j]);
        }
        return projects;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
