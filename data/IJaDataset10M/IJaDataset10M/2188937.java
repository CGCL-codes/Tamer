package org.avaje.ebean.bean;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.PersistenceException;
import org.avaje.ebean.Ebean;
import org.avaje.ebean.io.SerializeControl;
import org.avaje.ebean.util.InternalEbean;

/**
 * This is the object added to every entity bean using byte code enhancement.
 * <p>
 * This provides the mechanisms to support deferred fetching of reference beans
 * and oldValues generation for concurrency checking.
 * </p>
 */
public class EntityBeanIntercept implements Cloneable, Serializable {

    private static final long serialVersionUID = -3664031775464862645L;

    transient NodeUsageCollector nodeUsageCollector;

    /**
	 * The actual entity bean that 'owns' this intercept.
	 */
    protected EntityBean owner;

    /**
	 * The parent bean by relationship (1-1 or 1-M).
	 */
    protected Object parentBean;

    /**
	 * true if the bean properties have been loaded. false if it is a reference
	 * bean (will lazy load etc).
	 */
    protected boolean loaded;

    /**
	 * Set true when loaded or reference. 
	 * Used to bypass interception when created by user code.
	 */
    protected boolean intercepting;

    /**
	 * If true calling setters throws an exception.
	 */
    protected boolean readOnly;

    /**
	 * The bean as it was before it was modified. Null if no non-transient
	 * setters have been called.
	 */
    protected Object oldValues;

    /**
	 * Used when a bean is partially filled.
	 */
    protected Set<String> loadedProps;

    /**
	 * The EbeanServer this bean came from.
	 */
    protected String serverName;

    private String lazyLoadProperty;

    /**
	 * Create a intercept with a given entity.
	 * <p>
	 * Refer to agent ProxyConstructor.
	 * </p>
	 */
    public EntityBeanIntercept(Object owner) {
        this.owner = (EntityBean) owner;
    }

    /**
	 * Return the 'owning' entity bean.
	 */
    public EntityBean getOwner() {
        return owner;
    }

    public String toString() {
        if (!loaded) {
            return "Reference...";
        }
        return "OldValues: " + oldValues;
    }

    /**
	 * Turn on profile collection.
	 */
    public void setNodeUsageCollector(NodeUsageCollector usageCollector) {
        this.nodeUsageCollector = usageCollector;
    }

    /**
	 * Return the name of the associated EbeanServer.
	 */
    public String getServerName() {
        return serverName;
    }

    /**
	 * Set the name of the associated EbeanServer.
	 */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
	 * Return the parent bean (by relationship).
	 */
    public Object getParentBean() {
        return parentBean;
    }

    /**
	 * Special case for a OneToOne, Set the parent bean (by relationship). This
	 * is the owner of a 1-1.
	 */
    public void setParentBean(Object parentBean) {
        this.parentBean = parentBean;
    }

    /**
	 * Return true if oldValues exist. That is the bean has been modified and
	 * holds uncommitted changes.
	 */
    public boolean isDirty() {
        return (oldValues != null);
    }

    /**
	 * Return the old values used for ConcurrencyMode.ALL.
	 */
    public Object getOldValues() {
        return oldValues;
    }

    /**
	 * Return true if the bean should be treated as readOnly. If a setter method
	 * is called when it is readOnly an Exception is thrown.
	 */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
	 * Set the readOnly status. If readOnly then calls to setter methods through
	 * an exception.
	 */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
	 * Return true if the entity is a reference.
	 */
    public boolean isReference() {
        return intercepting && !loaded;
    }

    /**
	 * Set this as a reference object.
	 */
    public void setReference() {
        this.loaded = false;
        this.intercepting = true;
    }

    /**
	 * Return true if the entity has been loaded.
	 */
    public boolean isLoaded() {
        return loaded;
    }

    /**
	 * Set the loaded state to true.
	 * <p>
	 * Calls to setter methods after the bean is loaded can result in 'Old
	 * Values' being created to support ConcurrencyMode.ALL
	 * </p>
	 * <p>
	 * Worth noting that this is also set after a insert/update. By doing so
	 * it 'resets' the bean for making further changes and saving again.
	 * </p>
	 */
    public void setLoaded() {
        this.loaded = true;
        this.oldValues = null;
        this.intercepting = true;
    }

    /**
	 * Set the property names for a partially loaded bean.
	 * 
	 * @param loadedPropertyNames
	 *            the names of the loaded properties
	 */
    public void setLoadedProps(Set<String> loadedPropertyNames) {
        this.loadedProps = loadedPropertyNames;
    }

    /**
	 * Return the set of property names for a partially loaded bean.
	 */
    public Set<String> getLoadedProps() {
        return loadedProps;
    }

    /**
	 * Return the property read or write that triggered the lazy load.
	 */
    public String getLazyLoadProperty() {
        return lazyLoadProperty;
    }

    /**
	 * Load the bean when it is a reference.
	 */
    protected void loadBean(String loadProperty) {
        lazyLoadProperty = loadProperty;
        if (nodeUsageCollector != null) {
            nodeUsageCollector.setLoadProperty(lazyLoadProperty);
        }
        InternalEbean eb = (InternalEbean) Ebean.getServer(serverName);
        eb.lazyLoadBean(owner, nodeUsageCollector);
        loadedProps = null;
        loaded = true;
    }

    /**
	 * Create a copy of the bean as it is now. This is the original or 'old
	 * values' prior to any modification. This is used to perform concurrency
	 * testing.
	 */
    protected void createOldValues() {
        oldValues = owner._ebean_createCopy();
        if (nodeUsageCollector != null) {
            nodeUsageCollector.setModified();
        }
    }

    /**
	 * This is ONLY used for subclass entity beans.
	 * <p>
	 * This is not used when entity bean classes are enhanced 
	 * via javaagent or ant etc - only when a subclass is generated.
	 * </p>
	 * Returns a Serializable instance that is either the 'byte code generated'
	 * object or a 'Vanilla' copy of this bean depending on
	 * SerializeControl.isVanillaBeans().
	 */
    public Object writeReplaceIntercept() throws ObjectStreamException {
        if (!SerializeControl.isVanillaBeans()) {
            return owner;
        }
        return owner._ebean_createCopy();
    }

    /**
	 * Helper method to check if two objects are equal.
	 */
    @SuppressWarnings("unchecked")
    protected boolean areEqual(Object obj1, Object obj2) {
        if (obj1 == null) {
            return (obj2 == null);
        }
        if (obj2 == null) {
            return false;
        }
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 instanceof BigDecimal) {
            if (obj2 instanceof BigDecimal) {
                Comparable com1 = (Comparable) obj1;
                return (com1.compareTo(obj2) == 0);
            } else {
                return false;
            }
        } else {
            return obj1.equals(obj2);
        }
    }

    /**
	 * Method that is called prior to a getter method on the actual entity.
	 * <p>
	 * This checks if the bean is a reference and should be loaded.
	 * </p>
	 */
    public void preGetter(String propertyName) {
        if (!intercepting) {
            return;
        }
        if (nodeUsageCollector != null && loaded) {
            nodeUsageCollector.addUsed(propertyName);
        }
        if (!loaded) {
            loadBean(propertyName);
        } else if (loadedProps != null && !loadedProps.contains(propertyName)) {
            loadBean(propertyName);
        }
    }

    /**
	 * Return true if a modification check should be performed. That is, return
	 * true if we need to compare the new and old values to see if they have
	 * changed.
	 */
    public boolean preSetterIsModifyCheck(String propertyName) {
        if (!intercepting) {
            return false;
        }
        if (readOnly) {
            throw new PersistenceException("This bean is readOnly");
        }
        return (loaded && oldValues == null);
    }

    /**
	 * Check to see if the values are not equal. If they are not equal then
	 * create the old values for use with ConcurrencyMode.ALL.
	 */
    public void preSetter(String propertyName, Object newValue, Object oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (!areEqual(newValue, oldValue)) {
                createOldValues();
            }
        }
    }

    /**
	 * Check for primitive boolean.
	 */
    public void preSetter(String propertyName, boolean newValue, boolean oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (newValue != oldValue) {
                createOldValues();
            }
        }
    }

    /**
	 * Check for primitive int.
	 */
    public void preSetter(String propertyName, int newValue, int oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (newValue != oldValue) {
                createOldValues();
            }
        }
    }

    /**
	 * Check for primitive long.
	 */
    public void preSetter(String propertyName, long newValue, long oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (newValue != oldValue) {
                createOldValues();
            }
        }
    }

    /**
	 * Check for primitive double.
	 */
    public void preSetter(String propertyName, double newValue, double oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (newValue != oldValue) {
                createOldValues();
            }
        }
    }

    /**
	 * Check for primitive float.
	 */
    public void preSetter(String propertyName, float newValue, float oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (newValue != oldValue) {
                createOldValues();
            }
        }
    }

    /**
	 * Check for primitive short.
	 */
    public void preSetter(String propertyName, short newValue, short oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (newValue != oldValue) {
                createOldValues();
            }
        }
    }

    /**
	 * Check for primitive char.
	 */
    public void preSetter(String propertyName, char newValue, char oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (newValue != oldValue) {
                createOldValues();
            }
        }
    }

    /**
	 * Check for primitive byte.
	 */
    public void preSetter(String propertyName, byte newValue, byte oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (newValue != oldValue) {
                createOldValues();
            }
        }
    }

    /**
	 * Check for primitive char[].
	 */
    public void preSetter(String propertyName, char[] newValue, char[] oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (newValue != oldValue) {
                createOldValues();
            }
        }
    }

    /**
	 * Check for primitive byte[].
	 */
    public void preSetter(String propertyName, byte[] newValue, byte[] oldValue) {
        if (preSetterIsModifyCheck(propertyName)) {
            if (newValue != oldValue) {
                createOldValues();
            }
        }
    }
}
