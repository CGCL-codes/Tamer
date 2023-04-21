package org.itracker.web.scheduler.tasks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import javax.mail.internet.InternetAddress;
import org.apache.log4j.Logger;
import org.itracker.model.Issue;
import org.itracker.model.Notification;
import org.itracker.model.Notification.Type;
import org.itracker.services.ConfigurationService;
import org.itracker.services.IssueService;
import org.itracker.services.NotificationService;
import org.itracker.services.util.IssueUtilities;
import org.itracker.web.util.ServletContextUtils;

/**
  * This class can be used to send reminder emails to owners/admins
  * that issues need their attention.
  * @see SchedulableTask
  */
public class ReminderNotification extends BaseJob {

    public static final String DEFAULT_BASE_URL = "http://localhost:8080/itracker";

    public static final int DEFAULT_ISSUE_AGE = 30;

    private final Logger logger;

    public ReminderNotification() {
        this.logger = Logger.getLogger(getClass());
    }

    /**
      * This method is called by the scheduler to send the reminder
      * notifications.  The arguments can be used to configure which issues
      * and projects are included in the notifications.  The args should
      * include as the first parameter the base url of the server including
      * the scheme, hostname, port, and context.  For example:
      * <br>
      * http://localhost:8080/itracker
      * <br>
      * If no other arguments are supplied it sends reminders to all
      * owners/admins of unresolved issues in all projects that have not been
      * modified in 30 days.  The second element of the array can be a number
      * that represents the number of days to use to check the last modified
      * date.  The third optional element is a number that represents the project
      * id to limit the notifications to. A fourth optional argument is the severity
      * to send the notification for.
      * @param args optional arguments to configure the notification messages
      * @see SchedulableTask#performTask
      */
    public void performTask(String[] args) {
        List<Issue> issues;
        String baseURL = DEFAULT_BASE_URL;
        int issueAge = DEFAULT_ISSUE_AGE;
        int projectId = -1;
        int severity = -1;
        ConfigurationService configurationService = ServletContextUtils.getItrackerServices().getConfigurationService();
        if (args != null) {
            if (args.length > 0 && args[0] != null) {
                baseURL = args[0];
            }
            if (null == baseURL) {
                baseURL = configurationService.getSystemBaseURL();
            }
            if (args.length > 1) {
                try {
                    issueAge = Integer.parseInt(args[1]);
                } catch (NumberFormatException nfe) {
                    logger.debug("Invalid issue age specified in ReminderNotification task.");
                }
            }
            if (args.length > 2) {
                try {
                    projectId = Integer.parseInt(args[2]);
                } catch (NumberFormatException nfe) {
                    logger.debug("Invalid projectId specified in ReminderNotification task.");
                }
            }
            if (args.length > 3) {
                try {
                    severity = Integer.parseInt(args[3]);
                } catch (NumberFormatException nfe) {
                    logger.debug("Invalid severity specified in ReminderNotification task.");
                }
            }
        }
        logger.debug("Reminder Notifications being sent for project " + projectId + " with issues over " + issueAge + " days old with severity " + severity + ".  Base URL = " + baseURL);
        try {
            IssueService issueService = ServletContextUtils.getItrackerServices().getIssueService();
            NotificationService notificationService = ServletContextUtils.getItrackerServices().getNotificationService();
            GregorianCalendar cal = new GregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH, 0 - issueAge);
            Date oldDate = cal.getTime();
            Date currentDate = new Date();
            if (projectId > 0) {
                issues = issueService.getIssuesByProjectId(projectId, IssueUtilities.STATUS_RESOLVED);
            } else {
                issues = issueService.getIssuesWithStatusLessThan(IssueUtilities.STATUS_RESOLVED);
            }
            if (issues != null && issues.size() > 0) {
                for (int i = 0; i < issues.size(); i++) {
                    if (severity >= 0 && issues.get(i).getSeverity() != severity) {
                        continue;
                    }
                    if (issues.get(i).getLastModifiedDate() != null && issues.get(i).getLastModifiedDate().before(oldDate)) {
                        HashSet<InternetAddress> addresses = new HashSet<InternetAddress>();
                        long numMillis = currentDate.getTime() - issues.get(i).getLastModifiedDate().getTime();
                        int numDays = (int) (numMillis / (24 * 60 * 60 * 1000));
                        List<Notification> notifications = notificationService.getPrimaryIssueNotifications(issues.get(i));
                        for (int j = 0; j < notifications.size(); j++) {
                            if (notifications.get(j).getUser().getEmail() != null && notifications.get(j).getUser().getEmail().indexOf('@') >= 0) {
                                addresses.add(notifications.get(j).getUser().getEmailAddress());
                            }
                        }
                        InternetAddress[] addressesArray = new ArrayList<InternetAddress>(addresses).toArray(new InternetAddress[] {});
                        if (logger.isDebugEnabled()) {
                            logger.debug("Sending reminder notification for issue " + issues.get(i).getId() + " to " + addressesArray.length + " users.");
                        }
                        notificationService.sendNotification(issues.get(i), Type.ISSUE_REMINDER, baseURL, addressesArray, numDays);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error sending reminder notifications. Message: ", e);
            throw new RuntimeException("failed to send reminder notifications.", e);
        }
    }
}
