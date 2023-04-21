package com.shine.framework.LogUtil.model;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import com.shine.framework.JobUtil.model.QuartzJob;
import com.shine.framework.core.util.DateUtil;
import com.shine.framework.core.util.FileUtil;

/**
 * LogFileJob 定时生成log文件,每天生成一个
 * 
 * @author viruscodecn@gmail.com
 * @project JavaFramework 2.0 2010-11-30
 */
public class LogFileJob implements QuartzJob {

    private String logFolder;

    public LogFileJob(String logFolder) {
        this.logFolder = logFolder;
    }

    @Override
    public Trigger createTrigger() {
        Trigger trigger = TriggerUtils.makeDailyTrigger(0, 0);
        trigger.setName("LogFile");
        return trigger;
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        if (logFolder != null && logFolder.length() != 0) {
            FileUtil.createFolder(logFolder);
            FileUtil.createFile(logFolder + "\\" + DateUtil.getCurrentDate() + ".log");
        } else {
            FileUtil.createFile(DateUtil.getCurrentDate() + ".log");
        }
    }
}
