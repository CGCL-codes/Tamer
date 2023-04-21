package com.prolix.editor.resourcemanager.model;

import java.util.Hashtable;

/**
 * Class for the management and registration of instantiated resource items; 
 * needed for attaching itemtypes to ld data components and for representing them 
 * @author Stefan Zander
 *
 */
public class ResourceItemRegistry {

    private Hashtable itemregistry;

    public ResourceItemRegistry() {
        this.itemregistry = new Hashtable();
    }

    public void addResourceItem(Object key, ResourceTreeItem item) {
        itemregistry.put(key, item);
    }

    /**
	 * automatically adds an item to the registry and uses its identifier as key
	 * @param item
	 */
    public void addResourceItem(ResourceTreeItem item) {
        itemregistry.put(item.getId(), item);
    }

    public ResourceTreeItem getResourceItem(Object key) {
        return (ResourceTreeItem) itemregistry.get(key);
    }

    public boolean removeResourceItem(Object key) {
        return itemregistry.remove(key) != null;
    }

    public void dispose() {
        itemregistry = null;
    }
}
