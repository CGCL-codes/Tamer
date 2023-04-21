package org.systemsbiology.apps.gui.server.executor.TransitionListValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.systemsbiology.apps.gui.client.constants.PipelineStep;
import org.systemsbiology.apps.gui.domain.ATAQSProject;
import org.systemsbiology.apps.gui.domain.TransitionListValidatorRunInfo;
import org.systemsbiology.apps.gui.domain.TransitionListValidatorSetup;
import org.systemsbiology.apps.gui.domain.User;
import org.systemsbiology.apps.gui.server.MediumBlobFile;
import org.systemsbiology.apps.gui.server.WebappConstants;
import org.systemsbiology.apps.gui.server.executor.BasicExecutor;
import org.systemsbiology.apps.gui.server.executor.TransitionListValidator.TransitionListValidatorConstants.AUDIT;
import org.systemsbiology.apps.gui.server.executor.TransitionListValidator.TransitionListValidatorConstants.OUTPUT;
import org.systemsbiology.apps.gui.server.executor.TransitionListValidator.TransitionListValidatorConstants.SRMProphet;
import org.systemsbiology.apps.gui.server.executor.TransitionListValidator.TransitionListValidatorConstants.ScoredTransitionExportApp;
import org.systemsbiology.apps.gui.server.executor.TransitionListValidator.TransitionListValidatorConstants.ValidatorApp;
import org.systemsbiology.apps.gui.server.job.CompositeJob;
import org.systemsbiology.apps.gui.server.job.JobGroup;
import org.systemsbiology.apps.gui.server.job.JobResources;
import org.systemsbiology.apps.gui.server.provider.ProviderFactory;
import org.systemsbiology.apps.gui.server.provider.location.Location;
import org.systemsbiology.apps.gui.server.provider.program.Program;
import org.systemsbiology.apps.gui.server.provider.project.SRMFile;
import org.systemsbiology.apps.gui.server.util.FileUtils;

public class TransitionListValidatorModule extends BasicExecutor {

    private TransitionListValidatorSetup setup;

    private String binDir;

    private boolean overwrite;

    public TransitionListValidatorModule(User user, ATAQSProject prj, TransitionListValidatorSetup setup, Location projLocation, String binDir, boolean overwrite) {
        super(user, prj, projLocation, PipelineStep.TRAN_LIST_VAL);
        this.setup = setup;
        this.binDir = binDir;
        this.overwrite = overwrite;
    }

    @Override
    public String getExecutorName() {
        return this.getNamePrefix() + TransitionListValidatorModule.class.getName();
    }

    @Override
    public boolean prepare() {
        log.debug("Preparing for " + this.getPipelineStep().getName());
        if (overwrite) deleteOldResults();
        String csv = this.setup.getTempTransitionFile();
        if (!isFileExist(csv)) return false;
        if (setup.getPepxmlFile() != null) {
            String pepxml = this.getPepxmlComputeServerPath();
            if (!isFileExist(pepxml)) return false;
        }
        for (TransitionListValidatorRunInfo runInfo : setup.getTlvRunInfos()) {
            List<String> lcmsFilesFullPath = getLcmsComputeServerPaths(runInfo.getSampleSetID());
            for (int i = 0; i < lcmsFilesFullPath.size(); i++) {
                String lcmsFile = lcmsFilesFullPath.get(i);
                if (!isFileExist(lcmsFile)) return false;
            }
        }
        if (!FileUtils.copyFile(csv, this.getTransitionInputPath())) return false;
        return true;
    }

    @Override
    public boolean checkOutput() {
        log.debug("check output of transition list validator");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            log.debug("Didn't work: " + e.toString());
        }
        for (OUTPUT o : OUTPUT.values()) {
            String path = o.getOutputFilePath(this.getProjLocation().pathForWebServer(), this.getNamePrefix());
            File file = new File(path);
            if (!file.exists()) {
                log.error("checkOutput:" + file.getAbsolutePath() + "is missing");
                return false;
            }
        }
        return saveOutput();
    }

    private boolean saveOutput() {
        log.debug("Save output of " + this.getPipelineStep().getName() + "to database");
        Long blobFileId;
        MediumBlobFile blobFile;
        InputStream is;
        File file;
        String path;
        for (OUTPUT o : OUTPUT.values()) {
            path = o.getOutputFilePath(this.getProjLocation().pathForWebServer(), this.getNamePrefix());
            file = new File(path);
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                log.error(e);
                return false;
            }
            blobFile = ProviderFactory.instance().getLobProvider().createMediumBlobFile(is, this.getNamePrefix() + o.toString());
            blobFileId = blobFile.getFileID();
            switch(o) {
                case SRMProphetError:
                    this.setup.setScoredTransitionFileId(blobFileId);
                    break;
                case SRMProphetAllPeakGroup:
                    this.setup.setAllPeakGroupXlsFileId(blobFileId);
                    break;
                case SRMProphetFDR:
                    this.setup.setFdrFileId(blobFileId);
                    break;
                case SRMProphetROC:
                    this.setup.setRocFileId(blobFileId);
                    break;
                case SRMProphetSEPERATION_BARS:
                    this.setup.setSeparationBarsFileId(blobFileId);
                    break;
                case AUDIT:
                    this.setup.setAuditFileId(blobFileId);
                    break;
            }
        }
        boolean isSaveSetup = ProviderFactory.instance().getProjectInfoProvider().saveTransitionListValidatorSetup(this.setup);
        return isSaveSetup;
    }

    @Override
    public void cleanUpOnCancel() {
        deleteOldResults();
    }

    @Override
    public boolean deleteOldResults() {
        log.debug("Deleting old results");
        File inputTransition = new File(this.getTransitionInputPath());
        if (inputTransition.exists() && !inputTransition.delete()) return false;
        Long blobFileId = null;
        for (OUTPUT o : OUTPUT.values()) {
            String path = o.getOutputFilePath(this.getProjLocation().pathForWebServer(), this.getNamePrefix());
            File file = new File(path);
            if (file.exists() && !file.delete()) {
                log.error("deleteOldResults: cannot delete " + file.getAbsolutePath());
                return false;
            }
            switch(o) {
                case SRMProphetError:
                    blobFileId = this.setup.getScoredTransitionFileId();
                    this.setup.setScoredTransitionFileId(null);
                    break;
                case SRMProphetAllPeakGroup:
                    blobFileId = this.setup.getAllPeakGroupXlsFileId();
                    this.setup.setAllPeakGroupXlsFileId(null);
                    break;
                case SRMProphetFDR:
                    blobFileId = this.setup.getFdrFileId();
                    this.setup.setFdrFileId(null);
                    break;
                case SRMProphetROC:
                    blobFileId = this.setup.getRocFileId();
                    this.setup.setRocFileId(null);
                    break;
                case SRMProphetSEPERATION_BARS:
                    blobFileId = this.setup.getSeparationBarsFileId();
                    this.setup.setSeparationBarsFileId(null);
                    break;
                case AUDIT:
                    blobFileId = this.setup.getAuditFileId();
                    this.setup.setAuditFileId(null);
                    break;
            }
            if (blobFileId != null) {
                MediumBlobFile blobFile = ProviderFactory.instance().getLobProvider().getMediumBlobFile(blobFileId);
                if (!ProviderFactory.instance().getLobProvider().deleteMediumBlobFile(blobFile)) return false;
            }
            blobFileId = null;
        }
        boolean isSaveSetup = ProviderFactory.instance().getProjectInfoProvider().saveTransitionListValidatorSetup(this.setup);
        if (!isSaveSetup) return false;
        return ProviderFactory.instance().getProjectInfoProvider().deleteScoredTransitions(setup.getId());
    }

    @Override
    public JobGroup getJob() {
        JobGroup jobGroup = new JobGroup();
        String workingDir = this.getProjLocation().pathForWebServer();
        String jobName = this.getNamePrefix() + "_validator";
        List<List<String>> arglist = getValidatorAppArgs();
        List<String> executables = new ArrayList<String>();
        for (int i = 0; i < arglist.size(); i++) executables.add(this.getJavaExecutable(ValidatorApp.PROGRAM.toString()));
        arglist.add(this.getScoredTransitionExportAppArgs("mprophet", OUTPUT.ScoredTransitionExportApp_MProphet));
        executables.add(this.getJavaExecutable(ScoredTransitionExportApp.PROGRAM.toString()));
        arglist.add(this.getScoredTransitionExportAppArgs("audit", OUTPUT.ScoredTransitionExportApp_Audit));
        executables.add(this.getJavaExecutable(ScoredTransitionExportApp.PROGRAM.toString()));
        arglist.add(this.getSRMProphetAppArgs());
        executables.add(this.getSRMProphetExecutable());
        arglist.add(this.getAuditAppArgs());
        executables.add(this.getAUDITExecutable());
        CompositeJob job = new CompositeJob(workingDir, executables, arglist, jobName);
        job.setJobResources(getJobResources());
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder(" ");
            for (int i = 0; i < executables.size(); i++) {
                String executable = executables.get(i);
                sb = new StringBuilder(" ");
                List<String> args = arglist.get(i);
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                log.debug(executable + sb.toString());
            }
        }
        jobGroup.addJob(job);
        return jobGroup;
    }

    private List<List<String>> getValidatorAppArgs() {
        List<List<String>> validatorAppArgs = new ArrayList<List<String>>();
        List<String> fixedArgs = new ArrayList<String>();
        fixedArgs.add(ValidatorApp.USER + this.getUserName());
        fixedArgs.add(ValidatorApp.OUTPUT_FILE + this.setup.getId().toString());
        fixedArgs.add(ValidatorApp.MODIFICATION_FILE + this.binDir + File.separatorChar + WebappConstants.MODIFICATION_XML);
        fixedArgs.add(ValidatorApp.SAVE_DATABASE + "true");
        fixedArgs.add(ValidatorApp.OVERWRITE + "true");
        fixedArgs.add(ValidatorApp.WRITER + "ataqs");
        String csv = this.getTransitionInputPath();
        fixedArgs.add(ValidatorApp.TRANSITION_INPUTS + csv);
        if (setup.getAlgorithm() != null) fixedArgs.add(ValidatorApp.ALGORITHM + setup.getAlgorithm());
        if (setup.getPepxmlFile() != null) fixedArgs.add(ValidatorApp.PEPXML + this.getPepxmlComputeServerPath());
        Map<String, String> replicateMap = new HashMap<String, String>();
        Map<String, String> biologicalCondMap = new HashMap<String, String>();
        for (TransitionListValidatorRunInfo info : this.setup.getTlvRunInfos()) {
            replicateMap.put(info.getSampleSetID(), info.getReplicateID());
            biologicalCondMap.put(info.getSampleSetID(), info.getBiologicalCondition());
        }
        StringBuilder sb = new StringBuilder();
        for (String sampleSetID : replicateMap.keySet()) {
            List<String> lcmsFilesFullPath = getLcmsComputeServerPaths(sampleSetID);
            for (int i = 0; i < lcmsFilesFullPath.size(); i++) {
                String lcms = lcmsFilesFullPath.get(i);
                if (i == lcmsFilesFullPath.size() - 1) sb.append(lcms); else sb.append(lcms).append(",");
            }
            List<String> args = new ArrayList<String>();
            args.addAll(fixedArgs);
            String lcmsFiles = sb.toString();
            args.add(ValidatorApp.LCMS_INPUTS + lcmsFiles);
            args.add(ValidatorApp.PROJECT + this.getProjectName() + "_" + sampleSetID);
            args.add(ValidatorApp.BIOLOGICAL_CONDITION + biologicalCondMap.get(sampleSetID));
            args.add(ValidatorApp.REPLICATE_ID + replicateMap.get(sampleSetID));
            args.add(ValidatorApp.RUN_NAME + sampleSetID);
            validatorAppArgs.add(args);
            sb = new StringBuilder();
        }
        return validatorAppArgs;
    }

    private List<String> getScoredTransitionExportAppArgs(String writerType, OUTPUT _enum) {
        List<String> arg = new ArrayList<String>();
        arg.add(ScoredTransitionExportApp.TLV_SETUP_ID + this.setup.getId().toString());
        arg.add(ScoredTransitionExportApp.OUTPUT_FILE + _enum.getOutputFilePath(this.getProjLocation().pathForWebServer(), this.getNamePrefix()));
        arg.add(ScoredTransitionExportApp.MODIFICATION_FILE + this.binDir + File.separatorChar + WebappConstants.MODIFICATION_XML);
        arg.add(ScoredTransitionExportApp.WRITER + writerType);
        return arg;
    }

    private List<String> getSRMProphetAppArgs() {
        List<String> arg = new ArrayList<String>();
        String dataFile = OUTPUT.ScoredTransitionExportApp_MProphet.getOutputFilePath(this.getProjLocation().pathForWebServer(), this.getNamePrefix());
        String workingDir = this.getProjLocation().pathForWebServer();
        arg.add(SRMProphet.ATAQS_BIN + this.binDir);
        arg.add(SRMProphet.DATA_FILE + dataFile);
        arg.add(SRMProphet.WORKING_DIR + workingDir);
        arg.add(SRMProphet.PROJECT + this.getNamePrefix());
        arg.add(" < " + SRMProphet.getScriptPath(this.binDir));
        return arg;
    }

    private List<String> getAuditAppArgs() {
        List<String> arg = new ArrayList<String>();
        String dataFile = OUTPUT.ScoredTransitionExportApp_Audit.getOutputFilePath(this.getProjLocation().pathForWebServer(), this.getNamePrefix());
        String outputPrefix = this.getProjLocation().pathForWebServer();
        arg.add(AUDIT.LIBDIR + this.binDir);
        arg.add(AUDIT.DATA_FILE + dataFile);
        arg.add(AUDIT.PVALUE_THRESHOLD + "0.00001");
        arg.add(AUDIT.CV_THRESHOLD + "0.2");
        arg.add(AUDIT.ALLPAIRS + "TRUE");
        arg.add(AUDIT.DEBUG_ON + "TRUE");
        arg.add(AUDIT.OUTPUTPREFIX + outputPrefix);
        arg.add(" < " + AUDIT.getScriptPath(this.binDir));
        return arg;
    }

    private String getJavaExecutable(String programName) {
        StringBuilder classPath = new StringBuilder();
        boolean isFirst = true;
        String delimiter = "-cp ";
        for (String jarfile : TransitionListValidatorConstants.JARS) {
            classPath.append(delimiter + this.binDir + File.separatorChar + jarfile);
            if (isFirst) {
                delimiter = ":";
                isFirst = false;
            }
        }
        StringBuilder systemProperties = new StringBuilder();
        for (String sp : TransitionListValidatorConstants.SYSTEM_PROPERTIES) {
            systemProperties.append(" " + sp);
        }
        String executable = "java" + systemProperties + " " + classPath + " " + programName;
        return executable;
    }

    private String getSRMProphetExecutable() {
        return SRMProphet.R.toString();
    }

    private String getAUDITExecutable() {
        return AUDIT.R.toString();
    }

    protected final String getNamePrefix() {
        StringBuffer name = new StringBuffer();
        String user = this.getUserName();
        if (user != null) name.append(user);
        name.append("_");
        name.append(getProjectName());
        return name.toString();
    }

    private List<String> getLcmsComputeServerPaths(String sampleSetName) {
        List<String> filePaths = new ArrayList<String>();
        for (TransitionListValidatorRunInfo info : this.setup.getTlvRunInfos()) {
            if (sampleSetName.equals(info.getSampleSetID())) {
                SRMFile file = new SRMFile(info.getFileName());
                filePaths.add(file.mapRemote());
            }
        }
        return filePaths;
    }

    private String getPepxmlWebServerPath() {
        SRMFile file = new SRMFile(this.setup.getPepxmlFile());
        return file.mapLocal();
    }

    private String getPepxmlComputeServerPath() {
        SRMFile file = new SRMFile(this.setup.getPepxmlFile());
        return file.mapRemote();
    }

    protected JobResources getJobResources() {
        JobResources resources = new JobResources();
        resources.setPriority(100);
        resources.setWalltime(400);
        resources.setVmem(getMaxMemSize());
        return resources;
    }

    /**
	 * Size in bytes
	 * @return long
	 */
    protected long getMaxMemSize() {
        Program program = ProviderFactory.instance().getProgramInfoProvider().getTransitionListValidatorProgram(TransitionListValidatorConstants.PROGRAM_NAME);
        long mem = program.getModuleByName(TransitionListValidatorConstants.MODULE_NAME).getResources().getMemory();
        return mem;
    }

    private static boolean isFileExist(String str) {
        File output = new File(str);
        return output.exists() && output.isFile();
    }

    public String getTransitionInputPath() {
        return this.getProjLocation().pathForWebServer() + File.separatorChar + this.getNamePrefix() + ValidatorApp.TRANSITION_INPUT_FILE;
    }
}
