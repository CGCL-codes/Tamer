package org.dataminx.dts.batch;

import static org.dataminx.dts.batch.common.DtsBatchJobConstants.DTS_DATA_TRANSFER_STEP_KEY;
import static org.dataminx.dts.batch.common.DtsBatchJobConstants.DTS_JOB_DETAILS;
import static org.dataminx.dts.batch.common.DtsBatchJobConstants.DTS_SUBMIT_JOB_REQUEST_KEY;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataminx.dts.batch.common.DtsBatchJobConstants;
import org.dataminx.dts.batch.common.util.ExecutionContextCleaner;
import org.dataminx.dts.common.vfs.DtsVfsUtil;
import org.dataminx.dts.common.vfs.FileSystemManagerCache;
import org.dataminx.schemas.dts.x2009.x07.messages.SubmitJobRequestDocument.SubmitJobRequest;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The <code>DtsFileTransferJobListener</code> is the {@link JobExecutionListener} to the {@link DtsFileTransferJob}.
 *
 * @author Gerson Galang
 */
public class DtsFileTransferJobListener implements JobExecutionListener, InitializingBean {

    /** The logger. */
    private static final Log LOGGER = LogFactory.getLog(DtsFileTransferJobListener.class);

    /** A reference to the FileSystemManagerCache. */
    private FileSystemManagerCache mFileSystemManagerCache;

    /** A reference to the Spring Batch JobExplorer. */
    private JobExplorer mJobExplorer;

    /** A reference to the ExecutionContextCleaner. */
    private ExecutionContextCleaner mExecutionContextCleaner;

    /** A reference to the DtsVfsUtil. */
    private DtsVfsUtil mDtsVfsUtil;

    /**
     * Performs a cleanup of the user's credentials on the Spring Batch DB after the job completes.
     *
     * @param jobExecution the JobExecution
     */
    public void afterJob(final JobExecution jobExecution) {
        mFileSystemManagerCache.clear();
        final List<JobExecution> prevJobExecutions = mJobExplorer.getJobExecutions(jobExecution.getJobInstance());
        for (final JobExecution jobEx : prevJobExecutions) {
            if (!jobEx.equals(jobExecution)) {
                mExecutionContextCleaner.removeJobExecutionContextEntry(jobEx, DTS_JOB_DETAILS);
                mExecutionContextCleaner.removeJobExecutionContextEntry(jobEx, DTS_SUBMIT_JOB_REQUEST_KEY);
                mExecutionContextCleaner.forceRemoveStepExecutionContextEntries(jobEx.getStepExecutions(), new String[] { DTS_DATA_TRANSFER_STEP_KEY });
            }
        }
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            mDtsVfsUtil.clearCachedFileSystemOptions((SubmitJobRequest) jobExecution.getExecutionContext().get(DTS_SUBMIT_JOB_REQUEST_KEY));
            mExecutionContextCleaner.removeJobExecutionContextEntry(jobExecution, DTS_JOB_DETAILS);
            mExecutionContextCleaner.removeJobExecutionContextEntry(jobExecution, DTS_SUBMIT_JOB_REQUEST_KEY);
            removeJobStepFiles(jobExecution.getExecutionContext().getString(DtsBatchJobConstants.DTS_JOB_TAG));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void afterPropertiesSet() throws Exception {
        Assert.state(mFileSystemManagerCache != null, "FileSystemManagerCache has not been set.");
        Assert.state(mJobExplorer != null, "JobExplorer has not been set.");
        Assert.state(mExecutionContextCleaner != null, "ExecutionContextCleaner has not been set.");
    }

    /**
     * {@inheritDoc}
     */
    public void beforeJob(final JobExecution jobExecution) {
    }

    /**
     * Removes the JobStepFiles from the Job Step directory after the job completes.
     *
     * @param jobTag the Job Tag
     */
    private void removeJobStepFiles(final String jobTag) {
        LOGGER.debug("Deleting job step files.");
        final File jobStepDirectory = new File(System.getProperty(DtsBatchJobConstants.DTS_JOB_STEP_DIRECTORY_KEY), jobTag);
        final File[] jobStepFiles = jobStepDirectory.listFiles(dtsStepFilesFilter);
        for (final File jobStepFile : jobStepFiles) {
            jobStepFile.delete();
        }
        boolean deletedOk = jobStepDirectory.delete();
        LOGGER.debug("Deleting job's unique directory: " + deletedOk);
    }

    /**
     * Filter used to delete .dts step files.
     */
    private FilenameFilter dtsStepFilesFilter = new FilenameFilter() {

        public boolean accept(final File dir, final String name) {
            if (name.endsWith("dts")) {
                return true;
            }
            return false;
        }
    };

    public void setExecutionContextCleaner(final ExecutionContextCleaner executionContextCleaner) {
        mExecutionContextCleaner = executionContextCleaner;
    }

    public void setFileSystemManagerCache(final FileSystemManagerCache fileSystemManagerCache) {
        mFileSystemManagerCache = fileSystemManagerCache;
    }

    public void setJobExplorer(final JobExplorer jobExplorer) {
        mJobExplorer = jobExplorer;
    }

    public void setDtsVfsUtil(final DtsVfsUtil dtsVfsUtil) {
        mDtsVfsUtil = dtsVfsUtil;
    }
}
