package com.sefer.dragonfly.client.core.domain.scheduler;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.sefer.dragonfly.client.core.domain.lifecycle.BaseLifecycle;

public abstract class JobAdaptor<K, V> extends BaseLifecycle implements Job<K, V> {

    private static Logger logger = Logger.getLogger(JobAdaptor.class);

    private JobContext<K, V> context;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            beforeExec();
            execute();
            afterExec();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new JobExecutionException("" + e.getMessage(), e);
        }
    }

    public JobContext<K, V> getJobContext() {
        return context;
    }

    public void setJobContext(JobContext<K, V> context) {
        this.context = context;
    }
}
