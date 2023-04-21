package org.likken.core;

import org.likken.util.parser.Constants;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 * A class of object identified by an OID
 *
 * @author Stephane Boisson
 * @version $Revision: 1.1 $ $Date: 2000/12/07 22:45:27 $
 */
public abstract class DirectoryObject implements java.io.Serializable, Comparable {

    private String oid;

    private String description;

    private static RE oidPattern;

    static {
        try {
            oidPattern = new RE(Constants.NUMERIC_OID_PATTERN);
        } catch (final RESyntaxException e) {
            System.err.println(e);
        }
    }

    public static boolean isValidOid(final String anOid) {
        if (anOid == null) {
            return false;
        }
        return oidPattern.match(anOid);
    }

    public static void assertOidValidation(final String anOid) throws DirectoryObjectException {
        if (!isValidOid(anOid)) {
            throw new DirectoryObjectException("Invalid syntax", anOid);
        }
    }

    public DirectoryObject(final String anOid) throws DirectoryObjectException {
        assertOidValidation(anOid);
        oid = anOid;
    }

    public String getOid() {
        return oid;
    }

    public String getName() {
        return oid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String aDescription) {
        description = aDescription;
    }

    public boolean equals(final Object anObject) {
        if (this == anObject) {
            return true;
        }
        if ((anObject != null) && anObject.getClass().equals(this.getClass())) {
            return oid.equals(((DirectoryObject) anObject).getOid());
        }
        return false;
    }

    public int hashCode() {
        return oid.hashCode();
    }

    public String toString() {
        return this.getClass().getName() + "<" + this.getName() + ">";
    }

    public int compareTo(Object o) {
        return oid.compareTo(o);
    }
}
