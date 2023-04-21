package net.taylor.testing.mail;

import java.util.Properties;
import javax.mail.Session;

/**
 * 
 * @author jgilbert
 * @version $Id: SessionDependencyInjector.java,v 1.1 2006/01/03 21:38:33
 *          jgilbert01 Exp $
 * 
 */
public class SessionFactory {

    private String smtpHost = "localhost";

    /**
	 * This parameter should be set in the jboss-aop.xml file.
	 * 
	 * @param smtpHost
	 */
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    /**
	 * Create a session
	 * 
	 * @return
	 * @throws Exception
	 */
    public Session getSession() throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        Session mailSession = Session.getDefaultInstance(props, null);
        mailSession.setDebug(true);
        return mailSession;
    }
}
