package net.sf.dynxform.form.schema.types;

import java.util.Hashtable;

/**
 * Class SessionType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/08/11 17:32:55 $
 */
public class SessionType implements java.io.Serializable {

    /**
     * The global type
     */
    public static final int GLOBAL_TYPE = 0;

    /**
     * The instance of the global type
     */
    public static final SessionType GLOBAL = new SessionType(GLOBAL_TYPE, "global");

    /**
     * The container type
     */
    public static final int CONTAINER_TYPE = 1;

    /**
     * The instance of the container type
     */
    public static final SessionType CONTAINER = new SessionType(CONTAINER_TYPE, "container");

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;

    private SessionType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of SessionType
     */
    public static java.util.Enumeration enumerate() {
        return _memberTable.elements();
    }

    /**
     * Method getTypeReturns the type of this SessionType
     */
    public int getType() {
        return this.type;
    }

    /**
     * Method init
     */
    private static java.util.Hashtable init() {
        Hashtable members = new Hashtable();
        members.put("global", GLOBAL);
        members.put("container", CONTAINER);
        return members;
    }

    /**
     * Method toStringReturns the String representation of this
     * SessionType
     */
    public java.lang.String toString() {
        return this.stringValue;
    }

    /**
     * Method valueOfReturns a new SessionType based on the given
     * String value.
     * 
     * @param string
     */
    public static net.sf.dynxform.form.schema.types.SessionType valueOf(java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid SessionType";
            throw new IllegalArgumentException(err);
        }
        return (SessionType) obj;
    }
}
