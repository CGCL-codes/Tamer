package com.luzan.common.processor;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ProcessorJob implements Job {

    protected static Logger logger = Logger.getLogger(ProcessorJob.class);

    public ProcessorJob() {
    }

    @SuppressWarnings({ "unchecked" })
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            JobDataMap jdMap = jobExecutionContext.getJobDetail().getJobDataMap();
            String processorUID = jdMap.getString("processorUID");
            ProcessorListener.CommandType command = (ProcessorListener.CommandType) jdMap.get("command");
            ProcessorNotifierClient.notifyProcessor(processorUID, command);
        } catch (Throwable e) {
            if (e instanceof JobExecutionException) throw (JobExecutionException) e; else throw new JobExecutionException("Scheduled Processor error.", e);
        }
    }
}
