package org.datanucleus.store.rdbms.mapping.mysql;

import java.sql.SQLException;
import org.datanucleus.store.mapped.DatastoreField;
import org.datanucleus.store.mapped.MappedStoreManager;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.mapping.RDBMSMapping;
import org.datanucleus.store.rdbms.table.Column;

/**
 * Abstract base class for all MySQL spatial mappings. Contains helper methods
 * that convert WKB (Well Known Binary) data to MySQL spatial objects and vice versa.
 */
public abstract class MySQLSpatialRDBMSMapping extends RDBMSMapping {

    protected static final int SRID_LENGTH = 4;

    private static final byte XDR = 0;

    private static final byte NDR = 1;

    public MySQLSpatialRDBMSMapping(JavaTypeMapping mapping, MappedStoreManager storeMgr, DatastoreField field) {
        super(storeMgr, mapping);
        column = (Column) field;
        initialize();
    }

    public MySQLSpatialRDBMSMapping(MappedStoreManager storeMgr, JavaTypeMapping mapping) {
        super(storeMgr, mapping);
        initialize();
    }

    protected void initialize() {
        initTypeInfo();
    }

    /**
     * Converts a spatial object from MySQL binary format to WKB.
     * 
     * @param mysqlBinary A spatial object in MySQL binary format.
     * @return The spatial object as WKB.
     * @throws SQLException If the object contains invalid data.
     */
    protected byte[] mysqlBinaryToWkb(byte[] mysqlBinary) throws SQLException {
        byte[] wkb = new byte[mysqlBinary.length - SRID_LENGTH];
        System.arraycopy(mysqlBinary, SRID_LENGTH, wkb, 0, wkb.length);
        return wkb;
    }

    /**
     * Extracts the SRID of a spatial object in MySQL binary format.
     * 
     * @param mysqlBinary A spatial object in MySQL binary format.
     * @return The SRID
     * @throws SQLException If the object contains invalid data.
     */
    protected int mysqlBinaryToSrid(byte[] mysqlBinary) throws SQLException {
        if (isBigEndian(mysqlBinary[SRID_LENGTH])) {
            return mysqlBinary[0] << 24 | (mysqlBinary[1] & 0xff) << 16 | (mysqlBinary[2] & 0xff) << 8 | (mysqlBinary[3] & 0xff);
        } else {
            return mysqlBinary[3] << 24 | (mysqlBinary[2] & 0xff) << 16 | (mysqlBinary[1] & 0xff) << 8 | (mysqlBinary[0] & 0xff);
        }
    }

    /**
     * Converts a spatial object from WKB to MySQL binary format.
     * 
     * @param wkb A spatial object in WKB format.
     * @param srid The SRID of the spatial object.
     * @return The spatial object in MySQL binary format.
     * @throws SQLException If the object contains invalid data.
     */
    protected byte[] wkbToMysqlBinary(byte[] wkb, int srid) throws SQLException {
        byte[] mysqlBinary = new byte[wkb.length + SRID_LENGTH];
        if (isBigEndian(wkb[0])) {
            mysqlBinary[0] = (byte) (srid >> 24);
            mysqlBinary[1] = (byte) (srid >> 16);
            mysqlBinary[2] = (byte) (srid >> 8);
            mysqlBinary[3] = (byte) srid;
        } else {
            mysqlBinary[3] = (byte) (srid >> 24);
            mysqlBinary[2] = (byte) (srid >> 16);
            mysqlBinary[1] = (byte) (srid >> 8);
            mysqlBinary[0] = (byte) srid;
        }
        System.arraycopy(wkb, 0, mysqlBinary, SRID_LENGTH, wkb.length);
        return mysqlBinary;
    }

    private boolean isBigEndian(byte flag) throws SQLException {
        if (flag == XDR) return true; else if (flag == NDR) return false; else throw new SQLException("Unknown Endian type:" + flag);
    }
}
