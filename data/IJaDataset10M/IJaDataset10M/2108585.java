package au.gov.naa.digipres.dpr.task.step;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import au.gov.naa.digipres.dpr.core.Constants;
import au.gov.naa.digipres.dpr.core.DPRClient;
import au.gov.naa.digipres.dpr.dao.TransferJobDAO;
import au.gov.naa.digipres.dpr.dao.hibernate.HibernateDataAccessManager;
import au.gov.naa.digipres.dpr.model.job.JobEncapsulator;
import au.gov.naa.digipres.dpr.model.job.JobNumber;
import au.gov.naa.digipres.dpr.model.job.JobNumber.MalformedJobNumberException;
import au.gov.naa.digipres.dpr.model.job.JobStatus;
import au.gov.naa.digipres.dpr.model.transferjob.DataObject;
import au.gov.naa.digipres.dpr.model.transferjob.QFDataObjectProcessingRecord;
import au.gov.naa.digipres.dpr.model.transferjob.QFTransferJobProcessingRecord;
import au.gov.naa.digipres.dpr.model.transferjob.TransferJob;
import au.gov.naa.digipres.dpr.model.user.User;
import au.gov.naa.digipres.dpr.task.TransferJobProcessingTask;
import au.gov.naa.digipres.dpr.task.step.PreQuarantineProcessingStep.FileNotFoundOnMediaException;
import au.gov.naa.digipres.dpr.task.step.PreQuarantineProcessingStep.FilesOnMediaException;
import au.gov.naa.digipres.dpr.task.step.PreQuarantineProcessingStep.PreQuarantineProcessHandler;
import au.gov.naa.digipres.dpr.testutils.DPRTestCase;
import au.gov.naa.digipres.dpr.testutils.TestCaseLogOnHandler;
import au.gov.naa.digipres.dpr.testutils.TestCaseStepProcessingListener;
import au.gov.naa.digipres.dpr.testutils.TestConstants;
import au.gov.naa.digipres.dpr.testutils.TestDatabaseUtils;
import au.gov.naa.digipres.dpr.testutils.TestProcessingErrorHandlers;
import au.gov.naa.digipres.dpr.testutils.TransferJobGenerator;
import au.gov.naa.digipres.dpr.util.FileUtils;
import au.gov.naa.digipres.dpr.util.virus.ScanInformation;
import au.gov.naa.digipres.virus.FakeScanner;

public class VerifyPreQuarantineStep extends DPRTestCase {

    private DPRClient dprClient;

    private User currentUser;

    private static final String JOB_NUMBER_STRING = "9999/00004444";

    private static final int FAKE_BRIDGE_PORT = 9999;

    private static final String FAKE_BRIDGE_PORT_STRING = Integer.toString(FAKE_BRIDGE_PORT);

    private int mediaLocationRequests = 0;

    private FakeScanner fakeBridge;

    /**
	 * Default empty constructor
	 * 
	 */
    public VerifyPreQuarantineStep() {
        super();
    }

    @Before
    public void setUp() throws Exception {
        TestDatabaseUtils.setUpQFTables();
        dprClient = new DPRClient();
        dprClient.setDataAccessManager(new HibernateDataAccessManager(dprClient));
        Map<String, String> connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForQF(connectionProperties);
        dprClient.connectToDataStore(connectionProperties);
        currentUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        fakeBridge = new FakeScanner(false, 0, FAKE_BRIDGE_PORT);
        fakeBridge.startServer();
    }

    @After
    public void tearDown() throws Exception {
        dprClient.disconnectFromDataStore();
        fakeBridge.stopServer();
        FileUtils.deleteContentsOfDir(new File(TestConstants.TEST_CASE_PF_INPUT_CARRIER_LOCATION));
        FileUtils.deleteContentsOfDir(new File(TestConstants.TEST_CASE_PF_OUTPUT_CARRIER_LOCATION));
        FileUtils.deleteContentsOfDir(new File(TestConstants.TEST_CASE_MEDIA_LOCATION));
    }

    /**
	 * Test using a simple transfer with one media and no knowledge of the step. This should always pass.
	 */
    @Test
    public void testNaivePreQuarantineStepSingle() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_QUARANTINE_PERIOD_IN_DAYS);
        Date expectedDateOut = calendar.getTime();
        String quarantineReason = "";
        JobNumber jobNumber = new JobNumber();
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            runStepNaively(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, expectedDateOut, quarantineReason);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineProcessingBy() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckDefinitionsVersion() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckerName() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckerVersion() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineProcessingDate() != null);
        Assert.assertTrue(Boolean.TRUE.equals(retrievedQFRecord.getPreQuarantineVirusCheckDefinitionsCurrent()));
        Assert.assertTrue(retrievedQFRecord.getIntoQuarantineBy() != null);
        Assert.assertTrue(retrievedQFRecord.getIntoQuarantineDate() != null);
        Assert.assertTrue(!(retrievedQFRecord.getQuarantinePeriodReason() != null && retrievedQFRecord.getQuarantinePeriodReason().length() != 0));
        Assert.assertTrue(retrievedQFRecord.getExpectedDateOutOfQuarantine().after(retrievedQFRecord.getIntoQuarantineDate()));
        Assert.assertTrue(retrievedQFRecord.getExpectedDateOutOfQuarantine().after(new Date()));
        Iterator<Object> qfDataObjectRecords = tjDAO.getDataObjectQFRecords(retrievedTransferJob.getMostRecentQFRecord());
        QFDataObjectProcessingRecord qfDataObjectRecord = (QFDataObjectProcessingRecord) qfDataObjectRecords.next();
        Assert.assertTrue(!qfDataObjectRecords.hasNext());
        Assert.assertTrue(Boolean.TRUE.equals(qfDataObjectRecord.getPreQuarantineVirusCheckPassed()));
    }

    /**
	 * Test using a simple transfer with one media and no knowledge of the step. This should always pass.
	 */
    @Test
    public void testNaivePreQuarantineStep() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 5;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_QUARANTINE_PERIOD_IN_DAYS);
        Date expectedDateOut = calendar.getTime();
        String quarantineReason = "";
        JobNumber jobNumber = new JobNumber(JOB_NUMBER_STRING);
        try {
            runStepNaively(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, expectedDateOut, quarantineReason);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineProcessingBy() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckDefinitionsVersion() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckerName() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckerVersion() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineProcessingDate() != null);
        Assert.assertTrue(Boolean.TRUE.equals(retrievedQFRecord.getPreQuarantineVirusCheckDefinitionsCurrent()));
        Assert.assertTrue(retrievedQFRecord.getIntoQuarantineBy() != null);
        Assert.assertTrue(retrievedQFRecord.getIntoQuarantineDate() != null);
        Assert.assertTrue(!(retrievedQFRecord.getQuarantinePeriodReason() != null && retrievedQFRecord.getQuarantinePeriodReason().length() != 0));
        Assert.assertTrue(retrievedQFRecord.getExpectedDateOutOfQuarantine().after(retrievedQFRecord.getIntoQuarantineDate()));
        Assert.assertTrue(retrievedQFRecord.getExpectedDateOutOfQuarantine().after(new Date()));
        int counter = 0;
        Iterator<Object> qfDataObjectRecords = tjDAO.getDataObjectQFRecords(retrievedQFRecord);
        while (qfDataObjectRecords.hasNext()) {
            QFDataObjectProcessingRecord qfDataObjectProcessingRecord = (QFDataObjectProcessingRecord) qfDataObjectRecords.next();
            DataObject dataObject = qfDataObjectProcessingRecord.getDataObject();
            Assert.assertTrue(Boolean.TRUE.equals(dataObject.getQFDataObjectProcessingRecord(retrievedQFRecord).getPreQuarantineVirusCheckPassed()));
            counter++;
        }
        Assert.assertTrue(counter == dataObjectCount);
    }

    /**
	 * Test using a simple transfer with mutliple media and no knowledge of the step. This should throw a {@link ProcessingException}, 
	 * since the step will not be able to handle multiple media without having a {@link PreQuarantineProcessHandler} being supplied.
	 */
    @Test
    public void testNaivePreQuarantineStepMultiMedia() throws Exception {
        int mediaCount = 2;
        int dataObjectCount = 4;
        Date expectedDateOut = new Date();
        String quarantineReason = "Testing.";
        JobNumber jobNumber = new JobNumber(JOB_NUMBER_STRING);
        try {
            runStepNaively(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, expectedDateOut, quarantineReason);
            Assert.fail();
        } catch (StepException e) {
        }
        verifyDataNotWritten(jobNumber);
    }

    /**
	 * Test using a simple transfer with one media and no knowledge of the step, and a bad port given.
	 *  This should throw a {@link ProcessingException}.
	 */
    @Test
    public void testNaivePreQuarantineStepBadPort() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 10;
        String portString = "0001";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_QUARANTINE_PERIOD_IN_DAYS);
        Date expectedDateOut = calendar.getTime();
        String quarantineReason = "";
        JobNumber jobNumber = new JobNumber(JOB_NUMBER_STRING);
        try {
            runStepNaively(mediaCount, dataObjectCount, jobNumber, portString, expectedDateOut, quarantineReason);
            Assert.fail();
        } catch (StepException e) {
        }
        verifyDataNotWritten(jobNumber);
    }

    /**
	 * Test using a simple transfer with one media and no knowledge of the step, and a short period
	 * set, with a comment. This should pass.
	 */
    @Test
    public void testNaivePreQuarantineStepShortPeriod() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_QUARANTINE_PERIOD_IN_DAYS - 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date expectedDateOut = null;
        try {
            expectedDateOut = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
        } catch (ParseException e1) {
            e1.printStackTrace();
            Assert.fail("Expected date out was broken!");
        }
        String quarantineReason = "Test";
        JobNumber jobNumber = new JobNumber(JOB_NUMBER_STRING);
        try {
            runStepNaively(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, expectedDateOut, quarantineReason);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        verifyDataWritten(jobNumber, currentUser, expectedDateOut, quarantineReason, Constants.PASSED_STATE);
    }

    /**
	 * Test using a simple transfer with one media and no knowledge of the step, and a very short period
	 * set - ie out the same day it goes in, with a comment. This should pass.
	 */
    @Test
    public void testNaivePreQuarantineStepVeryShortPeriod() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 2;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date expectedDateOut = null;
        try {
            expectedDateOut = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e1) {
            e1.printStackTrace();
            Assert.fail("Expected date out was broken!");
        }
        String quarantineReason = "Test - same day test.";
        JobNumber jobNumber = new JobNumber(JOB_NUMBER_STRING);
        try {
            runStepNaively(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, expectedDateOut, quarantineReason);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        verifyDataWritten(jobNumber, currentUser, expectedDateOut, quarantineReason, Constants.PASSED_STATE);
    }

    /**
	 * Test using a simple transfer with one media and no knowledge of the step, and a short period
	 * set with *NO* comment. This should throw a {@link ProcessingException}.
	 */
    @Test
    public void testNaivePreQuarantineStepShortPeriodNoComment() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_QUARANTINE_PERIOD_IN_DAYS - 1);
        Date expectedDateOut = calendar.getTime();
        String quarantineReason = "";
        JobNumber jobNumber = new JobNumber(JOB_NUMBER_STRING);
        try {
            runStepNaively(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, expectedDateOut, quarantineReason);
            Assert.fail();
        } catch (StepException e) {
            Assert.assertTrue(e instanceof ProcessingException);
        }
        verifyDataNotWritten(jobNumber);
    }

    /**
	 * Test using a simple transfer with one media and explicit knowledge of the step class. This should pass.
	 */
    @Test
    public void testKnowPreQuarantineStep() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 4;
        int extraFilesOnMedia = 0;
        int numFilesToRemoveFromMedia = 0;
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        JobNumber jobNumber = new JobNumber(JOB_NUMBER_STRING);
        PreQuarantineProcessHandler handler = new PreQuarantineProcessHandler() {

            public boolean continueAfterVirusFound(ScanInformation scanResult) {
                return false;
            }

            public boolean continueWithOldDefinitions(Date definitionsDate) {
                return false;
            }

            public String getMediaLocationForFile(String fileName, String mediaId, String currentLocation) {
                throw new IllegalStateException("This should not be called as there is only one media item.");
            }

            public void setMediaCount(int mediaCountParm) {
            }

            public boolean continueAfterInvalidChecksum() {
                return false;
            }

            @Override
            public boolean notifyExtraFilesOnMedia(Map<String, List<String>> extraFilesByMedia) {
                return false;
            }

            @Override
            public void notifyMissingFilesFromManifest(Map<String, List<String>> filesByMedia) throws FilesOnMediaException {
            }

            @Override
            public String notifyMissingFileFromManifest(String fileName, String mediaId, String currentLocation) throws FileNotFoundOnMediaException {
                return null;
            }
        };
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        verifyDataWritten(jobNumber, currentUser, expectedDateOut, reasonForShortQuarantine, Constants.PASSED_STATE);
    }

    /**
	 * Test using a simple transfer with one media and explicit knowledge of the step class. This should pass.
	 */
    @Test
    public void testKnowPreQuarantineStepDoubleMedia() throws Exception {
        int mediaCount = 2;
        int dataObjectCount = 4;
        int extraFilesOnMedia = 0;
        int numFilesToRemoveFromMedia = 0;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        PreQuarantineProcessHandler handler = new PreQuarantineProcessHandler() {

            public boolean continueAfterVirusFound(ScanInformation scanResult) {
                return false;
            }

            public boolean continueWithOldDefinitions(Date definitionsDate) {
                return false;
            }

            public String getMediaLocationForFile(String fileName, String mediaId, String currentLocation) {
                return TestConstants.TEST_CASE_MEDIA_LOCATION + File.separator + mediaId;
            }

            public void setMediaCount(int mediaCountParm) {
            }

            public boolean continueAfterInvalidChecksum() {
                return false;
            }

            @Override
            public boolean notifyExtraFilesOnMedia(Map<String, List<String>> extraFilesByMedia) {
                return false;
            }

            @Override
            public void notifyMissingFilesFromManifest(Map<String, List<String>> filesByMedia) throws FilesOnMediaException {
            }

            @Override
            public String notifyMissingFileFromManifest(String fileName, String mediaId, String currentLocation) throws FileNotFoundOnMediaException {
                return null;
            }
        };
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        verifyDataWritten(jobNumber, currentUser, expectedDateOut, reasonForShortQuarantine, Constants.PASSED_STATE);
    }

    /**
	 * Test using a simple transfer with one media and explicit knowledge of the step class. This should pass.
	 */
    @Test
    public void testKnowPreQuarantineStepMultiMediaManyObjects() throws Exception {
        int mediaCount = 11;
        int dataObjectCount = 300;
        int extraFilesOnMedia = 0;
        int numFilesToRemoveFromMedia = 0;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        PreQuarantineProcessHandler handler = new PreQuarantineProcessHandler() {

            public boolean continueAfterVirusFound(ScanInformation scanResult) {
                return false;
            }

            public boolean continueWithOldDefinitions(Date definitionsDate) {
                return false;
            }

            public String getMediaLocationForFile(String fileName, String mediaId, String currentLocation) {
                return TestConstants.TEST_CASE_MEDIA_LOCATION + File.separator + mediaId;
            }

            public void setMediaCount(int mediaCountParm) {
            }

            public boolean continueAfterInvalidChecksum() {
                return false;
            }

            @Override
            public boolean notifyExtraFilesOnMedia(Map<String, List<String>> extraFilesByMedia) {
                return false;
            }

            @Override
            public void notifyMissingFilesFromManifest(Map<String, List<String>> filesByMedia) throws FilesOnMediaException {
            }

            @Override
            public String notifyMissingFileFromManifest(String fileName, String mediaId, String currentLocation) throws FileNotFoundOnMediaException {
                return null;
            }
        };
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        verifyDataWritten(jobNumber, currentUser, expectedDateOut, reasonForShortQuarantine, Constants.PASSED_STATE);
    }

    /**
	 * Test using a simple transfer with one media and explicit knowledge of the step class. This should fail!
	 */
    @Test
    public void testKnowPreQuarantineStepMultiMediaBadHandler() throws Exception {
        int mediaCount = 3;
        int dataObjectCount = 10;
        int extraFilesOnMedia = 0;
        int numFilesToRemoveFromMedia = 0;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        PreQuarantineProcessHandler handler = new PreQuarantineProcessHandler() {

            public boolean continueAfterVirusFound(ScanInformation scanResult) {
                return false;
            }

            public boolean continueWithOldDefinitions(Date definitionsDate) {
                return false;
            }

            public String getMediaLocationForFile(String fileName, String mediaId, String currentLocation) {
                return "slash-the-cat-sat on the mat. //.\\asdf25354";
            }

            public void setMediaCount(int mediaCountParm) {
            }

            public boolean continueAfterInvalidChecksum() {
                return false;
            }

            @Override
            public boolean notifyExtraFilesOnMedia(Map<String, List<String>> extraFilesByMedia) {
                return false;
            }

            @Override
            public void notifyMissingFilesFromManifest(Map<String, List<String>> filesByMedia) throws FilesOnMediaException {
            }

            @Override
            public String notifyMissingFileFromManifest(String fileName, String mediaId, String currentLocation) throws FileNotFoundOnMediaException {
                return getMediaLocationForFile(fileName, mediaId, currentLocation);
            }
        };
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
            Assert.fail();
        } catch (StepException e) {
            if (e instanceof ProcessingException) {
            } else {
                e.printStackTrace();
                Assert.fail();
            }
        }
        verifyDataNotWritten(jobNumber);
    }

    /**
	 * Test pausing processing after completing the first media.
	 */
    @Test
    public void testMultipleMediaPause() throws Exception {
        int mediaCount = 2;
        int dataObjectCount = 3;
        int extraFilesOnMedia = 0;
        int numFilesToRemoveFromMedia = 0;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        PreQuarantineProcessHandler handler = new PreQuarantineProcessHandler() {

            public boolean continueAfterVirusFound(ScanInformation scanResult) {
                return false;
            }

            public boolean continueWithOldDefinitions(Date definitionsDate) {
                return false;
            }

            public String getMediaLocationForFile(String fileName, String mediaId, String currentLocation) {
                mediaLocationRequests++;
                if (mediaId.equals("1")) {
                    return TestConstants.TEST_CASE_MEDIA_LOCATION + File.separator + mediaId;
                }
                return null;
            }

            public void setMediaCount(int mediaCountParm) {
            }

            public boolean continueAfterInvalidChecksum() {
                return false;
            }

            @Override
            public boolean notifyExtraFilesOnMedia(Map<String, List<String>> extraFilesByMedia) {
                return false;
            }

            @Override
            public void notifyMissingFilesFromManifest(Map<String, List<String>> filesByMedia) throws FilesOnMediaException {
            }

            @Override
            public String notifyMissingFileFromManifest(String fileName, String mediaId, String currentLocation) throws FileNotFoundOnMediaException {
                return null;
            }
        };
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        int expectedMediaRequestCount = 1;
        Assert.assertEquals(expectedMediaRequestCount, mediaLocationRequests);
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.PAUSED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * Test extra files on media
	 */
    @Test
    public void testExtraFileOnMedia() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 1;
        int extraFilesOnMedia = 1;
        int numFilesToRemoveFromMedia = 0;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ExtraFilesPreQuarantineHandler handler = new ExtraFilesPreQuarantineHandler();
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(extraFilesOnMedia, handler.numberOfExtraFilesOnMedia());
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.PASSED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * Test extra files on media
	 */
    @Test
    public void testExtraFilesOnMediaSmall() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 1;
        int extraFilesOnMedia = 10;
        int numFilesToRemoveFromMedia = 0;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ExtraFilesPreQuarantineHandler handler = new ExtraFilesPreQuarantineHandler();
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(extraFilesOnMedia, handler.numberOfExtraFilesOnMedia());
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.PASSED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * Test extra files on media
	 */
    @Test
    public void testExtraFilesOnMediaMedium() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 1;
        int extraFilesOnMedia = 100;
        int numFilesToRemoveFromMedia = 0;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ExtraFilesPreQuarantineHandler handler = new ExtraFilesPreQuarantineHandler();
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(extraFilesOnMedia, handler.numberOfExtraFilesOnMedia());
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.PASSED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * Test extra files on media
	 */
    @Test
    public void testExtraFilesOnMediaLarge() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 1;
        int extraFilesOnMedia = 1000;
        int numFilesToRemoveFromMedia = 0;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ExtraFilesPreQuarantineHandler handler = new ExtraFilesPreQuarantineHandler();
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(extraFilesOnMedia, handler.numberOfExtraFilesOnMedia());
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.PASSED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * Test extra files on media
	 */
    @Test
    public void testExtraFilesOnMediaSmallWithMoreDataObjects() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 10;
        int extraFilesOnMedia = 10;
        int numFilesToRemoveFromMedia = 0;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ExtraFilesPreQuarantineHandler handler = new ExtraFilesPreQuarantineHandler();
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(extraFilesOnMedia, handler.numberOfExtraFilesOnMedia());
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.PASSED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * Test extra files on media
	 */
    @Test
    public void testMissingFilesFromManifestSingle() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 10;
        int extraFilesOnMedia = 0;
        int numFilesToRemoveFromMedia = 1;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ExtraFilesPreQuarantineHandler handler = new ExtraFilesPreQuarantineHandler();
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        if (numFilesToRemoveFromMedia > dataObjectCount) {
            numFilesToRemoveFromMedia = dataObjectCount;
        }
        Assert.assertEquals(numFilesToRemoveFromMedia, handler.numberOfFilesMissingFromManifest());
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.UNSTARTED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * Test extra files on media
	 */
    @Test
    public void testMissingFilesFromManifestSmall() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 10;
        int extraFilesOnMedia = 0;
        int numFilesToRemoveFromMedia = 10;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ExtraFilesPreQuarantineHandler handler = new ExtraFilesPreQuarantineHandler();
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        if (numFilesToRemoveFromMedia > dataObjectCount) {
            numFilesToRemoveFromMedia = dataObjectCount;
        }
        Assert.assertEquals(numFilesToRemoveFromMedia, handler.numberOfFilesMissingFromManifest());
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.UNSTARTED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * Test extra files on media
	 */
    @Test
    public void testMissingFilesFromManifestMedium() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 1000;
        int extraFilesOnMedia = 0;
        int numFilesToRemoveFromMedia = 100;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ExtraFilesPreQuarantineHandler handler = new ExtraFilesPreQuarantineHandler();
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        if (numFilesToRemoveFromMedia > dataObjectCount) {
            numFilesToRemoveFromMedia = dataObjectCount;
        }
        Assert.assertEquals(numFilesToRemoveFromMedia, handler.numberOfFilesMissingFromManifest());
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.UNSTARTED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * Test extra files on media
	 */
    @Test
    public void testMissingFilesFromManifestAndExtraFilesSmall() throws Exception {
        int mediaCount = 1;
        int dataObjectCount = 100;
        int extraFilesOnMedia = 10;
        int numFilesToRemoveFromMedia = 10;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ExtraFilesPreQuarantineHandler handler = new ExtraFilesPreQuarantineHandler();
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        if (numFilesToRemoveFromMedia > dataObjectCount) {
            numFilesToRemoveFromMedia = dataObjectCount;
        }
        Assert.assertEquals(numFilesToRemoveFromMedia, handler.numberOfFilesMissingFromManifest());
        Assert.assertEquals(extraFilesOnMedia, handler.numberOfExtraFilesOnMedia());
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.UNSTARTED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * Test extra files on media
	 */
    @Test
    public void testMissingFilesFromManifestAndExtraFilesWithMultpleMediaSmall() throws Exception {
        int mediaCount = 2;
        int dataObjectCount = 100;
        int extraFilesOnMedia = 10;
        int numFilesToRemoveFromMedia = 10;
        JobNumber jobNumber = new JobNumber();
        Date expectedDateOut = new Date();
        String reasonForShortQuarantine = "Test - short quarantine";
        mediaLocationRequests = 0;
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        ExtraFilesPreQuarantineHandler handler = new ExtraFilesPreQuarantineHandler();
        try {
            runStepWithKnowledgeOfStep(mediaCount, dataObjectCount, jobNumber, FAKE_BRIDGE_PORT_STRING, handler, expectedDateOut, reasonForShortQuarantine, extraFilesOnMedia, numFilesToRemoveFromMedia);
        } catch (StepException e) {
            e.printStackTrace();
            Assert.fail();
        }
        if (numFilesToRemoveFromMedia > dataObjectCount) {
            numFilesToRemoveFromMedia = dataObjectCount;
        }
        Assert.assertEquals(numFilesToRemoveFromMedia, handler.numberOfFilesMissingFromManifest());
        Assert.assertEquals(extraFilesOnMedia, handler.numberOfExtraFilesOnMedia());
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertEquals(Constants.UNSTARTED_STATE, retrievedQFRecord.getPreQuarantineProcessingStatus());
    }

    /**
	 * <p>This method will run the step with full knowledge of the step itself.</p>
	 * <p>A transfer job will be created, using the </p>
	 * @param mediaCount
	 * @param dataObjectCount
	 * @param jobNumber
	 * @param extraFilesOnMedia Extra number of files to put on media, these wont appear in the manifest.
	 * @param numFilesToRemoveFromMedia The number of files to remove from media, this simulates, missing files from the manifest.
	 * @throws StepException 
	 */
    private void runStepWithKnowledgeOfStep(int mediaCount, int dataObjectCount, JobNumber jobNumber, String portString, PreQuarantineProcessHandler handler, Date expectedDateOut, String quarantinePeriodReason, int extraFilesOnMedia, int numFilesToRemoveFromMedia) throws Exception {
        TransferJobProcessingTask transferJobProcessingTask = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO transferJobDAO = dprClient.getDataAccessManager().getTransferJobDAO(transferJobProcessingTask);
        TransferJobGenerator generator = new TransferJobGenerator(dprClient.getDataAccessManager(), transferJobDAO);
        generator.setNumDataObjects(dataObjectCount);
        generator.setMediaCount(mediaCount);
        generator.setInputCarrierId(TestConstants.TEST_CASE_PF_INPUT_CARRIER_LOCATION);
        generator.setInputCarrierLocation(TestConstants.TEST_CASE_PF_INPUT_CARRIER_LOCATION);
        generator.setOutputCarrierId(TestConstants.TEST_CASE_PF_OUTPUT_CARRIER_LOCATION);
        generator.setOutputCarrierLocation(TestConstants.TEST_CASE_PF_OUTPUT_CARRIER_LOCATION);
        generator.setCreateFiles(true);
        generator.setJobNumber(jobNumber);
        generator.generateTransferJob(JobStatus.QF_INITIALISED_FROM_MANIFEST_PASSED, currentUser);
        String firstMediaLocation = generator.getFirstMediaLocation();
        if (numFilesToRemoveFromMedia > 0) {
            ExtraFilesPreQuarantineHandler extrHandler;
            if (handler instanceof ExtraFilesPreQuarantineHandler) {
                extrHandler = (ExtraFilesPreQuarantineHandler) handler;
                if (numFilesToRemoveFromMedia > dataObjectCount) {
                    numFilesToRemoveFromMedia = dataObjectCount;
                }
                int numFilesRemoved = 0;
                int numFilesPerMedia = numFilesToRemoveFromMedia / mediaCount;
                for (int mediaIndex = 1; mediaIndex <= mediaCount; mediaIndex++) {
                    File[] currentMediaFiles = new File(extrHandler.getMediaLocationForMediaId(String.valueOf(mediaIndex))).listFiles();
                    if (currentMediaFiles.length < numFilesPerMedia) {
                        Assert.fail();
                    }
                    for (int i = 0; i < numFilesPerMedia; i++) {
                        currentMediaFiles[i].delete();
                        numFilesRemoved++;
                    }
                }
                if (numFilesRemoved < numFilesToRemoveFromMedia) {
                    int filesLeft = numFilesToRemoveFromMedia - numFilesRemoved;
                    File[] currentMediaFiles = new File(extrHandler.getMediaLocationForMediaId(String.valueOf(mediaCount))).listFiles();
                    if (currentMediaFiles.length < filesLeft) {
                        Assert.fail();
                    }
                    for (int i = 0; i < filesLeft; i++) {
                        currentMediaFiles[i].delete();
                        numFilesRemoved++;
                    }
                }
            }
        }
        if (extraFilesOnMedia > 0) {
            ExtraFilesPreQuarantineHandler extrHandler;
            if (handler instanceof ExtraFilesPreQuarantineHandler) {
                extrHandler = (ExtraFilesPreQuarantineHandler) handler;
                int numFilesAdded = 0;
                int numFilesPerMedia = extraFilesOnMedia / mediaCount;
                for (int mediaIndex = 1; mediaIndex <= mediaCount; mediaIndex++) {
                    for (int i = 0; i < numFilesPerMedia; i++) {
                        File newFile = new File(firstMediaLocation, "extra_file_" + numFilesAdded + ".txt");
                        newFile.createNewFile();
                        numFilesAdded++;
                    }
                }
                if (numFilesAdded < extraFilesOnMedia) {
                    int filesLeft = extraFilesOnMedia - numFilesAdded;
                    for (int i = 0; i < filesLeft; i++) {
                        File newFile = new File(firstMediaLocation, "extra_file_" + numFilesAdded + ".txt");
                        newFile.createNewFile();
                        numFilesAdded++;
                    }
                }
            }
        }
        List<TransferJob> transferJobs = transferJobDAO.getTransferJobList();
        Assert.assertEquals(1, transferJobs.size());
        transferJobProcessingTask.setJobEncapsulator(new JobEncapsulator(transferJobs.get(0)));
        Step step = transferJobProcessingTask.getCurrentStep();
        if (step instanceof PreQuarantineProcessingStep) {
            PreQuarantineProcessingStep preQuarantineStep = (PreQuarantineProcessingStep) step;
            preQuarantineStep.setFirstMediaLocation(firstMediaLocation);
            preQuarantineStep.setOutputCarrierId("foo");
            preQuarantineStep.setScannerPortString(portString);
            preQuarantineStep.setOutputCarrierLocation(TestConstants.TEST_CASE_PF_INPUT_CARRIER_LOCATION);
            preQuarantineStep.setExpectedDateOut(expectedDateOut);
            preQuarantineStep.setQuarantinePeriodReason(quarantinePeriodReason);
            preQuarantineStep.setPreQuarantineProcessingHandler(handler);
            preQuarantineStep.doProcessing(TestProcessingErrorHandlers.getAbortProcessingErrorHandler());
        }
    }

    /**
	 * This method will run the step without any knowledge of what the step is - it will simply populate property fields
	 * and attempt to run the step. It will throw {@link ProcessingException} at the drop of a hat if there is an error.
	 * @param mediaCount
	 * @param dataObjectCount
	 * @param jobNumber
	 * @throws StepException 
	 */
    private void runStepNaively(int mediaCount, int dataObjectCount, JobNumber jobNumber, String portString, Date expectedDateOut, String reasonForQuarantinePeriod) throws Exception {
        TransferJobProcessingTask transferJobProcessingTask = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO transferJobDAO = dprClient.getDataAccessManager().getTransferJobDAO(transferJobProcessingTask);
        TransferJobGenerator generator = new TransferJobGenerator(dprClient.getDataAccessManager(), transferJobDAO);
        generator.setNumDataObjects(dataObjectCount);
        generator.setMediaCount(mediaCount);
        generator.setInputCarrierId(TestConstants.TEST_CASE_PF_INPUT_CARRIER_LOCATION);
        generator.setInputCarrierLocation(TestConstants.TEST_CASE_PF_INPUT_CARRIER_LOCATION);
        generator.setOutputCarrierId(TestConstants.TEST_CASE_PF_OUTPUT_CARRIER_LOCATION);
        generator.setOutputCarrierLocation(TestConstants.TEST_CASE_PF_OUTPUT_CARRIER_LOCATION);
        generator.setCreateFiles(true);
        generator.setJobNumber(jobNumber);
        generator.generateTransferJob(JobStatus.QF_INITIALISED_FROM_MANIFEST_PASSED, currentUser);
        String firstMediaLocation = generator.getFirstMediaLocation();
        List<TransferJob> transferJobs = transferJobDAO.getTransferJobList();
        Assert.assertEquals(1, transferJobs.size());
        transferJobProcessingTask.setJobEncapsulator(new JobEncapsulator(transferJobs.get(0)));
        Step step = transferJobProcessingTask.getCurrentStep();
        Properties stepProperties = step.getProperties();
        stepProperties.put(StepProperties.OUTPUT_CARRIER_LOCATION_PROPERTY_NAME, TestConstants.TEST_CASE_QF_OUTPUT_CARRIER_LOCATION);
        stepProperties.put(StepProperties.FIRST_MEDIA_LOCATION_PROPERTY_NAME, firstMediaLocation);
        stepProperties.put(StepProperties.SCANNER_PORT_PROPERTY_NAME, portString);
        stepProperties.put(StepProperties.REASON_FOR_SHORT_QUARANTINE_PROPERTY_NAME, reasonForQuarantinePeriod);
        stepProperties.put(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME, new SimpleDateFormat(Constants.DATE_FORMAT).format(expectedDateOut));
        step.setProperties(stepProperties);
        step.doProcessing(TestProcessingErrorHandlers.getAbortProcessingErrorHandler());
    }

    @Test
    public void testStepListenerEventsSingle() throws Exception {
        runStepListenerEventsTest(1);
    }

    @Test
    public void testStepListenerEventsSmall() throws Exception {
        runStepListenerEventsTest(10);
    }

    @Test
    public void testStepListenerEventsMedium() throws Exception {
        runStepListenerEventsTest(1000);
    }

    @Ignore
    @Test
    public void testStepListenerEventsLarge() throws Exception {
        runStepListenerEventsTest(10000);
    }

    public void runStepListenerEventsTest(int dataObjectCount) throws Exception {
        int mediaCount = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_QUARANTINE_PERIOD_IN_DAYS);
        Date expectedDateOut = calendar.getTime();
        String quarantineReason = "";
        JobNumber jobNumber = new JobNumber();
        try {
            jobNumber = new JobNumber(JOB_NUMBER_STRING);
        } catch (MalformedJobNumberException e) {
            e.printStackTrace();
            Assert.fail();
        }
        TransferJobProcessingTask transferJobProcessingTask = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO transferJobDAO = dprClient.getDataAccessManager().getTransferJobDAO(transferJobProcessingTask);
        TransferJobGenerator generator = new TransferJobGenerator(dprClient.getDataAccessManager(), transferJobDAO);
        generator.setNumDataObjects(dataObjectCount);
        generator.setMediaCount(mediaCount);
        generator.setInputCarrierId(TestConstants.TEST_CASE_PF_INPUT_CARRIER_LOCATION);
        generator.setInputCarrierLocation(TestConstants.TEST_CASE_PF_INPUT_CARRIER_LOCATION);
        generator.setOutputCarrierId(TestConstants.TEST_CASE_PF_OUTPUT_CARRIER_LOCATION);
        generator.setOutputCarrierLocation(TestConstants.TEST_CASE_PF_OUTPUT_CARRIER_LOCATION);
        generator.setCreateFiles(true);
        generator.setJobNumber(jobNumber);
        generator.generateTransferJob(JobStatus.QF_INITIALISED_FROM_MANIFEST_PASSED, currentUser);
        String firstMediaLocation = generator.getFirstMediaLocation();
        List<TransferJob> transferJobs = transferJobDAO.getTransferJobList();
        Assert.assertEquals(1, transferJobs.size());
        transferJobProcessingTask.setJobEncapsulator(new JobEncapsulator(transferJobs.get(0)));
        Step step = transferJobProcessingTask.getCurrentStep();
        Properties stepProperties = step.getProperties();
        stepProperties.put(StepProperties.OUTPUT_CARRIER_LOCATION_PROPERTY_NAME, TestConstants.TEST_CASE_QF_OUTPUT_CARRIER_LOCATION);
        stepProperties.put(StepProperties.FIRST_MEDIA_LOCATION_PROPERTY_NAME, firstMediaLocation);
        stepProperties.put(StepProperties.SCANNER_PORT_PROPERTY_NAME, FAKE_BRIDGE_PORT_STRING);
        stepProperties.put(StepProperties.REASON_FOR_SHORT_QUARANTINE_PROPERTY_NAME, quarantineReason);
        stepProperties.put(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME, new SimpleDateFormat(Constants.DATE_FORMAT).format(expectedDateOut));
        step.setProperties(stepProperties);
        TestCaseStepProcessingListener stepListener = new TestCaseStepProcessingListener();
        step.addListener(stepListener);
        step.doProcessing(TestProcessingErrorHandlers.getAbortProcessingErrorHandler());
        Assert.assertEquals(1, stepListener.getBeginStepProcessingMessageCount());
        Assert.assertEquals(1, stepListener.getStepProcessingCompleteMessageCount());
        Assert.assertEquals(1, stepListener.getBeginItemProcessingMessageCount());
        Assert.assertEquals(1, stepListener.getItemProcessingCompleteMessageCount());
        Assert.assertEquals(dataObjectCount * 2, stepListener.getCurrentItemCount());
        Assert.assertEquals(dataObjectCount * 2, stepListener.getProcessItemMessageCount());
    }

    /**
	 * Verify that the fields for pre-quarantine processing are blank.
	 * @param jobNumber
	 */
    private void verifyDataNotWritten(JobNumber jobNumber) throws Exception {
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineProcessingBy() == null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckDefinitionsVersion() == null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckerName() == null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckerVersion() == null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineProcessingDate() == null);
        Assert.assertTrue(retrievedQFRecord.getIntoQuarantineBy() == null);
        Assert.assertTrue(retrievedQFRecord.getIntoQuarantineDate() == null);
        Assert.assertTrue((retrievedQFRecord.getQuarantinePeriodReason() == null || retrievedQFRecord.getQuarantinePeriodReason().length() == 0));
        Assert.assertTrue(retrievedQFRecord.getExpectedDateOutOfQuarantine() == null);
    }

    /**
	 * Verify that the fields for pre-quarantine processing are blank.
	 * @param jobNumber
	 */
    private void verifyDataWritten(JobNumber jobNumber, User user, Date expectedDateOut, String comment, String status) throws Exception {
        TransferJobProcessingTask task = (TransferJobProcessingTask) dprClient.getTaskByName(currentUser, TransferJobProcessingTask.TASK_NAME);
        TransferJobDAO tjDAO = dprClient.getDataAccessManager().getTransferJobDAO(task);
        TransferJob retrievedTransferJob = tjDAO.getTransferJobByJobNumber(jobNumber);
        QFTransferJobProcessingRecord retrievedQFRecord = retrievedTransferJob.getMostRecentQFRecord();
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineProcessingBy().equals(user));
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckDefinitionsVersion() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckerName() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineVirusCheckerVersion() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineProcessingDate() != null);
        Assert.assertTrue(retrievedQFRecord.getPreQuarantineProcessingStatus().equals(status));
        if (TestDatabaseUtils.getDefaultDatabaseServer().equals(TestDatabaseUtils.DatabaseServer.MYSQL)) {
            if (expectedDateOut.getTime() != retrievedQFRecord.getExpectedDateOutOfQuarantine().getTime()) {
                expectedDateOut = removeMicroseconds(expectedDateOut);
            }
        }
        Assert.assertTrue(retrievedQFRecord.getIntoQuarantineBy().equals(user));
        Assert.assertTrue(retrievedQFRecord.getIntoQuarantineDate() != null);
        Assert.assertTrue(retrievedQFRecord.getQuarantinePeriodReason().equals(comment));
        Assert.assertNotNull(retrievedQFRecord.getExpectedDateOutOfQuarantine());
        Assert.assertTrue(expectedDateOut.equals(retrievedQFRecord.getExpectedDateOutOfQuarantine()));
    }
}

class ExtraFilesPreQuarantineHandler implements PreQuarantineProcessHandler {

    private Map<String, List<String>> extraFilesByMedia;

    private Map<String, List<String>> filesByMedia;

    private int mediaLocationRequests;

    private String currentMediaID;

    public ExtraFilesPreQuarantineHandler() {
        mediaLocationRequests = 0;
        currentMediaID = "1";
    }

    public boolean continueAfterVirusFound(ScanInformation scanResult) {
        return false;
    }

    public boolean continueWithOldDefinitions(Date definitionsDate) {
        return false;
    }

    /**
	 * @param fileName
	 * @param mediaId
	 * @param currentLocation
	 * @return
	 */
    public String getMediaLocationForFile(String fileName, String mediaId, String currentLocation) throws FileNotFoundOnMediaException {
        if (currentMediaID.equals(mediaId)) {
            FileNotFoundOnMediaException fnfEx = new FileNotFoundOnMediaException(fileName, mediaId, "Testing - Telling the system to ignore");
            fnfEx.setContinueCheckingFiles(true);
            throw fnfEx;
        }
        currentMediaID = mediaId;
        return TestConstants.TEST_CASE_MEDIA_LOCATION + File.separator + mediaId;
    }

    public void setMediaCount(int mediaCountParm) {
    }

    public boolean continueAfterInvalidChecksum() {
        return false;
    }

    @Override
    public boolean notifyExtraFilesOnMedia(Map<String, List<String>> extraFilesByMedia) {
        this.setExtraFilesByMedia(extraFilesByMedia);
        return true;
    }

    @Override
    public void notifyMissingFilesFromManifest(Map<String, List<String>> filesByMedia) throws FilesOnMediaException {
        this.setFilesByMedia(filesByMedia);
        FilesOnMediaException fomEx = new FilesOnMediaException("Testing");
        fomEx.setReset(true);
        fomEx.setExtraFiles(false);
        throw fomEx;
    }

    public void setExtraFilesByMedia(Map<String, List<String>> extraFilesByMedia) {
        this.extraFilesByMedia = extraFilesByMedia;
    }

    public Map<String, List<String>> getExtraFilesByMedia() {
        return extraFilesByMedia;
    }

    public void setFilesByMedia(Map<String, List<String>> filesByMedia) {
        this.filesByMedia = filesByMedia;
    }

    public Map<String, List<String>> getFilesByMedia() {
        return filesByMedia;
    }

    public int numberOfExtraFilesOnMedia() {
        int result = 0;
        for (String mediaId : extraFilesByMedia.keySet()) {
            result += extraFilesByMedia.get(mediaId).size();
        }
        return result;
    }

    public int numberOfFilesMissingFromManifest() {
        int result = 0;
        for (String mediaId : filesByMedia.keySet()) {
            result += filesByMedia.get(mediaId).size();
        }
        return result;
    }

    public String getMediaLocationForMediaId(String mediaId) {
        mediaLocationRequests++;
        return TestConstants.TEST_CASE_MEDIA_LOCATION + File.separator + mediaId;
    }

    @Override
    public String notifyMissingFileFromManifest(String fileName, String mediaId, String currentLocation) throws FileNotFoundOnMediaException {
        if (currentMediaID.equals(mediaId)) {
            FileNotFoundOnMediaException fnfEx = new FileNotFoundOnMediaException(fileName, mediaId, "Testing - Telling the system to ignore");
            fnfEx.setContinueCheckingFiles(true);
            throw fnfEx;
        }
        return null;
    }
}
