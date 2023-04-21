package org.jcvi.vapor.grid;

import org.ggf.drmaa.DrmaaException;
import org.ggf.drmaa.JobInfo;
import org.jcvi.vapor.Command;
import org.jcvi.vapor.SampleTarget;
import org.jcvi.vapor.VaporConfig;

/**
 *
 *
 * @author jsitz@jcvi.org
 */
public class GridAssemblyJob extends GridJob {

    private final SampleTarget target;

    public GridAssemblyJob(Command asmCommand, SampleTarget target, VaporConfig config) throws DrmaaException {
        super(asmCommand, config.get("project.code"));
        this.target = target;
        this.setName("FLAP_" + this.target.getReference());
        this.setArchitecture("lx*");
        this.setArchitecture(config.get("executive.grid.architecture"));
        if (config.getBoolean("executive.grid.msc")) {
            this.setNativeSpec("-l msc");
        }
        if (config.getBoolean("executive.grid.fast")) {
            this.setNativeSpec("-l fast");
        }
        this.setWorkingDirectory(asmCommand.getWorkingDir());
    }

    @Override
    public int postScheduling() {
        super.postScheduling();
        this.target.getLogger().info("Started grid job " + this.getJobID() + ".");
        return 0;
    }

    @Override
    public int postExecution(JobStatus finalStatus, JobInfo runInfo) throws DrmaaException {
        super.postExecution(finalStatus, runInfo);
        this.target.getLogger().info("Grid job " + runInfo.getJobId() + " completed.");
        if (runInfo.hasSignaled()) {
            this.target.addMessage("Assembly failed on signal " + runInfo.getTerminatingSignal() + " (Grid Job " + runInfo.getJobId() + ")");
            this.target.declareFailed();
            return 1;
        } else if (runInfo.hasExited() && (runInfo.getExitStatus() != 0)) {
            this.target.addMessage("Assembly failed: Exit code " + runInfo.getExitStatus() + " (Grid Job " + runInfo.getJobId() + ")");
            this.target.declareFailed();
            return 1;
        } else return 0;
    }
}
