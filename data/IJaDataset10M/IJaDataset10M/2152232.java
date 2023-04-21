package org.starobjects.jpa.runtime.persistence.objectstore.util;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

public final class QueryUtil {

    private static final Logger LOG = Logger.getLogger(QueryUtil.class);

    private QueryUtil() {
    }

    public static Query createQuery(Session session, final String alias, final String select, final NakedObjectSpecification specification, final String whereClause) {
        final StringBuilder buf = new StringBuilder(128);
        appendSelect(buf, select);
        appendFrom(buf, specification, alias);
        final String queryString = buf.toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug("creating query: " + queryString);
        }
        return session.createQuery(queryString);
    }

    private static StringBuilder appendSelect(final StringBuilder buf, final String select) {
        if (select != null) {
            buf.append(select).append(" ");
        }
        return buf;
    }

    private static StringBuilder appendFrom(final StringBuilder buf, final NakedObjectSpecification specification, final String alias) {
        return buf.append("from ").append(specification.getFullName()).append(" as ").append(alias);
    }
}
