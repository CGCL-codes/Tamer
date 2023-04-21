package com.j2biz.blogunity.services;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import com.j2biz.blogunity.exception.SystemException;
import com.j2biz.blogunity.jobs.RefererAndVisitedPagesRemover;
import com.j2biz.blogunity.pojo.SystemConfiguration;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class SchedulingService extends Service {

    private static final Log log = LogFactory.getLog(SchedulingService.class);

    private static SchedulingService SINGLETON = null;

    private Scheduler scheduler;

    public static SchedulingService getInstance() {
        if (SINGLETON == null) SINGLETON = new SchedulingService();
        return SINGLETON;
    }

    private SchedulingService() {
    }

    public void init(SystemConfiguration config) throws SystemException {
        if (log.isInfoEnabled()) {
            log.info("Initializing service: " + getName());
        }
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            scheduleJobs();
        } catch (SchedulerException e) {
            log.error("Error initializing " + getName(), e);
            throw new SystemException(e);
        }
    }

    public void destroy(SystemConfiguration config) throws SystemException {
        if (log.isInfoEnabled()) {
            log.info("Destroying service:" + getName());
        }
        try {
            if (scheduler != null) scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error("Error destroying " + getName(), e);
            throw new SystemException(e);
        }
    }

    private void scheduleJobs() throws SchedulerException {
        JobDetail jobDetail = new JobDetail("RefererAndVisitedPagesRemover", Scheduler.DEFAULT_GROUP, RefererAndVisitedPagesRemover.class);
        Date d = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        if (log.isDebugEnabled()) {
            log.debug("\tExecute RefererAndVisitedPagesRemover for the first time at " + calendar.getTime());
        }
        int millis = 24 * 60 * 60 * 1000;
        SimpleTrigger trigger = new SimpleTrigger("RefererAndVisitedPagesTrigger", Scheduler.DEFAULT_GROUP, calendar.getTime(), null, SimpleTrigger.REPEAT_INDEFINITELY, millis);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void addJob(JobDetail job, Trigger trigger) throws SchedulerException {
        scheduler.scheduleJob(job, trigger);
    }
}
