package au.gov.naa.digipres.dpr.stopprocessing;

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
import au.gov.naa.digipres.dpr.core.checksum.ChecksumResultType;
import au.gov.naa.digipres.dpr.dao.ConnectionException;
import au.gov.naa.digipres.dpr.dao.DataAccessManager;
import au.gov.naa.digipres.dpr.dao.TransferJobDAO;
import au.gov.naa.digipres.dpr.dao.hibernate.HibernateDataAccessManager;
import au.gov.naa.digipres.dpr.model.job.JobEncapsulator;
import au.gov.naa.digipres.dpr.model.job.JobNumber;
import au.gov.naa.digipres.dpr.model.job.JobStatus;
import au.gov.naa.digipres.dpr.model.transferjob.DataObject;
import au.gov.naa.digipres.dpr.model.transferjob.PFStopProcessingRecord;
import au.gov.naa.digipres.dpr.model.transferjob.PFTransferJobProcessingRecord;
import au.gov.naa.digipres.dpr.model.transferjob.QFTransferJobProcessingRecord;
import au.gov.naa.digipres.dpr.model.transferjob.TransferJob;
import au.gov.naa.digipres.dpr.model.user.User;
import au.gov.naa.digipres.dpr.task.AdministerTransferJobTask;
import au.gov.naa.digipres.dpr.task.Task;
import au.gov.naa.digipres.dpr.task.TransferJobProcessingTask;
import au.gov.naa.digipres.dpr.task.step.PFChecksumValidationStep;
import au.gov.naa.digipres.dpr.task.step.ProcessingErrorHandler;
import au.gov.naa.digipres.dpr.task.step.Step;
import au.gov.naa.digipres.dpr.task.step.StepProcessingListener;
import au.gov.naa.digipres.dpr.task.step.StepProperties;
import au.gov.naa.digipres.dpr.task.step.StepResults;
import au.gov.naa.digipres.dpr.testutils.DPRTestCase;
import au.gov.naa.digipres.dpr.testutils.TestCaseImportExportListener;
import au.gov.naa.digipres.dpr.testutils.TestCaseLogOnHandler;
import au.gov.naa.digipres.dpr.testutils.TestCaseStepProcessingListener;
import au.gov.naa.digipres.dpr.testutils.TestConstants;
import au.gov.naa.digipres.dpr.testutils.TestDatabaseUtils;
import au.gov.naa.digipres.dpr.testutils.TransferJobGenerator;
import au.gov.naa.digipres.dpr.testutils.TransferJobValidator;
import au.gov.naa.digipres.dpr.util.FileUtils;
import au.gov.naa.digipres.virus.FakeScanner;

/**
 * The basic gist of this test case will be to create a job on QF, import it into
 * the PF facility, Stop the job, log back onto QF, 'reprocess' the job, bring it back
 * onto PF again, and go all the way through.
 * 
 * Whew. Should be a good one!
 */
public class VerifyStopProcessingOnPFSendForReprocessing extends DPRTestCase {

    private DPRClient dprClient;

    private DataAccessManager dataAccessManager;

    private User currentUser;

    private static final String JOB_NUMBER_STRING = "9999/77772222";

    private JobNumber transferJobNumber;

    private static final int VIRUS_PORT = 9999;

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
        TestDatabaseUtils.setUpPFTables();
        fakeBridge = new FakeScanner(false, 1, VIRUS_PORT);
        fakeBridge.startServer();
        transferJobNumber = new JobNumber(JOB_NUMBER_STRING);
    }

    @After
    public void tearDown() throws Exception {
        fakeBridge.stopServer();
        if (dprClient != null && dprClient.isLoggedOn()) {
            dprClient.logOff(currentUser);
            dprClient.disconnectFromDataStore();
        }
    }

    @Test
    public void testSimpleSingle() throws Exception {
        int numDataObjects = 1;
        processOnQFthenStopOnPFandReprocessOnQF(numDataObjects);
    }

    @Test
    public void testSimple() throws Exception {
        int numDataObjects = 20;
        processOnQFthenStopOnPFandReprocessOnQF(numDataObjects);
    }

    @Test
    public void testSimpleSmall() throws Exception {
        int numDataObjects = 250;
        processOnQFthenStopOnPFandReprocessOnQF(numDataObjects);
    }

    @Test
    @Ignore
    public void testSimpleMedium() throws Exception {
        int numDataObjects = 10000;
        processOnQFthenStopOnPFandReprocessOnQF(numDataObjects);
    }

    private void processOnQFthenStopOnPFandReprocessOnQF(int numDataObjects) throws Exception {
        try {
            createJobOnQF(numDataObjects);
            processOnPFUntilStop();
            reprocessOnQF();
            importAndProcessOnPF();
            verifyTransferJobOnPF(numDataObjects);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void createJobOnQF(int numDataObjects) throws Exception {
        dprClient = new DPRClient();
        Logger rootLogger = Logger.getLogger(Constants.ROOT_LOGGING_PACKAGE);
        rootLogger.setLevel(Level.FINE);
        dataAccessManager = new HibernateDataAccessManager(dprClient);
        dprClient.setDataAccessManager(dataAccessManager);
        Map<String, String> connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForQF(connectionProperties);
        dprClient.connectToDataStore(connectionProperties);
        currentUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        Task transferJobProcessingTask = dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO qfTransferJobDAO = dataAccessManager.getTransferJobDAO(transferJobProcessingTask);
        TransferJobGenerator generator = new TransferJobGenerator(dataAccessManager, qfTransferJobDAO);
        generator.setNumDataObjects(numDataObjects);
        generator.setInputCarrierLocation(TestConstants.TEST_CASE_QF_OUTPUT_CARRIER_LOCATION);
        generator.setJobNumber(transferJobNumber);
        TransferJob transferJob = generator.generateTransferJob(JobStatus.QF_PROCESSING_COMPLETE, currentUser);
        transferJob.getMostRecentQFRecord().setQuarantinePeriodReason("short for testing.");
        transferJob.getMostRecentQFRecord().setOutOfQuarantineEarlyReason("out of early reason.");
        qfTransferJobDAO.saveTransferJob(transferJob);
        AdministerTransferJobTask adminTask = (AdministerTransferJobTask) dprClient.getTaskByName(currentUser, AdministerTransferJobTask.NAME);
        File exportFile = new File(getQFExportFileName());
        adminTask.serialiseTransferJob(transferJob, exportFile, null);
        dprClient.logOff(currentUser);
        dprClient.disconnectFromDataStore();
    }

    private void processOnPFUntilStop() throws Exception {
        dprClient = new DPRClient();
        Logger rootLogger = Logger.getLogger(Constants.ROOT_LOGGING_PACKAGE);
        rootLogger.setLevel(Level.FINE);
        dataAccessManager = new HibernateDataAccessManager(dprClient);
        dprClient.setDataAccessManager(dataAccessManager);
        Map<String, String> connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForPF(connectionProperties);
        dprClient.connectToDataStore(connectionProperties);
        currentUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        AdministerTransferJobTask adminTask = (AdministerTransferJobTask) dprClient.getTaskByName(currentUser, AdministerTransferJobTask.NAME);
        File importFile = new File(getQFExportFileName());
        adminTask.importTransferJob(importFile, new TestCaseImportExportListener());
        TransferJobProcessingTask transferJobProcessingTask = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO pfTransferJobDAO = dataAccessManager.getTransferJobDAO(transferJobProcessingTask);
        TransferJob transferJob = pfTransferJobDAO.getTransferJobByJobNumber(transferJobNumber);
        transferJobProcessingTask.setJobEncapsulator(new JobEncapsulator(transferJob));
        Step currentStep = transferJobProcessingTask.getCurrentStep();
        Assert.assertTrue(currentStep instanceof PFChecksumValidationStep);
        Properties stepProperties = currentStep.getProperties();
        stepProperties.setProperty(StepProperties.OUTPUT_CARRIER_ID_PROPERTY_NAME, TestConstants.TEST_CASE_PF_OUTPUT_CARRIER_ID);
        stepProperties.setProperty(StepProperties.OUTPUT_CARRIER_LOCATION_PROPERTY_NAME, TestConstants.TEST_CASE_PF_OUTPUT_CARRIER_LOCATION);
        int stepsToProcess = 2;
        while (stepsToProcess-- > 0) {
            currentStep.setProperties(stepProperties);
            currentStep.addListener(new TestCaseStepProcessingListener(true));
            currentStep.doProcessing(new ProcessingErrorHandler() {

                public ProcessingErrorAction determineAction(String stepName, StepResults results) {
                    return ProcessingErrorAction.RESET;
                }

                public boolean continueDespiteError(String stepName, String errorMessage) {
                    return false;
                }
            });
            currentStep = transferJobProcessingTask.getCurrentStep();
        }
        adminTask.sendTransferJobForReprocessing(transferJob);
        dprClient.logOff(currentUser);
        dprClient.disconnectFromDataStore();
    }

    private void reprocessOnQF() throws Exception {
        FileUtils.deleteContentsOfDir(new File(TestConstants.TEST_CASE_QF_OUTPUT_CARRIER_LOCATION));
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
            throw e;
        }
        currentUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        Task transferJobProcessingTask = dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO qfTransferJobDAO = dataAccessManager.getTransferJobDAO(transferJobProcessingTask);
        TransferJob transferJob = qfTransferJobDAO.getTransferJobByJobNumber(transferJobNumber);
        AdministerTransferJobTask adminTask = (AdministerTransferJobTask) dprClient.getTaskByName(currentUser, AdministerTransferJobTask.NAME);
        adminTask.reprocessTransferJob(transferJob, "Test case reprocessing.");
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        StepResults results = new StepResults();
        results.setErrorOccurred(false);
        task.setJobEncapsulator(new JobEncapsulator(transferJob));
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
        while (step != null) {
            StepProcessingListener listener = new TestCaseStepProcessingListener();
            step.addListener(listener);
            step.setProperties(stepProperties);
            results = step.doProcessing(new ProcessingErrorHandler() {

                public ProcessingErrorAction determineAction(String stepName, StepResults errorResults) {
                    Assert.fail();
                    return ProcessingErrorAction.RESET;
                }

                public boolean continueDespiteError(String stepName, String errorMessage) {
                    return false;
                }
            });
            Assert.assertFalse(results.isErrorOccurred());
            step = task.getCurrentStep();
        }
        dprClient.logOff(currentUser);
        dprClient.disconnectFromDataStore();
    }

    private void importAndProcessOnPF() throws Exception {
        dprClient = new DPRClient();
        Logger rootLogger = Logger.getLogger(Constants.ROOT_LOGGING_PACKAGE);
        rootLogger.setLevel(Level.FINE);
        dataAccessManager = new HibernateDataAccessManager(dprClient);
        dprClient.setDataAccessManager(dataAccessManager);
        Map<String, String> connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForPF(connectionProperties);
        try {
            dprClient.connectToDataStore(connectionProperties);
        } catch (ConnectionException e) {
            e.printStackTrace();
            throw e;
        }
        currentUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        AdministerTransferJobTask adminTask = (AdministerTransferJobTask) dprClient.getTaskByName(currentUser, AdministerTransferJobTask.NAME);
        File importFile = new File(getQFExportFileName());
        adminTask.importTransferJob(importFile, new TestCaseImportExportListener());
        dprClient.logOff(currentUser);
        dprClient.disconnectFromDataStore();
    }

    @SuppressWarnings("null")
    private void verifyTransferJobOnPF(int numDataObjects) throws Exception {
        dprClient = new DPRClient();
        Logger rootLogger = Logger.getLogger(Constants.ROOT_LOGGING_PACKAGE);
        rootLogger.setLevel(Level.FINE);
        dataAccessManager = new HibernateDataAccessManager(dprClient);
        dprClient.setDataAccessManager(dataAccessManager);
        Map<String, String> connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForPF(connectionProperties);
        try {
            dprClient.connectToDataStore(connectionProperties);
        } catch (ConnectionException e) {
            e.printStackTrace();
            throw e;
        }
        currentUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        AdministerTransferJobTask adminTask = (AdministerTransferJobTask) dprClient.getTaskByName(currentUser, AdministerTransferJobTask.NAME);
        TransferJobDAO transferJobDAO = dataAccessManager.getTransferJobDAO(adminTask);
        TransferJob transferJob = transferJobDAO.getTransferJobByJobNumber(transferJobNumber);
        Assert.assertEquals(2, transferJob.getQfProcessingRecords().size());
        Assert.assertEquals(2, transferJob.getPfProcessingRecords().size());
        QFTransferJobProcessingRecord firstQFRecord = null;
        QFTransferJobProcessingRecord secondQFRecord = null;
        PFTransferJobProcessingRecord firstPFRecord = null;
        PFTransferJobProcessingRecord secondPFRecord = null;
        for (QFTransferJobProcessingRecord qfRecord : transferJob.getQfProcessingRecords()) {
            if (qfRecord.getProcessingOrder() == 0) {
                firstQFRecord = qfRecord;
            } else if (qfRecord.getProcessingOrder() == 1) {
                secondQFRecord = qfRecord;
            } else {
                Assert.fail("Unknown processing order for QF record! " + qfRecord.getProcessingOrder());
            }
        }
        for (PFTransferJobProcessingRecord pfRecord : transferJob.getPfProcessingRecords()) {
            if (pfRecord.getProcessingOrder() == 0) {
                firstPFRecord = pfRecord;
            } else if (pfRecord.getProcessingOrder() == 1) {
                secondPFRecord = pfRecord;
            } else {
                Assert.fail("Unknonwn processing order for QF record! " + pfRecord.getProcessingOrder());
            }
        }
        Assert.assertNotNull(firstQFRecord);
        Assert.assertNotNull(secondQFRecord);
        Assert.assertNotNull(firstPFRecord);
        Assert.assertNotNull(secondPFRecord);
        TransferJobValidator validator = new TransferJobValidator();
        validator.verifyQFProcessingRecord(firstQFRecord);
        Assert.assertTrue(firstQFRecord.getReprocessed());
        Assert.assertNotNull(firstQFRecord.getReprocessingReason());
        Assert.assertEquals(0, firstQFRecord.getProcessingOrder());
        Assert.assertEquals(firstQFRecord, firstPFRecord.getImportProcessingRecord());
        Assert.assertEquals(currentUser, firstPFRecord.getImportedBy());
        Assert.assertNotNull(firstPFRecord.getImportedDate());
        Assert.assertNotNull(firstPFRecord.getInputCarrierId());
        Assert.assertNotNull(firstPFRecord.getInputCarrierLocation());
        Assert.assertEquals(currentUser, firstPFRecord.getChecksumBy());
        Assert.assertNotNull(firstPFRecord.getChecksumDate());
        Assert.assertEquals(ChecksumResultType.OK, firstPFRecord.getChecksumPassed());
        Assert.assertNotNull(firstPFRecord.getOutputCarrierId());
        Assert.assertNotNull(firstPFRecord.getOutputCarrierLocation());
        Assert.assertEquals(Constants.PASSED_STATE, firstPFRecord.getOutputPrepStatus());
        Assert.assertNotNull(firstPFRecord.getOutputPrepDate());
        Assert.assertEquals(currentUser, firstPFRecord.getOutputPrepBy());
        Assert.assertEquals(Constants.UNSTARTED_STATE, firstPFRecord.getBinaryNormalisingStatus());
        Assert.assertTrue(firstPFRecord.isStopped());
        PFStopProcessingRecord stopRecord = firstPFRecord.getPfStopProcessingRecord();
        Assert.assertTrue(stopRecord.getReturnedToQF());
        Assert.assertEquals(currentUser, stopRecord.getReturnedBy());
        Assert.assertNotNull(stopRecord.getDateReturned());
        Assert.assertNull(stopRecord.getRestarted());
        Assert.assertNull(stopRecord.getDateRestarted());
        Assert.assertNull(stopRecord.getRestartedBy());
        Assert.assertNull(stopRecord.getRestartedComments());
        Assert.assertEquals(PFStopProcessingRecord.RETURNED_TO_QF, stopRecord.getState());
        validator.verifyQFProcessingRecord(secondQFRecord);
        Assert.assertEquals(secondQFRecord, secondPFRecord.getImportProcessingRecord());
        Assert.assertEquals(currentUser, secondPFRecord.getImportedBy());
        Assert.assertNotNull(secondPFRecord.getImportedDate());
        Assert.assertNotNull(secondPFRecord.getInputCarrierId());
        Assert.assertNotNull(secondPFRecord.getInputCarrierLocation());
        int dataObjectCount = 0;
        Iterator<Object> dataObjectIterator = transferJobDAO.getDataObjectsForTransferJob(transferJob);
        while (dataObjectIterator.hasNext()) {
            DataObject dataObject = (DataObject) dataObjectIterator.next();
            Assert.assertEquals(2, dataObject.getQfProcessingRecords().size());
            Assert.assertEquals(firstQFRecord, dataObject.getQfProcessingRecords().get(0).getQfTransferJobProcessingRecord());
            Assert.assertEquals(secondQFRecord, dataObject.getQfProcessingRecords().get(1).getQfTransferJobProcessingRecord());
            Assert.assertEquals(2, dataObject.getPfProcessingRecords().size());
            if (dataObjectCount++ % Constants.MONITOR_DATA_OBJECT_COUNT == 0) {
                logger.fine("Verified " + dataObjectCount + " data objects.");
            }
        }
        Assert.assertEquals(numDataObjects, dataObjectCount);
        dprClient.logOff(currentUser);
        dprClient.disconnectFromDataStore();
    }

    private String getQFExportFileName() {
        return TestConstants.TEST_CASE_QF_OUTPUT_CARRIER_LOCATION + File.separator + "QF_" + transferJobNumber.getFullJobNumberString().replace('/', '_') + Constants.EXPORT_FILE_EXTENSION;
    }
}
