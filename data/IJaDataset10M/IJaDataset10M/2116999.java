package org.openscience.nmrshiftdb.om;

import java.util.Vector;
import org.apache.turbine.om.NumberKey;
import org.apache.turbine.om.ObjectKey;
import org.apache.turbine.om.peer.BasePeer;
import org.apache.turbine.services.db.TurbineDB;
import org.apache.turbine.util.Log;
import org.apache.turbine.util.db.Criteria;
import org.apache.turbine.util.db.map.MapBuilder;
import org.apache.turbine.util.db.map.TableMap;
import org.apache.turbine.util.db.pool.DBConnection;
import org.openscience.nmrshiftdb.om.map.DBPublisherMapBuilder;
import com.workingdogs.village.QueryDataSet;
import com.workingdogs.village.Record;

/**
 */
public abstract class BaseDBPublisherPeer extends BasePeer {

    /** the mapbuilder for this class */
    private static final DBPublisherMapBuilder mapBuilder = (DBPublisherMapBuilder) getMapBuilder(DBPublisherMapBuilder.CLASS_NAME);

    /** the table name for this class */
    public static final String TABLE_NAME = mapBuilder.getTable();

    /** 
     * @return the map builder for this peer
     */
    public static MapBuilder getMapBuilder() {
        return (mapBuilder);
    }

    /** the column name for the PUBLISHER_ID field */
    public static final String PUBLISHER_ID = mapBuilder.getDBPublisher_PublisherId();

    /** the column name for the NAME field */
    public static final String NAME = mapBuilder.getDBPublisher_Name();

    /** the column name for the PLACE field */
    public static final String PLACE = mapBuilder.getDBPublisher_Place();

    /** number of columns for this peer */
    public static final int numColumns = 3;

    /** A class that can be returned by this peer. */
    protected static final String CLASSNAME_DEFAULT = "org.openscience.nmrshiftdb.om.DBPublisher";

    /** A class that can be returned by this peer. */
    protected static final Class CLASS_DEFAULT = initClass();

    /** Initialization method for static CLASS_DEFAULT attribute */
    private static Class initClass() {
        Class c = null;
        try {
            c = Class.forName(CLASSNAME_DEFAULT);
        } catch (Exception e) {
            Log.error("A FATAL ERROR has occurred which should not" + "have happened under any circumstance.  Please notify" + "Turbine and give as many details as possible including the " + "error stacktrace.", e);
        }
        return c;
    }

    /**
     * Get the list of objects for a ResultSet.  Please not that your
     * resultset MUST return columns in the right order.  You can use
     * getFieldNames() in BaseObject to get the correct sequence.
     */
    public static Vector resultSet2Objects(java.sql.ResultSet results) throws Exception {
        QueryDataSet qds = null;
        Vector rows = null;
        try {
            qds = new QueryDataSet(results);
            rows = getSelectResults(qds);
        } finally {
            if (qds != null) qds.close();
        }
        return populateObjects(rows);
    }

    /** Method to do inserts */
    public static ObjectKey doInsert(Criteria criteria) throws Exception {
        return BasePeer.doInsert(criteria);
    }

    /** 
     * Method to do inserts.  This method is to be used during a transaction,
     * otherwise use the doInsert(Criteria) method.  It will take care of 
     * the connection details internally. 
     */
    public static ObjectKey doInsert(Criteria criteria, DBConnection dbCon) throws Exception {
        return BasePeer.doInsert(criteria, dbCon);
    }

    /** Add all the columns needed to create a new object */
    public static void addSelectColumns(Criteria criteria) throws Exception {
        criteria.addSelectColumn(PUBLISHER_ID);
        criteria.addSelectColumn(NAME);
        criteria.addSelectColumn(PLACE);
    }

    /** 
     * Create a new object of type cls from a resultset row starting
     * from a specified offset.  This is done so that you can select
     * other rows than just those needed for this object.  You may
     * for example want to create two objects from the same row.
     */
    public static DBPublisher row2Object(Record row, int offset, Class cls) throws Exception {
        DBPublisher obj = (DBPublisher) cls.newInstance();
        populateObject(row, offset, obj);
        obj.setModified(false);
        obj.setNew(false);
        return obj;
    }

    /** 
     * Populates an object from a resultset row starting
     * from a specified offset.  This is done so that you can select
     * other rows than just those needed for this object.  You may
     * for example want to create two objects from the same row.
     */
    public static void populateObject(Record row, int offset, DBPublisher obj) throws Exception {
        obj.setPublisherId(new NumberKey(row.getValue(offset + 0).asBigDecimal()));
        obj.setName(row.getValue(offset + 1).asString());
        obj.setPlace(row.getValue(offset + 2).asString());
    }

    /** Method to do selects */
    public static Vector doSelect(Criteria criteria) throws Exception {
        return populateObjects(doSelectVillageRecords(criteria));
    }

    /** Method to do selects within a transaction */
    public static Vector doSelect(Criteria criteria, DBConnection dbCon) throws Exception {
        return populateObjects(doSelectVillageRecords(criteria, dbCon));
    }

    /** 
     * Grabs the raw Village records to be formed into objects.
     * This method handles connections internally.  The Record objects
     * returned by this method should be considered readonly.  Do not
     * alter the data and call save(), your results may vary, but are
     * certainly likely to result in hard to track MT bugs.
     */
    public static Vector doSelectVillageRecords(Criteria criteria) throws Exception {
        if (criteria.getSelectColumns().size() == 0) {
            addSelectColumns(criteria);
        }
        return BasePeer.doSelect(criteria);
    }

    /** 
     * Grabs the raw Village records to be formed into objects.
     * This method should be used for transactions 
     */
    public static Vector doSelectVillageRecords(Criteria criteria, DBConnection dbCon) throws Exception {
        if (criteria.getSelectColumns().size() == 0) {
            addSelectColumns(criteria);
        }
        return BasePeer.doSelect(criteria, dbCon);
    }

    /** 
     * The returned vector will contain objects of the default type or
     * objects that inherit from the default.
     */
    public static Vector populateObjects(Vector records) throws Exception {
        Vector results = new Vector(records.size());
        for (int i = 0; i < records.size(); i++) {
            Record row = (Record) records.elementAt(i);
            results.add(DBPublisherPeer.row2Object(row, 1, DBPublisherPeer.getOMClass()));
        }
        return results;
    }

    /** 
     * The class that the Peer will make instances of. 
     * If the BO is abstract then you must implement this method
     * in the BO.
     */
    public static Class getOMClass() throws Exception {
        return CLASS_DEFAULT;
    }

    /**
     * Method to do updates. 
     *
     * @param Criteria object containing data that is used to create the UPDATE statement.
     */
    public static void doUpdate(Criteria criteria) throws Exception {
        Criteria selectCriteria = new Criteria(mapBuilder.getDatabaseMap().getName(), 2);
        selectCriteria.put(PUBLISHER_ID, criteria.remove(PUBLISHER_ID));
        BasePeer.doUpdate(selectCriteria, criteria);
    }

    /** 
     * Method to do updates.  This method is to be used during a transaction,
     * otherwise use the doUpdate(Criteria) method.  It will take care of 
     * the connection details internally. 
     *
     * @param Criteria object containing data that is used to create the UPDATE statement.
     */
    public static void doUpdate(Criteria criteria, DBConnection dbCon) throws Exception {
        Criteria selectCriteria = new Criteria(mapBuilder.getDatabaseMap().getName(), 2);
        selectCriteria.put(PUBLISHER_ID, criteria.remove(PUBLISHER_ID));
        BasePeer.doUpdate(selectCriteria, criteria, dbCon);
    }

    /** 
     * Method to do deletes.
     *
     * @param Criteria object containing data that is used DELETE from database.
     */
    public static void doDelete(Criteria criteria) throws Exception {
        BasePeer.doDelete(criteria);
    }

    /** 
     * Method to do deletes.  This method is to be used during a transaction,
     * otherwise use the doDelete(Criteria) method.  It will take care of 
     * the connection details internally. 
     *
     * @param Criteria object containing data that is used DELETE from database.
     */
    public static void doDelete(Criteria criteria, DBConnection dbCon) throws Exception {
        BasePeer.doDelete(criteria, dbCon);
    }

    /** Method to do inserts */
    public static void doInsert(DBPublisher obj) throws Exception {
        obj.setPrimaryKey(doInsert(buildCriteria(obj)));
        obj.setNew(false);
    }

    /**
     * @param obj the data object to update in the database.
     */
    public static void doUpdate(DBPublisher obj) throws Exception {
        doUpdate(buildCriteria(obj));
    }

    /**
     * @param obj the data object to delete in the database.
     */
    public static void doDelete(DBPublisher obj) throws Exception {
        doDelete(buildCriteria(obj));
    }

    /** 
     * Method to do inserts.  This method is to be used during a transaction,
     * otherwise use the doInsert(DBPublisher) method.  It will take 
     * care of the connection details internally. 
     *
     * @param obj the data object to insert into the database.
     */
    public static void doInsert(DBPublisher obj, DBConnection dbCon) throws Exception {
        obj.setPrimaryKey(doInsert(buildCriteria(obj), dbCon));
        obj.setNew(false);
    }

    /**
     * Method to do update.  This method is to be used during a transaction,
     * otherwise use the doUpdate(DBPublisher) method.  It will take 
     * care of the connection details internally. 
     *
     * @param obj the data object to update in the database.
     */
    public static void doUpdate(DBPublisher obj, DBConnection dbCon) throws Exception {
        doUpdate(buildCriteria(obj), dbCon);
    }

    /**
     * Method to delete.  This method is to be used during a transaction,
     * otherwise use the doDelete(DBPublisher) method.  It will take 
     * care of the connection details internally. 
     *
     * @param obj the data object to delete in the database.
     */
    public static void doDelete(DBPublisher obj, DBConnection dbCon) throws Exception {
        doDelete(buildCriteria(obj), dbCon);
    }

    /** Build a Criteria object from the data object for this peer */
    public static Criteria buildCriteria(DBPublisher obj) {
        Criteria criteria = new Criteria();
        if (!obj.isNew()) criteria.add(PUBLISHER_ID, obj.getPublisherId());
        criteria.add(NAME, obj.getName());
        criteria.add(PLACE, obj.getPlace());
        return criteria;
    }

    /** 
     * Retrieve a single object by pk
     *
     * @param ObjectKey pk
     */
    public static DBPublisher retrieveByPK(ObjectKey pk) throws Exception {
        DBConnection db = null;
        DBPublisher retVal = null;
        try {
            db = TurbineDB.getConnection(mapBuilder.getDatabaseMap().getName());
            retVal = retrieveByPK(pk, db);
        } finally {
            if (db != null) TurbineDB.releaseConnection(db);
        }
        return (retVal);
    }

    /** 
     * Retrieve a single object by pk
     *
     * @param ObjectKey pk
     * @param DBConnection dbcon
     */
    public static DBPublisher retrieveByPK(ObjectKey pk, DBConnection dbcon) throws Exception {
        Criteria criteria = new Criteria();
        criteria.add(PUBLISHER_ID, pk);
        Vector v = doSelect(criteria, dbcon);
        if (v.size() != 1) {
            throw new Exception("Failed to select one and only one row.");
        } else {
            return (DBPublisher) v.firstElement();
        }
    }

    /** 
     * Returns the TableMap related to this peer.  This method is not 
     * needed for general use but a specific application could have a
     * need.
     */
    protected static TableMap getTableMap() {
        return mapBuilder.getDatabaseMap().getTable(TABLE_NAME);
    }
}
