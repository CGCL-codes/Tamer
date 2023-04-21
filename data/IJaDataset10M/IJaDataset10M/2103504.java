package org.geotools.data.hatbox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.geotools.data.DataSourceException;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.JDBC1DataStore;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.referencing.crs.EPSGCRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * A cache of Hatbox catalogue entries.
 * 
 */
public class HatBoxCatalogue {

    private static final Logger LOGGER = Logger.getLogger(org.geotools.data.hatbox.HatBoxCatalogue.class.getName());

    private static final String[] TABLE_TYPE = new String[] { "TABLE" };

    private JDBC1DataStore dataStore;

    private HashMap<String, HatboxTable> typeMap;

    private HashMap<Integer, CoordinateReferenceSystem> crsMap = new HashMap<Integer, CoordinateReferenceSystem>();

    private DbSql dbSql;

    public HatBoxCatalogue(JDBC1DataStore dataStore, DbSql dbSql) {
        this.dataStore = dataStore;
        this.dbSql = dbSql;
    }

    public String getSchema() {
        return dataStore.getDatabaseSchemaName();
    }

    public DbSql getDbSql() {
        return dbSql;
    }

    /**
     * Load types from database.
     * @return
     * @throws IOException
     */
    public HashMap<String, HatboxTable> loadTypeMap() throws IOException {
        Connection conn = null;
        HashMap<String, HatboxTable> newTypes = new HashMap<String, HatboxTable>();
        try {
            conn = dataStore.getConnection(Transaction.AUTO_COMMIT);
            Statement stmt = conn.createStatement();
            DatabaseMetaData dbMeta = conn.getMetaData();
            ResultSet rs = dbMeta.getTables(null, dataStore.getDatabaseSchemaName(), "%HATBOX", TABLE_TYPE);
            while (rs.next()) {
                StringBuilder query = new StringBuilder("select NODE_DATA from \"");
                query.append(dataStore.getDatabaseSchemaName());
                query.append("\".\"");
                String table = rs.getString(3);
                query.append(table);
                query.append("\" where ID = 1");
                try {
                    ResultSet metaNodeRs = stmt.executeQuery(query.toString());
                    metaNodeRs.next();
                    HatboxTable meta = new HatboxTable(metaNodeRs.getBytes(1));
                    loadCrs(meta.getSrid());
                    if (loadCrs(meta.getSrid())) {
                        newTypes.put(meta.getTable(), meta);
                    } else {
                        LOGGER.log(Level.FINE, "Table " + table + " does not have a valid EPSG code specified for its SRID");
                    }
                } catch (SQLException sqle) {
                    LOGGER.log(Level.FINE, "Table " + table + " does not appear to be a valid rtree index table", sqle);
                }
            }
            typeMap = newTypes;
            return newTypes;
        } catch (SQLException sqlException) {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, sqlException);
            conn = null;
            String message = "Error querying database for list of tables:" + sqlException.getMessage();
            throw new DataSourceException(message, sqlException);
        } finally {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }
    }

    public HatboxTable getType(String table) {
        return getTypeMap().get(table);
    }

    public String[] getTypeNames() {
        Set<String> keySet = getTypeMap().keySet();
        return keySet.toArray(new String[keySet.size()]);
    }

    public boolean isGeomColumn(String table, String column) {
        HatboxTable type = getTypeMap().get(table);
        if (type == null) {
            return false;
        } else {
            return type.getGeomColumn().equalsIgnoreCase(column);
        }
    }

    public boolean isExposeIdToUser(String table) {
        HatboxTable type = getTypeMap().get(table);
        if (type == null) {
            return false;
        } else {
            return type.isExposePK();
        }
    }

    public int getSRID(String table, String column) {
        HatboxTable type = getTypeMap().get(table);
        if (type == null) {
            return -1;
        } else {
            if (type.getGeomColumn().equals(column)) {
                return type.getSrid();
            } else {
                return -1;
            }
        }
    }

    private boolean loadCrs(int srid) {
        CoordinateReferenceSystem crs = null;
        try {
            CRSAuthorityFactory factory = EPSGCRSAuthorityFactory.getDefault();
            crs = factory.createCoordinateReferenceSystem("EPSG:" + srid);
            if (crs != null) {
                crsMap.put(srid, crs);
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public AttributeDescriptor buildGeomAttribute(String table) {
        HatboxTable meta = getTypeMap().get(table);
        AttributeTypeBuilder atb = new AttributeTypeBuilder();
        atb.setName(meta.getGeomColumn());
        atb.setBinding(meta.getGeomType());
        atb.setCRS(crsMap.get(meta.getSrid()));
        atb.setMinOccurs(1);
        atb.setMaxOccurs(1);
        atb.setNillable(false);
        return atb.buildDescriptor(meta.getGeomColumn());
    }

    private HashMap<String, HatboxTable> getTypeMap() {
        try {
            if (typeMap == null) {
                loadTypeMap();
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to load type map", ioe);
        }
        return typeMap;
    }
}
