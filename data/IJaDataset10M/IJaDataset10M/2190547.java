package com.volantis.mcs.repository.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A typesafe enumeration that describes a type of JDBC repository.
 */
public final class JDBCRepositoryType {

    /**
     * Oracle8 repository type.
     */
    public static final JDBCRepositoryType ORACLE8 = new JDBCRepositoryType(JDBCDriverVendor.ORACLE8, "oracle.jdbc.driver.OracleDriver", "oracle");

    /**
     * Postgres repository type.
     */
    public static final JDBCRepositoryType POSTGRESQL = new JDBCRepositoryType(JDBCDriverVendor.POSTGRESQL, "org.postgresql.Driver", "postgresql");

    /**
     * MSSQL DATAD repository type.
     */
    public static final JDBCRepositoryType MSSQL_DATAD = new JDBCRepositoryType(JDBCDriverVendor.MSSQL_DATAD, "com.merant.datadirect.jdbc.sqlserver.SQLServerDriver", "sqlserver");

    /**
     * MSSQL JQL repository type.
     */
    public static final JDBCRepositoryType MSSQL_JSQL = new JDBCRepositoryType(JDBCDriverVendor.MSSQL_JSQL, "com.jnetdirect.jsql.JSQLDriver", "JSQLConnect");

    /**
     * MSSQL Microsoft repository type.
     */
    public static final JDBCRepositoryType MSSQL_MS = new JDBCRepositoryType(JDBCDriverVendor.MSSQL_MICROSOFT, "com.microsoft.jdbc.sqlserver.SQLServerDriver", "microsoft");

    /**
     * MSSQL Microsoft repository type.
     */
    public static final JDBCRepositoryType MSSQL_2005 = new JDBCRepositoryType(JDBCDriverVendor.MSSQL_2005, "com.microsoft.sqlserver.jdbc.SQLServerDriver", "");

    /**
     * DB2 repository type.
     */
    public static final JDBCRepositoryType DB2 = new JDBCRepositoryType(JDBCDriverVendor.DB2, "COM.ibm.db2.jdbc.net.DB2Driver", "db2");

    /**
     * DB2 MVS variants repository type.
     */
    public static final JDBCRepositoryType DB2_MVS = new JDBCRepositoryType(JDBCDriverVendor.DB2, "COM.ibm.db2.jdbc.app.DB2Driver", "db2");

    /**
     * DB2 type 4.
     */
    public static final JDBCRepositoryType DB2_TYPE4 = new JDBCRepositoryType(JDBCDriverVendor.DB2_TYPE4, "com.ibm.db2.jcc.DB2Driver", "db2");

    /**
     * Hypersonic repository type.
     */
    public static final JDBCRepositoryType HYPERSONIC = new JDBCRepositoryType(JDBCDriverVendor.HYPERSONIC, "org.hsqldb.jdbcDriver", "hsqldb");

    /**
     * MySQL repository type.
     */
    public static final JDBCRepositoryType MYSQL = new JDBCRepositoryType(JDBCDriverVendor.MYSQL, "com.mysql.jdbc.Driver", "mysql");

    /**
     * Derby repository type. The driver class is the DB2 JDBC Universal Driver.
     * which is compatible with DB2 JDBC Universal Driver release 1.2+
     */
    public static final JDBCRepositoryType DERBY = new JDBCRepositoryType(JDBCDriverVendor.DERBY_SERVER, "com.ibm.db2.jcc.DB2Driver", "derby");

    /**
     * Sybase repository type.
     */
    public static final JDBCRepositoryType SYBASE = new JDBCRepositoryType(JDBCDriverVendor.SYBASE, "com.sybase.jdbc2.jdbc.SybDriver", "sybase");

    /**
     * A Collection of all JDBCRepositoryTypes.
     */
    private static Collection types;

    /**
     * The vendow name of this JDBCRepositoryType.
     */
    private final JDBCDriverVendor vendor;

    /**
     * The class name of the driver for this JDBCRepositoryType.
     */
    private final String driverClassName;

    /**
     * The jdbc url sub-protocol for this type.
     */
    private final String subProtocol;

    /**
     * Construct a new JDBCRepositoryType.
     * @param vendor the vendor of this type
     * @param driverClassName the jdbc driver class name for this type
     * @param subProtocol the jdbc url sub-protocol for this type
     */
    private JDBCRepositoryType(JDBCDriverVendor vendor, String driverClassName, String subProtocol) {
        this.vendor = vendor;
        this.driverClassName = driverClassName;
        this.subProtocol = subProtocol;
        if (types == null) {
            types = new ArrayList();
        }
        types.add(this);
    }

    public final JDBCDriverVendor getVendor() {
        return vendor;
    }

    public final String getDriverClassName() {
        return driverClassName;
    }

    public final String getSubProtocol() {
        return subProtocol;
    }

    /**
     * Provide an Iterator over all JDBCRepositoryTypes.
     * @return the Iterator
     */
    public static final Iterator iterator() {
        return new Iterator() {

            private Iterator iterator = types.iterator();

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public Object next() {
                return iterator.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Provide the JDBCRepositoryType for a particular vendor.
     * @param vendor the vendor
     * @return the JDBCRepositoryType associated with the given vendor.
     */
    public static final JDBCRepositoryType getTypeForVendor(JDBCDriverVendor vendor) {
        JDBCRepositoryType type = null;
        Iterator iterator = iterator();
        while (iterator.hasNext() && type == null) {
            JDBCRepositoryType next = (JDBCRepositoryType) iterator.next();
            if (next.getVendor() == vendor) {
                type = next;
            }
        }
        return type;
    }

    /**
     * Provide the JDBCRepositoryType for a particular vendor.
     * @param vendor the vendor
     * @return the JDBCRepositoryType associated with the given vendor.
     */
    public static final JDBCRepositoryType getTypeForVendor(String vendor) {
        JDBCRepositoryType type = null;
        Iterator iterator = iterator();
        while (iterator.hasNext() && type == null) {
            JDBCRepositoryType next = (JDBCRepositoryType) iterator.next();
            if (next.getVendor().getName().equalsIgnoreCase(vendor)) {
                type = next;
            }
        }
        return type;
    }
}
