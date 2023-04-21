package com.nimbusinformatics.genomicstransfer;

public abstract class AbstractOperation implements Operation, Runnable {

    private Exception exception;

    private int progress;

    private int state = IN_PROGRESS;

    private String status;

    public void run() {
        try {
            execute();
            state = COMPLETED;
        } catch (Exception e) {
            exception = e;
            state = FAILED;
        }
    }

    public Exception getException() {
        return exception;
    }

    public int getProgress() {
        return progress;
    }

    public int getState() {
        return state;
    }

    public String getStatus() {
        return status;
    }

    protected abstract void execute() throws Exception;

    protected void setProgress(int progress) {
        this.progress = progress;
    }

    protected void setStatus(String status) {
        this.status = status;
    }
}
