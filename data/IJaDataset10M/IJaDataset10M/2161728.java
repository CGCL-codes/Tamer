package net.sf.yukatan;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;

/**
 * An importer for saving entity records in the Yukatan database.
 * A separate EntityImporter object is created for each top-level message.
 * The importer object creates the entity counter used to assign unique
 * sequence numbers to the saved entity records.
 * 
 * @author Jukka Zitting <jukka@zitting.name>
 */
public class EntityImporter {

    /**
     * The SQL statement used to save entities in the Yukatan database.
     */
    public static final String ENTITY_SQL = "INSERT INTO entity (msgno, entno, entparentno, entdate," + " entsendername, entsenderaddress, entmessageid, entcontentid," + " entdisposition, entfilename, enttypemajor, enttypeminor," + " entdescription, entsubject, enttext, entdata) VALUES" + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * The logger for warnings about errors accessing entity contents.
     */
    private static Logger logger = Logger.getLogger(EntityImporter.class.getName());

    /**
     * The INSERT statement used to save the entities.
     */
    private PreparedStatement insert;

    /**
     * The number of the message that contains these entities.
     */
    private int msgno;

    /**
     * The entity number counter.
     */
    private int counter;

    /**
     * Creates an importer object for saving entities in the
     * Yukatan database. The entity records saved by this object use a 
     * single entity number counter and the given SQL insert statement.
     * For each entity, the given statement is executed with
     * nime parameter values; the message number, the entity number,
     * the parent entity number, the Date timestamp, the sender name,
     * the sender address, the message identifier, the message subject,
     * and the text body. Note that only the first two parameters are
     * never NULL.
     * 
     * @param insert the INSERT statement
     * @param msgno the message number
     */
    public EntityImporter(PreparedStatement insert, int msgno) {
        this.insert = insert;
        this.msgno = msgno;
        this.counter = 0;
    }

    /**
     * Saves an entity record to the Yukatan database. An new entity
     * number is created and returned. The saved entity record is linked
     * to the given parent entity number (unless this is the root entity).
     * The entity body is expected to be available in the content argument.
     * If the content object is a String then it is saved as the text body
     * of the entity. Otherwise a NULL text body is saved.
     * 
     * @param entity the entity to be saved
     * @param parent parent entity number 
     * @param content the entity content
     * @return the entity number assigned to this entity
     * @throws SQLException on database errors
     */
    public int saveEntity(Part entity, int parent, Object content) throws SQLException {
        int entno = counter++;
        insert.setInt(1, msgno);
        insert.setInt(2, entno);
        if (entno > 0) {
            insert.setInt(3, parent);
        } else {
            insert.setNull(3, Types.INTEGER);
        }
        Address sender = getSender(entity);
        ContentType type = getContentType(entity);
        String major = type.getPrimaryType().toLowerCase();
        String minor = type.getSubType().toLowerCase();
        insert.setTimestamp(4, getSentDate(entity));
        insert.setString(5, getSenderName(sender));
        insert.setString(6, getSenderAddress(sender));
        insert.setString(7, getMessageID(entity));
        insert.setString(8, getContentID(entity));
        insert.setString(9, getDisposition(entity));
        insert.setString(10, getFilename(entity));
        insert.setString(11, major);
        insert.setString(12, minor);
        insert.setString(13, getDescription(entity));
        insert.setString(14, getSubject(entity));
        if (major.equals("text") && (content instanceof String)) {
            insert.setString(15, (String) content);
            insert.setBinaryStream(16, null, 0);
        } else if (content instanceof Multipart) {
            insert.setString(15, null);
            insert.setBinaryStream(16, null, 0);
        } else {
            insert.setString(15, null);
            try {
                insert.setBinaryStream(16, entity.getInputStream(), entity.getSize());
            } catch (MessagingException ex) {
                logger.log(Level.WARNING, "Invalid entity content", ex);
                insert.setBinaryStream(16, null, 0);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Invalid entity content", ex);
                insert.setBinaryStream(16, null, 0);
            }
        }
        insert.executeUpdate();
        return entno;
    }

    /**
     * Returns the Date timestamp of the given message entity. Returns
     * <code>null</code> if the timestamp is not available, or an error
     * occurred accessing it. Error conditions are logged as warnings.
     * 
     * @param entity the message entity
     * @return Date timestamp, or <code>null</code> if not accessible
     */
    private static Timestamp getSentDate(Part entity) {
        if (entity instanceof Message) {
            try {
                Date date = ((Message) entity).getSentDate();
                if (date != null) {
                    return new Timestamp(date.getTime());
                } else {
                    return null;
                }
            } catch (MessagingException ex) {
                logger.log(Level.WARNING, "Invalid Date header", ex);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the name string of the given sender address object.
     * 
     * @param sender the sender address
     * @return name string
     */
    private static String getSenderName(Address sender) {
        if (sender instanceof InternetAddress) {
            return ((InternetAddress) sender).getPersonal();
        } else {
            return null;
        }
    }

    /**
     * Returns the address string of the given sender address object.
     * 
     * @param sender the sender address
     * @return address string
     */
    private static String getSenderAddress(Address sender) {
        if (sender instanceof InternetAddress) {
            return ((InternetAddress) sender).getAddress();
        } else if (sender != null) {
            return sender.toString();
        } else {
            return null;
        }
    }

    /**
     * Returns the sender address of the given message entity. Returns
     * <code>null</code> if a sender address is not available, or if
     * an error occurred accessing it. Error conditions are logged
     * as warnings.
     *  
     * @param entity the message entity
     * @return sender address, or <code>null</code> if not available
     */
    private static Address getSender(Part entity) {
        if (entity instanceof MimeMessage) {
            try {
                return ((MimeMessage) entity).getSender();
            } catch (MessagingException ex) {
                logger.log(Level.WARNING, "Invalid Sender header", ex);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the message identifier string of the given message entity.
     * Returns <code>null</code> if a message identifier is not available,
     * or if an error occurred accessing it. Error conditions are logged
     * as warnings.
     * 
     * @param entity the message entity
     * @return message identifier, or <code>null</code> if not available
     */
    private static String getMessageID(Part entity) {
        if (entity instanceof MimeMessage) {
            try {
                return ((MimeMessage) entity).getMessageID();
            } catch (MessagingException ex) {
                logger.log(Level.WARNING, "Invalid Message-ID header", ex);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the content identifier string of the given message entity.
     * Returns <code>null</code> if a message identifier is not available,
     * or if an error occurred accessing it. Error conditions are logged
     * as warnings.
     * 
     * @param entity the message entity
     * @return content identifier, or <code>null</code> if not available
     */
    private static String getContentID(Part entity) {
        if (entity instanceof MimePart) {
            try {
                return ((MimePart) entity).getContentID();
            } catch (MessagingException ex) {
                logger.log(Level.WARNING, "Invalid Content-ID header", ex);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the content disposition value of the given message entity.
     * The returned disposition value is normalized to lower case. 
     * Returns <code>null</code> if the Content-Disposition header field
     * is not available, or if an error occurred accessing it. Error
     * conditions are logged as warnings.
     * 
     * @param entity the message entity
     * @return content disposition, or <code>null</code> if not available
     */
    private static String getDisposition(Part entity) {
        try {
            String disposition = entity.getDisposition();
            return (disposition != null) ? disposition.toLowerCase() : null;
        } catch (MessagingException ex) {
            logger.log(Level.WARNING, "Invalid Content-Disposition header", ex);
            return null;
        }
    }

    /**
     * Returns the attachment filename of the given message entity.
     * Returns <code>null</code> if the filename is not available, or
     * if an error occurred accessing it. Error conditions are logged
     * as warnings.
     * 
     * @param entity the message entity
     * @return attachment filename, or <code>null</code> if not available
     */
    private static String getFilename(Part entity) {
        try {
            String filename = entity.getFileName();
            return (filename != null) ? filename.toLowerCase() : null;
        } catch (MessagingException ex) {
            logger.log(Level.WARNING, "Invalid Content-Disposition header", ex);
            return null;
        }
    }

    /**
     * Returns the content type of the given message entity. Returns
     * the default "text/plain" content type if a content type is not
     * available. Error conditions are logged as warnings.
     * 
     * @param entity the message entity
     * @return content type, or <code>text/plain</code> if not available
     */
    private static ContentType getContentType(Part entity) {
        try {
            String type = entity.getContentType();
            if (type != null) {
                return new ContentType(type);
            } else {
                return new ContentType("text/plain");
            }
        } catch (MessagingException ex) {
            logger.log(Level.WARNING, "Invalid Content-Type header", ex);
            ContentType type = new ContentType();
            type.setPrimaryType("application");
            type.setSubType("octet-stream");
            return type;
        }
    }

    /**
     * Returns the content description of the given message entity.
     * The content description is returned in decoded Unicode format.
     * Returns <code>null</code> if a content description is not available,
     * or if an error occurred accessing it. Error conditions are logged
     * as warnings. 
     * 
     * @param entity the message entity
     * @return decoded content description, or <code>null</code> if not available 
     */
    private static String getDescription(Part entity) {
        if (entity instanceof MimePart) {
            try {
                return ((MimePart) entity).getDescription();
            } catch (MessagingException ex) {
                logger.log(Level.WARNING, "Invalid Content-Description header", ex);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the Subject string of the given message entity. The subject
     * string is returned in decoded Unicode format. Returns <code>null</code>
     * if a subject string is not available, or if an error occurred accessing
     * it. Error conditions are logged as warnings. 
     * 
     * @param entity the message entity
     * @return decoded subject string, or <code>null</code> if not available 
     */
    private static String getSubject(Part entity) {
        if (entity instanceof MimeMessage) {
            try {
                return ((MimeMessage) entity).getSubject();
            } catch (MessagingException ex) {
                logger.log(Level.WARNING, "Invalid Subject header", ex);
                return null;
            }
        } else {
            return null;
        }
    }
}
