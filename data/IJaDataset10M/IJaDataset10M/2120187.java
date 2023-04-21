package org.xith3d.ui.hud.listmodels;

import java.util.ArrayList;
import org.xith3d.ui.hud.base.ListModel;

/**
 * The default implementation of {@link ListModel}.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public abstract class DefaultAbstractListModel extends ListModel {

    @SuppressWarnings("rawtypes")
    private java.util.List items;

    private java.util.ArrayList<Object> userObjects = null;

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void addItemImpl(int index, Object item) {
        items.add(index, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setItemImpl(int index, Object item) {
        items.set(index, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object removeItemImpl(int index) {
        Object item = items.get(index);
        items.remove(index);
        if (getItemsCount() == 0) setSelectedIndex(-1);
        return (item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        items.clear();
        if (userObjects != null) userObjects.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemsCount() {
        return (items.size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItemImpl(int index) {
        return (items.get(index));
    }

    /**
     * Sets an item's user-object.
     * 
     * @param itemIndex
     * @param userObject
     */
    public void setItemUserObject(int itemIndex, Object userObject) {
        if (userObjects == null) {
            if (userObject == null) return;
            this.userObjects = new ArrayList<Object>();
        }
        if (userObjects.size() < getItemsCount()) {
            for (int i = userObjects.size(); i < getItemsCount(); i++) {
                userObjects.add(null);
            }
        }
        userObjects.set(itemIndex, userObject);
    }

    /**
     * Gets an item's user-object.
     * 
     * @param itemIndex
     * @return the items user-object.
     */
    public final Object getItemUserObject(int itemIndex) {
        if ((userObjects == null) || (itemIndex >= userObjects.size())) return (null);
        return (userObjects.get(itemIndex));
    }

    public DefaultAbstractListModel(java.util.List<?> items) {
        this.items = items;
        if (this.items == null) this.items = new java.util.ArrayList<Object>();
    }

    public DefaultAbstractListModel() {
        this.items = new java.util.ArrayList<Object>();
    }
}
