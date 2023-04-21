package org.apache.torque;

import org.apache.torque.adapter.DB;
import org.apache.torque.dsfactory.DataSourceFactory;
import org.apache.torque.map.DatabaseMap;
import org.apache.torque.oid.IDBroker;
import org.apache.torque.oid.IdGenerator;

/**
 * Bundles all information about a database. This includes the database adapter,
 * the database Map and the Data Source Factory.
 */
public class Database {

    /**
     * The name of the database. Must be the same as the key in Torque's
     * databaseMap.
     */
    private String name;

    /**
     * The Database adapter which encapsulates database-specific peculiarities.
     */
    private DB adapter;

    /**
     * the Map of this database.
     */
    private DatabaseMap databaseMap;

    /**
     * The DataSourceFactory to optain connections to this database.
     */
    private DataSourceFactory dataSourceFactory;

    /**
     * Creates a new Database with the given name.
     *
     * @param aName the name of the database, not null.
     */
    Database(final String aName) {
        this.name = aName;
    }

    /**
     * returns the name of the database.
     *
     * @return the name of the database. May be null.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the adapther to this database.
     *
     * @return the adapter to this database, or null if no adapter is set.
     */
    public DB getAdapter() {
        return adapter;
    }

    /**
     * Sets the adapter for this database.
     *
     * @param anAdapter The adapter for this database, or null to remove the
     *        current adapter from this database.
     */
    public void setAdapter(final DB anAdapter) {
        this.adapter = anAdapter;
    }

    /**
     * Returns the database map for this database.
     * If the database map does not exist yet, it is created by this method.
     */
    public synchronized DatabaseMap getDatabaseMap() {
        if (databaseMap == null) {
            databaseMap = new DatabaseMap(name);
        }
        return databaseMap;
    }

    /**
     * Returns the DataSourceFactory for this database.
     * The DataSourceFactory is responsible to create connections
     * to this database.
     *
     * @return the DataSourceFactory for this database, or null if no
     *         DataSourceFactory exists for this database.
     */
    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    /**
     * Sets the DataSourceFactory for this database.
     * The DataSourceFactory is responsible to create connections
     * to this database.
     *
     * @param aDataSourceFactory The new DataSorceFactory for this database,
     *        or null to remove the current DataSourceFactory.
     */
    public void setDataSourceFactory(final DataSourceFactory aDataSourceFactory) {
        this.dataSourceFactory = aDataSourceFactory;
    }

    /**
     * Get the IDBroker for this database.
     *
     * @return The IDBroker for this database, or null if no IdBroker has
     *         been started for this database.
     */
    public IDBroker getIDBroker() {
        if (databaseMap == null) {
            return null;
        }
        return databaseMap.getIDBroker();
    }

    /**
     * Creates the IDBroker for this DatabaseMap and starts it for the
     * given database.
     * The information about the IdTable is stored in the databaseMap.
     * If an IDBroker already exists for the DatabaseMap, the method
     * does nothing.
     *
     * @return true if a new IDBroker was created, false otherwise.
     */
    public synchronized boolean startIDBroker() {
        final DatabaseMap dbMap = getDatabaseMap();
        if (dbMap.getIDBroker() != null) {
            return false;
        }
        return dbMap.startIdBroker();
    }

    /**
     * Returns the IdGenerator of the given type for this Database.
     * @param type The type (i.e.name) of the IdGenerator
     * @return The IdGenerator of the requested type, or null if no IdGenerator
     *         exists for the requested type.
     */
    public IdGenerator getIdGenerator(final String type) {
        if (databaseMap == null) {
            return null;
        }
        return databaseMap.getIdGenerator(type);
    }

    /**
     * Adds an IdGenerator to the database.
     * @param type The type of the IdGenerator
     * @param idGen The new IdGenerator for the type, or null
     *        to remove the IdGenerator of the given type.
     */
    public void addIdGenerator(final String type, final IdGenerator idGen) {
        getDatabaseMap().addIdGenerator(type, idGen);
    }

    /**
     * Returns the database schema for this Database.
     * @return the database schema for this database, or null if no schema
     *         has been set.
     */
    public String getSchema() {
        final DataSourceFactory dsf = getDataSourceFactory();
        if (dsf == null) {
            return null;
        }
        return dsf.getSchema();
    }

    /**
     * Sets the schema for this database.
     * @param schema the name of the database schema to set, or null to remove
     *        the current schema.
     * @throws NullPointerException if no DatasourceFactory exists for this
     *         database.
     */
    public void setSchema(final String schema) {
        getDataSourceFactory().setSchema(schema);
    }
}
