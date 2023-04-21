package com.fisoft.phucsinh.phucsinhsrv.service.common;

import com.fisoft.phucsinh.phucsinhsrv.entity.EntityStatus;
import com.fisoft.phucsinh.phucsinhsrv.entity.ICommonEntity;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityExistsException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityLockedException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityRemovedException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPUniqueConstraintViolationException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPUnrecoverableException;
import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Version: 1.0
 * Session bean Facade that provides business functions to module Contact
 * Copy all from ERP project
 */
@Stateless
public class CRUDManager implements ICRUDManager {

    @EJB
    private ICRUDEAO commonEAO;

    public CRUDManager() {
    }

    /**
     * Persists an entity into database.
     * @param pEntity - the entity to be persisted. This entity must not be bound to any children (Collection<?> = null)
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityExistsException if entity already exists
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity> void create(T pEntity) throws ERPEntityExistsException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        commonEAO.create(pEntity);
    }

    /**
     * Persists a Collection of entities into database.
     * @param pEntityList - the Collection of entities to be persisted. Each entity must not be bound to any children (Collection<?> = null)
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityExistsException if entity already exists
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity> void createList(Collection<T> pEntityList) throws ERPEntityExistsException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        for (T entity : pEntityList) {
            commonEAO.create(entity);
        }
    }

    /**
     * Updates an entity into database, this method can't remove children entities from the parent.
     * @param <T> entity type
     * @param pEntity - the entity to be merged. Identity of all elements of children Collections must not be changed.
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityLockedException if entity gets optimistic lock
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityRemovedException if entity is already removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity> void editSingleEntity(T pEntity) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        try {
            commonEAO.edit(pEntity);
        } catch (ERPEntityLockedException mel) {
            throw mel;
        }
    }

    /**
     * Only used when edit parent entity and add children with relation OTM, check each child for Genrated ID & perist entity if it's new, no need if using Toplink provider
     * @param <T>
     * @param pEntityList
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException
     */
    private <T extends ICommonEntity> void checkAndCreateNewEntity(Collection<T> pEntityList) throws ERPUnrecoverableException {
        for (T entity : pEntityList) {
            try {
                commonEAO.create(entity);
            } catch (ERPEntityExistsException eex) {
                eex.printStackTrace();
            } catch (Exception ex) {
                throw new ERPUnrecoverableException(ex);
            }
        }
    }

    /**
     * Only used when edit parent entity and add children with relation MTM, check each child for Genrated ID & perist entity if it's new, no need if using Toplink provider
     * @param <T>
     * @param pEntityList
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException
     */
    private <T extends ICommonEntity> void checkEntityExisting(Collection<T> pEntityList) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        for (T entity : pEntityList) {
            commonEAO.edit(entity);
        }
    }

    private <T extends ICommonEntity> void updateChildrenList(Collection<T> pEntityList) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        for (T entity : pEntityList) {
            this.editSingleEntity(entity);
        }
    }

    /**
     * General method to Updates an entity into database, and add/update new/existing children belonged to this.
     *
     * @param <T> entity type
     * @param <R> type of related entity
     * @param relationCtrl controller for a particular pair of relationship
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityLockedException if entity gets optimistic lock
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityRemovedException if entity is already removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity, R extends ICommonEntity> void editAddChildrenOTM(RelationController<T, R> relationCtrl) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        Collection<R> pChildrenToAdd = relationCtrl.getChildrenToAdd();
        if (pChildrenToAdd != null) {
            for (R child : pChildrenToAdd) {
                relationCtrl.setJoinProperty(child);
            }
            checkAndCreateNewEntity(pChildrenToAdd);
        }
        Collection<R> originalChildren = relationCtrl.getChildrenCollection();
        updateChildrenList(originalChildren);
        T parentEntity = relationCtrl.getParentEntity();
        if (pChildrenToAdd != null) {
            for (R child : pChildrenToAdd) {
                originalChildren.add(child);
            }
        }
        this.editSingleEntity(parentEntity);
    }

    /**
     * One-To-Many Relationship: Updates an entity into database and existing children belonged to this then remove children entities from the parent.
     *
     * @param <T> entity type
     * @param <R> type of related entity
     * @param relationCtrl controller for a particular pair of relationship
     * @param removeChildFlag remove child entity out of database or not
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityLockedException if entity gets optimistic lock
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityRemovedException if entity is already removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity, R extends ICommonEntity> void editRemoveChildrenOTM(RelationController<T, R> relationCtrl, Boolean removeChildFlag) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        Collection<R> originalChildren = relationCtrl.getChildrenCollection();
        T parentEntity = relationCtrl.getParentEntity();
        Collection<R> pChildrenToRemove = relationCtrl.getChildrenToRemove();
        if (pChildrenToRemove != null) {
            for (R child : pChildrenToRemove) {
                originalChildren.remove(child);
                if (removeChildFlag) {
                    commonEAO.remove(child);
                } else {
                    relationCtrl.setJoinProperty(null);
                    commonEAO.edit(child);
                }
            }
        }
        updateChildrenList(originalChildren);
        this.editSingleEntity(parentEntity);
    }

    /**
     * One-To-Many Relationship:Updates an entity of <code>T</code> into database and add/remove children entities.
     *
     * @param <T> entity type
     * @param <R> type of related entity
     * @param relationCtrl controller for a particular pair of relationship
     * @param removeChildFlag remove child entity out of database or not
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityLockedException if entity gets optimistic lock
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityRemovedException if entity is already removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity, R extends ICommonEntity> void editAddRemoveChildrenOTM(RelationController<T, R> relationCtrl, Boolean removeChildFlag) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        Collection<R> originalChildren = relationCtrl.getChildrenCollection();
        T parentEntity = relationCtrl.getParentEntity();
        Collection<R> pChildrenToAdd = relationCtrl.getChildrenToAdd();
        Collection<R> pChildrenToRemove = relationCtrl.getChildrenToRemove();
        if (pChildrenToRemove != null) {
            for (R child : pChildrenToRemove) {
                originalChildren.remove(child);
                if (removeChildFlag) {
                    commonEAO.remove(child);
                } else {
                    relationCtrl.setJoinProperty(null);
                    commonEAO.edit(child);
                }
            }
        }
        updateChildrenList(originalChildren);
        if (pChildrenToAdd != null) {
            for (R child : pChildrenToAdd) {
                relationCtrl.setJoinProperty(child);
            }
            checkAndCreateNewEntity(pChildrenToAdd);
        }
        if (pChildrenToAdd != null) {
            for (R child : pChildrenToAdd) {
                originalChildren.add(child);
            }
        }
        this.editSingleEntity(parentEntity);
    }

    /**
     * Many-To-Many Relationship: General method to Updates an entity into database and add/update new/existing children belonged to this.
     *
     * @param <T> entity type
     * @param <R> type of related entity
     * @param relationCtrl controller for a particular pair of relationship
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityLockedException if entity gets optimistic lock
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityRemovedException if entity is already removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity, R extends ICommonEntity> void editAddChildrenMTM(RelationController<T, R> relationCtrl) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        Collection<R> originalChildren = relationCtrl.getChildrenCollection();
        updateChildrenList(originalChildren);
        T parentEntity = relationCtrl.getParentEntity();
        Collection<R> pChildrenToAdd = relationCtrl.getChildrenToAdd();
        checkEntityExisting(pChildrenToAdd);
        originalChildren.addAll(pChildrenToAdd);
        this.editSingleEntity(parentEntity);
    }

    /**
     * Many-To-Many Relationship: Updates an entity into database and existing children belonged to this then remove children entities from the parent.
     *
     * @param <T> entity type
     * @param <R> type of related entity
     * @param relationCtrl controller for a particular pair of relationship
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityLockedException if entity gets optimistic lock
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityRemovedException if entity is already removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity, R extends ICommonEntity> void editRemoveChildrenMTM(RelationController<T, R> relationCtrl) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        Collection<R> originalChildren = relationCtrl.getChildrenCollection();
        T parentEntity = relationCtrl.getParentEntity();
        Collection<R> pChildrenToRemove = relationCtrl.getChildrenToRemove();
        originalChildren.removeAll(pChildrenToRemove);
        updateChildrenList(originalChildren);
        this.editSingleEntity(parentEntity);
    }

    /**
     * Many-To-Many Relationship: Updates an entity into database and add/remove children entities from the parent.
     *
     * @param <T> entity type
     * @param <R> type of related entity
     * @param relationCtrl controller for a particular pair of relationship
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityLockedException if entity gets optimistic lock
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityRemovedException if entity is already removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity, R extends ICommonEntity> void editAddRemoveChildrenMTM(RelationController<T, R> relationCtrl) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        Collection<R> originalChildren = relationCtrl.getChildrenCollection();
        Collection<R> pChildrenToRemove = relationCtrl.getChildrenToRemove();
        if (pChildrenToRemove != null) {
            originalChildren.removeAll(pChildrenToRemove);
        }
        updateChildrenList(originalChildren);
        T parentEntity = relationCtrl.getParentEntity();
        Collection<R> pChildrenToAdd = relationCtrl.getChildrenToAdd();
        if (pChildrenToAdd != null) {
            checkEntityExisting(pChildrenToAdd);
            originalChildren.addAll(pChildrenToAdd);
        }
        this.editSingleEntity(parentEntity);
    }

    /**
     * Removes an entity out of database and removes all children related to this entity.
     *
     * @param <T> entity type
     * @param pEntity entity to be removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityLockedException if entity gets optimistic lock
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityRemovedException if entity is already removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity> void remove(T pEntity) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUnrecoverableException {
        commonEAO.remove(pEntity);
    }

    /**
     * Remove a Collection of entities out of database and removes all related children.
     *
     * @param <T> entity type
     * @param pEntityList list of entities to be removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityLockedException if entity gets optimistic lock
     * @throws com.fisoft.mass.massprosrv.exception.ERPEntityRemovedException if entity is already removed
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity> void removeList(Collection<T> pEntityList) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUnrecoverableException {
        for (T entity : pEntityList) {
            commonEAO.remove(entity);
        }
    }

    public final <T extends ICommonEntity> void activate(T pEntity, EntityStatus activeFlag) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUnrecoverableException {
        try {
            pEntity.setActiveStatus(activeFlag.getValue());
            commonEAO.edit(pEntity);
        } catch (ERPEntityLockedException eel) {
            throw eel;
        } catch (ERPEntityRemovedException eer) {
            throw eer;
        } catch (ERPUnrecoverableException eu) {
            throw eu;
        } catch (Exception e) {
            throw new ERPUnrecoverableException(e);
        }
    }

    public final <T extends ICommonEntity> void activateList(Collection<T> pEntityList, EntityStatus activeFlag) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUnrecoverableException {
        for (T entity : pEntityList) {
            this.activate(entity, activeFlag);
        }
    }

    public final <T extends ICommonEntity> T find(Class<T> baseClass, Object id, boolean activeOnly) throws ERPUnrecoverableException {
        T entity = commonEAO.find(baseClass, id);
        if (activeOnly) {
            if (entity != null) {
                int mDeleted = entity.getActiveStatus();
                if (mDeleted == EntityStatus.INACTIVE.getValue()) {
                    return null;
                }
            }
        }
        return entity;
    }

    /**
     * Finds entities by criteria
     *
     * @param <T> entity type
     * @param querySelector query selector for this entity type
     * @param searchOption search option
     * @param pCriteria a HashMap of criteria (key,value)
     * @return list of entities found
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public final <T extends ICommonEntity> List<T> findByCriteria(QuerySelector<T> querySelector, HashMap pCriteria) throws ERPUnrecoverableException {
        querySelector.selectQuery(pCriteria);
        if (querySelector.isUseNamedQuery()) {
            return commonEAO.findByNamedQuery(querySelector.getQuery(), pCriteria);
        } else {
            return commonEAO.findBySQLStatement(querySelector.getQuery(), pCriteria);
        }
    }

    /**
     * Finds entities by a named query with criteria
     * @param <T> entity type
     * @param pQueryName Named Query that already defined for this entity
     * @param pCriteria a HashMap of criteria (key,value)
     * @return list of entities found
     * @throws com.fisoft.mass.massprosrv.exception.ERPUnrecoverableException if there's critical error
     */
    public <T extends ICommonEntity> List<T> findByNamedQuery(String pQueryName, HashMap pCriteria) throws ERPUnrecoverableException {
        return commonEAO.findByNamedQuery(pQueryName, pCriteria);
    }
}
