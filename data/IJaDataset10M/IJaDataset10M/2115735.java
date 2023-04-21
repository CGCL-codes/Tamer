package org.nexopenframework.persistence.support;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nexopenframework.persistence.FlushMode;
import org.nexopenframework.persistence.PersistenceManager;
import org.springframework.util.ClassUtils;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Represents a bridge (GoF pattern) between the well-known interface {@link EntityManager} of
 * JSR-220 specification and the {@link PersistenceManager} interface. So, you can use the standard
 * interface in your applications without configuring any  {@link  javax.persistence.spi.PersistenceProvider}
 * and only configuring the {@link PersistenceManager}.</p>
 * 
 * @see EntityManager
 * @see PersistenceManager
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public abstract class EntityManagerBridge {

    /**logging facility*/
    private static final Log LOGGER = LogFactory.getLog(EntityManagerBridge.class);

    /**
	 * <p></p>
	 * 
	 * @param pm
	 * @return
	 */
    public static EntityManager createEntityManager(final PersistenceManager pm) {
        final Class<?> interfaces[] = new Class[] { EntityManager.class };
        return createEntityManager(pm, interfaces);
    }

    /**
	 * @param pm
	 * @param interfaces
	 * @return
	 */
    public static EntityManager createEntityManager(final PersistenceManager pm, final Class<?> interfaces[]) {
        final EntityManager em = (EntityManager) Proxy.newProxyInstance(EntityManagerBridge.class.getClassLoader(), interfaces, new EntityManagerBridgeHandler(pm));
        return em;
    }

    private static class EntityManagerBridgeHandler implements InvocationHandler {

        /**The persistence manager which performs all the persistence operations*/
        private final PersistenceManager pm;

        EntityManagerBridgeHandler(final PersistenceManager pm) {
            this.pm = pm;
        }

        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Invoked method " + method);
            }
            if (method.getName().equals("equals")) {
                return (proxy == args[0]);
            } else if (method.getName().equals("hashCode")) {
                return hashCode();
            } else if (method.getName().equals("toString")) {
                return "Bridge EntityManager-PersistenceManager";
            } else if (method.getName().equals("isOpen")) {
                return true;
            } else if (method.getName().equals("close")) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Not implemented method close");
                }
                return null;
            } else if (method.getName().equals("clear")) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Not implemented method clear");
                }
                return null;
            } else if (method.getName().equals("contains")) {
                final Object entity = args[0];
                return pm.contains(entity);
            } else if (method.getName().equals("getTransaction")) {
                throw new IllegalStateException("Not allowed to create transaction on shared EntityManager - " + "use Spring transactions or EJB CMT instead");
            } else if (method.getName().equals("joinTransaction")) {
                throw new IllegalStateException("Not allowed to join transaction on shared EntityManager - " + "use Spring transactions or EJB CMT instead");
            } else if (method.getName().equals("getDelegate")) {
                return pm;
            } else if (method.getName().equals("merge")) {
                final Object entity = args[0];
                pm.merge(entity);
                return entity;
            } else if (method.getName().equals("remove")) {
                Object entity = args[0];
                pm.delete(entity);
                return null;
            } else if (method.getName().equals("clear")) {
                return null;
            } else if (method.getName().equals("find") || method.getName().equals("getReference")) {
                final Class<?> entityClass = (Class<?>) args[0];
                final Serializable primaryKey = (Serializable) args[1];
                final Object entity = this.pm.findByPrimaryKey(entityClass, primaryKey);
                if (entity == null && method.getName().equals("getReference")) {
                    throw new EntityNotFoundException("Entity " + entityClass.getName() + " not found for id " + primaryKey);
                }
                return entity;
            } else if (method.getName().equals("createQuery")) {
                final org.nexopenframework.persistence.Query q = this.pm.createQuery((String) args[0]);
                final QueryBridge handler = new QueryBridge(q);
                return handler;
            } else if (method.getName().equals("createNamedQuery")) {
                final org.nexopenframework.persistence.Query q = this.pm.createNamedQuery((String) args[0]);
                final QueryBridge handler = new QueryBridge(q);
                return handler;
            } else if (method.getName().equals("createNativeQuery")) {
                if (args.length == 1) {
                    final org.nexopenframework.persistence.Query q = this.pm.createNativeQuery((String) args[0]);
                    final QueryBridge handler = new QueryBridge(q);
                    return handler;
                } else if (args.length == 2) {
                    final org.nexopenframework.persistence.Query q = (args[1] instanceof String) ? this.pm.createNativeQuery((String) args[0], (String) args[1]) : this.pm.createNativeQuery((String) args[0], (Class) args[1]);
                    final QueryBridge handler = new QueryBridge(q);
                    return handler;
                }
            } else if (method.getName().equals("getDelegate")) {
                return this.pm;
            }
            final Method m_pm = ClassUtils.getMethodIfAvailable(pm.getClass(), method.getName(), method.getParameterTypes());
            if (m_pm != null) {
                return m_pm.invoke(pm, args);
            }
            LOGGER.warn("No method found in bridge " + method.getName());
            return null;
        }
    }

    private static class QueryBridge implements Query {

        /**The NexOpen Query object which performs th operations*/
        final org.nexopenframework.persistence.Query query;

        QueryBridge(final org.nexopenframework.persistence.Query query) {
            this.query = query;
        }

        @Override
        public String toString() {
            return "Bridge JPA Query - PersistenceManager Query";
        }

        public int executeUpdate() {
            return query.executeUpdate();
        }

        public List getResultList() {
            return query.execute();
        }

        public Object getSingleResult() {
            return query.executeUnique();
        }

        public Query setFirstResult(int startPosition) {
            query.setFirstResult(startPosition);
            return this;
        }

        public Query setFlushMode(FlushModeType flushMode) {
            int int_flushMode = -1;
            switch(flushMode) {
                case COMMIT:
                    int_flushMode = FlushMode.COMMIT;
                    break;
                case AUTO:
                    int_flushMode = FlushMode.AUTO;
                    break;
            }
            query.setFlushMode(int_flushMode);
            return this;
        }

        public Query setHint(String hintName, Object value) {
            query.setHint(hintName, value);
            return this;
        }

        public Query setMaxResults(int maxResult) {
            query.setMaxResults(maxResult);
            return this;
        }

        public Query setParameter(String name, Object value) {
            query.setParameter(name, value);
            return this;
        }

        public Query setParameter(int position, Object value) {
            query.setParameter(position, value);
            return this;
        }

        public Query setParameter(String name, Date value, TemporalType temporalType) {
            int int_temporalType = -1;
            switch(temporalType) {
                case DATE:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.DATE;
                    break;
                case TIME:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.TIME;
                    break;
                case TIMESTAMP:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.TIMESTAMP;
                    break;
            }
            query.setParameter(name, value, int_temporalType);
            return this;
        }

        public Query setParameter(String name, Calendar value, TemporalType temporalType) {
            int int_temporalType = -1;
            switch(temporalType) {
                case DATE:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.DATE;
                    break;
                case TIME:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.TIME;
                    break;
                case TIMESTAMP:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.TIMESTAMP;
                    break;
            }
            query.setParameter(name, value, int_temporalType);
            return this;
        }

        public Query setParameter(int position, Date value, TemporalType temporalType) {
            int int_temporalType = -1;
            switch(temporalType) {
                case DATE:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.DATE;
                    break;
                case TIME:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.TIME;
                    break;
                case TIMESTAMP:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.TIMESTAMP;
                    break;
            }
            query.setParameter(position, value, int_temporalType);
            return null;
        }

        public Query setParameter(int position, Calendar value, TemporalType temporalType) {
            int int_temporalType = -1;
            switch(temporalType) {
                case DATE:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.DATE;
                    break;
                case TIME:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.TIME;
                    break;
                case TIMESTAMP:
                    int_temporalType = org.nexopenframework.persistence.TemporalType.TIMESTAMP;
                    break;
            }
            query.setParameter(position, value, int_temporalType);
            return this;
        }
    }
}
