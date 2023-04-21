package hudson.plugins.robotframework;

import hudson.FilePath;
import hudson.model.Project;

public class RobotFrameworkProjectAction extends RobotFrameworkAction {

    private Project<?, ?> project;

    public RobotFrameworkProjectAction(Project<?, ?> project, String testExecutionsResultPath) {
        super(testExecutionsResultPath);
        this.project = project;
    }

    public Project<?, ?> getProject() {
        return project;
    }

    @Override
    protected FilePath getReportRootDir() {
        return getRobotReportsDir(project.getWorkspace());
    }
}
