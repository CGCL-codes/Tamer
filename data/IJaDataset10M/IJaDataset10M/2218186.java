package net.sourceforge.cruisecontrol.dashboard.web.command;

import java.io.File;
import net.sourceforge.cruisecontrol.dashboard.service.ConfigurationService;

public class DownLoadArtifactsCommand implements DownLoadFile {

    private String projectName;

    private String build;

    private String fileToBeDownloaded;

    private final ConfigurationService configuration;

    public DownLoadArtifactsCommand(ConfigurationService configuration) {
        this.configuration = configuration;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getFileToBeDownloaded() {
        return fileToBeDownloaded;
    }

    public void setFileToBeDownloaded(String fileToBeDownloaded) {
        this.fileToBeDownloaded = fileToBeDownloaded;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public File getDownLoadFile() {
        File artifactRoot = configuration.getArtifactRoot(projectName);
        File buildRoot = new File(artifactRoot, build.substring(3, 17));
        return new File(buildRoot, fileToBeDownloaded);
    }
}
