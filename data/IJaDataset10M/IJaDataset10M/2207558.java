package eu.planets_project.pp.plato.action.workflow;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import org.apache.commons.logging.Log;
import org.jboss.annotation.ejb.cache.Cache;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import eu.planets_project.pp.plato.action.interfaces.IEvaluateExperiments;
import eu.planets_project.pp.plato.action.interfaces.IRunExperiments;
import eu.planets_project.pp.plato.action.interfaces.IWorkflowStep;
import eu.planets_project.pp.plato.bean.BooleanCapsule;
import eu.planets_project.pp.plato.bean.ExperimentStatus;
import eu.planets_project.pp.plato.bean.PrepareChangesForPersist;
import eu.planets_project.pp.plato.model.Alternative;
import eu.planets_project.pp.plato.model.DetailedExperimentInfo;
import eu.planets_project.pp.plato.model.DigitalObject;
import eu.planets_project.pp.plato.model.Experiment;
import eu.planets_project.pp.plato.model.PlanState;
import eu.planets_project.pp.plato.model.PreservationActionDefinition;
import eu.planets_project.pp.plato.model.SampleObject;
import eu.planets_project.pp.plato.model.User;
import eu.planets_project.pp.plato.model.XcdlDescription;
import eu.planets_project.pp.plato.model.measurement.MeasurableProperty;
import eu.planets_project.pp.plato.model.measurement.Measurement;
import eu.planets_project.pp.plato.model.values.FreeStringValue;
import eu.planets_project.pp.plato.model.values.Value;
import eu.planets_project.pp.plato.services.PlatoServiceException;
import eu.planets_project.pp.plato.services.action.IEmulationAction;
import eu.planets_project.pp.plato.services.action.IMigrationAction;
import eu.planets_project.pp.plato.services.action.IPreservationAction;
import eu.planets_project.pp.plato.services.action.MigrationResult;
import eu.planets_project.pp.plato.services.action.PreservationActionServiceFactory;
import eu.planets_project.pp.plato.services.characterisation.DROIDIntegration;
import eu.planets_project.pp.plato.services.characterisation.FormatIdentification;
import eu.planets_project.pp.plato.services.characterisation.FormatIdentification.FormatIdentificationResult;
import eu.planets_project.pp.plato.services.characterisation.fits.FitsIntegration;
import eu.planets_project.pp.plato.services.characterisation.jhove.JHoveAdaptor;
import eu.planets_project.pp.plato.services.characterisation.xcl.XcdlExtractor;
import eu.planets_project.pp.plato.util.Downloader;
import eu.planets_project.pp.plato.util.FileUtils;
import eu.planets_project.pp.plato.util.OS;
import eu.planets_project.pp.plato.util.PlatoLogger;

/**
 * Implements actions for workflow step 'Run Experiments'
 *
 * Gives the user the opportunity to document the outcome of the experiments per alternative
 * and upload the result file. Furthermore the user can run experiments based on action services.
 *
 * @author Hannes Kulovits
 */
@Stateful
@Scope(ScopeType.SESSION)
@Name("runexperiments")
@Cache(org.jboss.ejb3.cache.NoPassivationCache.class)
public class RunExperimentsAction extends AbstractWorkflowStep implements IRunExperiments {

    private static final long serialVersionUID = 4241837780040966155L;

    protected boolean needsClearEm() {
        return true;
    }

    private static final Log log = PlatoLogger.getLogger(RunExperimentsAction.class);

    @In(create = true)
    IEvaluateExperiments evalexperiments;

    @DataModel
    List<Alternative> consideredAlternatives;

    @DataModelSelection
    Alternative selectedAlternative;

    private JHoveAdaptor jHoveAdaptor;

    @Out(required = false)
    private DigitalObject up;

    /**
     * Indicates if browse field is displayed.
     */
    @Out(required = false)
    BooleanCapsule showUpload = new BooleanCapsule(false);

    @In(required = false)
    private User user;

    @Out
    private BooleanCapsule hasAutomatedExperiments = new BooleanCapsule();

    @Out
    private List<MeasurableProperty> measurableProperties = new ArrayList<MeasurableProperty>();

    @Out(required = false)
    Alternative emulationAlternative;

    private Map<DigitalObject, String> tempFiles = new HashMap<DigitalObject, String>();

    private File tempDir = null;

    @In(required = false)
    @Out(required = false)
    private DetailedExperimentInfo selectedExperimentInfo;

    @In(required = false)
    @Out(required = false)
    private ExperimentStatus experimentStatus = new ExperimentStatus();

    private FitsIntegration fits;

    public RunExperimentsAction() {
        requiredPlanState = new Integer(PlanState.EXPERIMENT_DEFINED);
    }

    /**
     * @see AbstractWorkflowStep#getSuccessor()
     */
    protected IWorkflowStep getSuccessor() {
        return evalexperiments;
    }

    /**
     * @see AbstractWorkflowStep#save()
     */
    public String save() {
        prepareAlternatives();
        prepareTempFileSaving();
        super.save(selectedPlan.getAlternativesDefinition());
        changed = "";
        selectedPlan.getTree().initValues(selectedPlan.getAlternativesDefinition().getConsideredAlternatives(), selectedPlan.getSampleRecordsDefinition().getRecords().size());
        super.save(selectedPlan.getTree());
        doClearEm();
        init();
        return null;
    }

    public void prepareTempFileSaving() {
        for (Alternative a : selectedPlan.getAlternativesDefinition().getConsideredAlternatives()) {
            for (SampleObject so : selectedPlan.getSampleRecordsDefinition().getRecords()) {
                DigitalObject result = a.getExperiment().getResults().get(so);
                if (tempFiles.containsKey(result)) {
                    try {
                        File file = new File(tempFiles.get(result));
                        byte[] data = FileUtils.getBytesFromFile(file);
                        DigitalObject storedResult = em.merge(result);
                        storedResult.getData().setData(data);
                        a.getExperiment().getResults().put(so, storedResult);
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
            }
        }
    }

    public void prepareAlternatives() {
        PrepareChangesForPersist prep = new PrepareChangesForPersist(user.getUsername());
        for (Alternative alt : selectedPlan.getAlternativesDefinition().getAlternatives()) {
            prep.prepare(alt);
        }
    }

    protected void doClearEm() {
        OS.deleteDirectory(tempDir);
        tempFiles.clear();
        super.doClearEm();
    }

    /**
     * @see AbstractWorkflowStep#init()
     */
    public void init() {
        if (tempDir != null) {
            OS.deleteDirectory(tempDir);
        }
        tempDir = new File(OS.getTmpPath() + "digitalobjects" + System.nanoTime());
        tempDir.mkdir();
        tempDir.deleteOnExit();
        tempFiles.clear();
        log.debug("using temp directory " + tempDir.getAbsolutePath());
        try {
            fits = new FitsIntegration();
        } catch (Throwable e) {
            fits = null;
            log.error("Could not instantiate FITS, it is not configured properly.", e);
            FacesMessages.instance().add(FacesMessage.SEVERITY_WARN, "Could not instantiate FITS, it is not configured properly.");
        }
        consideredAlternatives = selectedPlan.getAlternativesDefinition().getConsideredAlternatives();
        jHoveAdaptor = new JHoveAdaptor();
        hasAutomatedExperiments.setBool(false);
        Iterator<Alternative> iter = consideredAlternatives.iterator();
        while (iter.hasNext() && !hasAutomatedExperiments.isBool()) {
            Alternative a = iter.next();
            if (a.isExecutable()) {
                hasAutomatedExperiments.setBool(true);
            }
        }
        List<SampleObject> allRecords = selectedPlan.getSampleRecordsDefinition().getRecords();
        for (Alternative alternative : consideredAlternatives) {
            Experiment exp = alternative.getExperiment();
            for (SampleObject record : allRecords) {
                DigitalObject u = exp.getResults().get(record);
                if (u == null) {
                    exp.addRecord(record);
                    u = exp.getResults().get(record);
                }
            }
        }
        showUpload.setBool(false);
        refreshMeasurableProperties();
    }

    private void refreshMeasurableProperties() {
        measurableProperties.clear();
        measurableProperties.addAll(selectedPlan.getMeasurableProperties());
        for (MeasurableProperty p : measurableProperties) {
            log.debug("prop:: " + p.getName());
        }
    }

    /**
     * @see AbstractWorkflowStep#discard()
     */
    @Override
    @RaiseEvent("reload")
    public String discard() {
        String result = super.discard();
        init();
        return result;
    }

    /**
     * @see AbstractWorkflowStep#destroy()
     */
    @Destroy
    @Remove
    public void destroy() {
    }

    /**
     * @see AbstractWorkflowStep#validate(boolean)
     */
    public boolean validate(boolean showValidationErrors) {
        return true;
    }

    /**
     * @see AbstractWorkflowStep#getWorkflowstepName()
     */
    protected String getWorkflowstepName() {
        return "runexperiments";
    }

    /**
     * Adds an error message with {@link javax.faces.application.FacesMessage#SEVERITY_ERROR}
     * to FacesMessages.
     *
     * @param message error message
     */
    private void failure(String message) {
        FacesMessages.instance().add(FacesMessage.SEVERITY_ERROR, message);
        showUpload.setBool(false);
    }

    /**
     * Removes a result file from an alternative's experiment.
     *
     * @param object {@link eu.planets_project.pp.plato.model.SampleObject}
     */
    public void removeUpload(Object object) {
        log.debug("Object: " + object);
        if (object instanceof SampleObject) {
            SampleObject sample = (SampleObject) object;
            selectedAlternative.getExperiment().getResults().put(sample, new DigitalObject());
            log.debug("File in RunExperiment removed");
            showUpload.setBool(false);
        } else {
            failure("Couldn't remove upload");
        }
    }

    /**
     * Determines upload file to a specific sample record.
     *
     * @param object sample record
     */
    public void setUpload(Object object) {
        log.debug("Number: " + object);
        if (object instanceof SampleObject) {
            SampleObject sample = (SampleObject) object;
            up = em.merge(selectedAlternative.getExperiment().getResults().get(sample));
            selectedAlternative.getExperiment().getResults().put(sample, up);
            if (up == null) {
                failure("Couldn't start upload process");
            } else {
                showUpload.setBool(true);
            }
        } else {
            failure("Couldn't start upload process");
        }
    }

    public void upload() {
        saveTempFile(up, up);
        characteriseFits(up);
        showUpload.setBool(false);
    }

    /**
     * Downloads the a digital object
     * @param object the object the user wants to download 
     */
    public void download(Object object) {
        if (object == null) {
            failure("Couldn't start download process");
        } else {
            DigitalObject up = (DigitalObject) object;
            if (tempFiles.containsKey(up)) {
                Downloader.instance().download(up, tempFiles.get(up));
            } else {
                DigitalObject u = (DigitalObject) em.merge(up);
                Downloader.instance().download(u);
            }
        }
    }

    private void runSingle(Alternative a) {
        if (!a.isExecutable()) {
            return;
        }
        IPreservationAction action = PreservationActionServiceFactory.getPreservationAction(a.getAction());
        if (action == null) {
            String msg = String.format("Preservation action %s - %s is not registered or accessible and cant be executed. (Please check the registry.)", a.getAction().getShortname(), a.getAction().getInfo());
            setUniformProgramOutput(a, msg, false);
        }
        this.changed = "T";
        PreservationActionDefinition pad = em.merge(a.getAction());
        pad.setExecute(a.getAction().isExecute());
        String settings = a.getExperiment().getSettings();
        pad.setParamByName("settings", settings);
        StringBuffer runDescription = new StringBuffer();
        if (action instanceof IMigrationAction) {
            DigitalObject migrationResultObject;
            DigitalObject experimentResultObject;
            MigrationResult migrationResult = null;
            IMigrationAction migrationAction = (IMigrationAction) action;
            SampleObject record = experimentStatus.getNextSample();
            while (record != null) {
                if (record.isDataExistent()) {
                    SampleObject objectToMigrate = em.merge(record);
                    try {
                        migrationResult = migrationAction.migrate(pad, objectToMigrate);
                    } catch (NullPointerException npe) {
                        log.error("Caught nullpointer exception when running a migration tool. ### WRONG CONFIGURATION? ###", npe);
                    } catch (Throwable t) {
                        log.error("Caught unchecked exception when running a migration tool: " + t.getMessage(), t);
                    }
                    if (migrationResult != null) {
                        if (migrationResult.isSuccessful() && migrationResult.getMigratedObject() != null) {
                            experimentResultObject = a.getExperiment().getResults().get(record);
                            migrationResultObject = migrationResult.getMigratedObject();
                            experimentResultObject.setContentType(migrationResultObject.getContentType());
                            experimentResultObject.getFormatInfo().assignValues(migrationResultObject.getFormatInfo());
                            int size = saveTempFile(experimentResultObject, migrationResultObject);
                            experimentResultObject.getData().setSize(size);
                            experimentResultObject.setFullname(migrationResultObject.getFullname());
                            characterise(experimentResultObject);
                            characteriseFits(experimentResultObject);
                            experimentResultObject.setJhoveXMLString(jHoveAdaptor.describe(tempFiles.get(experimentResultObject)));
                        }
                        extractDetailedInfos(a.getExperiment(), record, migrationResult);
                    } else {
                        DetailedExperimentInfo info = a.getExperiment().getDetailedInfo().get(record);
                        if (info == null) {
                            info = new DetailedExperimentInfo();
                            a.getExperiment().getDetailedInfo().put(record, info);
                        }
                        info.setProgramOutput(String.format("Applying action %s to sample %s failed.", a.getAction().getShortname(), record.getFullname()));
                    }
                }
                record = experimentStatus.getNextSample();
            }
        }
        refreshMeasurableProperties();
    }

    public void characterise(DigitalObject dobject) {
        FormatIdentification ident = null;
        String filename = tempFiles.get(dobject);
        if (filename == null || "".equals(filename)) {
            DigitalObject o2 = em.merge(dobject);
            try {
                ident = DROIDIntegration.getInstance().identifyFormat(o2.getData().getData(), o2.getFullname());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            ident = DROIDIntegration.getInstance().identify(filename);
        }
        if (ident == null) {
            return;
        }
        if (ident.getResult() == FormatIdentificationResult.ERROR) {
            log.error("DROID could not identify the format of the file." + ident.getInfo());
        } else if (ident.getResult() == FormatIdentificationResult.NOHIT) {
            log.info("DROID did not get a hit identifying the file. " + ident.getInfo());
        } else if ((ident.getResult() == FormatIdentificationResult.POSITIVE)) {
            dobject.getFormatInfo().assignValues(ident.getFormatHits().get(0).getFormat());
            dobject.touch();
        }
    }

    /**
     * Processes an experiment.
     *
     * @param alt alternative which in this case is a preservation action.
     */
    public void run(Object alt) {
        if (!(alt instanceof Alternative)) {
            return;
        }
        if (experimentStatus == null) {
            experimentStatus = new ExperimentStatus();
        }
        experimentStatus.experimentSetup(Arrays.asList((Alternative) alt), selectedPlan.getSampleRecordsDefinition().getRecords());
    }

    /**
     * Runs experiments of all considered alternatives.
     *
     */
    public void runAllExperiments() {
        if (experimentStatus == null) {
            experimentStatus = new ExperimentStatus();
        }
        experimentStatus.experimentSetup(consideredAlternatives, selectedPlan.getSampleRecordsDefinition().getRecords());
    }

    /**
     * runs all experiments scheduled in experimentStatus
     */
    public void startExperiments() {
        if (experimentStatus == null) {
            experimentStatus = new ExperimentStatus();
        }
        Alternative alt = experimentStatus.getNextAlternative();
        while ((alt != null) && (!experimentStatus.isCanceled())) {
            runSingle(alt);
            alt = experimentStatus.getNextAlternative();
        }
        System.gc();
    }

    /**
     * for the given alternative the program output of all experiment infos is set to <param>msg</param>.
     *  
     */
    private void setUniformProgramOutput(Alternative a, String msg, boolean successful) {
        List<SampleObject> sampleObjects = selectedPlan.getSampleRecordsDefinition().getRecords();
        for (SampleObject o : sampleObjects) {
            DetailedExperimentInfo info = a.getExperiment().getDetailedInfo().get(o);
            if (info == null) {
                info = new DetailedExperimentInfo();
                a.getExperiment().getDetailedInfo().put(o, info);
            }
            info.setProgramOutput(msg);
            info.setSuccessful(successful);
        }
    }

    /**
     * 
     * @param migratedObject the object that shall be used as KEY for storing the result bytestream
     * @param resultObject the object that contains the actual bytestream to be stored
     * @return the size of the bytestream
     */
    private int saveTempFile(DigitalObject migratedObject, DigitalObject resultObject) {
        String tempFileName = tempDir.getAbsolutePath() + "/" + System.nanoTime();
        OutputStream fileStream;
        try {
            fileStream = new BufferedOutputStream(new FileOutputStream(tempFileName));
            byte[] data = resultObject.getData().getData();
            fileStream.write(data);
            fileStream.close();
            tempFiles.put(migratedObject, tempFileName);
            return data.length;
        } catch (FileNotFoundException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
        return 0;
    }

    /**
     * stores {@link MigrationResult migration results} for the given sample object in {@link DetailedExperimentInfo experiment info} of experiment <param>e</param>. 
     */
    private void extractDetailedInfos(Experiment e, SampleObject rec, MigrationResult r) {
        DetailedExperimentInfo info = e.getDetailedInfo().get(rec);
        if (info != null) {
            info.getMeasurements().clear();
        } else {
            info = new DetailedExperimentInfo();
            e.getDetailedInfo().put(rec, info);
        }
        if (r == null) {
            return;
        }
        info.getMeasurements().putAll(r.getMeasurements());
        info.setSuccessful(r.isSuccessful());
        if (r.getReport() == null) {
            info.setProgramOutput("The tool didn't provide any output.");
        } else {
            info.setProgramOutput(r.getReport());
        }
        int sizeMigratedObject = (r.getMigratedObject() == null || r.getMigratedObject().getData() == null) ? 0 : r.getMigratedObject().getData().getSize();
        if (r.isSuccessful() && sizeMigratedObject == 0) {
            info.setSuccessful(false);
            String programOutput = info.getProgramOutput();
            programOutput += "\nSomething went wrong during migration. No result file has been generated.";
            info.setProgramOutput(programOutput);
        }
    }

    /**
     * Extracts object properties of all experiment results. 
     */
    public void extractObjectProperties() {
        List<SampleObject> records = selectedPlan.getSampleRecordsDefinition().getRecords();
        XcdlExtractor extractor = new XcdlExtractor();
        boolean missingResultfiles = false;
        ArrayList<String> failed = new ArrayList<String>();
        for (Alternative alt : consideredAlternatives) {
            for (SampleObject record : records) {
                DigitalObject result = alt.getExperiment().getResults().get(record);
                XcdlDescription xcdl = null;
                if ((result != null) && (result.isDataExistent())) {
                    try {
                        String filepath = tempFiles.get(result);
                        if ((filepath != null) && (!"".equals(filepath))) {
                            xcdl = extractor.extractProperties(result.getFullname(), filepath);
                        } else {
                            DigitalObject u = (DigitalObject) em.merge(result);
                            xcdl = extractor.extractProperties(u);
                        }
                        if (xcdl == null) {
                            failed.add(alt.getName() + ", " + record.getFullname() + ": The description service returned an empty result.");
                        }
                    } catch (PlatoServiceException e) {
                        failed.add(alt.getName() + ", " + record.getFullname() + ": " + e.getMessage());
                    }
                } else {
                    if (record.isDataExistent()) {
                        missingResultfiles = true;
                    }
                }
                result.setXcdlDescription(xcdl);
            }
        }
        if (missingResultfiles) {
            FacesMessages.instance().add(FacesMessage.SEVERITY_INFO, "Some result files could not be described, because they are missing. Please upload them first.");
        }
        if (failed.size() > 0) {
            StringBuffer msg = new StringBuffer();
            msg.append("Description failed for following result files:<br/><br/>");
            msg.append("<ul>");
            for (String f : failed) {
                msg.append("<li>").append(f).append("</li>");
            }
            msg.append("</ul>");
            FacesMessages.instance().add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Some result files could not be decribed successfully.", msg.toString()));
        }
        if ((!missingResultfiles) && (failed.size() == 0)) {
            FacesMessages.instance().add(FacesMessage.SEVERITY_INFO, "Successfully described all result files.");
        }
    }

    public void selectEmulationAlternative(Object alt) {
        emulationAlternative = (Alternative) alt;
    }

    public void runEmulation(Object rec) {
        if (rec instanceof SampleObject) {
            SampleObject sample = (SampleObject) rec;
            Alternative a = emulationAlternative;
            if (!a.isExecutable()) {
                return;
            }
            IPreservationAction action = PreservationActionServiceFactory.getPreservationAction(a.getAction());
            if (action == null) {
                String msg = String.format("Preservation action %s - %s is not registered or accessible and cant be executed. (Please check the registry.)", a.getAction().getShortname(), a.getAction().getInfo());
                setUniformProgramOutput(a, msg, false);
            }
            if (action instanceof IEmulationAction) {
                try {
                    DetailedExperimentInfo info = a.getExperiment().getDetailedInfo().get(sample);
                    String sessionID = null;
                    if (info == null) {
                        info = new DetailedExperimentInfo();
                        a.getExperiment().getDetailedInfo().put(sample, info);
                    } else {
                        Value sid = info.getMeasurements().get("sessionid").getValue();
                        if (sid != null && (sid instanceof FreeStringValue)) {
                        }
                    }
                    SampleObject objectToView = em.merge(sample);
                    byte[] b = objectToView.getData().getData();
                    if (sessionID == null) {
                        sessionID = ((IEmulationAction) action).startSession(a.getAction(), objectToView);
                    }
                    a.getExperiment().getDetailedInfo().get(sample).setSuccessful(true);
                    info.getMeasurements().put("sessionid", new Measurement("sessionID", sessionID));
                } catch (PlatoServiceException e) {
                    String errorMsg = "Could not start emulation service." + e.getMessage();
                    setUniformProgramOutput(a, errorMsg, false);
                    FacesMessages.instance().add(FacesMessage.SEVERITY_ERROR, "Could not start emulation service." + e.getMessage());
                    log.error("Could not start emulation service." + e.getMessage() + ": ", e.getCause());
                }
            }
        }
    }

    /**
     * ensures that there is a detaild experiment info per sample object
     */
    public void updateSelectedExperimentInfo(Object alternative, Object sampleObject) {
        Alternative a = (Alternative) alternative;
        SampleObject so = (SampleObject) sampleObject;
        DetailedExperimentInfo info = a.getExperiment().getDetailedInfo().get(sampleObject);
        if (info == null) {
            info = new DetailedExperimentInfo();
            a.getExperiment().getDetailedInfo().put(so, info);
        }
        this.selectedExperimentInfo = info;
        if (this.selectedExperimentInfo.getProgramOutput() == null) {
            this.selectedExperimentInfo.setProgramOutput("");
        }
    }

    public void saveDetailedExperimentInfo() {
    }

    /**
     * @param object
     * @return null
     */
    public String characteriseFits(Object object) {
        if (fits == null) {
            log.debug("FITS is not available and needs to be reconfigured.");
            return null;
        }
        if (object instanceof DigitalObject) {
            DigitalObject dObject = (DigitalObject) object;
            if (dObject != null && dObject.isDataExistent()) {
                try {
                    String fitsXML = null;
                    String filepath = tempFiles.get(dObject);
                    if ((filepath != null) && (!"".equals(filepath))) {
                        fitsXML = fits.characterise(new File(filepath));
                    } else {
                        DigitalObject mergedObj = em.merge(dObject);
                        saveTempFile(mergedObj, dObject);
                        filepath = tempFiles.get(dObject);
                        fitsXML = fits.characterise(new File(filepath));
                    }
                    dObject.setFitsXMLString(fitsXML);
                } catch (PlatoServiceException e) {
                    log.error("characterisation with FITS failed.", e);
                    return null;
                }
            }
        }
        return null;
    }
}
