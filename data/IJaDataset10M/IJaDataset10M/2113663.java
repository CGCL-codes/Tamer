package org.hsqldb.jdbc;

import java.io.IOException;
import java.sql.RowId;
import java.sql.SQLException;
import java.util.Arrays;
import org.hsqldb.Trace;
import org.hsqldb.lib.StringConverter;

/**
 *
 * The representation (mapping) in the Java programming language of an SQL ROWID
 * value. An SQL ROWID is a built-in type, a value of which can be thought of as
 * an address  for its identified row in a database table. Whether that address
 * is logical or, in any  respects, physical is determined by its originating data
 * source.
 * <p>
 * Methods in the interfaces <code>ResultSet</code>, <code>CallableStatement</code>,
 * and <code>PreparedStatement</code>, such as <code>getRowId</code> and <code>setRowId</code>
 * allow a programmer to access a SQL <code>ROWID</code>  value. The <code>RowId</code>
 * interface provides a method
 * for representing the value of the <code>ROWID</code> as a byte array or as a
 * <code>String</code>.
 * <p>
 * The method <code>getRowIdLifetime</code> in the interface <code>DatabaseMetaData</code>,
 * can be used
 * to determine if a <code>RowId</code> object remains valid for the duration of the transaction in
 * which  the <code>RowId</code> was created, the duration of the session in which
 * the <code>RowId</code> was created,
 * or, effectively, for as long as its identified row is not deleted. In addition
 * to specifying the duration of its valid lifetime outside its originating data
 * source, <code>getRowIdLifetime</code> specifies the duration of a <code>ROWID</code>
 * value's valid lifetime
 * within its originating data source. In this, it differs from a large object,
 * because there is no limit on the valid lifetime of a large  object within its
 * originating data source.
 * <p>
 * All methods on the <code>RowId</code> interface must be fully implemented if the 
 * JDBC driver supports the data type.
 *
 * @see java.sql.DatabaseMetaData
 * @since JDK 1.6, HSQLDB 1.8.x
 * @author boucherb@users
 */
public final class jdbcRowId implements RowId {

    /**
     * Compares this <code>RowId</code> to the specified object. The result is
     * <code>true</code> if and only if the argument is not null and is a RowId
     * object that represents the same ROWID as  this object.
     * <p>
     * It is important
     * to consider both the origin and the valid lifetime of a <code>RowId</code>
     * when comparing it to another <code>RowId</code>. If both are valid, and
     * both are from the same table on the same data source, then if they are equal
     * they identify
     * the same row; if one or more is no longer guaranteed to be valid, or if
     * they originate from different data sources, or different tables on the
     * same data source, they  may be equal but still
     * not identify the same row.
     *
     * @param obj the <code>Object</code> to compare this <code>RowId</code> object
     *     against.
     * @return true if the <code>RowId</code>s are equal; false otherwise
     * @since JDK 1.6, HSQLDB 1.8.x
     */
    public boolean equals(Object obj) {
        return (obj instanceof jdbcRowId) && Arrays.equals((byte[]) this.id, (byte[]) ((jdbcRowId) obj).id);
    }

    /**
     * Returns an array of bytes representing the value of the SQL <code>ROWID</code>
     * designated by this <code>java.sql.RowId</code> object.
     *
     * @return an array of bytes, whose length is determined by the driver supplying
     *     the connection, representing the value of the ROWID designated by this
     *     java.sql.RowId object.
     */
    public byte[] getBytes() {
        return (byte[]) id.clone();
    }

    /**
      * Returns a String representing the value of the SQL ROWID designated by this
      * <code>java.sql.RowId</code> object.
      * <p>
      *Like <code>java.sql.Date.toString()</code>
      * returns the contents of its DATE as the <code>String</code> "2004-03-17"
      * rather than as  DATE literal in SQL (which would have been the <code>String</code>
      * DATE "2004-03-17"), toString()
      * returns the contents of its ROWID in a form specific to the driver supplying
      * the connection, and possibly not as a <code>ROWID</code> literal.
      *
      * @return a String whose format is determined by the driver supplying the
      *     connection, representing the value of the <code>ROWID</code> designated
      *     by this <code>java.sql.RowId</code>  object.
      */
    public String toString() {
        return StringConverter.byteArrayToHex(id);
    }

    /**
      * Returns a hash code value of this <code>RowId</code> object.
      *
      * @return a hash code for the <code>RowId</code>
      */
    public int hashCode() {
        if (hash == 0) {
            hash = Arrays.hashCode(id);
        }
        return hash;
    }

    private final byte[] id;

    private int hash;

    private String str;

    /**
     * Constructs a new jdbcRowid instance wrapping the given octet sequence. <p>
     *
     * This constructor may be used internally to retrieve result set values as
     * RowId objects, yet it also may need to be public to allow access from
     * other packages. As such (in the interest of efficiency) this object
     * maintains a reference to the given octet sequence rather than making a
     * copy; special care should be taken by extenal clients never to use this
     * constructor with a byte array object that may later be modified
     * extenally.
     *
     * @param id the octet sequence representing the Rowid value
     * @throws SQLException if the argument is null
     */
    public jdbcRowId(final byte[] id) throws SQLException {
        if (id == null) {
            throw Util.nullArgument("id");
        }
        this.id = id;
    }

    /**
     * Constructs a new jdbcRowId instance whose internal octet sequence is
     * is a copy of the octet sequence of the given RowId object. <p>
     *
     * @param id the octet sequence representing the Rowid value
     * @throws SQLException if the argument is null
     */
    public jdbcRowId(RowId id) throws SQLException {
        this(id.getBytes());
    }

    public jdbcRowId(final String hex) throws SQLException {
        if (hex == null) {
            throw Util.nullArgument("hex");
        }
        try {
            this.id = StringConverter.hexToByteArray(hex);
        } catch (IOException e) {
            throw Util.sqlException(Trace.JDBC_INVALID_ARGUMENT, "hex: " + e);
        }
    }

    protected Object id() {
        return id;
    }
}
