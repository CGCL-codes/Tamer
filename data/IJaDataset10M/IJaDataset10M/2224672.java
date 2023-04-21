package au.gov.naa.digipres.dpr.task.step;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import au.gov.naa.digipres.dpr.core.Constants;
import au.gov.naa.digipres.dpr.dao.DataAccessManager;
import au.gov.naa.digipres.dpr.dao.ReprocessingJobDAO;
import au.gov.naa.digipres.dpr.model.reprocessingjob.DRReprocessingJobProcessingRecord;
import au.gov.naa.digipres.dpr.model.reprocessingjob.DRReprocessingJobStopProcessingRecord;
import au.gov.naa.digipres.dpr.model.reprocessingjob.ReprocessingJob;
import au.gov.naa.digipres.dpr.model.reprocessingjob.ReprocessingJobAIP;
import au.gov.naa.digipres.dpr.model.user.User;
import au.gov.naa.digipres.dpr.task.JobProcessingTask;

public class ReprocessingJobStopProcessingDRStep extends Step {

    public static final String STEP_NAME = "Handle Stopped Reprocessing Job in DR";

    private ReprocessingJob reprocessingJob;

    private DRReprocessingJobProcessingRecord drReprocessingJobRecord;

    private DataAccessManager dataAccessManager;

    private ReprocessingJobDAO reprocessingJobDAO;

    public ReprocessingJobStopProcessingDRStep(User currentUser, JobProcessingTask task, DRReprocessingJobProcessingRecord drRecord) {
        super(currentUser, task);
        drReprocessingJobRecord = drRecord;
        reprocessingJob = drRecord.getReprocessingJob();
        dataAccessManager = task.getDPRClient().getDataAccessManager();
        reprocessingJobDAO = dataAccessManager.getReprocessingJobDAO(task);
    }

    @Override
    protected void abort() {
        throw new IllegalStateException("Can not abort this step.");
    }

    @Override
    public void failStep() {
        throw new IllegalStateException("Can not fail this step.");
    }

    @Override
    public StepResults doProcessing(ProcessingErrorHandler processingErrorHandler) {
        try {
            verifyStepState();
        } catch (StepException e) {
            throw new IllegalStateException("VerifyStepState should never fail for this step.");
        }
        fireStepProcessingBeginningEvent("Handling stopped reprocessing job");
        logger.fine("Processing of Stopped Reprocessing job beginning");
        StepResults results = new StepResults();
        if (!drReprocessingJobRecord.isStopped()) {
            dataAccessManager.beginTransaction();
            drReprocessingJobRecord.addNewStopRecord();
            reprocessingJobDAO.saveReprocessingJob(reprocessingJob);
            dataAccessManager.commitTransaction();
        }
        DRReprocessingJobStopProcessingRecord stopProcessingRecord = drReprocessingJobRecord.getDrStopProcessingRecord();
        if (stopProcessingRecord == null) {
            throw new IllegalArgumentException("Stop Processing DR Step called with a null stop processing record.");
        }
        String stopJobAction = properties.getProperty(StepProperties.DR_STOP_REPROCESSING_JOB_ACTION_PROPERTY_NAME);
        String actionReason = properties.getProperty(StepProperties.REASON_FOR_STOP_JOB_ACTION_PROPERTY_NAME);
        if (StepProperties.RESTART_JOB_OPTION.equals(stopJobAction)) {
            logger.fine("Restarting reprocessing job " + reprocessingJob.getJobNumber() + " with reason: " + actionReason);
            dataAccessManager.beginTransaction();
            stopProcessingRecord.restartInDR(actionReason, currentUser);
            reprocessingJobDAO.saveReprocessingJob(reprocessingJob);
            Iterator<Object> aipIterator = reprocessingJobDAO.getAIPsForReprocessingJob(reprocessingJob);
            fireItemProcessingBeginEvent(reprocessingJobDAO.getAIPCountForReprocessingJob(reprocessingJob));
            while (aipIterator.hasNext()) {
                logger.finest("Adding new AIP DR record.");
                ReprocessingJobAIP aip = (ReprocessingJobAIP) aipIterator.next();
                aip.addNewDRProcessingRecord();
                reprocessingJobDAO.saveAIP(aip);
                fireItemProcessEvent("Adding new DR record for AIP - " + aip.getXenaId());
            }
            fireItemProcessingCompleteEvent();
            dataAccessManager.commitTransaction();
        } else {
            String mesg = "No action provided to undertake!";
            logger.severe(mesg);
            throw new IllegalStateException("Programming error - invalid action passed to StopProcessing step.");
        }
        fireCompletedStepProcessingEvent(results);
        return results;
    }

    @Override
    public String getDescription() {
        return "Stop processing on the Digital Repository Facility";
    }

    @Override
    public Set<String> getRequiredPropertyNames() {
        Set<String> propertyList = new LinkedHashSet<String>();
        propertyList.add(StepProperties.DR_STOP_REPROCESSING_JOB_ACTION_PROPERTY_NAME);
        propertyList.add(StepProperties.REASON_FOR_STOP_JOB_ACTION_PROPERTY_NAME);
        return propertyList;
    }

    @Override
    public String getStepName() {
        return STEP_NAME;
    }

    @Override
    public String getStepStatus() {
        DRReprocessingJobStopProcessingRecord stopProcessingRecord = drReprocessingJobRecord.getDrStopProcessingRecord();
        if (stopProcessingRecord != null && Boolean.TRUE.equals(stopProcessingRecord.getRestarted())) {
            return Constants.PASSED_STATE;
        }
        return Constants.UNSTARTED_STATE;
    }
}
