package com.hs.mail.imap.mailbox;

/**
 * Mailbox is a class that represents a mailbox for mail messages.
 * 
 * @author Won Chul Doh
 * @since Feb 2, 2010
 * 
 */
public class Mailbox {

    /**
	 * Delimiter string that separates mailbox's pathname from the names of
	 * immediate sub-mailboxes.
	 */
    public static String folderSeparator = ".";

    private long mailboxID;

    private String name;

    private long ownerID;

    private long nextUID;

    private long uidValidity;

    private boolean noInferiors = false;

    private boolean noSelect = false;

    private boolean readOnly = false;

    private boolean marked = false;

    private boolean hasChildren = false;

    public Mailbox() {
        super();
    }

    public Mailbox(String name) {
        super();
        this.name = name;
    }

    public long getMailboxID() {
        return mailboxID;
    }

    public void setMailboxID(long mailboxID) {
        this.mailboxID = mailboxID;
    }

    /**
	 * Returns the full name of this mailbox. If the mailbox resides under the
	 * root hierarchy, the returned name may contain the hierarchy delimiter.
	 * 
	 * @return name of the mailbox
	 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    /**
	 * Returns the predicted UID that will be assigned to the next message that
	 * is appended to this mailbox.
	 * 
	 * @return the UIDNEXT value
	 */
    public long getNextUID() {
        return nextUID;
    }

    public void setNextUID(long nextUid) {
        this.nextUID = nextUid;
    }

    /**
	 * Returns the UIDValidity for this mailbox.
	 */
    public long getUidValidity() {
        return uidValidity;
    }

    public void setUidValidity(long uidValidity) {
        this.uidValidity = uidValidity;
    }

    /**
	 * Check whether this mailbox can have child mailboxes.
	 */
    public boolean isNoInferiors() {
        return noInferiors;
    }

    public void setNoInferiors(boolean noInferiors) {
        this.noInferiors = noInferiors;
    }

    /**
	 * Check whether this mailbox can be selected.
	 */
    public boolean isNoSelect() {
        return noSelect;
    }

    public void setNoSelect(boolean noSelect) {
        this.noSelect = noSelect;
    }

    /**
	 * Check whether this mailbox is read-only.
	 */
    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    /**
	 * Check whether this mailbox has child mailboxes.
	 */
    public boolean hasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
	 * Check if this mailbox is subsequent child of the given mailbox.
	 * 
	 * @param mailbox
	 *            potential parent mailbox
	 * @return true if child, otherwise false
	 */
    public boolean isChildOf(Mailbox mailbox) {
        return (mailbox != null) ? mailbox.getName().equals(getParent(name)) : false;
    }

    public Mailbox rename(String base, String dest) {
        if (base.equals(name)) {
            name = dest;
        } else {
            name = dest + folderSeparator + name.substring(base.length());
        }
        return this;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Mailbox) return name.equals(((Mailbox) obj).getName()); else if (obj instanceof String) return name.equals((String) obj); else return false;
    }

    /**
	 * Get the full name of parent mailbox for given mailbox.
	 * 
	 * @param mailboxName
	 *            full name of the mailbox
	 * @return full name of parent mailbox
	 */
    public static String getParent(String mailboxName) {
        int i;
        if ((i = mailboxName.lastIndexOf(Mailbox.folderSeparator)) != -1) {
            return mailboxName.substring(0, i);
        } else {
            return "";
        }
    }
}
