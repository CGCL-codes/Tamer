package org.systemsbiology.apps.corragui.server.executor.msbid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.systemsbiology.apps.corragui.client.constants.PipelineStep;
import org.systemsbiology.apps.corragui.client.data.Status;
import org.systemsbiology.apps.corragui.domain.Location;
import org.systemsbiology.apps.corragui.domain.ProjectSetup;
import org.systemsbiology.apps.corragui.domain.User;
import org.systemsbiology.apps.corragui.server.executor.BasicExecutor;
import org.systemsbiology.apps.corragui.server.job.CorraJob;
import org.systemsbiology.apps.corragui.server.job.Job;
import org.systemsbiology.apps.corragui.server.job.JobResources;
import org.systemsbiology.apps.corragui.server.provider.location.CorraFileUtil;
import org.systemsbiology.apps.corragui.server.util.FileUtils;

public abstract class MsBidModuleExecutor extends BasicExecutor {

    private ProjectSetup setup;

    private Location projLocation;

    private String binDir;

    public MsBidModuleExecutor(User user, ProjectSetup projSetup, Location projLocation, PipelineStep step, String binDir) {
        super(user, projSetup.getProjectName(), projLocation, step);
        init(projSetup, projLocation, binDir);
    }

    public MsBidModuleExecutor(User user, ProjectSetup projSetup, Location projLocation, PipelineStep step, String binDir, boolean overwrite) {
        super(user, projSetup.getProjectName(), projLocation, step, overwrite);
        init(projSetup, projLocation, binDir);
    }

    private void init(ProjectSetup projSetup, Location projLocation, String binDir) {
        this.setup = projSetup;
        this.projLocation = projLocation;
        this.binDir = binDir;
    }

    protected List<String> getMzxmlFilesWebServer() {
        return CorraFileUtil.getMzxmlWebServerPaths(this.getProjectSetup().getCorraMzxmlFiles());
    }

    protected List<String> getMzxmlFilesComputeServer() {
        return CorraFileUtil.getMzxmlComputeServerPaths(this.getProjectSetup().getCorraMzxmlFiles());
    }

    protected final int getMzxmlFileCount() {
        return this.getProjectSetup().getMzxmlFileCount();
    }

    protected final List<String> getDataFileNames(String appendExt) {
        List<String> fileNames = CorraFileUtil.getFileNames(this.setup.getCorraMzxmlFiles());
        List<String> newExtFiles = new ArrayList<String>(fileNames.size());
        for (String file : fileNames) {
            newExtFiles.add(FileUtils.replaceExtension(file, appendExt));
        }
        return newExtFiles;
    }

    protected final String getProjectDirWebServer() {
        return this.projLocation.pathForWebServer();
    }

    protected final String getProjectDirComputeServer() {
        return this.projLocation.pathForComputeServer();
    }

    protected final ProjectSetup getProjectSetup() {
        return this.setup;
    }

    @Override
    public void saveStatus(Status status) {
    }

    protected final String getNamePrefix() {
        StringBuffer name = new StringBuffer();
        String user = setup.getUser();
        if (user != null) name.append(user);
        name.append("_");
        name.append(getProjectName());
        return name.toString();
    }

    protected String getBinDir() {
        return this.binDir;
    }

    public CorraJob getJob() {
        int jobCount = getJobCount();
        StringBuffer classPath = new StringBuffer();
        boolean isFirst = true;
        String delimiter = "-cp ";
        for (String jarfile : MsBidConstants.JAR_FILES) {
            classPath.append(delimiter + this.getBinDir() + File.separatorChar + jarfile);
            if (isFirst) {
                delimiter = ":";
                isFirst = false;
            }
        }
        String exePath = "java -Xms3g -Xms3g " + classPath + " " + getExeName();
        String baseJobName = getExecutorName();
        String logFileName = MsBidConstants.Output.LOG.toString();
        CorraJob corraJob = new CorraJob();
        for (int i = 0; i < jobCount; i++) {
            List<String> args = getJobArguments(i);
            String jobName = jobCount > 1 ? baseJobName + "_" + i : baseJobName;
            Job job = new Job(projLocation.pathForComputeServer(), exePath, args, jobName);
            job.setOutFile(logFileName, true);
            job.setJobResources(getJobResources());
            job.setLocalWorkingDir(projLocation.pathForWebServer());
            corraJob.addJob(job);
        }
        return corraJob;
    }

    protected JobResources getJobResources() {
        JobResources resources = new JobResources();
        resources.setPriority(100);
        resources.setWalltime(200);
        resources.setVmem(getMaxMemSize());
        return resources;
    }

    protected abstract int getJobCount();

    protected abstract List<String> getJobArguments(int jobIndex);

    protected abstract String getExeName();

    /**
	 * Size in bytes
	 * @return
	 */
    protected abstract long getMaxMemSize();
}
