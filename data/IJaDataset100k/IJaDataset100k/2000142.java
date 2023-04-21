package org.objectstyle.cayenne.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.objectstyle.cayenne.ObjectId;
import org.objectstyle.cayenne.map.DbAttribute;
import org.objectstyle.cayenne.map.DbEntity;

/**
 * Batched INSERT query. Allows inserting multiple object snapshots (DataRows) for a given
 * DbEntity in a single query. InsertBatchQuery normally is not used directly. Rather
 * DataContext creates one internally when committing DataObjects.
 * 
 * @author Andriy Shapochka
 */
public class InsertBatchQuery extends BatchQuery {

    /**
     * @since 1.2
     */
    protected List objectIds;

    protected List objectSnapshots;

    protected List dbAttributes;

    /**
     * Creates new InsertBatchQuery for a given DbEntity and estimated capacity.
     */
    public InsertBatchQuery(DbEntity entity, int batchCapacity) {
        super(entity);
        this.objectSnapshots = new ArrayList(batchCapacity);
        this.objectIds = new ArrayList(batchCapacity);
        this.dbAttributes = new ArrayList(getDbEntity().getAttributes());
    }

    public Object getValue(int dbAttributeIndex) {
        DbAttribute attribute = (DbAttribute) dbAttributes.get(dbAttributeIndex);
        Map currentSnapshot = (Map) objectSnapshots.get(batchIndex);
        return getValue(currentSnapshot, attribute);
    }

    /**
     * Adds a snapshot to batch. A shortcut for "add(snapshot, null)".
     */
    public void add(Map snapshot) {
        add(snapshot, null);
    }

    /**
     * Adds a snapshot to batch. Optionally stores the object id for the snapshot. Note
     * that snapshot can hold either the real values or the instances of
     * org.apache.commons.collections.Factory that will be resolved to the actual value on
     * the spot, thus allowing deferred propagated keys resolution.
     * 
     * @since 1.2
     */
    public void add(Map snapshot, ObjectId id) {
        objectSnapshots.add(snapshot);
        objectIds.add(id);
    }

    public int size() {
        return objectSnapshots.size();
    }

    public List getDbAttributes() {
        return dbAttributes;
    }

    /**
     * Returns an ObjectId associated with the current batch iteration. Used internally by
     * Cayenne to match current iteration with a specific object and assign it generated
     * keys.
     * 
     * @since 1.2
     */
    public ObjectId getObjectId() {
        return (ObjectId) objectIds.get(batchIndex);
    }
}
