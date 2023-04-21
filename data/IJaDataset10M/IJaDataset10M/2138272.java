package com.protest.temp;

import java.util.Map;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class QuratzJob implements StatefulJob {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("我被调用了");
        Map map = jobExecutionContext.getJobDetail().getJobDataMap();
        System.out.println("name:" + jobExecutionContext.getJobDetail().getName() + "message:" + map.get("message"));
        map.put("message", "wokaoakoa");
    }
}
