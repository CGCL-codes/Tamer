package org.middleheaven.core.wiring;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 
 */
public class PropertyManagers {

    private LinkedList<PropertyManager> managers = new LinkedList<PropertyManager>();

    /**
	 * Adds a {@link PropertyManager} after another identified by the name.
	 * 
	 * The added  {@link PropertyManager} will be consulted after the named one.
	 * @param managerName
	 * @param manager
	 * @return
	 */
    public boolean addAfter(String managerName, PropertyManager manager) {
        for (ListIterator<PropertyManager> it = managers.listIterator(managers.size() - 1); it.hasPrevious(); ) {
            if (it.previous().getName().equals(managerName)) {
                it.add(manager);
                return true;
            }
        }
        return false;
    }

    /**
	 * Adds a {@link PropertyManager} before another identified by the name.
	 * 
	 * The added  {@link PropertyManager} will be consulted before the named one.
	 * @param managerName
	 * @param manager
	 * @return
	 */
    public boolean addBefore(String managerName, PropertyManager manager) {
        for (ListIterator<PropertyManager> it = managers.listIterator(); it.hasNext(); ) {
            if (it.next().getName().equals(managerName)) {
                it.add(manager);
                return true;
            }
        }
        return false;
    }

    public void addFirst(PropertyManager manager) {
        managers.addFirst(manager);
    }

    public void addLast(PropertyManager manager) {
        managers.addLast(manager);
    }

    public void removePropertyManager(PropertyManager manager) {
        managers.remove(manager);
    }

    public Object getProperty(String key) {
        for (PropertyManager manager : managers) {
            if (manager.containsProperty(key)) {
                return manager.getProperty(key);
            }
        }
        return null;
    }
}
