package org.dspace.uri.dao.oracle;

import org.apache.log4j.Logger;
import org.dspace.core.Context;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;
import org.dspace.uri.ObjectIdentifier;
import org.dspace.uri.dao.ObjectIdentifierDAO;
import org.dspace.uri.dao.ObjectIdentifierStorageException;
import org.dspace.uri.dao.postgres.ObjectIdentifierDAOPostgres;
import java.sql.SQLException;
import java.util.UUID;

/**
 * DAO implementation to persist ObjectIdentifier objects in a PostgreSQL database
 *
 * FIXME: UNTESTED; identical to ExternalIdentifierDAOPostgres
 *
 * @author Richard Jones
 */
public class ObjectIdentifierDAOOracle extends ObjectIdentifierDAO {

    /** log4j logger */
    private Logger log = Logger.getLogger(ObjectIdentifierDAOPostgres.class);

    /** SQL to retrieve a record based on UUID */
    private String retrieveUUID = "SELECT * FROM uuid WHERE uuid = ?";

    /** SQL to retrieve a record based on resource type and storage id */
    private String retrieveID = "SELECT * FROM uuid WHERE resource_type = ? AND resource_id = ?";

    /** SQL to retrieve a record based on all its properties (i.e. existance test) */
    private String existing = "SELECT * FROM uuid WHERE uuid = ? AND resource_type = ? AND resource_id = ?";

    /** SQL to insert a record into the database */
    private String insertSQL = "INSERT INTO uuid (uuid, resource_type, resource_id) VALUES (?, ?, ?)";

    /** SQL to delete a record from the database based on resource type and storage id */
    private String deleteObjectSQL = "DELETE FROM uuid WHERE resource_type = ? AND resource_id = ?";

    /**
     * Construct a new ObjectIdentifierDAOOracle with the given DSpace Context
     *
     * @param context
     */
    public ObjectIdentifierDAOOracle(Context context) {
        super(context);
    }

    /**
     * Create a persistent record of the given ObjectIdentifier
     *
     * @param oid
     */
    public void create(ObjectIdentifier oid) throws ObjectIdentifierStorageException {
        try {
            Object[] params = { oid.getUUID().toString(), new Integer(oid.getResourceTypeID()), new Integer(oid.getResourceID()) };
            DatabaseManager.updateQuery(context, insertSQL, params);
        } catch (SQLException e) {
            log.error("caught exception: ", e);
            throw new ObjectIdentifierStorageException(e);
        }
    }

    /**
     * Retrieve the ObjectIdentifier associated with the given DSpaceObject
     *
     * @param uuid
     * @return
     */
    public ObjectIdentifier retrieve(UUID uuid) throws ObjectIdentifierStorageException {
        try {
            Object[] params = { uuid.toString() };
            TableRowIterator tri = DatabaseManager.query(context, retrieveUUID, params);
            if (!tri.hasNext()) {
                tri.close();
                return null;
            }
            TableRow row = tri.next();
            ObjectIdentifier oid = new ObjectIdentifier(uuid, row.getIntColumn("resource_type"), row.getIntColumn("resource_id"));
            tri.close();
            return oid;
        } catch (SQLException e) {
            log.error("caught exception: ", e);
            throw new ObjectIdentifierStorageException(e);
        }
    }

    /**
     * Retrieve the ObjectIdentifier associated with the given DSpace object type and
     * storage layer id
     *
     * @param type
     * @param id
     * @return
     */
    public ObjectIdentifier retrieve(int type, int id) throws ObjectIdentifierStorageException {
        try {
            Object[] params = { new Integer(type), new Integer(id) };
            TableRowIterator tri = DatabaseManager.query(context, retrieveID, params);
            if (!tri.hasNext()) {
                tri.close();
                return null;
            }
            TableRow row = tri.next();
            ObjectIdentifier oid = new ObjectIdentifier(row.getStringColumn("uuid"), type, id);
            tri.close();
            return oid;
        } catch (SQLException e) {
            log.error("caught exception: ", e);
            throw new ObjectIdentifierStorageException(e);
        }
    }

    /**
     * Update the record of the given ObjectIdentifier
     *
     * @param oid
     */
    public void update(ObjectIdentifier oid) throws ObjectIdentifierStorageException {
        try {
            if (!exists(oid)) {
                create(oid);
            }
        } catch (SQLException e) {
            log.error("caught exception: ", e);
            throw new ObjectIdentifierStorageException(e);
        }
    }

    /**
     * Delete all record of the given ObjectIdentifier
     *
     * @param oid
     */
    public void delete(ObjectIdentifier oid) throws ObjectIdentifierStorageException {
        try {
            Object[] params = { new Integer(oid.getResourceTypeID()), new Integer(oid.getResourceID()) };
            DatabaseManager.updateQuery(context, deleteObjectSQL, params);
        } catch (SQLException e) {
            log.error("caught exception: ", e);
            throw new ObjectIdentifierStorageException(e);
        }
    }

    /**
     * Test whether the given ObjectIdentifier exists in the database
     *
     * @param oid
     * @return
     * @throws java.sql.SQLException
     */
    private boolean exists(ObjectIdentifier oid) throws SQLException {
        Object[] params = { oid.getUUID().toString(), new Integer(oid.getResourceTypeID()), new Integer(oid.getResourceID()) };
        TableRowIterator tri = DatabaseManager.query(context, existing, params);
        if (!tri.hasNext()) {
            tri.close();
            return false;
        }
        tri.close();
        return true;
    }
}
