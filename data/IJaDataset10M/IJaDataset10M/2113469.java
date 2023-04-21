package net.codesmarts.log4j;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Bug Report appender that sends BugReports via email
 * @author Fred McCann
 */
public class EmailBugReportAppender extends AbstractEmailBugReportAppender {

    /**
     * Layout for the html portion of the message
     */
    private Layout htmlLayout = new org.apache.log4j.HTMLLayout();

    /**
     * @see net.codesmarts.log4j.AbstractEmailBugReportAppender#getMessage(net.codesmarts.log4j.BugReport)
     */
    public String getMessage(BugReport report) {
        return getMessage(report, getLayout());
    }

    /**
     * @see net.codesmarts.log4j.AbstractEmailBugReportAppender#getHTMLMessage(net.codesmarts.log4j.BugReport)
     */
    public String getHTMLMessage(BugReport report) {
        return getMessage(report, htmlLayout);
    }

    /**
     * Formate report with specified layout
     * @param report the bug report
     * @param layout
     */
    private String getMessage(BugReport report, Layout layout) {
        StringBuffer message = new StringBuffer();
        List events = report.getEvents();
        LoggingEvent event = null;
        Iterator i = events.iterator();
        while (i.hasNext()) {
            event = (LoggingEvent) i.next();
            message.append(getLayout().format(event));
        }
        return message.toString();
    }

    /**
     * @see net.codesmarts.log4j.AbstractEmailBugReportAppender#getSubject(net.codesmarts.log4j.BugReport)
     */
    public String getSubject(BugReport report) {
        String subject = null;
        List events = report.getEvents();
        LoggingEvent event = null;
        Iterator i = events.iterator();
        while (i.hasNext() && subject == null) {
            event = (LoggingEvent) i.next();
            if (event.getLevel().isGreaterOrEqual(this.getThresholdPriority())) subject = event.getMessage().toString();
        }
        return subject;
    }

    /**
     * @param htmlLayout The htmlLayout to set.
     */
    public void setHtmlLayout(Layout htmlLayout) {
        this.htmlLayout = htmlLayout;
    }
}
