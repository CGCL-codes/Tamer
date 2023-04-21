package org.systemsbiology.apps.corragui.server.executor.specarray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.systemsbiology.apps.corragui.client.constants.PipelineStep;
import org.systemsbiology.apps.corragui.domain.FeaturePickingSetup;
import org.systemsbiology.apps.corragui.domain.Location;
import org.systemsbiology.apps.corragui.domain.Program;
import org.systemsbiology.apps.corragui.domain.ProjectSetup;
import org.systemsbiology.apps.corragui.domain.User;
import org.systemsbiology.apps.corragui.server.executor.specarray.SpecArrayConstants.Step;
import org.systemsbiology.apps.corragui.server.provider.ProviderFactory;

public class PepBof2ApmlExecutor extends SpecArrayModuleExecutor {

    protected static final String PEPBOF_FILELIST = "PepBof_fileList";

    private final FeaturePickingSetup fpSetup;

    public PepBof2ApmlExecutor(User user, ProjectSetup projSetup, FeaturePickingSetup fpSetup, Location projLocation, PipelineStep step, String binDir) {
        super(user, projSetup, projLocation, step, binDir);
        this.fpSetup = fpSetup;
    }

    public boolean prepare() {
        log.debug("Preparing for " + Step.PEPBOF2APMLCONVERTER);
        deleteOldResults();
        if (!createPepBofFileList()) return false;
        return true;
    }

    private boolean createPepBofFileList() {
        List<String> pepBofFiles = this.getDataFileNames("pepBof");
        String outFile = this.getProjectDirLocal() + File.separatorChar + pepBofListFile();
        log.debug("Creating input file: " + outFile);
        return writeListToFile(outFile, pepBofFiles);
    }

    private String pepBofListFile() {
        return PEPBOF_FILELIST;
    }

    protected int getJobCount() {
        return 1;
    }

    protected String getExeName() {
        return "PepBof2APMLConverter";
    }

    protected List<String> getJobArguments(int jobIndex) {
        List<String> args = new ArrayList<String>();
        args.add("-inputfile");
        args.add(pepBofListFile());
        args.add("-outputfile");
        args.add(fpSetup.getOutputFileName());
        return args;
    }

    public boolean checkOutput() {
        log.debug("Checking output of " + Step.PEPBOF2APMLCONVERTER);
        File apmlFile = getApmlFile();
        if (!apmlFile.exists()) return false;
        if (apmlFile.length() == 0) return false;
        return true;
    }

    private File getApmlFile() {
        return new File(this.getProjectDirLocal() + File.separatorChar + this.fpSetup.getOutputFileName());
    }

    public String getExecutorName() {
        return this.getNamePrefix() + "_PB2A";
    }

    public PipelineStep getPipelineStep() {
        return PipelineStep.FEATURE_PICKING;
    }

    @Override
    public boolean deleteOldResults() {
        File apml = getApmlFile();
        if (apml.exists()) {
            return apml.delete();
        } else return true;
    }

    @Override
    protected long getMaxMemSize() {
        Program program = ProviderFactory.instance().getProgramInfoProvider().getFeaturePickingProgram(Program.SPECARRAY);
        long mem = program.getModuleByName(Step.PEPBOF2APMLCONVERTER.toString()).getResources().getMemory();
        return mem;
    }

    @Override
    public void cleanUpOnCancel() {
        deleteOldResults();
    }
}
