package com.ubiteck.jsf.bean;

import java.util.List;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.Logger;
import com.ubiteck.entity.IEntity;
import com.ubiteck.jsf.bean.util.JSFHelper;

/**
 * 
 * Entity collection manager 
 * 
 * @author sursini
 */
public abstract class CollectionMgr<Model extends IEntity> extends BackingBean implements ITableProvider<Model> {

    private static final Logger logger = Logger.getLogger(CollectionMgr.class);

    private EntityMgr<Model> entityMgr;

    private ISingleSelector<Model> caller;

    private Object[] selectedItems;

    private final TableBean<Model> table;

    /**
	 * Parent entity :
	 * Optional parent entity
	 */
    private IEntity ownerEntity = null;

    public CollectionMgr() {
        super();
        logger.debug("Create Collection Mgr : " + this.getClass().getSimpleName());
        table = new TableBean<Model>(this) {

            @Override
            public void onInit() {
                logger.debug("On init...");
                setValues(listModels());
            }
        };
    }

    public EntityMgr<Model> getEntityMgr() {
        return entityMgr;
    }

    public void setEntityMgr(EntityMgr<Model> entityMgr) {
        this.entityMgr = entityMgr;
    }

    public final String edit() {
        logger.debug("Editing " + getOutcome() + "...");
        try {
            if (getEntityMgr() == null) throw new IllegalStateException("Entity mgr property not defined for class :" + getClass().getSimpleName());
            getEntityMgr().doClear();
            getEntityMgr().setCollectionMgr(this);
            getEntityMgr().setFormMode(FormMode.EDIT);
            if (getSelected() == null) throw new IllegalStateException("No model selected !");
            logger.debug("\tselected model :" + getSelected());
            entityMgr.setCurrent(doFetch(getSelected()));
            onEdit(getSelected());
            return "edit" + getOutcome();
        } catch (Throwable e) {
            logger.error("Error in edit");
            handleException(e);
        }
        return null;
    }

    /**
	 * View an event
	 * @return 
	 */
    public String view() {
        try {
            logger.debug("Viewing " + getOutcome() + "...");
            getEntityMgr().doClear();
            getEntityMgr().setFormMode(FormMode.VIEW);
            getEntityMgr().setCurrent(doFetch(getSelected()));
            getEntityMgr().setCollectionMgr(this);
            onView(getSelected());
            return "edit" + getOutcome();
        } catch (Throwable throwable) {
            handleException(throwable);
        }
        return null;
    }

    /**
	 * View an event
	 * @return 
	 */
    public String add() {
        logger.debug("Adding " + getOutcome() + "...");
        try {
            getEntityMgr().doClear();
            Model newInstance = createNewInstance();
            if (newInstance == null) throw new IllegalStateException("Created instance is null [" + this.getClass().getSimpleName() + "]");
            getEntityMgr().setFormMode(FormMode.ADD);
            getEntityMgr().setCurrent(newInstance);
            getEntityMgr().setCollectionMgr(this);
            return "edit" + getOutcome();
        } catch (Throwable throwable) {
            handleException(throwable);
        }
        return null;
    }

    /**
	 * Delete 
	 * @return 
	 */
    public String delete() {
        logger.debug("Deleting " + getOutcome() + "...");
        try {
            if (doDelete(getSelected())) {
                getEntityMgr().setFormMode(FormMode.BROWSE);
                return browse();
            } else {
                displayError("serialNumberId", "error.unable.delete", new Object[] { getOutcome() });
                return null;
            }
        } catch (Throwable throwable) {
            handleException(throwable);
        }
        return null;
    }

    public String browse() {
        logger.debug("Browsing " + getOutcome() + "...");
        try {
            getEntityMgr().setFormMode(FormMode.BROWSE);
            getTable().setValues(listModels());
            return getBrowseOutcome();
        } catch (Throwable throwable) {
            handleException(throwable);
        }
        return null;
    }

    public void refresh() {
        logger.debug("Refreshing collection...[" + getClass().getSimpleName() + "]");
        try {
            getTable().refresh();
        } catch (Throwable throwable) {
            handleException(throwable);
        }
    }

    @Override
    public void doRefresh() {
        refresh();
    }

    protected String getBrowseOutcome() {
        return "browse" + getOutcome();
    }

    /**
	 * <p>Fill or search an entity</p>
	 * <p>
	 *     Display a form to enter a new entity or search for an existing one
	 *  </p>
	 * <p> 
	 * 		Don't forget to implement the jsf page eg :fillCustomer.xhtml or fillMobileItem.xhtml
	 * </p>
	 * @return fill outcome
	 */
    public String fill() {
        logger.debug("Filling " + getOutcome() + "...");
        try {
            getEntityMgr().setFormMode(FormMode.ADD);
            Model newInstance = createNewInstance();
            getEntityMgr().setCurrent(newInstance);
            getEntityMgr().setCollectionMgr(this);
            return "fill" + getOutcome();
        } catch (Throwable throwable) {
            handleException(throwable);
        }
        return null;
    }

    protected void onValueSelected(Model event) {
    }

    protected void onView(Model event) {
    }

    protected void onEdit(Model event) {
    }

    /**
	 * Select the current managed object
	 * @param event
	 */
    public void select(ActionEvent event) {
        logger.debug("Selecting...");
        try {
            String selectionKey = (String) JSFHelper.getRequestParams().get("selectionKey");
            if (selectionKey == null) throw new IllegalStateException("Selection key cannot be null : ");
            getTable().setSelected((resolve(selectionKey)));
            if (getSelected() == null) throw new IllegalStateException("Selected object cannot be null");
            onValueSelected(getSelected());
        } catch (Throwable e) {
            handleException(e);
        }
    }

    /**
	 * Resolve and entity according to the selection key
	 * @param selectionKey
	 * @return an entity
	 */
    protected Model resolve(String selectionKey) {
        if (selectionKey == null) throw new NullArgumentException("selection key");
        logger.debug("Resolving selection key : " + selectionKey);
        Long id = Long.valueOf(selectionKey);
        for (Model value : getTable().getValues()) {
            if (value.getId().equals(id)) return value;
        }
        logger.debug("\tCalling doLoad with id : " + id);
        return doLoad(id);
    }

    protected void refreshTable() {
        logger.debug("Refresh table...");
        getTable().setValues(listModels());
    }

    /**
	 * @return the table
	 */
    public TableBean<Model> getTable() {
        logger.debug("getTable...");
        logger.debug("\tcount : " + (table == null ? "null" : table.getCount()));
        return table;
    }

    public void onException(Throwable throwable) {
        logger.debug("onException...");
        handleException(throwable);
    }

    protected Model getSelected() {
        logger.debug("getSelected...");
        return getTable().getSelected();
    }

    protected abstract Model createNewInstance();

    /**
	 * Use to define the jsf outcomes
	 * @return
	 */
    protected abstract Class<?> getModelClass();

    /**
	 * Can be overriden if the target file is not corresponding to the entity
	 *  
	 * @return outcome name  
	 */
    protected String getOutcome() {
        return getModelClass().getSimpleName();
    }

    protected abstract boolean doDelete(Model model);

    protected abstract Model doCreate(Model model);

    protected abstract Model doUpdate(Model model);

    /**
	 * Load the entity but not as {@link #doFetch(IEntity)} 
	 * @param id
	 * @return the loaded entity
	 */
    protected Model doLoad(Long id) {
        throw new NotImplementedException("doLoad Not implemented [" + getClass().getSimpleName() + "]! ");
    }

    /**
	 * Load the lazy loaded entity properties
	 * @param model
	 * @return the entity fully loaded
	 * 
	 */
    protected Model doFetch(Model model) {
        return model;
    }

    protected abstract List<Model> listModels();

    public Object[] getSelectedItems() {
        return selectedItems;
    }

    @SuppressWarnings("unchecked")
    public void setSelectedItems(Object[] selectedItems) {
        logger.debug("set selected : " + selectedItems);
        if (selectedItems != null) {
            getTable().setSelected((Model) selectedItems[0]);
            for (Object object : selectedItems) {
                logger.debug("\tObject : " + object);
            }
        }
        this.selectedItems = selectedItems;
    }

    public ISingleSelector<Model> getCaller() {
        logger.debug("getCaller...");
        return caller;
    }

    public void setCaller(ISingleSelector<Model> caller) {
        logger.debug("setCaller... : " + caller);
        this.caller = caller;
    }

    public boolean hasCaller() {
        return caller != null;
    }

    /**
	 * Optional parent item
	 * @return
	 */
    public IEntity getOwnerEntity() {
        return ownerEntity;
    }

    /**
	 * Define the owner entity in {@link com.ubiteck.jsf.bean.EntityMgr#doModelChange(IEntity)} 
	 * @param ownerEntity
	 */
    public void setOwnerEntity(final IEntity ownerEntity) {
        this.ownerEntity = ownerEntity;
    }

    /**
	 * Should be override if a parent entity has been defined
	 */
    protected void doRefreshParentEntity() {
        if (ownerEntity != null) logger.debug("Do refresh parent entity...");
    }
}
