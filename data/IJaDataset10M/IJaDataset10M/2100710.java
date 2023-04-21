package com.luxmedien.jbox.scheduling;

import java.text.ParseException;
import java.util.Properties;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManager {

    private Scheduler scheduler;

    private Properties properties;

    /**
	 * Creates a scheduler that checks every minute for new directories
	 * 
	 */
    public QuartzManager() {
    }

    public void setConfiguration(Properties pProperties) {
        properties = pProperties;
    }

    public void start() {
        SchedulerFactory lSF = new StdSchedulerFactory();
        try {
            scheduler = lSF.getScheduler();
            scheduler.start();
            CronTrigger lJobTrigger = new CronTrigger("Timestamp", "default");
            lJobTrigger.setCronExpression("0/30 * * * * ?");
            scheduler.scheduleJob(createJobDetail(), lJobTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private JobDetail createJobDetail() {
        JobDetail lDetail = new JobDetail("Timestamp", "default", TimestampCheckJob.class);
        lDetail.getJobDataMap().put("startuptime", System.currentTimeMillis());
        lDetail.getJobDataMap().put("config", properties);
        return lDetail;
    }
}
