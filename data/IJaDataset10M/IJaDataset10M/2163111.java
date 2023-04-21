package collab.fm.server.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.criterion.Restrictions;
import collab.fm.server.util.exception.EntityPersistenceException;
import collab.fm.server.util.exception.StaleDataException;

/**
 * Generic DAO Hibernate implementation.
 * @author Yi Li
 *
 * @param <EntityType>
 * @param <IdType>
 */
public abstract class GenericDaoImpl<EntityType, IdType extends Serializable> implements GenericDao<EntityType, IdType> {

    static Logger logger = Logger.getLogger(GenericDaoImpl.class);

    private Class<EntityType> entityClass;

    public GenericDaoImpl() {
        entityClass = (Class<EntityType>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<EntityType> getEntityClass() {
        return entityClass;
    }

    @SuppressWarnings("unchecked")
    protected List getByAttrValue(Long modelId, String attrName, String val, String entityClassName, boolean like) throws EntityPersistenceException, StaleDataException {
        String queryString = "select entity " + "from " + entityClassName + " as entity " + (modelId != null ? "join entity.model as m " : "") + "join entity.attrs as a " + "join a.values as v " + "where index(a) = '" + attrName + "' " + (modelId != null ? "and m.id = :mId " : "") + (like ? "and v.strVal like :val" : "and v.strVal = :val");
        Query qry = HibernateUtil.getCurrentSession().createQuery(queryString);
        if (modelId != null) {
            qry = qry.setLong("mId", modelId);
        }
        if (like) {
            qry = qry.setString("val", "%" + val + "%");
        } else {
            qry = qry.setString("val", val);
        }
        try {
            List result = qry.list();
            return result.isEmpty() ? null : result;
        } catch (StaleObjectStateException sose) {
            logger.warn("Stale data detected. Force client to retry.", sose);
            throw new StaleDataException(sose);
        } catch (Exception e) {
            logger.warn("Query failed.", e);
            throw new EntityPersistenceException("Query failed.", e);
        }
    }

    protected List getAll() throws EntityPersistenceException, StaleDataException {
        try {
            Criteria crit = HibernateUtil.getCurrentSession().createCriteria(getEntityClass());
            List result = crit.list();
            return result.isEmpty() ? null : result;
        } catch (StaleObjectStateException sose) {
            logger.warn("Stale data detected. Force client to retry.", sose);
            throw new StaleDataException(sose);
        } catch (RuntimeException e) {
            logger.warn("Couldn't get all.", e);
            throw new EntityPersistenceException(e);
        }
    }

    protected List getAll(IdType modelId, String modelPropertyName) throws EntityPersistenceException, StaleDataException {
        try {
            Criteria crit = HibernateUtil.getCurrentSession().createCriteria(getEntityClass()).createCriteria(modelPropertyName).add(Restrictions.eq("id", modelId));
            List result = crit.list();
            return result.isEmpty() ? null : result;
        } catch (StaleObjectStateException sose) {
            logger.warn("Stale data detected. Force client to retry.", sose);
            throw new StaleDataException(sose);
        } catch (RuntimeException e) {
            logger.warn("Couldn't get all.", e);
            throw new EntityPersistenceException(e);
        }
    }

    public EntityType getById(IdType id, boolean lock) throws EntityPersistenceException, StaleDataException {
        try {
            if (lock) {
                return (EntityType) HibernateUtil.getCurrentSession().get(getEntityClass(), id, LockMode.UPGRADE);
            } else {
                return (EntityType) HibernateUtil.getCurrentSession().get(getEntityClass(), id);
            }
        } catch (StaleObjectStateException sose) {
            logger.warn("Stale data detected. Force client to retry.", sose);
            throw new StaleDataException(sose);
        } catch (RuntimeException e) {
            logger.warn("Get by ID failed. (ID=" + id + ")", e);
            throw new EntityPersistenceException(e);
        }
    }

    public EntityType save(EntityType entity) throws EntityPersistenceException, StaleDataException {
        try {
            Session session = HibernateUtil.getCurrentSession();
            session.saveOrUpdate(entity);
            return entity;
        } catch (StaleObjectStateException sose) {
            logger.warn("Stale data detected. Force client to retry.", sose);
            throw new StaleDataException(sose);
        } catch (RuntimeException e) {
            logger.warn("Couldn't save entity", e);
            throw new EntityPersistenceException(e);
        }
    }

    public List<EntityType> saveAll(List<EntityType> entities) throws EntityPersistenceException, StaleDataException {
        return null;
    }

    public void deleteById(IdType id) throws EntityPersistenceException, StaleDataException {
        HibernateUtil.getCurrentSession().delete(this.getById(id, false));
    }

    public void delete(EntityType entity) throws EntityPersistenceException, StaleDataException {
        HibernateUtil.getCurrentSession().delete(entity);
    }
}
