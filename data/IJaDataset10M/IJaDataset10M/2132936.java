package net.sf.jmoney.model2;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the base class for all objects that may have extension
 * property sets added by plug-ins.  The framework supports the
 * following objects that may be extended:
 * <UL>
 * <LI>Session</LI>
 * <LI>Commodity</LI>
 * <LI>Account</LI>
 * <LI>Transaction</LI>
 * <LI>Entry</LI>
 * </UL>
 * <P>
 * Plug-ins are also able to create new classes of extendable
 * objects by deriving classes from this class.
 * <P>
 * This class contains abstract methods for which an implementation
 * must be provided.
 * 
 * @author  Nigel Westbury
 */
public abstract class ExtendableObject {

    /**
	 * The key from which this object can be fetched from
	 * the datastore and a reference to this object obtained.
	 */
    IObjectKey objectKey;

    /**
	 * Extendable objects may have extensions containing additional data needed
	 * by the plug-ins. Plug-ins add properties to an object class by creating a
	 * property set and then adding that property set to the object class. This
	 * map will map property sets to the appropriate extension object.
	 */
    protected Map<ExtensionPropertySet<?>, ExtensionObject> extensions = new HashMap<ExtensionPropertySet<?>, ExtensionObject>();

    /**
	 * The key which contains this object's parent and also the list property
	 * which contains this object.
	 */
    protected ListKey parentKey;

    protected abstract String getExtendablePropertySetId();

    /**
	 * Constructs a new object with property values obtained from
	 * the given IValues interface.
	 * 
	 * Derived classes will set their own properties from this interface,
	 * but this method is responsible for ensuring the appropriate extensions
	 * are created and passes on the IValues interface to the extension constructors.
	 */
    protected ExtendableObject(IObjectKey objectKey, ListKey parentKey, IValues extensionValues) {
        this.objectKey = objectKey;
        this.parentKey = parentKey;
        for (ExtensionPropertySet<?> propertySet : extensionValues.getNonDefaultExtensions()) {
            ExtensionObject extensionObject = propertySet.constructImplementationObject(this, extensionValues);
            extensions.put(propertySet, extensionObject);
        }
    }

    /**
	 * Constructs a new object with default property values.
	 */
    protected ExtendableObject(IObjectKey objectKey, ListKey parentKey) {
        this.objectKey = objectKey;
        this.parentKey = parentKey;
    }

    /**
	 * @return The key that fetches this object.
	 */
    public IObjectKey getObjectKey() {
        return objectKey;
    }

    public IObjectKey getParentKey() {
        return parentKey == null ? null : parentKey.getParentKey();
    }

    public ListKey getParentListKey() {
        return parentKey;
    }

    /**
	 * @return The session containing this object
	 */
    public Session getSession() {
        return objectKey.getSession();
    }

    /**
	 * @return The data manager containing this object
	 */
    public DataManager getDataManager() {
        return objectKey.getDataManager();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ExtendableObject) {
            ExtendableObject extendableObject = (ExtendableObject) object;
            return getObjectKey().equals(extendableObject.getObjectKey());
        } else if (object instanceof ExtensionObject) {
            ExtensionObject extensionObject = (ExtensionObject) object;
            return getObjectKey().equals(extensionObject.getObjectKey());
        } else {
            return false;
        }
    }

    /**
	 * Required to support hash maps.
	 * 
	 * If the datastore plug-in keeps the entire datastore in
	 * memory then the default hashCode implementation in the
	 * object key will work fine.  However, if the datastore is
	 * backed by a database then multiple instances of the same
	 * object key may exist in memory.  In such a case, a hashCode
	 * implementation must be provided for the object keys that
	 * return the same hash code for each instance of the object key.
	 */
    @Override
    public int hashCode() {
        return getObjectKey().hashCode();
    }

    public <V> void processPropertyChange(final ScalarPropertyAccessor<V> propertyAccessor, final V oldValue, final V newValue) {
        if (newValue instanceof ExtendableObject && ((ExtendableObject) newValue).getDataManager() != getDataManager()) {
            throw new RuntimeException("The object being set as the value of a property and the parent object are being managed by different data managers.  Objects cannot contain references to objects from other data managers.");
        }
        if (oldValue == newValue || (oldValue != null && oldValue.equals(newValue))) return;
        ExtendablePropertySet<?> actualPropertySet = PropertySet.getPropertySet(this.getClass());
        int count = actualPropertySet.getScalarProperties3().size();
        Object[] oldValues = new Object[count];
        Object[] newValues = new Object[count];
        int i = 0;
        for (ScalarPropertyAccessor<?> propertyAccessor2 : actualPropertySet.getScalarProperties3()) {
            if (propertyAccessor2 == propertyAccessor) {
                oldValues[i] = oldValue;
                newValues[i] = newValue;
            } else {
                Object value = getPropertyValue(propertyAccessor2);
                oldValues[i] = value;
                newValues[i] = value;
            }
            i++;
        }
        objectKey.updateProperties(actualPropertySet, oldValues, newValues);
        getSession().getChangeManager().processPropertyUpdate(this, propertyAccessor, oldValue, newValue);
        getDataManager().fireEvent(new ISessionChangeFirer() {

            public void fire(SessionChangeListener listener) {
                listener.objectChanged(ExtendableObject.this, propertyAccessor, oldValue, newValue);
            }
        });
    }

    /**
	 * Get the extension that implements the properties needed by
	 * a given plug-in.
	 * 
	 * @param alwaysReturnNonNull
	 *            If true then the return value is guaranteed to be non-null. If false
	 *            then the return value may be null, indicating that all properties in
	 *            the extension have default values.
	 */
    public <X extends ExtensionObject> X getExtension(ExtensionPropertySet<X> propertySet, boolean alwaysReturnNonNull) {
        X extension = propertySet.classOfObject.cast(extensions.get(propertySet));
        if (extension == null && alwaysReturnNonNull) {
            extension = propertySet.constructDefaultImplementationObject(this);
            extensions.put(propertySet, extension);
        }
        return extension;
    }

    /**
     * Returns the value of a given property.
     * <P>
     * The property may be any property in the passed object,
     * including properties that are stored in extension objects.
     * The property may be defined in the actual class or
     * any super classes which the class extends.  The property
     * may also be a property in any extension class which extends
     * the class of this object or which extends any super class
     * of the class of this object.
     * <P>
     * If the property is in an extension and that extension does
     * not exist in this object then the default value of the
     * property is returned.
     */
    public <T> T getPropertyValue(ScalarPropertyAccessor<T> propertyAccessor) {
        PropertySet propertySet = propertyAccessor.getPropertySet();
        Object objectWithProperties;
        Class<?> implementationClass;
        if (!propertySet.isExtension()) {
            objectWithProperties = this;
            implementationClass = propertySet.getImplementationClass();
        } else {
            ExtensionObject extension = getExtension((ExtensionPropertySet<?>) propertySet, false);
            implementationClass = ((ExtensionPropertySet) propertySet).getExtendablePropertySet().getImplementationClass();
            if (extension == null) {
                return propertyAccessor.getDefaultValue();
            }
            objectWithProperties = extension;
        }
        if (!implementationClass.isAssignableFrom(getClass())) {
            throw new RuntimeException("Property " + propertyAccessor.getName() + " is implemented by " + implementationClass.getName() + " but is being called on an object of type " + getClass().getName());
        }
        return propertyAccessor.invokeGetMethod(objectWithProperties);
    }

    /**
	 * Obtain a the collection of values of a list property.
	 * 
	 * @param propertyAccessor The property accessor for the property
	 * 			whose values are to be obtained.  The property
	 * 			must be a list property (and not a scalar property).
	 */
    public <E2 extends ExtendableObject> ObjectCollection<E2> getListPropertyValue(ListPropertyAccessor<E2> owningListProperty) {
        return owningListProperty.getElements(this);
    }

    public <V> void setPropertyValue(ScalarPropertyAccessor<V> propertyAccessor, V value) {
        Object objectWithProperties;
        PropertySet propertySet = propertyAccessor.getPropertySet();
        if (!propertySet.isExtension()) {
            objectWithProperties = this;
        } else {
            objectWithProperties = getExtension((ExtensionPropertySet<?>) propertySet, true);
        }
        propertyAccessor.invokeSetMethod(objectWithProperties, value);
    }

    /**
	 * Return a list of extension that exist for this object.
	 * This is the list of extensions that have actually been
	 * created for this object, not the list of valid extensions
	 * for this object type.  If no property values have yet been set
	 * in an extension that the extension will not have been created
	 * and will thus not be returned by this method.
	 * <P>
	 * It is more efficient to use this method than to loop through
	 * all the possible extension property sets and see which ones exist
	 * in this object.
	 *
	 * @return an Iterator that returns elements of type
	 * 		<code>Map.Entry</code>.  Each Map.Entry contains a
	 * 		key of type PropertySet and a value of
	 * 		ExtensionObject.
	 */
    public Collection<ExtensionPropertySet<?>> getExtensions() {
        return extensions.keySet();
    }

    /**
	 * This method is used to enable other classes in the package to
	 * access protected fields in the extendable objects.
	 * 
	 * @param theObjectKeyField
	 * @return
	 */
    Object getProtectedFieldValue(Field theObjectKeyField) {
        try {
            return theObjectKeyField.get(this);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("internal error", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("internal error - field protection problem");
        }
    }

    /**
	 * This method allows datastore implementations to re-parent an
	 * object (move it from one list to another).
	 * <P>
	 * This method is to be used by datastore implementations only.
	 * Other plug-ins should not be calling this method.
	 * 
	 * @param listKey
	 */
    void replaceParentListKey(ListKey listKey) {
        this.parentKey = listKey;
    }
}
