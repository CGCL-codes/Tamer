package org.vqwiki;

import org.apache.log4j.Logger;
import org.vqwiki.persistency.file.FileChangeLog;
import org.vqwiki.persistency.file.FileReminders;
import org.vqwiki.utils.WikiRemindFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A monitor which should persistently run on the VQWiki server.
 * It schedules a new process once every 24 hours to check all
 * WikiReminder files for the presence of scheduled reminders, which
 * it then send via email to the registered recipient(s). The time
 * of day at which the recurring process runs is dependent upon the
 * time at which the program was instantiated.
 *
 * @author Robert E Brewer
 * @version 0.1
 */
public class ReminderMonitor {

    private static final long ONE_DAY = 1000 * 60 * 60 * 24;

    protected static Logger logger = Logger.getLogger(ReminderMonitor.class);

    /**
     * Creates a new ReminderMonitor object and notifies the operator.
     */
    public static void main(String[] argv) {
        new ReminderMonitor();
        logger.info("The VQWiki Reminder Monitor is running...");
    }

    /**
     * Schedules a sendReminderTask at a fixed rate of 24 hours.
     */
    public ReminderMonitor() {
        Timer timer = new Timer();
        sendReminderTask aTask = new sendReminderTask();
        timer.scheduleAtFixedRate(aTask, 0, ONE_DAY);
    }

    /**
     * Analyzes reminder files (".rmd") and sends scheduled reminders via email
     * to registered addresses.
     */
    class sendReminderTask extends TimerTask {

        /**
         *
         */
        public void run() {
            Date todaysDate = new Date();
            FilenameFilter remindFilter = new WikiRemindFilter();
            File dir = new File(Environment.getValue(Environment.PROP_FILE_HOME_DIR));
            File[] reminderFiles = dir.listFiles(remindFilter);
            if (reminderFiles != null) {
                for (int i = 0; i < reminderFiles.length; i++) {
                    if (reminderFiles[i].isFile()) {
                        try {
                            Reminders aReminder = new FileReminders(reminderFiles[i]);
                            aReminder.sendReminders(todaysDate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
