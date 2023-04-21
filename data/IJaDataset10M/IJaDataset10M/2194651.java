package org.objectstyle.cayenne.query;

import java.util.Map;
import org.objectstyle.cayenne.ObjectId;
import org.objectstyle.cayenne.map.DbEntity;
import org.objectstyle.cayenne.map.EntityResolver;
import org.objectstyle.cayenne.map.ObjEntity;

/**
 * Describes insert database operation. InsertQuery is initialized with object values
 * snapshot and ObjectId.
 * 
 * @deprecated since 1.2 Object InsertQuery is not needed anymore. It shouldn't be used
 *             directly anyway, but in cases where one might want to have access to it,
 *             InsertBatchQuery is a reasonable substitute.
 */
public class InsertQuery extends AbstractQuery {

    protected ObjectId objectId;

    protected Map objectSnapshot;

    /** Creates empty InsertQuery. */
    public InsertQuery() {
    }

    /** Creates InsertQuery with the <code>rootEntity</code> as the root object */
    public InsertQuery(ObjEntity rootEntity) {
        this.setRoot(rootEntity);
    }

    /** Creates InsertQuery with the <code>rootClass</code> as the root object */
    public InsertQuery(Class rootClass) {
        this.setRoot(rootClass);
    }

    /** Creates InsertQuery with the <code>rootClass</code> as the root object */
    public InsertQuery(Class rootClass, Map dataRow) {
        this.setRoot(rootClass);
        this.setObjectSnapshot(dataRow);
    }

    /** Creates InsertQuery with <code>objEntityName</code> parameter. */
    public InsertQuery(String objEntityName) {
        this.setRoot(objEntityName);
    }

    public QueryMetadata getMetaData(final EntityResolver resolver) {
        return new DefaultQueryMetadata() {

            public ObjEntity getObjEntity() {
                return resolver.lookupObjEntity(objectId.getEntityName());
            }

            public DbEntity getDbEntity() {
                return getObjEntity().getDbEntity();
            }
        };
    }

    /**
     * Calls "makeUpdate" on the visitor.
     * 
     * @since 1.2
     */
    public SQLAction createSQLAction(SQLActionVisitor visitor) {
        return visitor.updateAction(this);
    }

    public Map getObjectSnapshot() {
        return objectSnapshot;
    }

    public void setObjectSnapshot(Map objectSnapshot) {
        this.objectSnapshot = objectSnapshot;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }
}
