package de.ios.framework.db;

import java.util.Enumeration;
import java.util.Vector;
import java.sql.*;
import de.ios.framework.basic.*;

/**
 * This class implements a meta description object for a DB query.
 * It manages it's fields in a vector of DBMetaAttributes.
 *
 * @see de.ios.framework.db.DBMetaAttribute
 */
public class DBMetaObject {

    /**
   * A simple test program for out class
   *
   * @exception ClassNotFoundException
   */
    public static void main(String[] argv) throws ClassNotFoundException {
        System.out.println("DBMetaObject: Testing...");
        String dbname = "jdbc:adabasd://dilbert/DBTEST";
        String user = "DBTEST";
        String passwd = "DBTEST";
        try {
            DataBaseConnect dbc = new DataBaseConnect(dbname, user, passwd);
            String query = "SELECT * FROM VIEW1";
            String where = "";
            QueryResults queryresults = new QueryResultsImpl(dbc, null, query, where);
            if (!queryresults.isValid()) {
                if (debug) System.out.println("DBMetaObject: Query is not valid");
                return;
            }
            String tabname = "VIEW1";
            DBMetaObject metaobj = new DBMetaObject(dbc, queryresults.getMetaData(), tabname, true, false, false);
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("DBMetaObject: Stopping.");
            System.exit(-1);
        }
        System.out.println("DBMetaObject: End.");
    }

    /**
   * Constructor needs meta data from a query and a database connection 
   *
   * @param _dbconnect       the database connection
   * @param _metadata        the meta data of a query result
   * @param _showkeys        set true, if keys should be visible
   * @param _keysmodifiable  set true, if keys can be modified
   * @param _autoserial      set true, if first key is an int and can be
   *                         automatically incremented via a serial table
   *
   * @exception SQLException anything nasty happened
   */
    public DBMetaObject(DataBaseConnect _dbconnect, ResultSetMetaData _metadata, String _tabname, boolean _showkeys, boolean _keysmodifiable, boolean _autoserial) throws SQLException {
        dbconnect = _dbconnect;
        metadata = _metadata;
        dbmetadata = _dbconnect.connection.getMetaData();
        tabname = _tabname;
        catname = _dbconnect.connection.getCatalog();
        autoserial = _autoserial;
        hasprimkeys = false;
        if (metadata == null || dbmetadata == null || tabname == null) {
            if (debug) System.out.println("DBMetaObject: Got empty parameters");
            return;
        }
        ResultSet keycolumns = null;
        String schema = null;
        String schematerm = dbmetadata.getSchemaTerm();
        if ((schematerm != null) && (schematerm.toUpperCase().equals("OWNER"))) schema = dbmetadata.getUserName();
        try {
            keycolumns = dbmetadata.getPrimaryKeys(catname, schema, tabname);
            primarykeys = getKeyAttributes(keycolumns, "COLUMN_NAME");
            if (primarykeys.size() > 0) hasprimkeys = true;
        } catch (SQLException e) {
            primarykeys = new Vector();
            if (debug) System.out.println("DBMetaObject: No primary keys");
        } finally {
            if (keycolumns != null) {
                keycolumns.close();
                keycolumns = null;
            }
        }
        try {
            if ((!dbmetadata.getDatabaseProductName().equals("Oracle")) && (!dbmetadata.getDatabaseProductName().equals("PostgreSQL"))) {
                keycolumns = dbmetadata.getImportedKeys(catname, schema, tabname);
                foreignkeys = getKeyAttributes(keycolumns, "PKCOLUMN_NAME");
            } else foreignkeys = new Vector();
        } catch (SQLException e) {
            e.printStackTrace();
            foreignkeys = new Vector();
            if (debug) System.out.println("DBMetaObject: No foreign keys");
        } finally {
            if (keycolumns != null) {
                keycolumns.close();
                keycolumns = null;
            }
        }
        try {
            if ((!dbmetadata.getDatabaseProductName().equals("Oracle")) && (!dbmetadata.getDatabaseProductName().equals("PostgreSQL"))) {
                keycolumns = dbmetadata.getExportedKeys(catname, schema, tabname);
                referencekeys = getKeyAttributes(keycolumns, "PKCOLUMN_NAME");
            } else referencekeys = new Vector();
        } catch (SQLException e) {
            e.printStackTrace();
            referencekeys = new Vector();
            if (debug) System.out.println("DBMetaObject: No reference keys");
        } finally {
            if (keycolumns != null) {
                keycolumns.close();
                keycolumns = null;
            }
        }
        initAttributes(_showkeys, _keysmodifiable);
        checkAutoSerial();
        if (debug) System.out.println("DBMetaObject: autoserial is " + autoserial);
    }

    /**
   * Returns name of the table
   */
    public String getTableName() {
        return tabname;
    }

    /**
   * Returns vector of all managed attributes
   */
    public Vector getAttributes() {
        return attributes;
    }

    /**
   * Returns number of managed attributes
   */
    public int getAttributeCount() {
        return attributes.size();
    }

    /**
   * Returns true, if the object has any primary key attribute
   */
    public boolean hasPrimaryKeys() {
        return hasprimkeys;
    }

    /**
   * Returns a certain attribute
   *
   * @param i      position of the attribute
   *
   * @exception ArrayIndexOutOfBoundsException no such attribute exists
   */
    public DBMetaAttribute getAttribute(int i) throws ArrayIndexOutOfBoundsException {
        return (DBMetaAttribute) attributes.elementAt(i);
    }

    /**
   * Returns true, if attribute is a primary key
   *
   * @param colname     name of the attribute
   */
    public boolean isPrimaryKey(String colname) {
        return primarykeys.contains(colname);
    }

    /**
   * Returns true, if attribute is a foreign key
   *
   * @param colname     name of the attribute
   */
    public boolean isForeignKey(String colname) {
        return foreignkeys.contains(colname);
    }

    /**
   * Returns true, if attribute is a references key
   *
   * @param colname     name of the attribute
   */
    public boolean isReferenceKey(String colname) {
        return referencekeys.contains(colname);
    }

    /**
   * Creates a vector with empty values. Integers and Floats are set to 0 and
   * 0.0 respectively. If autoserial was set true, the first appearing key
   * which is an int is set to the next value.
   * @param nullValue if not 'null', this value is assigned to all fields (except to automatic-serial-fields)
   *
   * @return vector with empty initialized values
   */
    public Vector createEmptyValues(boolean forNew, boolean db2ObjectServerSupport, String revname) {
        String colname;
        Vector values = new Vector(getAttributeCount());
        Enumeration attribs = getAttributes().elements();
        boolean hasserial = false;
        while (attribs.hasMoreElements()) {
            DBMetaAttribute attrib = (DBMetaAttribute) (attribs.nextElement());
            switch(attrib.getType()) {
                case DBMetaAttribute.STRING_TYPE:
                case DBMetaAttribute.CHAR_TYPE:
                    values.addElement(null);
                    break;
                case DBMetaAttribute.DATE_TYPE:
                    values.addElement(null);
                    break;
                case DBMetaAttribute.FIXED_TYPE:
                case DBMetaAttribute.INT_TYPE:
                    {
                        String value = null;
                        if (db2ObjectServerSupport && forNew) {
                            colname = attrib.getName().toUpperCase();
                            if (colname.compareTo("OBJECTID") == 0) {
                                try {
                                    value = getNextObjectId();
                                } catch (SQLException e) {
                                    System.err.println("DBMetaObject.createEmptyValues: Error creating next serial value!");
                                    System.err.println(e);
                                }
                            } else if (colname.compareTo(revname) == 0) value = "1";
                        }
                        if (attrib.isPrimaryKey() && autoserial && !hasserial) {
                            try {
                                value = Integer.toString(getNextSerial());
                            } catch (SQLException e) {
                                System.err.println("DBMetaObject.createEmptyValues: Error creating next serial value!");
                                System.err.println(e);
                            }
                            hasserial = true;
                        }
                        values.addElement(value);
                        break;
                    }
                case DBMetaAttribute.FLOAT_TYPE:
                    values.addElement(null);
                    break;
                default:
                    values.addElement(null);
                    break;
            }
        }
        if (debug) {
        }
        return values;
    }

    /**
   * Initialize a copied Object.
   */
    public void initCopied(Vector values, Vector cvalues, Vector changed, String handleNull, boolean db2ObjectServerSupport, String revname) {
        Enumeration attribs = getAttributes().elements();
        DBMetaAttribute attrib = null;
        String colname, v;
        int i = 0;
        if (!db2ObjectServerSupport) return;
        while (attribs.hasMoreElements()) {
            attrib = (DBMetaAttribute) (attribs.nextElement());
            if (attrib.getType() == DBMetaAttribute.INT_TYPE) {
                colname = attrib.getName().toUpperCase();
                if (colname.compareTo("OBJECTID") == 0) {
                    try {
                        v = getNextObjectId();
                        cvalues.setElementAt(v, i);
                        changed.setElementAt(new Boolean(true), i);
                    } catch (SQLException e) {
                        System.err.println("DBMetaObject.createEmptyValues: Error creating next serial value!");
                        System.err.println(e);
                    }
                } else if (colname.compareTo(revname) == 0) {
                    cvalues.setElementAt("1", i);
                    changed.setElementAt(new Boolean(true), i);
                }
            }
            i++;
        }
    }

    /** 
   * Returns true, if object is auto-serialable
   */
    public boolean hasAutoSerial() {
        return autoserial;
    }

    /**
   * Gets a new ObjectId (db2-ObjectServer-Compatible).
   * @return a new serial for the Object-Id.
   */
    public String getNextObjectId() throws SQLException {
        long nextserial = 1;
        String s0 = "lock table serials in exclusive mode";
        String s1 = "SELECT nextserial FROM serials WHERE tablename = 'SERVER_OIDS'";
        String s2;
        try {
            Statement stmt = dbconnect.connection.createStatement();
            stmt.executeUpdate(s0);
            ResultSet rs = stmt.executeQuery(s1);
            if (!rs.next()) {
                s2 = "insert into serials (tablename,nextserial) values ('SERVER_OIDS', " + (nextserial) + ")";
            } else {
                nextserial = rs.getLong(1) + 1;
                s2 = "update serials set nextserial=" + (nextserial) + " where tablename='SERVER_OIDS'";
            }
            stmt.executeUpdate(s2);
            dbconnect.connection.commit();
            rs.close();
            stmt.close();
            return "" + nextserial;
        } catch (SQLException e) {
            dbconnect.connection.rollback();
            throw e;
        }
    }

    /**
   * Returns the next serial number, but does not increment it in the DB
   *
   * @exception java.sql.SQLException something nasty happened
   */
    public int getNextSerial() throws SQLException {
        String querystm = "SELECT nextserial FROM serials WHERE tablename = '" + tabname + "'";
        Statement statement = dbconnect.connection.createStatement();
        ResultSet results = statement.executeQuery(querystm);
        int nextserial = 0;
        if (results.next()) nextserial = results.getInt("NEXTSERIAL");
        return nextserial;
    }

    /**
   * Sets the next serial number
   *
   * @return       true, if everything was ok, false otherwise
   *
   * @exception java.sql.SQLException something nasty happened
   */
    public boolean setNextSerial() throws SQLException {
        int nextserial = getNextSerial() + 1;
        String updatestm = "UPDATE OF serials (nextserial) VALUES (" + nextserial + ") WHERE tablename ='" + tabname + "'";
        Statement statement = dbconnect.connection.createStatement();
        int state = statement.executeUpdate(updatestm);
        return state != 0;
    }

    /**
   * Checks, if autoserial really is possible
   *
   * @exception java.sql.SQLException something nasty happened
   */
    protected void checkAutoSerial() throws SQLException {
        boolean hasserial = false;
        Enumeration attribs = attributes.elements();
        while (attribs.hasMoreElements()) {
            DBMetaAttribute attrib = (DBMetaAttribute) (attribs.nextElement());
            if (attrib.isPrimaryKey() && attrib.getType() == DBMetaAttribute.INT_TYPE) {
                hasserial = true;
                break;
            }
        }
        if (!hasserial) {
            if (debug) System.out.println("DBMetaObject: Table/view has no primary keys of type integer");
            autoserial = false;
            return;
        }
        String querystm = "SELECT tablename FROM serials WHERE tablename ='" + tabname + "'";
        if (debug) System.out.println("DBMetaObject: autoserial query is " + querystm);
        try {
            Statement statement = dbconnect.connection.createStatement();
            ResultSet results = statement.executeQuery(querystm);
            if (results.next()) autoserial = true;
        } catch (SQLException e) {
            System.err.println("DBMetaObject: Could not read from table SERIALS. Add the 'no-auto' flag!");
        }
    }

    /**
   * Initializes the vector of attributes
   *
   * @param showkeys         keys should be visible
   * @param keysmodifiable   keys can be modified
   *
   * @exception java.sql.SQLException something nasty happened
   */
    protected void initAttributes(boolean showkeys, boolean keysmodifiable) throws SQLException {
        int colcount = metadata.getColumnCount();
        if (debug) System.out.println("DBMetaObject: columns " + colcount);
        for (int i = 1; i <= colcount; i++) {
            String name = metadata.getColumnName(i).trim();
            String label = metadata.getColumnLabel(i).trim();
            int width = metadata.getColumnDisplaySize(i);
            int sqltype = metadata.getColumnType(i);
            int precision = metadata.getPrecision(i);
            int scale = metadata.getScale(i);
            if (debug) {
                System.out.println("DBMetaObject: Name " + name);
                System.out.println("DBMetaObject: Label " + label);
                System.out.println("DBMetaObject: Width " + width);
                System.out.println("DBMetaObject: Precision " + precision);
                System.out.println("DBMetaObject: Scale " + scale);
                System.out.println("DBMetaObject: SQLType " + sqltype);
            }
            int key = DBMetaAttribute.NO_KEY;
            if (isPrimaryKey(name)) key |= DBMetaAttribute.PRIMARY_KEY;
            if (isForeignKey(name)) key |= DBMetaAttribute.FOREIGN_KEY;
            if (isReferenceKey(name)) key |= DBMetaAttribute.REFERENCE_KEY;
            if (debug) System.out.println("DBMetaObject: Keytype " + key);
            boolean visible = true;
            boolean modifiable = true;
            if (key != DBMetaAttribute.NO_KEY) {
                if (!showkeys) visible = false;
                if (!keysmodifiable) modifiable = false;
            }
            int type = DBMetaAttribute.INVALID_TYPE;
            switch(sqltype) {
                case Types.DATE:
                    type = DBMetaAttribute.DATE_TYPE;
                    break;
                case Types.VARCHAR:
                    type = DBMetaAttribute.STRING_TYPE;
                    break;
                case Types.CHAR:
                    type = DBMetaAttribute.CHAR_TYPE;
                    break;
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.NUMERIC:
                    type = DBMetaAttribute.FLOAT_TYPE;
                    break;
                case Types.DECIMAL:
                    type = DBMetaAttribute.FIXED_TYPE;
                    break;
                case Types.INTEGER:
                    type = DBMetaAttribute.INT_TYPE;
                    break;
                case Types.BINARY:
                case Types.LONGVARBINARY:
                    type = DBMetaAttribute.IMAGE_TYPE;
                    break;
                default:
                    System.err.println("DBMetaObject: Could not handle type " + type);
            }
            if (debug) System.out.println("DBMetaObject: SQLType: " + sqltype + " => DBMetaAttributes-Type: " + type);
            DBMetaAttribute attr = new DBMetaAttribute(name, tabname, label, type, width, scale, key, visible, modifiable);
            attr.setPrecision(precision);
            attributes.addElement(attr);
        }
    }

    /**
   * Builds a vector of attribute names which are in a result set of key
   * columns for a table. The result set can be primary, foreign or
   * references keys.
   *
   * @param keycolumns  results set holding the keys for a table
   *
   * @exception java.sql.SQLException something nasty happened
   */
    protected Vector getKeyAttributes(ResultSet keycolumns, String colname) throws SQLException {
        Vector columns = new Vector();
        while (keycolumns.next()) {
            columns.addElement(keycolumns.getString(colname).trim());
        }
        if (debug) System.out.println("DBMetaObject: keyattributes " + columns);
        return columns;
    }

    /**
   * Connected Database
   */
    protected DataBaseConnect dbconnect;

    /**
   * Metadata to find key columns for a table
   */
    protected DatabaseMetaData dbmetadata;

    /**
   * The result of a query
   */
    protected ResultSetMetaData metadata;

    /**
   * List of handled attributes
   */
    protected Vector attributes = new Vector();

    protected boolean autoserial;

    protected String tabname;

    protected String catname;

    protected Vector primarykeys;

    protected Vector foreignkeys;

    protected Vector referencekeys;

    protected boolean hasprimkeys;

    protected static final boolean debug = false;
}
