package dk.highflier.airlog.utility;

/** A smarter ResultSet returning objects instead of primitives
 * @author jba
 * @version $Revision: 1.1 $
 */
public class SmartResultSet extends java.lang.Object {

    java.sql.ResultSet basicResultSet = null;

    /** Constructs a SmartResultSet based on a normal ResultSet
 * @param basicResultSet the ResultSet to be wrapped
 */
    public SmartResultSet(java.sql.ResultSet basicResultSet) {
        this.basicResultSet = basicResultSet;
    }

    /** Returns the wrapped ResultSet.
 * @return the wrapped ResultSet
 */
    public java.sql.ResultSet getBasicResultSet() {
        return basicResultSet;
    }

    /** Gets the value of the designated column in the current row of this ResultSet object as a Long in the Java programming language.
 * @param columnName the SQL name of the column
 * @throws SQLException if a database access error occurs
 * @return the column value; if the value is SQL NULL, the value returned is NULL
 */
    public Long getLong(String columnName) throws java.sql.SQLException {
        long primitiveValue = basicResultSet.getLong(columnName);
        if (basicResultSet.wasNull()) return null;
        return new Long(primitiveValue);
    }

    /** Gets the value of the designated column in the current row of this ResultSet object as a Integer in the Java programming language.
 * @param columnName the SQL name of the column
 * @throws SQLException if a database access error occurs
 * @return the column value; if the value is SQL NULL, the value returned is NULL
 */
    public Integer getInt(String columnName) throws java.sql.SQLException {
        int primitiveValue = basicResultSet.getInt(columnName);
        if (basicResultSet.wasNull()) return null;
        return new Integer(primitiveValue);
    }

    /** Gets the value of the designated column in the current row of this ResultSet object as a Boolean in the Java programming language.
 * @param columnName the SQL name of the column
 * @throws SQLException if a database access error occurs
 * @return the column value; if the value is SQL NULL, the value returned is NULL
 */
    public Boolean getBoolean(String columnName) throws java.sql.SQLException {
        String s = basicResultSet.getString(columnName);
        if (s == null) return null;
        boolean primitiveValue = (s.compareTo("Y") == 0 ? true : false);
        if (basicResultSet.wasNull()) return null;
        return new Boolean(primitiveValue);
    }

    /** Gets the value of the designated column in the current row of this ResultSet object as a String in the Java programming language.
 * @param columnName the SQL name of the column
 * @throws SQLException if a database access error occurs
 * @return the column value; if the value is SQL NULL, the value returned is NULL
 */
    public String getString(String columnName) throws java.sql.SQLException {
        return basicResultSet.getString(columnName);
    }

    /** Gets the value of the designated column in the current row of this ResultSet object as a java.sql.Date in the Java programming language.
 * @param columnName the SQL name of the column
 * @throws SQLException if a database access error occurs
 * @return the column value; if the value is SQL NULL, the value returned is NULL
 */
    public java.sql.Date getDate(String columnName) throws java.sql.SQLException {
        return new java.sql.Date(basicResultSet.getLong(columnName));
    }

    /** Gets the value of the designated column in the current row of this ResultSet object as a java.sql.Date in the Java programming language.
 * @param columnName the SQL name of the column
 * @throws SQLException if a database access error occurs
 * @return the column value; if the value is SQL NULL, the value returned is NULL
 */
    public java.sql.Timestamp getTimestamp(String columnName) throws java.sql.SQLException {
        return basicResultSet.getTimestamp(columnName);
    }

    /** Gets the value of the designated column in the current row of this ResultSet object as a java.math.BigDecimal in the Java programming language.
 * @param columnName the SQL name of the column
 * @throws SQLException if a database access error occurs
 * @return the column value; if the value is SQL NULL, the value returned is NULL
 */
    public java.math.BigDecimal getBigDecimal(String columnName) throws java.sql.SQLException {
        return basicResultSet.getBigDecimal(columnName);
    }

    /** Returns the value of the designated column in the current row of this ResultSet object as a Blob object in the Java programming language.
 * @param columnName the SQL name of the column
 * @throws SQLException if a database access error occurs
 * @return the column value; if the value is SQL NULL, the value returned is NULL
 */
    public java.sql.Blob getBlob(String columnName) throws java.sql.SQLException {
        return basicResultSet.getBlob(columnName);
    }

    /** Gets the value of the designated column in the current row of this ResultSet object as a Float in the Java programming language.
 * @param columnName the SQL name of the column
 * @throws SQLException if a database access error occurs
 * @return the column value; if the value is SQL NULL, the value returned is NULL
 */
    public Float getFloat(String columnName) throws java.sql.SQLException {
        float primitiveValue = basicResultSet.getFloat(columnName);
        if (basicResultSet.wasNull()) return null;
        return new Float(primitiveValue);
    }

    /** Gets the value of the designated column in the current row of this ResultSet object as a Character in the Java programming language.
 * @param columnName the SQL name of the column
 * @throws SQLException if a database access error occurs
 * @return the column value; if the value is SQL NULL, the value returned is NULL
 */
    public Character getChar(String columnName) throws java.sql.SQLException {
        String primitiveValue = basicResultSet.getString(columnName);
        if (basicResultSet.wasNull()) return null;
        return new Character(primitiveValue.charAt(0));
    }

    public byte[] getBytes(String columnName) throws java.sql.SQLException {
        return basicResultSet.getBytes(columnName);
    }
}
