package de.buelowssiege.mail;

import java.io.File;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

/**
 * This class represents a mimebodypart, and adds a few useful methods to it.
 * 
 * @author Maximilian Schwerin
 * @created 18. Juli 2002
 */
public class ExtendedMimeBodyPart extends MimeBodyPart {

    /**
     * This is the constructor for the <code>ExtendedMimeBodyPart</code> It
     * creates a BodyPart containing a file.
     * 
     * @param file
     *            The content file of this BodyPart
     * @exception MessagingException
     */
    public ExtendedMimeBodyPart(File file) throws MessagingException {
        super();
        setDataHandler(new DataHandler(new FileDataSource(file)));
        setFileName(file.getName());
        updateHeaders();
    }

    /**
     * This is the constructor for the <code>ExtendedMimeBodyPart</code>
     * 
     * @param content
     *            The content
     * @param type
     *            The type of the content
     * @exception MessagingException
     */
    public ExtendedMimeBodyPart(Object content, String type) throws MessagingException {
        super();
        setContent(content, type);
        updateHeaders();
    }
}
