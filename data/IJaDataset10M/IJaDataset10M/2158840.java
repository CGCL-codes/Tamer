package com.herestudio;

import java.util.Date;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzPractice {

    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory schedFact = new StdSchedulerFactory();
        Scheduler sched = schedFact.getScheduler();
        sched.start();
        JobDetail jobDetail = new JobDetail("myJob", sched.DEFAULT_GROUP, DumbJob.class);
        jobDetail.setName("test");
        jobDetail.setGroup("luzm");
        Trigger trigger = TriggerUtils.makeSecondlyTrigger(5);
        trigger.setStartTime(TriggerUtils.getEvenHourDate(new Date()));
        trigger.setStartTime(new Date());
        trigger.setName("myTrigger");
        sched.scheduleJob(jobDetail, trigger);
    }
}
