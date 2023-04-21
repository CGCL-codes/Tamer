package au.gov.naa.digipres.dpr.task;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import au.gov.naa.digipres.dpr.core.Constants;
import au.gov.naa.digipres.dpr.core.DPRClient;
import au.gov.naa.digipres.dpr.dao.ConnectionException;
import au.gov.naa.digipres.dpr.dao.DataAccessManager;
import au.gov.naa.digipres.dpr.dao.TransferJobDAO;
import au.gov.naa.digipres.dpr.dao.hibernate.HibernateDataAccessManager;
import au.gov.naa.digipres.dpr.model.job.JobEncapsulator;
import au.gov.naa.digipres.dpr.model.job.JobNumber;
import au.gov.naa.digipres.dpr.model.job.JobStatus;
import au.gov.naa.digipres.dpr.model.transferjob.DataObject;
import au.gov.naa.digipres.dpr.model.transferjob.QFDataObjectProcessingRecord;
import au.gov.naa.digipres.dpr.model.transferjob.QFTransferJobProcessingRecord;
import au.gov.naa.digipres.dpr.model.transferjob.TransferJob;
import au.gov.naa.digipres.dpr.model.user.User;
import au.gov.naa.digipres.dpr.task.step.PostQuarantineStep;
import au.gov.naa.digipres.dpr.task.step.ProcessingErrorHandler;
import au.gov.naa.digipres.dpr.task.step.Step;
import au.gov.naa.digipres.dpr.task.step.StepProcessingListener;
import au.gov.naa.digipres.dpr.task.step.StepProperties;
import au.gov.naa.digipres.dpr.task.step.StepResults;
import au.gov.naa.digipres.dpr.testutils.DPRTestCase;
import au.gov.naa.digipres.dpr.testutils.TestCaseLogOnHandler;
import au.gov.naa.digipres.dpr.testutils.TestCaseStepProcessingListener;
import au.gov.naa.digipres.dpr.testutils.TestConstants;
import au.gov.naa.digipres.dpr.testutils.TestDatabaseUtils;
import au.gov.naa.digipres.dpr.testutils.TransferJobPreparator;
import au.gov.naa.digipres.dpr.util.FileUtils;
import au.gov.naa.digipres.virus.FakeScanner;

/**
 * Test case to verify the stop job task.
 * 
 * This will process the job till we get to post quarantine, at which point the stop processing
 * task will be called to restart the job (simulating a carrier failure). Processing will then resume,
 * hopefully to completion.
 * 
 * @author andy
 *
 */
public class VerifyStopJobTransferJobTaskRejectJob extends DPRTestCase {

    private static final String JOB_NUMBER_STRING = "9999/11110000";

    private JobNumber transferJobNumber;

    private int VIRUS_PORT = 9999;

    private DPRClient dprClient;

    private User currentUser;

    private DataAccessManager dataAccessManager;

    private TransferJobDAO transferJobDAO;

    String manifestFileName = TestConstants.TEST_CASE_MEDIA_LOCATION + File.separator + Constants.MANIFEST_FILENAME;

    String firstMediaLocation = TestConstants.TEST_CASE_MEDIA_LOCATION + File.separator + 1;

    private FakeScanner fakeBridge;

    /**
	 * Setup the connection to the DPRClient and the Data Access Manager, and log in the user.
	 */
    @Before
    public void setUp() throws Exception {
        FileUtils.deleteContentsOfDir(new File(TestConstants.TEST_CASE_MEDIA_LOCATION));
        FileUtils.deleteContentsOfDir(new File(TestConstants.TEST_CASE_QF_OUTPUT_CARRIER_LOCATION));
        FileUtils.deleteContentsOfDir(new File(TestConstants.TEST_CASE_DR_INPUT_CARRIER_LOCATION));
        FileUtils.deleteContentsOfDir(new File(TestConstants.TEST_CASE_REPOSITORY_LOCATION));
        TestDatabaseUtils.setUpQFTables();
        dprClient = new DPRClient();
        Logger rootLogger = Logger.getLogger(Constants.ROOT_LOGGING_PACKAGE);
        rootLogger.setLevel(Level.FINE);
        dataAccessManager = new HibernateDataAccessManager(dprClient);
        dprClient.setDataAccessManager(dataAccessManager);
        Map<String, String> connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForQF(connectionProperties);
        try {
            dprClient.connectToDataStore(connectionProperties);
        } catch (ConnectionException e) {
            e.printStackTrace();
            return;
        }
        currentUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        Task transferJobProcessingTask = dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        transferJobDAO = dataAccessManager.getTransferJobDAO(transferJobProcessingTask);
        transferJobNumber = new JobNumber(JOB_NUMBER_STRING);
        fakeBridge = new FakeScanner(false, 1, VIRUS_PORT);
        fakeBridge.startServer();
    }

    @After
    public void tearDown() throws Exception {
        dprClient.logOff(currentUser);
        dprClient.disconnectFromDataStore();
        fakeBridge.stopServer();
    }

    @Test
    public void testQFProcessingSimpleSingle() throws Exception {
        int numDataObjects = 1;
        int mediaCount = 1;
        processQFWithStop(numDataObjects, mediaCount);
        verifyQFProcessingWithStop(numDataObjects);
    }

    @Test
    public void testQFProcessingSimpleSmall() throws Exception {
        int numDataObjects = 10;
        int mediaCount = 1;
        processQFWithStop(numDataObjects, mediaCount);
        verifyQFProcessingWithStop(numDataObjects);
    }

    @Test
    public void testQFProcessingMultiMedium() throws Exception {
        int numDataObjects = 5000;
        int mediaCount = 1;
        processQFWithStop(numDataObjects, mediaCount);
        verifyQFProcessingWithStop(numDataObjects);
    }

    @Ignore
    @Test
    public void testQFProcessingMultiLargeRandom() throws Exception {
        int numDataObjects = 20000;
        int mediaCount = 1;
        processQFWithStop(numDataObjects, mediaCount);
        verifyQFProcessingWithStop(numDataObjects);
    }

    public void processQFWithStop(int numDataObjects, int mediaCount) throws Exception {
        TransferJobPreparator preparator = new TransferJobPreparator();
        preparator.setNumDataObjects(numDataObjects);
        preparator.setMediaCount(mediaCount);
        preparator.prepareTransferJob();
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        StepResults results = new StepResults();
        results.setErrorOccurred(false);
        task.setJobEncapsulator(new JobEncapsulator(new TransferJob()));
        Step step = task.getCurrentStep();
        Assert.assertNotNull(step);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date expectedDateOut = calendar.getTime();
        String expectedDateOutString = new SimpleDateFormat(Constants.DATE_FORMAT).format(expectedDateOut);
        String shortQuarantineReason = "Pressure from above.";
        String outEarlyReason = "Ministerial intervention.";
        String portString = Integer.toString(VIRUS_PORT);
        Properties stepProperties = step.getProperties();
        stepProperties.put(StepProperties.TRANSFER_JOB_NUMBER_PROPERTY_NAME, JOB_NUMBER_STRING);
        stepProperties.put(StepProperties.OUTPUT_CARRIER_LOCATION_PROPERTY_NAME, TestConstants.TEST_CASE_QF_OUTPUT_CARRIER_LOCATION);
        stepProperties.put(StepProperties.OUTPUT_CARRIER_ID_PROPERTY_NAME, TestConstants.TEST_CASE_QF_OUTPUT_CARRIER_ID);
        stepProperties.put(StepProperties.MANIFEST_FILE_NAME_PROPERTY_NAME, manifestFileName);
        stepProperties.put(StepProperties.FIRST_MEDIA_LOCATION_PROPERTY_NAME, firstMediaLocation);
        stepProperties.put(StepProperties.SCANNER_PORT_PROPERTY_NAME, portString);
        stepProperties.put(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME, expectedDateOutString);
        stepProperties.put(StepProperties.REASON_FOR_SHORT_QUARANTINE_PROPERTY_NAME, shortQuarantineReason);
        stepProperties.put(StepProperties.REASON_FOR_LEAVING_QUARANTINE_EARLY_PROPERTY_NAME, outEarlyReason);
        StepProcessingListener listener = new TestCaseStepProcessingListener();
        boolean hasBeenStopped = false;
        while (step != null) {
            step.addListener(listener);
            step.setProperties(stepProperties);
            if (step instanceof PostQuarantineStep) {
                StopJobTask stopJobTask = (StopJobTask) dprClient.getTaskByName(currentUser, StopJobTask.TASK_NAME);
                stopJobTask.rejectTransferJob(task.getJobEncapsulator(), "Records are not to be archived after all.");
                hasBeenStopped = true;
            } else {
                results = step.doProcessing(new ProcessingErrorHandler() {

                    public ProcessingErrorAction determineAction(String stepName, StepResults results) {
                        Assert.fail();
                        return ProcessingErrorAction.RESET;
                    }

                    public boolean continueDespiteError(String stepName, String errorMessage) {
                        return false;
                    }
                });
                Assert.assertTrue(!results.isErrorOccurred());
            }
            step = task.getCurrentStep();
            if (hasBeenStopped) {
                Assert.assertTrue(step == null);
            }
        }
    }

    public void verifyQFProcessingWithStop(int numDataObjects) {
        TransferJob transferJob = transferJobDAO.getTransferJobByJobNumber(transferJobNumber);
        Assert.assertTrue(transferJob.getManifestFileChecksum() != null);
        Assert.assertTrue(transferJob.getCreatedBy().equals(currentUser));
        Assert.assertTrue(transferJob.getDateCreated() != null);
        Assert.assertTrue(transferJob.getJobStatus().equals(JobStatus.REJECTED_IN_QF));
        Assert.assertTrue(transferJob.getNumDataObjects() == numDataObjects);
        Assert.assertTrue(transferJob.getQfProcessingRecords().size() == 1);
        QFTransferJobProcessingRecord qfRecord = transferJob.getMostRecentQFRecord();
        Assert.assertNotNull(qfRecord.getCarrierDeviceLocation());
        Assert.assertNotNull(qfRecord.getCarrierDeviceId());
        Assert.assertNotNull(qfRecord.getManifestReadBy());
        Assert.assertNotNull(qfRecord.getManifestReadDate());
        Assert.assertNotNull(qfRecord.getManifestReadStatus());
        Assert.assertNotNull(qfRecord.getPreQuarantineProcessingBy());
        Assert.assertNotNull(qfRecord.getPreQuarantineProcessingDate());
        Assert.assertNotNull(qfRecord.getPreQuarantineProcessingStatus());
        Assert.assertNotNull(qfRecord.getPreQuarantineVirusCheckDefinitionsCurrent());
        Assert.assertNotNull(qfRecord.getPreQuarantineVirusCheckDefinitionsVersion());
        Assert.assertNotNull(qfRecord.getPreQuarantineVirusCheckerName());
        Assert.assertNotNull(qfRecord.getPreQuarantineVirusCheckerVersion());
        Assert.assertNotNull(qfRecord.getIntoQuarantineBy());
        Assert.assertNotNull(qfRecord.getIntoQuarantineDate());
        Assert.assertNotNull(qfRecord.getQuarantinePeriodReason());
        Assert.assertNotNull(qfRecord.getExpectedDateOutOfQuarantine());
        Assert.assertNull(qfRecord.getOutOfQuarantineBy());
        Assert.assertNull(qfRecord.getOutOfQuarantineDate());
        Assert.assertNull(qfRecord.getOutOfQuarantineEarlyReason());
        Assert.assertNull(qfRecord.getPostQuarantineProcessingBy());
        Assert.assertNull(qfRecord.getPostQuarantineProcessingDate());
        Assert.assertEquals(qfRecord.getPostQuarantineProcessingStatus(), Constants.UNSTARTED_STATE);
        Assert.assertNull(qfRecord.getPostQuarantineVirusCheckDefinitionsCurrent());
        Assert.assertNull(qfRecord.getPostQuarantineVirusCheckDefinitionsVersion());
        Assert.assertNull(qfRecord.getPostQuarantineVirusCheckerName());
        Assert.assertNull(qfRecord.getPostQuarantineVirusCheckerVersion());
        Assert.assertNull(qfRecord.getDateMarkedCompleted());
        Assert.assertNull(qfRecord.getMarkedCompleted());
        Assert.assertNull(qfRecord.getMarkedCompletedBy());
        Assert.assertTrue(transferJob.getMostRecentQFRecord().isRejected());
    }

    public void verifyDataObjects(int numDataObjects) {
        int dataObjectCount = 0;
        TransferJob transferJob = transferJobDAO.getTransferJobByJobNumber(transferJobNumber);
        QFTransferJobProcessingRecord qfRecord = transferJob.getMostRecentQFRecord();
        Iterator<Object> qfDataObjectRecordIterator = transferJobDAO.getDataObjectQFRecords(qfRecord);
        while (qfDataObjectRecordIterator.hasNext()) {
            QFDataObjectProcessingRecord qfDORecord = (QFDataObjectProcessingRecord) qfDataObjectRecordIterator.next();
            Assert.assertTrue(Boolean.TRUE.equals(qfDORecord.getPostQuarantineChecksumPassed()));
            Assert.assertTrue(Boolean.TRUE.equals(qfDORecord.getPostQuarantineVirusCheckPassed()));
            Assert.assertTrue(Boolean.TRUE.equals(qfDORecord.getPreQuarantineChecksumPassed()));
            Assert.assertTrue(Boolean.TRUE.equals(qfDORecord.getPreQuarantineVirusCheckPassed()));
            DataObject dataObject = qfDORecord.getDataObject();
            Assert.assertNotNull(dataObject.getAlgorithm());
            Assert.assertNotNull(dataObject.getChecksum());
            Assert.assertNotNull(dataObject.getDateLastModified());
            dataObjectCount++;
        }
        Assert.assertTrue(dataObjectCount == numDataObjects);
    }
}
