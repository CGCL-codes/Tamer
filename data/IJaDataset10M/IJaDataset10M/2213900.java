package fr.mywiki.business.versioning;

import java.rmi.RemoteException;
import java.util.Date;
import javax.ejb.FinderException;
import fr.mywiki.business.user.User;
import fr.mywiki.model.ejbAdapter.Value;

/**
 * Represents a version object. The data is not directly stored in it, it is
 * only linked to it via a multiversion object.
 * 
 * @author Thomas
 */
public class VersionLite extends Value {

    private static final long serialVersionUID = 1L;

    /** The type of this object */
    public static final String TYPE_ENT = "version";

    public static final String FIELD_DATE = "date";

    public static final String FIELD_AUTHOR = "author";

    public static final String FIELD_MULTIVERSION = "id_multiversion";

    public static final String LINK_VERSION_ENTRY = "id_entry";

    /** Date of this version object. */
    private Date date;

    /** Author of this version. */
    private User author;

    /** Identifier of the multiversion object linked to this one */
    private Long multiversion;

    public String getTypeEnt() {
        return TYPE_ENT;
    }

    public Object get(String attName) {
        if (FIELD_DATE.equals(attName)) return getDate();
        if (FIELD_AUTHOR.equals(attName)) return getAuthor();
        if (FIELD_MULTIVERSION.equals(attName)) return getMultiversion();
        return null;
    }

    public void set(String attName, Object value) {
        if (FIELD_DATE.equals(attName)) setDate((Date) date); else if (FIELD_AUTHOR.equals(attName)) setAuthor((User) value); else if (FIELD_MULTIVERSION.equals(attName)) setMultiversion((Long) value);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Long getMultiversion() {
        return multiversion;
    }

    public void setMultiversion(Long multiversion) {
        this.multiversion = multiversion;
    }

    /**
	 * Returns the content of this version.
	 * 
	 * @return a String object.
	 */
    public String getContent() throws RemoteException {
        return VersionManager.getInstance().getVersionContent(this);
    }

    /**
	 * Returns the version number, i.e. the index of the version in the history.
	 *  
	 * @return an integer >= 1.
	 */
    public int getNumber() throws RemoteException, FinderException {
        return VersionManager.getInstance().getVersionNumber(this);
    }

    /**
	 * Tells if this version is the current one for the entry.
	 * 
	 * @return true if and only if the version is linked to the entry by the
	 *         link currentVersion
	 */
    public boolean isCurrent() throws RemoteException, FinderException {
        return VersionManager.getInstance().isCurrentVersion(this);
    }
}
