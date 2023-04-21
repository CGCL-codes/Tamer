package org.objectstyle.cayenne.query;

import org.apache.commons.lang.StringUtils;
import org.objectstyle.cayenne.CayenneRuntimeException;
import org.objectstyle.cayenne.ObjectId;
import org.objectstyle.cayenne.exp.Expression;
import org.objectstyle.cayenne.exp.ExpressionFactory;
import org.objectstyle.cayenne.map.EntityResolver;
import org.objectstyle.cayenne.map.ObjEntity;
import org.objectstyle.cayenne.util.Util;

/**
 * A query that matches zero or one object or data row corresponding to the ObjectId. Used
 * internally by Cayenne to lookup objects by id. Notice that cache policies of
 * ObjectIdQuery are different from generic {@link QueryMetadata} cache policies.
 * ObjectIdQuery is special - it is the only query that can be done against Cayenne main
 * cache, thus cache handling is singnificantly different from all other of the queries.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
public class ObjectIdQuery extends IndirectQuery {

    public static final int CACHE = 1;

    public static final int CACHE_REFRESH = 2;

    public static final int CACHE_NOREFRESH = 3;

    protected ObjectId objectId;

    protected int cachePolicy;

    protected boolean fetchingDataRows;

    private ObjectIdQuery() {
        this.cachePolicy = CACHE_REFRESH;
    }

    /**
     * Creates a refreshing SingleObjectQuery.
     */
    public ObjectIdQuery(ObjectId objectID) {
        this(objectID, false, CACHE_REFRESH);
    }

    /**
     * Creates a new ObjectIdQuery.
     */
    public ObjectIdQuery(ObjectId objectId, boolean fetchingDataRows, int cachePolicy) {
        if (objectId == null) {
            throw new NullPointerException("Null objectID");
        }
        this.objectId = objectId;
        this.cachePolicy = cachePolicy;
        this.fetchingDataRows = fetchingDataRows;
    }

    public QueryMetadata getMetaData(final EntityResolver resolver) {
        return new DefaultQueryMetadata() {

            public ObjEntity getObjEntity() {
                return resolver.lookupObjEntity(objectId.getEntityName());
            }

            public boolean isFetchingDataRows() {
                return fetchingDataRows;
            }
        };
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    protected Query createReplacementQuery(EntityResolver resolver) {
        if (objectId == null) {
            throw new CayenneRuntimeException("Can't resolve query - objectId is null.");
        }
        if (objectId.isTemporary() && !objectId.isReplacementIdAttached()) {
            throw new CayenneRuntimeException("Can't build a query for temporary id: " + objectId);
        }
        SelectQuery query = new SelectQuery(objectId.getEntityName(), ExpressionFactory.matchAllDbExp(objectId.getIdSnapshot(), Expression.EQUAL_TO));
        query.setRefreshingObjects(true);
        query.setFetchingDataRows(fetchingDataRows);
        return query;
    }

    /**
     * @deprecated since 1.2
     */
    public Object getRoot() {
        return objectId.getEntityName();
    }

    public int getCachePolicy() {
        return cachePolicy;
    }

    public boolean isFetchMandatory() {
        return cachePolicy == CACHE_REFRESH;
    }

    public boolean isFetchAllowed() {
        return cachePolicy != CACHE_NOREFRESH;
    }

    public boolean isFetchingDataRows() {
        return fetchingDataRows;
    }

    /**
     * Overrides toString() outputting a short string with query class and ObjectId.
     */
    public String toString() {
        return StringUtils.substringAfterLast(getClass().getName(), ".") + ":" + objectId;
    }

    /**
     * An object is considered equal to this query if it is also a SingleObjectQuery with
     * an equal ObjectId.
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ObjectIdQuery)) {
            return false;
        }
        ObjectIdQuery query = (ObjectIdQuery) object;
        return Util.nullSafeEquals(objectId, query.getObjectId());
    }

    /**
     * Implements a standard hashCode contract considering custom 'equals' implementation.
     */
    public int hashCode() {
        return (objectId != null) ? objectId.hashCode() : 11;
    }
}
