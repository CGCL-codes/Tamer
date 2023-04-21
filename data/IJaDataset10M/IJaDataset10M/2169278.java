package org.dataminx.dts.batch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.impl.DefaultFileSystemManager;
import org.apache.xmlbeans.XmlException;
import org.dataminx.dts.DtsException;
import org.dataminx.dts.batch.common.DtsBatchJobConstants;
import org.dataminx.dts.common.DtsConstants;
import org.dataminx.dts.common.vfs.DtsVfsUtil;
import org.proposal.dmi.schemas.dts.x2010.dmiCommon.DataCopyActivityDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.ac.dl.escience.vfs.util.VFSUtil;

/**
 * A unit test for the AbstractJobPartitioningStrategy class that uses 
 * MixedFilesJobStepAllocator.
 * 
 * @author Gerson Galang
 * @author David Meredith 
 */
@ContextConfiguration(locations = { "/org/dataminx/dts/batch/client-context.xml" })
@Test(groups = { "unit-test" })
public class VfsMixedFilesJobPartitioningStrategyTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private AbstractJobPartitioningStrategy mPartitioningStrategy;

    private DtsVfsUtil mDtsVfsUtil;

    private static final long FILE_SIZE_10MB = 10485760;

    public VfsMixedFilesJobPartitioningStrategyTest() {
        TestUtils.assertTestEnvironmentOk();
    }

    @BeforeClass
    public void init() {
        mDtsVfsUtil = mock(DtsVfsUtil.class);
        System.setProperty(DtsBatchJobConstants.DTS_JOB_STEP_DIRECTORY_KEY, System.getProperty("java.io.tmpdir"));
    }

    @Test(groups = { "local-file-transfer-test" })
    public void testPartitionWith1File() throws IOException, XmlException, JobScopingException, Exception {
        final File f = new ClassPathResource("/org/dataminx/dts/batch/transfer-1file.xml").getFile();
        final DataCopyActivityDocument dtsJob = TestUtils.getTestDataCopyActivityDocument(f);
        mPartitioningStrategy.setDtsVfsUtil(mDtsVfsUtil);
        mPartitioningStrategy.setMaxTotalByteSizePerStepLimit(FILE_SIZE_10MB);
        mPartitioningStrategy.setMaxTotalFileNumPerStepLimit(3);
        mPartitioningStrategy.setTotalSizeLimit(-1);
        mPartitioningStrategy.setTotalFilesLimit(-1);
        final FileSystemManager fileSystemManager = VFSUtil.createNewFsManager(false, false, false, false, true, true, false, System.getProperty("java.io.tmpdir"));
        when(mDtsVfsUtil.createNewFsManager()).thenReturn((DefaultFileSystemManager) fileSystemManager);
        final String jobId = UUID.randomUUID().toString();
        final String jobTag = jobId;
        final DtsJobDetails jobDetails = mPartitioningStrategy.partitionTheJob(dtsJob.getDataCopyActivity(), jobId, jobTag);
        assertNotNull(jobDetails);
        assertEquals(jobDetails.getJobSteps().size(), 1);
        assertEquals(jobDetails.getTotalFiles(), 1);
        assertEquals(jobDetails.getSourceTargetMaxTotalFilesToTransfer().size(), 2);
        assertEquals(jobDetails.getSourceTargetMaxTotalFilesToTransfer().get(DtsConstants.TMP_ROOT_PROTOCOL).intValue(), 1);
    }

    @Test(groups = { "local-file-transfer-test" })
    public void testPartitionWith20Files() throws IOException, XmlException, JobScopingException, Exception {
        final File f = new ClassPathResource("/org/dataminx/dts/batch/transfer-20files.xml").getFile();
        final DataCopyActivityDocument dtsJob = TestUtils.getTestDataCopyActivityDocument(f);
        mPartitioningStrategy.setDtsVfsUtil(mDtsVfsUtil);
        mPartitioningStrategy.setMaxTotalByteSizePerStepLimit(FILE_SIZE_10MB);
        mPartitioningStrategy.setMaxTotalFileNumPerStepLimit(3);
        mPartitioningStrategy.setTotalSizeLimit(-1);
        mPartitioningStrategy.setTotalFilesLimit(-1);
        final FileSystemManager fileSystemManager = VFSUtil.createNewFsManager(false, false, false, false, true, true, false, System.getProperty("java.io.tmpdir"));
        when(mDtsVfsUtil.createNewFsManager()).thenReturn((DefaultFileSystemManager) fileSystemManager);
        final String jobId = UUID.randomUUID().toString();
        final String jobTag = jobId;
        final DtsJobDetails jobDetails = mPartitioningStrategy.partitionTheJob(dtsJob.getDataCopyActivity(), jobId, jobTag);
        assertNotNull(jobDetails);
        assertEquals(jobDetails.getJobSteps().size(), 8);
        assertEquals(jobDetails.getTotalFiles(), 20);
        assertEquals(jobDetails.getSourceTargetMaxTotalFilesToTransfer().size(), 2);
        assertEquals(jobDetails.getSourceTargetMaxTotalFilesToTransfer().get(DtsConstants.TMP_ROOT_PROTOCOL).intValue(), 11);
    }

    @Test(groups = { "local-file-transfer-test" })
    public void test2ExpectedPartitionsWith20Files() throws IOException, XmlException, JobScopingException, Exception {
        final File f = new ClassPathResource("/org/dataminx/dts/batch/transfer-20files.xml").getFile();
        final DataCopyActivityDocument dtsJob = TestUtils.getTestDataCopyActivityDocument(f);
        mPartitioningStrategy.setDtsVfsUtil(mDtsVfsUtil);
        mPartitioningStrategy.setMaxTotalByteSizePerStepLimit(FILE_SIZE_10MB * 100);
        mPartitioningStrategy.setMaxTotalFileNumPerStepLimit(500);
        mPartitioningStrategy.setTotalSizeLimit(-1);
        mPartitioningStrategy.setTotalFilesLimit(-1);
        final FileSystemManager fileSystemManager = VFSUtil.createNewFsManager(false, false, false, false, true, true, false, System.getProperty("java.io.tmpdir"));
        when(mDtsVfsUtil.createNewFsManager()).thenReturn((DefaultFileSystemManager) fileSystemManager);
        final String jobId = UUID.randomUUID().toString();
        final String jobTag = jobId;
        final DtsJobDetails jobDetails = mPartitioningStrategy.partitionTheJob(dtsJob.getDataCopyActivity(), jobId, jobTag);
        assertNotNull(jobDetails);
        assertEquals(jobDetails.getJobSteps().size(), 2);
        assertEquals(jobDetails.getTotalFiles(), 20);
        assertEquals(jobDetails.getSourceTargetMaxTotalFilesToTransfer().size(), 2);
        assertEquals(jobDetails.getSourceTargetMaxTotalFilesToTransfer().get(DtsConstants.TMP_ROOT_PROTOCOL).intValue(), 11);
    }

    @Test(groups = { "local-file-transfer-test" }, expectedExceptions = DtsException.class)
    public void testNegativeMaxTotalFileNumPerStepLimit() throws IOException, XmlException, JobScopingException, Exception {
        final File f = new ClassPathResource("/org/dataminx/dts/batch/transfer-1file.xml").getFile();
        final DataCopyActivityDocument dtsJob = TestUtils.getTestDataCopyActivityDocument(f);
        mPartitioningStrategy.setDtsVfsUtil(mDtsVfsUtil);
        mPartitioningStrategy.setMaxTotalFileNumPerStepLimit(-1);
        mPartitioningStrategy.setTotalSizeLimit(-1);
        mPartitioningStrategy.setTotalFilesLimit(-1);
        final String jobId = UUID.randomUUID().toString();
        final String jobTag = jobId;
        mPartitioningStrategy.partitionTheJob(dtsJob.getDataCopyActivity(), jobId, jobTag);
    }

    @Test(groups = { "local-file-transfer-test" }, expectedExceptions = DtsException.class)
    public void testNegativeMaxTotalByteSizePerStepLimit() throws IOException, XmlException, JobScopingException, Exception {
        final File f = new ClassPathResource("/org/dataminx/dts/batch/transfer-1file.xml").getFile();
        final DataCopyActivityDocument dtsJob = TestUtils.getTestDataCopyActivityDocument(f);
        mPartitioningStrategy.setDtsVfsUtil(mDtsVfsUtil);
        mPartitioningStrategy.setMaxTotalByteSizePerStepLimit(-1);
        mPartitioningStrategy.setTotalSizeLimit(-1);
        mPartitioningStrategy.setTotalFilesLimit(-1);
        final String jobId = UUID.randomUUID().toString();
        final String jobTag = jobId;
        mPartitioningStrategy.partitionTheJob(dtsJob.getDataCopyActivity(), jobId, jobTag);
    }

    @Test(groups = { "local-file-transfer-test" }, expectedExceptions = JobScopingException.class)
    public void testMaxTotalFilesLimit() throws IOException, XmlException, JobScopingException, Exception {
        final File f = new ClassPathResource("/org/dataminx/dts/batch/transfer-20files.xml").getFile();
        final DataCopyActivityDocument dtsJob = TestUtils.getTestDataCopyActivityDocument(f);
        mPartitioningStrategy.setDtsVfsUtil(mDtsVfsUtil);
        mPartitioningStrategy.setMaxTotalByteSizePerStepLimit(FILE_SIZE_10MB);
        mPartitioningStrategy.setMaxTotalFileNumPerStepLimit(3);
        mPartitioningStrategy.setTotalSizeLimit(-1);
        mPartitioningStrategy.setTotalFilesLimit(10);
        final FileSystemManager fileSystemManager = VFSUtil.createNewFsManager(false, false, false, false, true, true, false, System.getProperty("java.io.tmpdir"));
        when(mDtsVfsUtil.createNewFsManager()).thenReturn((DefaultFileSystemManager) fileSystemManager);
        final String jobId = UUID.randomUUID().toString();
        final String jobTag = jobId;
        mPartitioningStrategy.partitionTheJob(dtsJob.getDataCopyActivity(), jobId, jobTag);
    }

    @Test(groups = { "local-file-transfer-test" }, expectedExceptions = JobScopingException.class)
    public void testMaxTotalSizeLimit() throws IOException, XmlException, JobScopingException, Exception {
        final File f = new ClassPathResource("/org/dataminx/dts/batch/transfer-1file.xml").getFile();
        final DataCopyActivityDocument dtsJob = TestUtils.getTestDataCopyActivityDocument(f);
        mPartitioningStrategy.setDtsVfsUtil(mDtsVfsUtil);
        mPartitioningStrategy.setMaxTotalByteSizePerStepLimit(FILE_SIZE_10MB);
        mPartitioningStrategy.setMaxTotalFileNumPerStepLimit(3);
        mPartitioningStrategy.setTotalFilesLimit(-1);
        mPartitioningStrategy.setTotalSizeLimit(100);
        final FileSystemManager fileSystemManager = VFSUtil.createNewFsManager(false, false, false, false, true, true, false, System.getProperty("java.io.tmpdir"));
        when(mDtsVfsUtil.createNewFsManager()).thenReturn((DefaultFileSystemManager) fileSystemManager);
        final String jobId = UUID.randomUUID().toString();
        final String jobTag = jobId;
        mPartitioningStrategy.partitionTheJob(dtsJob.getDataCopyActivity(), jobId, jobTag);
    }

    @Test(groups = { "local-file-transfer-test" }, expectedExceptions = JobScopingException.class)
    public void testInvalidTotalSizeLimit() throws IOException, XmlException, JobScopingException, Exception {
        AbstractJobPartitioningStrategy localPs = new AbstractJobPartitioningStrategy() {

            public DtsJobStepAllocator createDtsJobStepAllocator() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        localPs.setDtsVfsUtil(mDtsVfsUtil);
        localPs.setMaxTotalByteSizePerStepLimit(FILE_SIZE_10MB);
        localPs.setMaxTotalFileNumPerStepLimit(3);
        localPs.setTotalFilesLimit(-1);
        localPs.setTotalSizeLimit(-2);
        localPs.afterPropertiesSet();
    }

    @Test(groups = { "local-file-transfer-test" }, expectedExceptions = JobScopingException.class)
    public void testInvalidTotalFilesLimit() throws IOException, XmlException, JobScopingException, Exception {
        AbstractJobPartitioningStrategy localPs = new AbstractJobPartitioningStrategy() {

            public DtsJobStepAllocator createDtsJobStepAllocator() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        localPs.setDtsVfsUtil(mDtsVfsUtil);
        localPs.setMaxTotalByteSizePerStepLimit(FILE_SIZE_10MB);
        localPs.setMaxTotalFileNumPerStepLimit(3);
        localPs.setTotalFilesLimit(-2);
        localPs.setTotalSizeLimit(-1);
        localPs.afterPropertiesSet();
    }

    @Test(groups = { "local-file-transfer-test" }, expectedExceptions = IllegalArgumentException.class)
    public void testNullJobDefinitionParameter() throws JobScopingException {
        final String jobId = UUID.randomUUID().toString();
        final String jobTag = jobId;
        mPartitioningStrategy.partitionTheJob(null, jobId, jobTag);
    }

    @Test(groups = { "local-file-transfer-test" }, expectedExceptions = IllegalArgumentException.class)
    public void testNullJobResourceKeyParameter() throws IOException, XmlException, JobScopingException, Exception {
        final File f = new ClassPathResource("/org/dataminx/dts/batch/transfer-1file.xml").getFile();
        final DataCopyActivityDocument dtsJob = TestUtils.getTestDataCopyActivityDocument(f);
        mPartitioningStrategy.partitionTheJob(dtsJob.getDataCopyActivity(), null, null);
    }
}
