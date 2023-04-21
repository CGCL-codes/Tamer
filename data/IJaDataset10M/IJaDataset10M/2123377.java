package com.liferay.twitter.job;

import com.liferay.portal.kernel.job.JobSchedulerUtil;
import com.liferay.portal.kernel.job.Scheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="TwitterScheduler.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TwitterScheduler implements Scheduler {

    public void schedule() {
        if (SynchronizeTwitterJob.INTERVAL <= 0) {
            if (_log.isDebugEnabled()) {
                _log.debug("Synchronization of Twitter is disabled");
            }
            _synchronizeTwitterJob = null;
        }
        JobSchedulerUtil.schedule(_synchronizeTwitterJob);
    }

    public void unschedule() {
        JobSchedulerUtil.unschedule(_synchronizeTwitterJob);
    }

    private static Log _log = LogFactory.getLog(TwitterScheduler.class);

    private SynchronizeTwitterJob _synchronizeTwitterJob = new SynchronizeTwitterJob();
}
