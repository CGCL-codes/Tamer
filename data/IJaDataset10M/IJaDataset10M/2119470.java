package org.homemotion.ui.widgets;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.homemotion.building.Building;
import org.homemotion.dao.Item;
import org.homemotion.dao.ItemManager;
import org.homemotion.ui.state.RequestState;

public abstract class AbstractFormPage<T extends Item> implements Serializable {

    private String itemName;

    private T item;

    private boolean readOnly;

    private Class<T> typeClass;

    protected abstract ItemManager<T> getItemManager();

    public AbstractFormPage(String itemName, Class<T> typeClass) {
        if (itemName == null) {
            throw new IllegalArgumentException("itemName may not be null.");
        }
        this.itemName = itemName;
        this.typeClass = typeClass;
        init();
    }

    protected abstract T createNewItem();

    public void init() {
    }

    public String update() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (item != null) {
            try {
                getItemManager().update(this.item);
                this.item = null;
                return getUpdatedTarget();
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not update object: " + e.getMessage(), e.getMessage());
                context.addMessage(null, message);
            }
        } else {
            FacesMessage message = new FacesMessage("Could not update: no object found.");
            context.addMessage(null, message);
        }
        return null;
    }

    public String refresh() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (item != null) {
            try {
                getItemManager().refresh(this.item);
                return getUpdatedTarget();
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not refresh object: " + e.getMessage(), e.getMessage());
                context.addMessage(null, message);
            }
        } else {
            FacesMessage message = new FacesMessage("Could not refresh: no object found.");
            context.addMessage(null, message);
        }
        return null;
    }

    protected String getUpdatedTarget() {
        return getListTarget();
    }

    protected String getDeletedTarget() {
        return getListTarget();
    }

    public String persist() {
        if (this.item != null) {
            getItemManager().create(this.item);
            this.item = null;
            return getPersistedTarget();
        }
        return null;
    }

    public String cancel() {
        if (this.item != null) {
            this.item = null;
        }
        return getListTarget();
    }

    protected String getPersistedTarget() {
        return getListTarget();
    }

    public String delete() {
        FacesContext context = FacesContext.getCurrentInstance();
        T item = getItem();
        if (item != null) {
            try {
                getItemManager().delete(item);
                return getDeletedTarget();
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not delete object: " + e.getMessage(), e.getMessage());
                context.addMessage(null, message);
            }
            this.item = null;
        } else {
            FacesMessage message = new FacesMessage("Could not delete: No Object found.");
            context.addMessage(null, message);
        }
        return null;
    }

    protected String getListTarget() {
        return this.itemName + "List";
    }

    protected T get(Long key) {
        return getItemManager().get(key);
    }

    protected String getKeyParamName() {
        return "id";
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public T getItem() {
        Object found = RequestState.getData(this.itemName);
        if (found != null && this.typeClass.isAssignableFrom(found.getClass())) {
            this.item = (T) found;
            RequestState.setData(this.itemName, null);
            System.err.println("Item read (thread): " + this.item);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        if (this.item == null) {
            HttpServletRequest hrequest = (HttpServletRequest) context.getExternalContext().getRequest();
            found = hrequest.getSession(true).getAttribute(this.itemName);
            if (found != null && this.typeClass.isAssignableFrom(found.getClass())) {
                this.item = (T) found;
                System.err.println("Item read (session): " + this.item);
            }
        }
        if (this.item == null) {
            String idParam = context.getExternalContext().getRequestParameterMap().get("id");
            if (idParam != null) {
                Long id = Long.parseLong(idParam);
                this.item = get(id);
                System.err.println("Item read (parameter): " + this.item);
            }
        }
        if (this.item == null) {
            this.item = createNewItem();
        }
        if (this.item == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Item not found.", "No instance of " + getItemName() + " found, aborting...");
            context.addMessage(null, message);
        }
        return this.item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
