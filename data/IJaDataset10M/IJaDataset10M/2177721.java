package org.homemotion.dao;

import java.util.Date;
import java.util.List;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.homemotion.user.AppUser;
import org.homemotion.user.impl.AnonymousUser;

public abstract class AbstractItemManagerImpl<T extends Item> implements ItemManager<T> {

    protected final Logger logger = Logger.getLogger(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    protected abstract Class<T> getItemClass();

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Long create(T item) {
        setCreationFields(item);
        try {
            this.entityManager.persist(item);
            return item.getId();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create instance: " + item, e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T getByName(String name) {
        List<T> itemsFound = findItems(name);
        if (itemsFound.isEmpty()) {
            return null;
        }
        if (itemsFound.size() == 1) {
            return itemsFound.get(0);
        } else {
            throw new IllegalArgumentException("Name given was not unique for item type " + getItemClass() + " , name was '" + name + "'.");
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(T item) {
        try {
            item = this.entityManager.merge(item);
            this.entityManager.remove(item);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to delete instance: " + item, e);
        }
    }

    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<T> findItems(String nameExpression) {
        Query query = this.entityManager.createQuery("SELECT item FROM " + getItemClass().getSimpleName() + " item WHERE item.name LIKE :nameExpression");
        query.setParameter("nameExpression", nameExpression);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T get(Long id) {
        return this.entityManager.find(getItemClass(), id);
    }

    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<T> getAllItems() {
        Query query = this.entityManager.createQuery(getAllItemQuery());
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected String getAllItemQuery() {
        return "SELECT item FROM " + getItemClass().getSimpleName() + " item";
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T update(T item) {
        try {
            return this.entityManager.merge(item);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to update instance: " + item, e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T refresh(T item) {
        try {
            return get(item.getId());
        } catch (Exception e) {
            throw new IllegalStateException("Nested transaction not supported!", e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean isPersistent(T item) {
        try {
            return this.entityManager.contains(item);
        } catch (Exception e) {
            return false;
        }
    }

    protected AppUser getCurrentUser() {
        return AnonymousUser.getInstance();
    }

    protected String getCurrentUserID() {
        AppUser user = getCurrentUser();
        if (user != null) {
            return user.getName();
        }
        return AnonymousUser.getInstance().getName();
    }

    protected void setCreationFields(T item) {
        if (item instanceof AbstractItem) {
            ((AbstractItem) item).setCreatedFrom(getCurrentUserID());
            ((AbstractItem) item).setUpdatedFrom(((AbstractItem) item).getCreatedFrom());
        }
    }

    protected void setUpdateFields(T item) {
        if (item instanceof AbstractItem) {
            ((AbstractItem) item).setUpdatedDT(new Date());
            ((AbstractItem) item).setUpdatedFrom(getCurrentUserID());
        }
    }
}
