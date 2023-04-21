package com.movilnet.clom.framework.repository.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import com.movilnet.clom.framework.repository.IGenericRepositoryLocal;
import com.movilnet.clom.framework.repository.IGenericRepositoryRemote;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Remote(IGenericRepositoryRemote.class)
@Local(IGenericRepositoryLocal.class)
public class GenericRepository<T, ID extends Serializable> implements IGenericRepositoryLocal<T, ID>, IGenericRepositoryRemote<T, ID> {

    private EntityManager entityManager;

    @PersistenceContext(unitName = "clomDS")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public T findById(Class<T> type, ID id) {
        return this.entityManager.find(type, id);
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    @Override
    public T save(T entity) {
        return this.entityManager.merge(entity);
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    @Override
    public void persist(T entity) {
        this.entityManager.persist(entity);
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    @Override
    public void delete(T entity) {
        this.entityManager.refresh(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll(Class<T> type) {
        Session session = (Session) this.entityManager.getDelegate();
        Criteria criteria = session.createCriteria(type);
        return criteria.list();
    }

    @Override
    public List<T> findReferenceData(Class<T> type) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByExample(Class<T> type, T entity) {
        Session session = (Session) this.entityManager.getDelegate();
        Criteria criteria = session.createCriteria(type).add(Example.create(entity));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByQueryNamed(Class<T> type, String nameQuery, Object... params) {
        Query query = this.entityManager.createNamedQuery(nameQuery, type);
        for (int index = 0; index < params.length; ++index) query.setParameter(index + 1, params[index]);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByQueryNamedAndNamedParams(Class<T> type, String nameQuery, Map<String, ? extends Object> params) {
        Query query = this.entityManager.createNamedQuery(nameQuery, type);
        for (final Map.Entry<String, ? extends Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        return query.getResultList();
    }

    @Override
    public Integer countByExample(Class<T> type, T entity) {
        return this.findByExample(type, entity).size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findScalarByNamedQuery(Map<String, Object> params, final String nameQuery) {
        Query query = this.entityManager.createQuery(nameQuery);
        for (final Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        return query.getResultList();
    }
}
