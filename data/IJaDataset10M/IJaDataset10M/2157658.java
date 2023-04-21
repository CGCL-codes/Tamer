package org.pixory.pxmodel;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * utility methods for the Hibernate Session
 */
public class PXSessionUtility extends Object {

    private static final Log LOG = LogFactory.getLog(PXSessionUtility.class);

    private PXSessionUtility() {
    }

    /**
	 * the hibernate method throws an exception if a row doesn't exist for the
	 * id. Here we trap them and just return null
	 * From Pixory 0.5.3 to 0.5.4 the session.load is refactored to a session.get
	 * Reason that pre 0.5.4 catched the exception when loading failed and 
	 * returned a null. This is the default behaviour of get().
	 */
    public static Object tryLoad(Session session, Class clazz, Serializable id) throws SQLException, HibernateException {
        Object tryLoad = null;
        if (session != null) {
            tryLoad = session.get(clazz, id);
        }
        return tryLoad;
    }

    /**
	 * returns a List of PXPersistentObjects of type clazz, which match ids
	 */
    public static List getObjectsForIds(Session session, Class clazz, Set ids) throws HibernateException {
        List getObjectsForIds = null;
        if ((session != null) && (clazz != null) && (ids != null)) {
            String aClassName = clazz.getName();
            String aSetString = getSetString(ids);
            String aQueryString = "from " + aClassName + " subject where subject.id in " + aSetString;
            Query q = session.createQuery(aQueryString);
            getObjectsForIds = q.list();
        }
        return getObjectsForIds;
    }

    /**
	 * given List of ids, turn it into a Set specifier suitable for use in a SQL
	 * 'IN' clause
	 */
    private static String getSetString(Set ids) {
        String getSetString = null;
        if (ids != null) {
            StringBuffer aBuffer = new StringBuffer();
            aBuffer.append("(");
            Iterator anIdIterator = ids.iterator();
            while (anIdIterator.hasNext()) {
                String anId = (String) anIdIterator.next();
                if (anId != null) {
                    aBuffer.append("'" + anId + "'");
                    if (anIdIterator.hasNext()) {
                        aBuffer.append(", ");
                    }
                }
            }
            aBuffer.append(")");
            getSetString = aBuffer.toString();
        }
        return getSetString;
    }
}
