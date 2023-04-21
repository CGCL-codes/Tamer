package org.torweg.pulse.email;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.torweg.pulse.service.PulseException;

/**
 * an object encapsulating both {@code Email} and {@code MimeMessage}
 * objects.
 * 
 * @author Thomas Weber
 * @version $Revision: 1388 $
 */
public final class QueueItem implements Serializable {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = 5186955392586121134L;

    /**
	 * the encapsulated object.
	 */
    private MimeMessageWrapper mm;

    /**
	 * the encapsulated object.
	 */
    private Email em;

    /**
	 * the number of sending failures.
	 */
    private int failCount = 0;

    /**
	 * builds a {@code QueueItem} for a {@code MimeMessage}.
	 * 
	 * @param m
	 *            the MIME message to encapsulate
	 */
    protected QueueItem(final MimeMessage m) {
        super();
        this.mm = new MimeMessageWrapper(m);
    }

    /**
	 * builds a {@code QueueItem} for an {@code Email}.
	 * 
	 * @param e
	 *            the e-mail to encapsulate
	 */
    protected QueueItem(final Email e) {
        super();
        this.em = e;
    }

    /**
	 * returns {@code true}, if the contained object is a
	 * {@code MimeMessage}.
	 * 
	 * @return {@code true}, if the contained object is a
	 *         {@code MimeMessage}. Otherwise {@code false}.
	 */
    protected boolean isMimeMessage() {
        return (this.em == null);
    }

    /**
	 * returns the encapsulated {@code MimeMessage} or {@code null},
	 * if the encapsulated object is an {@code Email}.
	 * 
	 * @return the encapsulated {@code MimeMessage} or {@code null},
	 *         if the encapsulated object is an {@code Email}
	 */
    protected MimeMessage getMimeMessage() {
        return this.mm.getMessage();
    }

    /**
	 * returns the encapsulated {@code Email} or {@code null}, if the
	 * encapsulated object is an {@code MimeMessage}.
	 * 
	 * @return the encapsulated {@code Email} or {@code null}, if the
	 *         encapsulated object is an {@code MimeMessage}
	 */
    protected Email getEmail() {
        return this.em;
    }

    /**
	 * increases the number of sending failures by {@code 1}.
	 * 
	 * @return the {@code QueueItem}
	 */
    protected QueueItem increaseFailCount() {
        this.failCount++;
        return this;
    }

    /**
	 * returns the number of sending failures.
	 * 
	 * @return the number of sending failures
	 */
    protected int getFailCount() {
        return this.failCount;
    }

    /**
	 * wraps a MimeMessage to make it serializable.
	 * 
	 * @author Thomas Weber
	 * @version $Revision: 1388 $
	 */
    public static final class MimeMessageWrapper implements Serializable {

        /**
		 * serialVersionUID.
		 */
        private static final long serialVersionUID = 9073158470615393629L;

        /**
		 * the wrapped message.
		 */
        private transient MimeMessage msg;

        /**
		 * creates a new wrapper.
		 * 
		 * @param m
		 *            the message to be wrapped
		 */
        protected MimeMessageWrapper(final MimeMessage m) {
            super();
            this.msg = m;
        }

        /**
		 * returns the wrapped {@code MimeMessage}.
		 * 
		 * @return the wrapped message
		 */
        protected MimeMessage getMessage() {
            return this.msg;
        }

        /**
		 * writes the object.
		 * 
		 * @param oos
		 *            the object output stream
		 * @throws IOException
		 *             on errors
		 */
        private void writeObject(final ObjectOutputStream oos) throws IOException {
            try {
                this.msg.writeTo(oos);
            } catch (MessagingException e) {
                throw new PulseException("Error serializing MimeMessage: " + e.getLocalizedMessage(), e);
            }
        }

        /**
		 * reads the object.
		 * 
		 * @param ois
		 *            the object input stream
		 * @throws IOException
		 *             on errors
		 * @throws ClassNotFoundException
		 *             on errors
		 */
        private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
            try {
                this.msg = new MimeMessage((javax.mail.Session) null, ois);
            } catch (MessagingException e) {
                throw new PulseException("Error deserializing MimeMessage: " + e.getLocalizedMessage(), e);
            }
        }
    }
}
