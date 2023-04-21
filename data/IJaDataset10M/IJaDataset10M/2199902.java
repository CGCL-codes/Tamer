package net.jforum.summary;

import java.io.IOException;
import java.text.ParseException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Schedule the summaries to be sent to the users.
 * 
 * @see net.jforum.summary.SummaryJob
 * 
 * @author Franklin S. Dattein (<a href="mailto:franklin@portaljava.com">franklin@portaljava.com</a>)
 * @version $Id: SummaryScheduler.java,v 1.6 2006/10/10 00:40:53 rafaelsteil Exp $
 */
public class SummaryScheduler {

    private static Scheduler scheduler;

    private static Logger logger = Logger.getLogger(SummaryScheduler.class);

    private static boolean isStarted;

    /**
	 * Starts the summary Job. Conditions to start: Is not started yet and is enabled on the file
	 * SystemGlobasl.properties. The to enable it is "summary.enabled"
	 * (ConfigKeys.SUMMARY_IS_ENABLED).
	 * 
	 * @throws SchedulerException
	 * @throws IOException
	 */
    public static void startJob() throws SchedulerException {
        boolean isEnabled = SystemGlobals.getBoolValue(ConfigKeys.SUMMARY_IS_ENABLED);
        if (!isStarted && isEnabled) {
            String filename = SystemGlobals.getValue(ConfigKeys.QUARTZ_CONFIG);
            String cronExpression = SystemGlobals.getValue("org.quartz.context.summary.cron.expression");
            scheduler = new StdSchedulerFactory(filename).getScheduler();
            Trigger trigger = null;
            try {
                trigger = new CronTrigger(SummaryJob.class.getName(), "summaryJob", cronExpression);
                logger.info("Starting quartz summary expression " + cronExpression);
                scheduler.scheduleJob(new JobDetail(SummaryJob.class.getName(), "summaryJob", SummaryJob.class), trigger);
                scheduler.start();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        isStarted = true;
    }
}
