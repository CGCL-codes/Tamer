package org.objectstyle.cayenne.remote;

import org.objectstyle.cayenne.map.DataMap;
import org.objectstyle.cayenne.map.DbEntity;
import org.objectstyle.cayenne.map.EntityResolver;
import org.objectstyle.cayenne.map.ObjEntity;
import org.objectstyle.cayenne.map.Procedure;
import org.objectstyle.cayenne.query.PrefetchTreeNode;
import org.objectstyle.cayenne.query.Query;
import org.objectstyle.cayenne.query.QueryMetadata;
import org.objectstyle.cayenne.query.QueryRouter;
import org.objectstyle.cayenne.query.SQLAction;
import org.objectstyle.cayenne.query.SQLActionVisitor;

/**
 * A client wrapper for the incremental query that overrides the metadata to ensure that
 * query result is cached on the server, so that subranges could be retrieved at a later
 * time.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
class IncrementalQuery implements Query {

    private Query query;

    private String cacheKey;

    IncrementalQuery(Query query, String cacheKey) {
        this.query = query;
        this.cacheKey = cacheKey;
    }

    public QueryMetadata getMetaData(EntityResolver resolver) {
        final QueryMetadata metadata = query.getMetaData(resolver);
        return new QueryMetadata() {

            public String getCacheKey() {
                return cacheKey;
            }

            public String getCachePolicy() {
                return metadata.getCachePolicy();
            }

            public DataMap getDataMap() {
                return metadata.getDataMap();
            }

            public DbEntity getDbEntity() {
                return metadata.getDbEntity();
            }

            public int getFetchLimit() {
                return metadata.getFetchLimit();
            }

            public int getFetchStartIndex() {
                return metadata.getFetchStartIndex();
            }

            public ObjEntity getObjEntity() {
                return metadata.getObjEntity();
            }

            public int getPageSize() {
                return metadata.getPageSize();
            }

            public PrefetchTreeNode getPrefetchTree() {
                return metadata.getPrefetchTree();
            }

            public Procedure getProcedure() {
                return metadata.getProcedure();
            }

            public boolean isFetchingDataRows() {
                return metadata.isFetchingDataRows();
            }

            public boolean isRefreshingObjects() {
                return metadata.isRefreshingObjects();
            }

            public boolean isResolvingInherited() {
                return metadata.isResolvingInherited();
            }
        };
    }

    public void route(QueryRouter router, EntityResolver resolver, Query substitutedQuery) {
        query.route(router, resolver, substitutedQuery);
    }

    public SQLAction createSQLAction(SQLActionVisitor visitor) {
        return query.createSQLAction(visitor);
    }

    public String getName() {
        return query.getName();
    }

    /**
     * @deprecated since 1.2 as super is deprecated.
     */
    public void setName(String name) {
        this.query.setName(name);
    }

    /**
     * @deprecated since 1.2 as super is deprecated.
     */
    public Object getRoot() {
        return query.getRoot();
    }

    /**
     * @deprecated since 1.2 as super is deprecated.
     */
    public void setRoot(Object root) {
        query.setRoot(root);
    }
}
