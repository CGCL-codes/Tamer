package de.cue4net.eventservice.email;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * MockJavaMailSender
 *
 * @author Thorsten Vogel
 * @version $Id: MockJavaMailSender.java,v 1.2 2008-06-05 12:19:10 keino Exp $
 */
public class MockJavaMailSender extends JavaMailSenderImpl {

    private MockTransport transport;

    @Override
    protected Transport getTransport(Session session) throws NoSuchProviderException {
        if (this.transport == null) {
            this.transport = new MockTransport(session, null);
        }
        return this.transport;
    }
}
