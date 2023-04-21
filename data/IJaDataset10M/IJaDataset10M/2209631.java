package org.systemsbiology.apps.corragui.server.executor.specarray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.systemsbiology.apps.corragui.client.constants.PipelineStep;
import org.systemsbiology.apps.corragui.domain.AlignmentSetup;
import org.systemsbiology.apps.corragui.domain.Location;
import org.systemsbiology.apps.corragui.domain.Program;
import org.systemsbiology.apps.corragui.domain.ProjectSetup;
import org.systemsbiology.apps.corragui.domain.User;
import org.systemsbiology.apps.corragui.server.executor.specarray.SpecArrayConstants.Step;
import org.systemsbiology.apps.corragui.server.provider.ProviderFactory;

public class PepMatchExecutor extends SpecArrayModuleExecutor {

    private final AlignmentSetup alignSetup;

    public PepMatchExecutor(User user, ProjectSetup projSetup, AlignmentSetup alignSetup, Location projLocation, PipelineStep step, String binDir) {
        super(user, projSetup, projLocation, step, binDir);
        this.alignSetup = alignSetup;
    }

    public boolean prepare() {
        log.debug("Preparing for " + Step.PEPMATCH);
        deleteOldResults();
        return true;
    }

    protected int getJobCount() {
        return 1;
    }

    protected String getExeName() {
        return "PepMatch";
    }

    protected List<String> getJobArguments(int jobIndex) {
        List<String> args = new ArrayList<String>();
        args.add("-inputfile");
        List<String> pepBofFiles = this.getDataFileNames("pepBof");
        for (String file : pepBofFiles) {
            args.add(file);
        }
        args.add("-outputfile");
        args.add(pepBofOutFile());
        args.add("-paramfile");
        args.add(paramOutFile());
        return args;
    }

    protected String pepBofOutFile() {
        return this.getProjectName() + ".pepBof";
    }

    protected String paramOutFile() {
        return this.getProjectName() + ".param.tmp";
    }

    public boolean checkOutput() {
        log.debug("Checking output of " + Step.PEPMATCH);
        File outFile = new File(this.getProjectDirLocal() + File.separatorChar + pepBofOutFile());
        if (!outFile.exists()) return false;
        if (outFile.length() == 0) return false;
        outFile = new File(this.getProjectDirLocal() + File.separatorChar + paramOutFile());
        if (!outFile.exists()) return false;
        if (outFile.length() == 0) return false;
        return true;
    }

    public String getExecutorName() {
        return this.getNamePrefix() + "_PM";
    }

    @Override
    public boolean deleteOldResults() {
        log.debug("Deleting old pepmatch output");
        boolean allDeleted = true;
        File outFile = new File(this.getProjectDirLocal() + File.separatorChar + pepBofOutFile());
        if (outFile.exists() && !outFile.delete()) {
            log.error("Old output file: " + outFile.getAbsolutePath() + " could not be deleted");
            allDeleted = false;
        }
        outFile = new File(this.getProjectDirLocal() + File.separatorChar + paramOutFile());
        if (outFile.exists() && !outFile.delete()) {
            log.error("Old output file: " + outFile.getAbsolutePath() + " could not be deleted");
            allDeleted = false;
        }
        return allDeleted;
    }

    @Override
    protected long getMaxMemSize() {
        Program program = ProviderFactory.instance().getProgramInfoProvider().getAlignmentProgram(Program.SPECARRAY);
        long mem = program.getModuleByName(Step.PEPMATCH.toString()).getResources().getMemory();
        return mem;
    }

    @Override
    public void cleanUpOnCancel() {
        deleteOldResults();
    }
}
