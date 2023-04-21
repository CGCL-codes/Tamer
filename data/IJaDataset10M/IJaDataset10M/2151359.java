package org.jumpmind.symmetric.job;

import org.jumpmind.symmetric.ISymmetricEngine;
import org.jumpmind.symmetric.service.ClusterConstants;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class DataGapPurgeJob extends AbstractJob {

    public DataGapPurgeJob(ISymmetricEngine engine, ThreadPoolTaskScheduler taskScheduler) {
        super("job.purge.datagaps", true, engine.getParameterService().is("start.purge.job"), engine, taskScheduler);
    }

    @Override
    public long doJob() throws Exception {
        return engine.getPurgeService().purgeDataGaps();
    }

    public String getClusterLockName() {
        return ClusterConstants.PURGE_DATA_GAPS;
    }

    public boolean isClusterable() {
        return true;
    }
}
