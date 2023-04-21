package com.googlecode.agscrum.model.repository.jpa;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import com.googlecode.agscrum.model.exceptions.GScrumDomainException;
import com.googlecode.agscrum.model.exceptions.MessageFactory;
import com.googlecode.agscrum.model.exceptions.GScrumDomainException.TypeMessage;
import com.googlecode.agscrum.model.repository.Repository;
import com.googlecode.agscrum.model.util.ReflectionUtil;

public class RepositoryImpl<Entity, Key> implements Repository<Entity, Key> {

    private Class<Entity> persistentClass = null;

    protected EntityManager entityManager;

    @Autowired
    private MessageFactory messageFactory;

    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManagerArg) {
        entityManager = entityManagerArg;
    }

    public void setPersistentClass(Class<Entity> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @SuppressWarnings("unchecked")
    public Class<Entity> getPersistentClass() {
        if (persistentClass == null) {
            this.persistentClass = (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    public void setPersistentClass(String persistentClass) {
        try {
            this.persistentClass = (Class<Entity>) Class.forName(persistentClass);
        } catch (ClassNotFoundException e) {
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Entity save(Entity entity) {
        String fieldName = ReflectionUtil.getFieldNameID(persistentClass);
        Object id = ReflectionUtil.getValueField(persistentClass, entity, fieldName);
        duplicateValidate(entity, id);
        if (id == null) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return entity;
    }

    @Override
    public Entity findBy(Key id) {
        return entityManager.find(persistentClass, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Entity> findAll() {
        return entityManager.createQuery("from " + persistentClass.getName()).getResultList();
    }

    @Override
    public void remove(Entity entity) {
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
    }

    private void duplicateValidate(Entity entity, Object id) {
        String queryString = null;
        try {
            queryString = ReflectionUtil.getQueryString(getPersistentClass().getSimpleName() + ".duplicateValidate", getPersistentClass());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (queryString != null) {
            if (id != null) {
                queryString = queryString.replaceAll("(?i)where ", "where (");
                queryString += ") and id!=:id";
            }
            Query query = entityManager.createQuery(queryString);
            Pattern pattern = Pattern.compile(":\\w+");
            Matcher matcher = pattern.matcher(queryString);
            try {
                while (matcher.find()) {
                    String atributo = matcher.group();
                    query.setParameter(atributo.substring(1), ReflectionUtil.getValueField(getPersistentClass(), entity, atributo.substring(1)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Long total = (Long) query.getSingleResult();
            if (total > 0) {
                throw new GScrumDomainException(messageFactory.createMessage(getPersistentClass().getSimpleName() + ".not.duplicate", "default.not.duplicate", TypeMessage.ERROR));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Entity> readByCriteria(com.googlecode.agscrum.model.util.Criteria criteria, int initialPos, int finalPos) {
        Query query = getQueryByCriteria(criteria);
        query.setFirstResult(initialPos).setMaxResults(finalPos);
        return query.getResultList();
    }

    private Query getQueryByCriteria(com.googlecode.agscrum.model.util.Criteria criteria) {
        if (criteria == null || criteria.getNamedQuery() == null) {
            throw new IllegalArgumentException("Criteria/QueryString is null");
        }
        Query query;
        String whereString = criteria.getWhereString();
        Map<String, Object> parameters = criteria.getParameters();
        if (parameters != null && parameters.size() > 0) {
            query = entityManager.createQuery(whereString);
            for (String key : parameters.keySet()) {
                query = query.setParameter(key, parameters.get(key));
            }
        } else {
            query = entityManager.createQuery(whereString);
        }
        if (criteria.isCacheable()) {
            query.setHint("org.hibernate.cacheable", true);
        }
        return query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Entity> readByCriteria(com.googlecode.agscrum.model.util.Criteria criteria) {
        Query query = getQueryByCriteria(criteria);
        return query.getResultList();
    }

    @Override
    public Long totalOfByCriteria(com.googlecode.agscrum.model.util.Criteria criteria) {
        if (criteria == null || criteria.getNamedQuery() == null) {
            throw new IllegalArgumentException("Criteria/QueryString is null");
        }
        String whereString = criteria.getWhereString().replaceAll("select.+?from", "select count(*) as total from");
        Query query = null;
        Map<String, Object> parameters = criteria.getParameters();
        if (parameters != null && parameters.size() > 0) {
            query = entityManager.createQuery(whereString);
            for (String key : parameters.keySet()) {
                query = query.setParameter(key, parameters.get(key));
            }
        } else {
            query = entityManager.createQuery(whereString);
        }
        return (Long) query.getSingleResult();
    }
}
