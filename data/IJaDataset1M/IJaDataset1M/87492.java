package org.josef.web.jsf.bean.crud;

import java.util.List;
import javax.ejb.EJBException;
import org.josef.ejb.CrudChildActions;
import org.josef.jpa.PersistAction;
import org.josef.jpa.Persistable;
import org.josef.util.CDebug;
import org.josef.util.Util;
import org.josef.web.jsf.bean.JsfBeanMode;
import org.josef.web.jsf.util.JsfMessages;

/**
 * Super class for JSF Beans that perform CRUD actions (including searching and
 * sorting) on a single JPA Entity, that is part of a many-to-one relation, by
 * using a Stateless Session Bean that implements the
 * {@link org.josef.ejb.CrudChildActions}
 * interface.
 * @author Kees Schotanus
 * @version 1.1 $Revision: 2841 $
 * @param <T> The type of the Entities to maintain.
 */
public abstract class AbstractCrudManyToOneEntityUsingSessionBean<T extends Persistable> extends AbstractCrudManyToOneEntityBean<T> {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = 2743473459106165323L;

    /**
     * Public constructor to make this a bean.
     */
    public AbstractCrudManyToOneEntityUsingSessionBean() {
    }

    /**
     * Gets the Controller to maintain the Entities.
     * @return The Controller to maintain the Entities.
     */
    public abstract CrudChildActions<T> getController();

    /**
     * Called when a search needs to be executed.
     * @return The found Entities.
     */
    @Override
    public List<T> searchItems() {
        return getController().findByCriteria(getSearchCriteria());
    }

    /**
     * Selects the Entity identified by the supplied objectId.
     * @param objectId The object ID of the Entity.
     *  <br>The supplied objectId must be non null and of type Long.
     * @return The Entity with the supplied objectId or null when no such
     *  Entity exists.
     * @throws NullPointerException When the supplied objectId is null.
     * @throws AssertionError When the supplied objectId is not of type
     *  Long.
     */
    @Override
    public T selectItem(final Object objectId) {
        CDebug.checkParameterNotNull(objectId, "objectId");
        assert objectId instanceof Long : "objectId must be of type Long";
        return getController().findById((Long) objectId);
    }

    /**
     * Persists the Entity.
     * <br>Postcondition: When the persist fails, a JSF Message is added.
     * Postcondition: When the persist is successful the mode is set to
     * {@link JsfBeanMode#UPDATE}.
     */
    @Override
    public void persist() {
        try {
            getController().persist(getDetailDelegate().getItem(), getParentId());
            getDetailDelegate().setMode(JsfBeanMode.UPDATE);
            getDetailDelegate().addSuccessMessage(PersistAction.PERSIST, JsfMessages.ENTITY_PERSIST_SUCCESSFULL, getItemType().getSimpleName());
        } catch (final EJBException exception) {
            getDetailDelegate().addErrorMessage(Util.getDeepestNestedCause(exception), PersistAction.PERSIST, JsfMessages.ENTITY_PERSIST_ERROR, getItemType().getSimpleName());
        }
    }

    /**
     * Merges the Entity.
     * <br>Postcondition: When the merge fails, a JSF Message is added.
     */
    @Override
    public void merge() {
        try {
            getDetailDelegate().setItem(getController().merge(getDetailDelegate().getItem()));
            getDetailDelegate().addSuccessMessage(PersistAction.MERGE, JsfMessages.ENTITY_MERGE_SUCCESSFULL, getItemType().getSimpleName());
        } catch (final EJBException exception) {
            getDetailDelegate().addErrorMessage(Util.getDeepestNestedCause(exception), PersistAction.MERGE, JsfMessages.ENTITY_MERGE_ERROR, getItemType().getSimpleName());
        }
    }

    /**
     * Removes the Entity.
     * <br>Postcondition: When the remove fails, a JSF Message is added.
     * @return True when the removal was successful, otherwise false is
     *  returned.
     */
    @Override
    public boolean remove() {
        try {
            getController().remove(getDetailDelegate().getItem());
            getDetailDelegate().addSuccessMessage(PersistAction.REMOVE, JsfMessages.ENTITY_REMOVE_SUCCESSFULL, getItemType().getSimpleName());
            return true;
        } catch (final EJBException exception) {
            getDetailDelegate().addErrorMessage(Util.getDeepestNestedCause(exception), PersistAction.REMOVE, JsfMessages.ENTITY_REMOVE_ERROR, getItemType().getSimpleName());
            return false;
        }
    }
}
