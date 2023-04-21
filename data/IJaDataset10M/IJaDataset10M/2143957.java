package org.paquitosoft.lml.model.action;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.List;
import org.paquitosoft.lml.model.util.ModelUtilities;
import org.paquitosoft.lml.model.annotation.PersistentAttribute;
import org.paquitosoft.lml.model.annotation.AssociatedEntityList;
import org.paquitosoft.lml.model.annotation.AssociationType;
import org.paquitosoft.lml.model.dao.DAOFactory;
import org.paquitosoft.lml.model.dao.IDefaultDAO;
import org.paquitosoft.lml.model.exception.DataNotFoundException;
import org.paquitosoft.lml.model.exception.InternalErrorException;
import org.paquitosoft.lml.model.exception.ReflectionException;
import org.paquitosoft.lml.util.LMLGlobalOperations;

/**
 *  This action is used to get an unique entity by its identifier.
 * 
 * @author paquitosoft
 */
public class ReadEntityAction<T> implements INonTransactionalAction {

    private Class<T> entityType;

    private T entity;

    private Object entityId;

    private Integer detailLevel;

    /**
     * This constructor is used by external classes when they need to 
     * look for an entity.
     * 
     * @param entityType
     * @param entityId
     * @param detailLevel
     */
    public ReadEntityAction(Class<T> entityType, Object entityId, Integer detailLevel) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.detailLevel = detailLevel;
    }

    /**
     * This constructor is used for child classes or packages classes that only needs to 
     * fill an entity (they deal with the task of look for the entity itself).
     * 
     * @param entity
     * @param detailLevel
     */
    protected ReadEntityAction(T entity, Integer detailLevel) {
        this.entity = entity;
        this.detailLevel = detailLevel;
    }

    public T execute(Connection connection) throws InternalErrorException {
        T result = entity;
        if (result == null) {
            IDefaultDAO dao = DAOFactory.getDefaultDAO(connection);
            result = dao.read(entityType, entityId);
        }
        fillEntity(result, detailLevel, connection);
        return result;
    }

    /**
     * This method is used to complete an entity with the information of its related 
     *  entities depending on the detailLevel.
     * 
     * @param entity
     * @throws org.paquitosoft.lml.model.exception.InternalErrorException
     */
    protected void fillEntity(Object entity, Integer detailLevel, Connection connection) throws InternalErrorException {
        if (detailLevel != null && 1 < detailLevel) {
            List<Field> associatedFields = ModelUtilities.getAllAssociatedEntityFields(entity.getClass());
            List<Field> associatedLists = ModelUtilities.getAllAnnotatedEntityFields(entity.getClass(), AssociatedEntityList.class);
            for (Field f : associatedFields) {
                f.setAccessible(true);
                try {
                    Object id = ModelUtilities.getEntityIdentifier(f.get(entity));
                    f.set(entity, new ReadEntityAction(f.getType(), id, detailLevel - 1).execute(connection));
                } catch (DataNotFoundException e) {
                    if (AssociationType.REQUIRED.equals(f.getAnnotation(PersistentAttribute.class).readAssociationType())) {
                        throw e;
                    }
                } catch (ReflectionException e) {
                    throw new InternalErrorException("ReadEntityAction::fillEntity", e);
                } catch (IllegalAccessException e) {
                    throw new InternalErrorException("ReadEntityAction::fillEntity", e);
                }
            }
            for (Field f : associatedLists) {
                f.setAccessible(true);
                try {
                    String query = ModelUtilities.generateFindRelatedEntitiesQuery(f);
                    Object id = ModelUtilities.getEntityIdentifier(entity);
                    if (id.equals(entity)) {
                        List<Field> pkFields = ModelUtilities.getEntityIdentifierFields(entity.getClass());
                        Object[] pkFieldValues = new Object[pkFields.size()];
                        for (int i = 0; i < pkFields.size(); i++) {
                            pkFields.get(i).setAccessible(true);
                            pkFieldValues[i] = pkFields.get(i).get(entity);
                        }
                        id = pkFieldValues;
                    }
                    INonTransactionalAction action = null;
                    AssociatedEntityList ann = f.getAnnotation(AssociatedEntityList.class);
                    if (ann != null && ann.joinTableName().length() > 0) {
                        action = new FindExternalRelatedEntitiesAction(entity, f, ann.joinTableName(), detailLevel - 1);
                    } else {
                        action = new FindEntitiesAction(query, LMLGlobalOperations.getCollectionType(f), detailLevel - 1, id);
                    }
                    f.set(entity, action.execute(connection));
                } catch (DataNotFoundException exception) {
                    if (AssociationType.REQUIRED.equals(f.getAnnotation(AssociatedEntityList.class).readAssociationType())) {
                        throw exception;
                    }
                } catch (ReflectionException e) {
                    throw new InternalErrorException("ReadEntityAction::fillEntity", e);
                } catch (IllegalAccessException e) {
                    throw new InternalErrorException("ReadEntityAction::fillEntity", e);
                }
            }
        }
    }
}
