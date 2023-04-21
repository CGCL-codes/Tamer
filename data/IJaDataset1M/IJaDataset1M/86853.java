package ru.fizteh.fivt.JobProcessor;

public interface ProcessorPrx extends Ice.ObjectPrx {

    public String submitProblem(String problemName, Parameter[] inParams) throws TooManyJobsException;

    public String submitProblem(String problemName, Parameter[] inParams, java.util.Map<String, String> __ctx) throws TooManyJobsException;

    public String getJobStatus(String id) throws UnknownJobIdException;

    public String getJobStatus(String id, java.util.Map<String, String> __ctx) throws UnknownJobIdException;

    public Parameter[] getJobResult(String id) throws JobException, NotReadyException, UnknownJobIdException;

    public Parameter[] getJobResult(String id, java.util.Map<String, String> __ctx) throws JobException, NotReadyException, UnknownJobIdException;

    public void cancelJob(String id) throws UnknownJobIdException;

    public void cancelJob(String id, java.util.Map<String, String> __ctx) throws UnknownJobIdException;

    public Parameter[] solve(Problem theProblem) throws JobException, TooManyJobsException;

    public Parameter[] solve(Problem theProblem, java.util.Map<String, String> __ctx) throws JobException, TooManyJobsException;

    public void solve_async(AMI_Processor_solve __cb, Problem theProblem);

    public void solve_async(AMI_Processor_solve __cb, Problem theProblem, java.util.Map<String, String> __ctx);

    public JobInfo getJobInfo(String id) throws UnknownJobIdException;

    public JobInfo getJobInfo(String id, java.util.Map<String, String> __ctx) throws UnknownJobIdException;

    public java.util.Map<java.lang.String, JobInfo> getListOfJobs();

    public java.util.Map<java.lang.String, JobInfo> getListOfJobs(java.util.Map<String, String> __ctx);

    public JobPrx getJob(Problem theProblem) throws TooManyJobsException;

    public JobPrx getJob(Problem theProblem, java.util.Map<String, String> __ctx) throws TooManyJobsException;

    public java.util.Map<java.lang.String, java.lang.String> getSliceChecksums();

    public java.util.Map<java.lang.String, java.lang.String> getSliceChecksums(java.util.Map<String, String> __ctx);

    public java.util.Map<java.lang.String, java.lang.String> getProperties();

    public java.util.Map<java.lang.String, java.lang.String> getProperties(java.util.Map<String, String> __ctx);
}
