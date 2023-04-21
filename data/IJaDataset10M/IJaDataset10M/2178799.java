package prisms.message;

/** Represents an attachment of arbitrary type to a message */
public class Attachment {

    private Message theMessage;

    private final long theID;

    private String theName;

    private final String theType;

    private final long theLength;

    private final long theCRC;

    private boolean isDeleted;

    /**
	 * Creates an attachment. This is public so that classes from other packages can implement
	 * {@link MessageManager}, but it should only be used by the
	 * {@link MessageManager#createAttachment(Message, String, String, java.io.InputStream, prisms.records.RecordsTransaction)}
	 * method of an implementation of that class.
	 * 
	 * @param message The message to attach the data to
	 * @param id The storage ID of this attachment
	 * @param name The name of this attachment
	 * @param type The type of this attachment
	 * @param length The length of this attachment's content
	 * @param crc The CRC code of this attachment's content
	 */
    public Attachment(Message message, long id, String name, String type, long length, long crc) {
        theMessage = message;
        theID = id;
        theName = name;
        theType = type;
        theLength = length;
        theCRC = crc;
    }

    /** @return The header of the message that the data is attached to */
    public Message getMessage() {
        return theMessage;
    }

    /** @return The storage ID of the attachment */
    public long getID() {
        return theID;
    }

    /** @return The name of this attachment */
    public String getName() {
        return theName;
    }

    /** @param name The new name for this attachment */
    public void setName(String name) {
        theName = name;
    }

    /** @return The type of this attachment--traditionally, but not necessarily, a MIME type */
    public String getType() {
        return theType;
    }

    /** @return The number of bytes in the attachment */
    public long getLength() {
        return theLength;
    }

    /** @return The CRC code for this attacment's content */
    public long getCRC() {
        return theCRC;
    }

    /** @return Whether this attachment has been removed from its message */
    public boolean isDeleted() {
        return isDeleted;
    }

    /** @param deleted Whether this attachment has been removed from its message */
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    /**
	 * Clones this attachment for a clone of this attachment's message
	 * 
	 * @param msg The message to clone this attachment for
	 * @return The cloned attachment
	 */
    Attachment clone(Message msg) {
        Attachment ret;
        try {
            ret = (Attachment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Attachment.clone not supported", e);
        }
        ret.theMessage = msg;
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Attachment && ((Attachment) o).theID == theID;
    }

    @Override
    public int hashCode() {
        return ((int) theID) ^ ((int) (theID >>> 32));
    }
}
