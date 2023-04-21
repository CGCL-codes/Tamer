package net.sourceforge.gateway.sstp.scheduler.jobs;

import java.io.File;
import java.util.Calendar;
import net.sourceforge.gateway.sstp.utilities.Utils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LogPurgeJob implements Job {

    protected static final Logger LOG = Logger.getLogger("net.sourceforge.gateway");

    public LogPurgeJob() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.warn("Log Purge Job Started");
        String filePath = Utils.getFilePath();
        File dir = new File(filePath);
        String[] files = dir.list();
        Calendar cal = Calendar.getInstance();
        cal.add(cal.DAY_OF_MONTH, -30);
        int count = 0;
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            if (fileName.contains("gateway.log.") || fileName.contains("server.log.")) {
                File f = new File(filePath + File.separator + fileName);
                if (f.lastModified() <= cal.getTimeInMillis()) {
                    if (f.delete()) {
                        count++;
                    } else {
                        LOG.warn("Unable to delete log file: " + fileName);
                    }
                }
            }
        }
        LOG.warn("Erased " + count + " log files.");
        LOG.warn("Log Purge Job Ended");
    }
}
