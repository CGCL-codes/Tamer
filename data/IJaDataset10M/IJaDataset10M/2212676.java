package edu.uga.galileo.voci.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import edu.uga.galileo.voci.mail.EmailManager;
import edu.uga.galileo.voci.model.Configuration;

/**
 * An implementation of the {@link edu.uga.galileo.voci.logging.LogListener}
 * that will email <code>ERROR</code> and <code>FATAL</code> log messages
 * to the user(s) defined in the deployment descriptor. 
 * 
 * @author <a href="mailto:mdurant@uga.edu">Mark Durant</a>
 * @version 1.0
 */
public class EmailAlertListener implements LogListener {

    private boolean shutDown = false;

    /**
	 * @see edu.uga.galileo.voci.logging.LogListener#handleMessage(java.lang.String, int, java.lang.Throwable)
	 */
    public void handleMessage(String msg, int level, Throwable e) {
        if (!shutDown) {
            if (Configuration.getString("enableErrorEmailAlerts").equals("false")) {
                shutDown = true;
            } else {
                if (level >= Logger.ERROR) {
                    String subject = Configuration.getString("instanceAddress") + " / " + Configuration.getString("instanceName");
                    StringBuffer message = new StringBuffer();
                    if (level == Logger.ERROR) {
                        subject += " error";
                        message.append("An ");
                    } else {
                        subject += " FATAL error";
                        message.append("A FATAL ");
                    }
                    message.append("error occurred in the ");
                    message.append(Configuration.getString("instanceAddress"));
                    message.append(" instance of the ");
                    message.append(Configuration.getString("instanceName"));
                    message.append(" server.<br/><br/>");
                    message.append("-----------------------<br/><br/>");
                    message.append("<b>Message:</b><br/>");
                    message.append(msg);
                    message.append("<br/><br/>");
                    if (e != null) {
                        message.append("-----------------------<br/><br/>");
                        message.append("<b>Stack Trace:</b><br/>");
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        message.append("<pre>");
                        message.append(sw.toString());
                        message.append("</pre><br/><br/>");
                    }
                    String[] to = null, cc = null, bcc = null;
                    String temp, from = Configuration.getString("adminEmailReplyTo");
                    if ((temp = Configuration.getString("errorEmailRecipients")) != null) {
                        to = temp.split(",");
                    }
                    if ((temp = Configuration.getString("errorCCRecipients")) != null) {
                        cc = temp.split(",");
                    }
                    if ((temp = Configuration.getString("errorBCCRecipients")) != null) {
                        bcc = temp.split(",");
                    }
                    Logger.debug("sending email from " + from + " to " + to + " and " + cc + " and " + bcc);
                    try {
                        EmailManager.sendMessage(from, to, cc, bcc, subject, message.toString(), true);
                    } catch (AddressException e1) {
                        shutDown = true;
                        Logger.error("Addressing exception -- shutting down the EmailAlertListener", e1);
                    } catch (MessagingException e1) {
                        shutDown = true;
                        Logger.error("Messaging exception -- shutting down the EmailAlertListener", e1);
                    }
                }
            }
        }
    }

    /**
	 * @see edu.uga.galileo.voci.logging.LogListener#allowDeferredLogNotification()
	 */
    public boolean allowDeferredLogNotification() {
        return true;
    }
}
