package org.cid.distribution.plugins.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.cid.distribution.DistributionMessage;
import org.cid.distribution.DistributionMessagingException;

/**
 * <p>Represents each one of the messages received or sent by the application.</p> 
 * @version $Revision:127 $
 */
public class DistributionMail extends MimeMessage {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(DistributionMail.class);

    /**
	 * The additional properties of the message
	 */
    protected HashMap<String, Object> properties;

    /**
	 * Builds a new DistributionMessage
	 */
    public DistributionMail(Session session) {
        super(session);
        this.properties = new HashMap<String, Object>();
    }

    /**
	 * Builds a new DistributionMessage, which is a copy from other
	 */
    public DistributionMail(Session session, MimeMessage message) throws MessagingException, IOException {
        super(session);
        ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
        message.writeTo(os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        this.parse(is);
        is.close();
        os.close();
        this.properties = new HashMap<String, Object>();
    }

    /**
	 * Copies a message
	 */
    public DistributionMail copy() throws DistributionMessagingException {
        return copy(Session.getDefaultInstance(new Properties()));
    }

    /**
	 * Clones a mail message 
	 */
    public DistributionMail copy(Session session) throws DistributionMessagingException {
        DistributionMail newMessage = new DistributionMail(session);
        ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
        try {
            writeTo(os);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            newMessage.parse(is);
            is.close();
            os.close();
        } catch (IOException e) {
            throw new DistributionMessagingException("Could not copy a mail message", e);
        } catch (MessagingException e) {
            throw new DistributionMessagingException("Could not copy a mail message", e);
        }
        newMessage.properties = (HashMap<String, Object>) this.getProperties().clone();
        return newMessage;
    }

    public void write(OutputStream os) throws DistributionMessagingException {
        try {
            this.writeTo(os);
        } catch (IOException e) {
            throw new DistributionMessagingException("IO error trying to write a mail message to a stream", e);
        } catch (MessagingException e) {
            throw new DistributionMessagingException("Messaging error trying to write a mail message to a stream", e);
        }
    }

    /**
	 * @return Returns the variables.
	 */
    public HashMap<String, Object> getProperties() {
        return properties;
    }
}
