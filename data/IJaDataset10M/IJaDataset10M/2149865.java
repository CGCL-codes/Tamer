package org.dataminx.dts.batch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.proposal.dmi.schemas.dts.x2010.dmiCommon.DataCopyActivityType;

/**
 * This class holds all the details about the DTS Job that Spring Batch will go through to process the data transfer
 * requests. It is storred in the Job ExecutionContext and so the data in this
 * object will be available to future steps.
 *
 * @author Gerson Galang
 * @author David Meredith (modifications)
 */
public class DtsJobDetails implements Serializable {

    /** The serial version UID needed to serialize this class. */
    private static final long serialVersionUID = 1L;

    /** The list of DtsJobSteps to be processed by the job.
     TODO: remove this list and use the steps written to file rather than rely on an in-mem collection */
    private List<DtsJobStep> mJobSteps;

    /** The current size of files in bytes that have been transferred at a given time. */
    private int mBytesTransferred;

    /** The total size of files to be transferred by the job. */
    private long mTotalBytes;

    /** The total number of files to be transferred by the job. */
    private int mTotalFiles;

    private DataCopyActivityType mDataCopyActivity;

    /** The job resource key. */
    private String mJobId;

    /** The job tag. */
    private String mJobTag;

    /** The directory where all this job's job steps are written */
    private String mRootJobDir;

    /** The list of files that have been excluded from the job and won't be transferred. */
    private List<String> mExcludedFiles = new ArrayList<String>();

    /**
     * For every distinct ROOT URI that appears in all the DataTransfer elements
     * in a document (both sources and sinks), the element that defines the MAX
     * number of files to copy is stored against the ROOT URI (the key). 
     *
     * <br/>
     * For example, if 2 source elements are defined which
     * have a common ROOT URI (gridftp://host1.dl.ac.uk), then the element
     * that has the highest file count value will have its
     * file count stored in the map against the common ROOT URI. The common sink
     * ROOT URI (gridftp://host2.dl.ac.uk) will also have this maximum value
     * (illustrated below):
     *
     * <pre>
     * Given the following job document:
     *
     *   <DataTransfer>
     *    <sourceURI> gridftp://host1.dl.ac.uk/some/dir/200files </sourceURI>
     *    <targetURI> gridftp://host2.dl.ac.uk/sink/dir </targetURI>
     *   </DataTransfer>
     *   <DataTransfer>
     *     <sourceURI> gridftp://host1.dl.ac.uk/some/other/dir/400files </sourceURI>
     *     <targetURI> gridftp://host2.dl.ac.uk/sink/dir </targetURI>
     *   </DataTransfer>
     *
     * Results in two map entries:
     * Map entry 1 (<gridftp://host1.dl.ac.uk> , <400>)   [400 files max from DataTransfer element 2]
     * Map entry 2 (<gridftp://host2.dl.ac.uk> , <400>)   [400 files max to sink]
     *
     * Important: The map DOES NOT hold the TOTAL number of files to copied
     * from each source/sink ROOT URI (this is very different - which would result in
     * the following map entires):
     * Erronous Map entry 1 (<gridftp://host1.dl.ac.uk> , <600>)   [200+400 = 600 files max from DataTransfer element 1 + 2]
     * Erronous Map entry 2 (<gridftp://host2.dl.ac.uk> , <600>)   [600 files max to sink]
     *
     * </pre>
     */
    private final Map<String, Integer> mSourceTargetMaxTotalFilesToTransfer = new HashMap<String, Integer>();

    /**
     * The DtsJobDetails constructor.
     */
    public DtsJobDetails() {
        mJobSteps = new ArrayList<DtsJobStep>();
        mExcludedFiles = new ArrayList<String>();
        mJobId = "";
    }

    /**
     * Increment the current size of files that have been tranferred by bytesTransferred.
     *
     * @param bytesTransferred the size of file in bytes
     */
    public synchronized void addBytesTransferred(final int bytesTransferred) {
        mBytesTransferred += bytesTransferred;
    }

    public int getBytesTransferred() {
        return mBytesTransferred;
    }

    public List<String> getExcludedFiles() {
        return mExcludedFiles;
    }

    public DataCopyActivityType getDataCopyActivity() {
        return this.mDataCopyActivity;
    }

    public String getJobId() {
        return mJobId;
    }

    public String getJobTag() {
        return mJobTag;
    }

    public List<DtsJobStep> getJobSteps() {
        return mJobSteps;
    }

    public Map<String, Integer> getSourceTargetMaxTotalFilesToTransfer() {
        return mSourceTargetMaxTotalFilesToTransfer;
    }

    public long getTotalBytes() {
        return mTotalBytes;
    }

    public int getTotalFiles() {
        return mTotalFiles;
    }

    public boolean isCompleted() {
        return mBytesTransferred == mTotalBytes;
    }

    public void setJobSteps(final List<DtsJobStep> jobSteps) {
        mJobSteps = jobSteps;
    }

    public void setExcludedFiles(final List<String> excludedFiles) {
        mExcludedFiles = excludedFiles;
    }

    public void setDataCopyActivity(final DataCopyActivityType dataCopyActivity) {
        this.mDataCopyActivity = dataCopyActivity;
    }

    public void setJobId(final String jobId) {
        mJobId = jobId;
    }

    public void setJobTag(final String jobTag) {
        mJobTag = jobTag;
    }

    public void setTotalBytes(final long totalBytes) {
        mTotalBytes = totalBytes;
    }

    public void setTotalFiles(final int totalFiles) {
        mTotalFiles = totalFiles;
    }

    public void setRootJobDir(String rootJobDir) {
        this.mRootJobDir = rootJobDir;
    }

    public String getRootJobDir() {
        return this.mRootJobDir;
    }
}
