package org.streets.extention.quartz;

import java.util.Collection;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.slf4j.Logger;
import org.streets.extention.quartz.internal.QuartzSchedulerManagerImpl;

/**
 * @author dzb
 */
public class QuartzModule {

    /**
     * bind the <a href="http://www.opensymphony.com/quartz/">Quartz</a> based scheduler manager.
     *
     * @param schedulerFactory     the scheduler factory
     * @param jobSchedulingBundles list of job detail and trigger bundles
     *
     * @return scheduler manager
     */
    @EagerLoad
    public static QuartzSchedulerManager buildSchedulerManager(Logger logger, RegistryShutdownHub shutdownHub, final Collection<QuartzJob> quartzJobs) {
        final QuartzSchedulerManager manager = new QuartzSchedulerManagerImpl(logger, quartzJobs);
        shutdownHub.addRegistryShutdownListener(new RegistryShutdownListener() {

            public void registryDidShutdown() {
                manager.shutdown();
            }
        });
        return manager;
    }
}
