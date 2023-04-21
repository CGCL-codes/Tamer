package org.ivoa.util;

import org.eclipse.persistence.internal.databaseaccess.DatabaseCall;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.queries.SQLCall;
import org.eclipse.persistence.sessions.Session;
import org.ivoa.conf.RuntimeConfiguration;
import org.ivoa.jpa.JPAFactory;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Simple SQL Query executor using eclipseLink
 *
 * @author Laurent Bourges (voparis) / Gerard Lemson (mpe)
 */
public class SQLUtils {

    /**
   * TODO : Field Description
   */
    public static final int MAX_ROWS_UNLIMITED = -1;

    /**
   * TODO : Field Description
   */
    public static final int QUERY_NO_TIMEOUT = -1;

    /**
   * Constructor
   */
    private SQLUtils() {
    }

    /**
   * TODO : Method Description
   *
   * @param em 
   * @param sql 
   *
   * @return value TODO : Value Description
   */
    public static List<?> executeQuery(final EntityManager em, final String sql) {
        return executeQuery(em, sql, MAX_ROWS_UNLIMITED, QUERY_NO_TIMEOUT);
    }

    /**
   * TODO : Method Description
   *
   * @param sql 
   * @param maxRows 
   * @param timeout 
   *
   * @return value TODO : Value Description
   */
    public static List<?> executeQuery(final String sql, final int maxRows, final int timeout) {
        EntityManager em = null;
        try {
            final JPAFactory jf = JPAFactory.getInstance(RuntimeConfiguration.get().getJPAPU());
            em = jf.getEm();
            return executeQuery(em, sql, maxRows, timeout);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
   * TODO : Method Description
   *
   * @param em 
   * @param sql 
   * @param maxRows 
   * @param timeout 
   *
   * @return value TODO : Value Description
   */
    public static List<?> executeQuery(final EntityManager em, final String sql, final int maxRows, final int timeout) {
        List<?> rows = null;
        final JpaEntityManager eem = JpaHelper.getEntityManager(em);
        if (eem != null) {
            final Session s = eem.getSession();
            final DatabaseCall call = new SQLCall(sql);
            if (maxRows > 0) {
                call.setMaxRows(maxRows);
            }
            if (timeout > 0) {
                call.setQueryTimeout(timeout);
            }
            rows = s.executeSelectingCall(call);
        }
        return rows;
    }
}
