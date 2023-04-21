package com.google.api.adwords.v201109.cm;

public interface MutateJobServiceInterface extends java.rmi.Remote {

    /**
     * Query the status of existing jobs, both simple and bulk API.<br/>
     * Use a {@link JobSelector} to query and return a list which may
     *         contain both {@link BulkMutateJob} and/or {@link SimpleMutateJob}.
     * 
     *         <p>This method will limit the number of returned results to
     * the most
     *         recent 100 jobs. You should use a selector which will return
     * a reasonable
     *         number of jobs in the result.
     */
    public com.google.api.adwords.v201109.cm.Job[] get(com.google.api.adwords.v201109.cm.JobSelector selector) throws java.rmi.RemoteException, com.google.api.adwords.v201109.cm.ApiException;

    /**
     * Query mutation results, of a {@code COMPLETED} job.<br/>
     *         Use a {@link JobSelector} to query and return either a
     *         {@link BulkMutateResult} or a {@link SimpleMutateResult}.
     */
    public com.google.api.adwords.v201109.cm.JobResult getResult(com.google.api.adwords.v201109.cm.JobSelector selector) throws java.rmi.RemoteException, com.google.api.adwords.v201109.cm.ApiException;

    /**
     * Simplified way of submitting a mutation job. The provided list
     * of
     *         operations, if valid, will create a new job with a unique
     * id, which will
     *         be returned. This id can later be used in invocations of
     *         {@link #get} and {@link #getResult}.
     *         policy can optionally be specified.
     *         
     *         <p>When this method returns with success, the job will be
     * in
     *         {@code PROCESSING} or {@code PENDING} state and no further
     * action is
     *         needed for the job to get executed.<br/>
     *         You should not use the returned {@link BulkMutateJobId} with
     * bulk API
     *         {@link BulkMutateJobService#mutate} method.</p>
     */
    public com.google.api.adwords.v201109.cm.SimpleMutateJob mutate(com.google.api.adwords.v201109.cm.Operation[] operations, com.google.api.adwords.v201109.cm.BulkMutateJobPolicy policy) throws java.rmi.RemoteException, com.google.api.adwords.v201109.cm.ApiException;
}
