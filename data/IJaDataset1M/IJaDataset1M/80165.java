package org.dataminx.dts.ws.service;

import org.dataminx.dts.ws.model.Job;
import org.dataminx.schemas.dts.x2009.x07.messages.CancelJobRequestDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.GetJobDetailsRequestDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.GetJobStatusRequestDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.ResumeJobRequestDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.SubmitJobRequestDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.SuspendJobRequestDocument;

/**
 * This specifies the operations supported by the Data Transfer Service WS. The
 * reason why the methods take in the Request objects is because they are needed
 * by the Worker Node Manager and there's no easy way of getting the Request objects
 * from their child elements.
 *
 * @author Gerson Galang
 */
public interface DataTransferService {

    /**
     * Process the submit job request.
     *
     * @param submitJobRequest the submit job request
     * @return the Universally Unique Identifier (aka Global Unique Identifier)
     *         for the submitted job
     */
    String submitJob(SubmitJobRequestDocument submitJobRequest);

    /**
     * Process the cancel job request.
     *
     * @param cancelJobRequest the cancel job request
     */
    void cancelJob(CancelJobRequestDocument cancelJobRequest);

    /**
     * Process the suspend job request.
     *
     * @param suspendJobRequest the suspend job request
     */
    void suspendJob(SuspendJobRequestDocument suspendJobRequest);

    /**
     * Process the resume job request.
     *
     * @param resumeJobRequest the resume job request
     */
    void resumeJob(ResumeJobRequestDocument resumeJobRequest);

    /**
     * Process the get job status request. The GetJobStatusRequest object
     * doesn't get passed down to the Worker Node. The only reason why this
     * method has been defined this way is so it uses the same standard as the
     * rest of the methods provided by the web service.
     *
     * @param getJobStatusRequest the get job status request
     * @return the job status
     */
    String getJobStatus(GetJobStatusRequestDocument getJobStatusRequest);

    /**
     * Process the get job details request. Same as the getJobStatus(), this
     * method doesn't pass the request all the way to the workernode as all of
     * the information that this method needs to provide are already in the WS
     * database.
     *
     * @param getJobDetailsRequest the get job details request
     * @return the detailed information about the job
     */
    Job getJobDetails(GetJobDetailsRequestDocument getJobDetailsRequest);
}
