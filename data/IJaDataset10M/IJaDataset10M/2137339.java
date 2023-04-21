package net.sf.dynxform.report.schema.types;

import java.util.Hashtable;

/**
 * Class SqlOperand.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/08/11 17:32:56 $
 */
public class SqlOperand implements java.io.Serializable {

    /**
     * The AND type
     */
    public static final int AND_TYPE = 0;

    /**
     * The instance of the AND type
     */
    public static final SqlOperand AND = new SqlOperand(AND_TYPE, "AND");

    /**
     * The OR type
     */
    public static final int OR_TYPE = 1;

    /**
     * The instance of the OR type
     */
    public static final SqlOperand OR = new SqlOperand(OR_TYPE, "OR");

    /**
     * The NOT type
     */
    public static final int NOT_TYPE = 2;

    /**
     * The instance of the NOT type
     */
    public static final SqlOperand NOT = new SqlOperand(NOT_TYPE, "NOT");

    /**
     * The IN type
     */
    public static final int IN_TYPE = 3;

    /**
     * The instance of the IN type
     */
    public static final SqlOperand IN = new SqlOperand(IN_TYPE, "IN");

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

    private SqlOperand(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of SqlOperand
     */
    public static java.util.Enumeration enumerate() {
        return _memberTable.elements();
    }

    /**
     * Method getTypeReturns the type of this SqlOperand
     */
    public int getType() {
        return this.type;
    }

    /**
     * Method init
     */
    private static java.util.Hashtable init() {
        Hashtable members = new Hashtable();
        members.put("AND", AND);
        members.put("OR", OR);
        members.put("NOT", NOT);
        members.put("IN", IN);
        return members;
    }

    /**
     * Method toStringReturns the String representation of this
     * SqlOperand
     */
    public java.lang.String toString() {
        return this.stringValue;
    }

    /**
     * Method valueOfReturns a new SqlOperand based on the given
     * String value.
     * 
     * @param string
     */
    public static net.sf.dynxform.report.schema.types.SqlOperand valueOf(java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid SqlOperand";
            throw new IllegalArgumentException(err);
        }
        return (SqlOperand) obj;
    }
}
