package jws.ejb;

import jws.core.jobs.IScheduledJob;
import jws.core.jobs.JobException;
import java.io.Serializable;

public class JobWrapper implements Serializable {

    private long _create_ts;

    private long _count;

    private IScheduledJob _job;

    public JobWrapper(IScheduledJob job) {
        _job = job;
        _create_ts = System.currentTimeMillis();
        _count = 0;
    }

    public long getCreateTimestamp() {
        return _create_ts;
    }

    public long getExecCount() {
        return _count;
    }

    public IScheduledJob getJob() {
        return _job;
    }

    public void exec() throws JobException {
        _count++;
        _job.run();
    }
}
