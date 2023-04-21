package org.objectstyle.cayenne.access;

import org.objectstyle.cayenne.map.EntityResolver;
import org.objectstyle.cayenne.query.AbstractQuery;
import org.objectstyle.cayenne.query.Query;
import org.objectstyle.cayenne.query.QueryRouter;
import org.objectstyle.cayenne.query.SQLAction;
import org.objectstyle.cayenne.query.SQLActionVisitor;

public class MockNoRoutingQuery extends AbstractQuery {

    protected boolean routed;

    public void route(QueryRouter router, EntityResolver resolver, Query substitutedQuery) {
        this.routed = true;
    }

    public SQLAction createSQLAction(SQLActionVisitor visitor) {
        throw new UnsupportedOperationException();
    }

    public boolean isRouted() {
        return routed;
    }
}
