package org.jomc.sdk.jpa;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
public final class QueryFactory {

    public Query getObject() {
        final EntityManager em = this.getEntityManager();
        if (em != null) {
            final Query q = em.createQuery(this.getQuery());
            for (Map.Entry<String, Object> e : this.getParameterMap().entrySet()) {
                q.setParameter(e.getKey(), e.getValue());
            }
            return q;
        }
        return null;
    }

    /** Creates a new {@code QueryFactory} instance. */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    public QueryFactory() {
        super();
    }

    /**
     * Gets the {@code <EntityManager>} dependency.
     * <p>
     *   This method returns the {@code <JOMC SDK JPA>} object of the {@code <javax.persistence.EntityManager>} specification at any specification level.
     *   That specification does not apply to any scope. A new object is returned whenever requested.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <EntityManager>} dependency.
     * {@code null} if no object is available.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private javax.persistence.EntityManager getEntityManager() {
        return (javax.persistence.EntityManager) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "EntityManager");
    }

    /**
     * Gets the value of the {@code <parameterMap>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @return Map of parameters to set on the query.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private java.util.Map<String, Object> getParameterMap() {
        final java.util.Map<String, Object> _p = (java.util.Map<String, Object>) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "parameterMap");
        assert _p != null : "'parameterMap' property not found.";
        return _p;
    }

    /**
     * Gets the value of the {@code <query>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @return Query to provide.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private java.lang.String getQuery() {
        final java.lang.String _p = (java.lang.String) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "query");
        assert _p != null : "'query' property not found.";
        return _p;
    }
}
