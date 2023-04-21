package com.justin.foundation.job;

public abstract class AbstractAsyncJob extends AbstractJob implements IAsyncJob {

    private static final long serialVersionUID = -2613620116806698948L;

    public AbstractAsyncJob(String jobId) {
        super(jobId);
    }
}
