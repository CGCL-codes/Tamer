package org.openuss.messaging;

/**
 * 
 */
public abstract class MessageBase implements Message, java.io.Serializable {

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -1396399022084794986L;

    private java.lang.Long id;

    /**
     * @see org.openuss.messaging.Message#getId()
     */
    public java.lang.Long getId() {
        return this.id;
    }

    /**
     * @see org.openuss.messaging.Message#setId(java.lang.Long id)
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    private java.lang.String senderName;

    /**
     * @see org.openuss.messaging.Message#getSenderName()
     */
    public java.lang.String getSenderName() {
        return this.senderName;
    }

    /**
     * @see org.openuss.messaging.Message#setSenderName(java.lang.String senderName)
     */
    public void setSenderName(java.lang.String senderName) {
        this.senderName = senderName;
    }

    private java.lang.String subject;

    /**
     * @see org.openuss.messaging.Message#getSubject()
     */
    public java.lang.String getSubject() {
        return this.subject;
    }

    /**
     * @see org.openuss.messaging.Message#setSubject(java.lang.String subject)
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }

    /**
     * Returns <code>true</code> if the argument is an Message instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Message)) {
            return false;
        }
        final Message that = (Message) object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers.
     */
    public int hashCode() {
        int hashCode = 0;
        hashCode = 29 * hashCode + (id == null ? 0 : id.hashCode());
        return hashCode;
    }
}
