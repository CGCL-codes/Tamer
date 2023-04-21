package org.skycastle.util.propertyaccess;

import org.apache.commons.collections15.collection.CompositeCollection;
import org.apache.commons.collections15.collection.UnmodifiableCollection;
import org.skycastle.util.ParameterChecker;
import org.skycastle.util.listenable.collection.list.ListenableArrayList;
import org.skycastle.util.listenable.collection.list.ListenableList;
import org.skycastle.util.propertyaccess.metadata.PropertyMetadata;
import org.skycastle.util.propertyaccess.metadata.PropertyMetadatas;
import org.skycastle.util.propertyaccess.metadata.PropertyValidationReport;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Common functionality for PropertyAccessors.
 * <p/>
 * Retrieves property values from the additional property accessor or the default values in the metadata
 * if some value is not available.
 *
 * @author Hans H�ggstr�m
 */
public abstract class AbstractPropertyAccessor<H> implements PropertyAccessor<H> {

    private final MinimalPropertyAccessor<H> myAdditionalPropertyAccessor;

    private final PropertyMetadatas myMetadatas;

    private final H myHostObject;

    private static final ListenableArrayList<Object> EMPTY_LISTENABLE_LIST = new ListenableArrayList<Object>(0);

    public final Object getPropertyValue(final String name, final Object defaultValue, final H hostObject) {
        ParameterChecker.checkNotNull(name, "name");
        final Object value;
        if (delegatedHasProperty(name)) {
            value = delegatedGetExistingPropertyValue(name, myHostObject);
        } else if (myAdditionalPropertyAccessor != null && myAdditionalPropertyAccessor.hasProperty(name)) {
            value = myAdditionalPropertyAccessor.getPropertyValue(name, defaultValue, hostObject);
        } else {
            value = getMetadataDefaultValue(name, defaultValue);
        }
        return value;
    }

    public final Collection<String> getAvailableProperties() {
        if (myAdditionalPropertyAccessor != null) {
            return UnmodifiableCollection.decorate(new CompositeCollection<String>(delegatedGetAvailableProperties(), myAdditionalPropertyAccessor.getAvailableProperties()));
        } else {
            return delegatedGetAvailableProperties();
        }
    }

    public final boolean hasProperty(final String name) {
        final boolean hasProperty;
        if (delegatedHasProperty(name)) {
            hasProperty = true;
        } else if (myAdditionalPropertyAccessor != null) {
            hasProperty = myAdditionalPropertyAccessor.hasProperty(name);
        } else {
            hasProperty = false;
        }
        return hasProperty;
    }

    public PropertyMetadatas getMetadatas() {
        return myMetadatas;
    }

    public final Object get(String name) {
        return getPropertyValue(name, null, myHostObject);
    }

    public final boolean getBoolean(String name, boolean defaultValue) {
        final Object value = get(name);
        if (value != null) {
            return (Boolean) value;
        } else {
            return defaultValue;
        }
    }

    public final int getInt(String name, int defaultValue) {
        final Object value = get(name);
        if (value != null) {
            return ((Number) value).intValue();
        } else {
            return defaultValue;
        }
    }

    public final long getLong(String name, long defaultValue) {
        final Object value = get(name);
        if (value != null) {
            return ((Number) value).longValue();
        } else {
            return defaultValue;
        }
    }

    public final double getFloat(String name, float defaultValue) {
        final Object value = get(name);
        if (value != null) {
            return ((Number) value).floatValue();
        } else {
            return defaultValue;
        }
    }

    public final double getDouble(String name, double defaultValue) {
        final Object value = get(name);
        if (value != null) {
            return ((Number) value).doubleValue();
        } else {
            return defaultValue;
        }
    }

    public final String getString(String name, String defaultValue) {
        final Object value = get(name);
        if (value != null) {
            return value.toString();
        } else {
            return defaultValue;
        }
    }

    public List<Object> getList(final String name) {
        checkIsList(name);
        final Object value = get(name);
        if (value != null && value instanceof ListenableList) {
            return ((ListenableList) value).getReadOnlyList();
        } else if (value != null && value instanceof List) {
            return Collections.unmodifiableList((List) value);
        } else {
            return EMPTY_LISTENABLE_LIST;
        }
    }

    public <L> List<L> getList(final String name, final Class<L> type) {
        ParameterChecker.checkNotNull(type, "type");
        checkIsListOType(name, type);
        return (List<L>) getList(name);
    }

    public H getHostObject() {
        return myHostObject;
    }

    /**
     * Creates a new abstract propery accessor.
     */
    protected AbstractPropertyAccessor() {
        this(null, null);
    }

    /**
     * Creates a new abstract propery accessor with the specified host object and metadata.
     *
     * @param hostObject the host object, or null if none available..  Used e.g. when calculating property values on the fly.
     * @param metadatas  the metadatas to use for the properties, or null if none available.
     */
    protected AbstractPropertyAccessor(final H hostObject, final PropertyMetadatas metadatas) {
        this(hostObject, metadatas, null);
    }

    /**
     * Creates a new abstract propery accessor with the specified host object and metadata and additional properties accessor.
     *
     * @param hostObject                 the host object, or null if none available..  Used e.g. when calculating property values on the fly.
     * @param metadatas                  the metadatas to use for the properties, or null if none available.
     * @param additionalPropertyAccessor an optional additional property provider, that is used if the property is not
     *                                   found in this property accessor.  Can be used to implement default values
     *                                   and such.
     */
    protected AbstractPropertyAccessor(final H hostObject, final PropertyMetadatas metadatas, final MinimalPropertyAccessor<H> additionalPropertyAccessor) {
        myHostObject = hostObject;
        myMetadatas = metadatas;
        myAdditionalPropertyAccessor = additionalPropertyAccessor;
    }

    /**
     * @return true if a property with the specified name exists in the properties, false if not.
     */
    protected abstract boolean delegatedHasProperty(final String propertyIdentifier);

    /**
     * @return a read only collection with the names of the properties currently
     *         available from these properties.
     */
    protected abstract Collection<String> delegatedGetAvailableProperties();

    /**
     * Called by the AbstractPropertyAccessor to get the property value, if hasProperty( name ) returned true.
     *
     * @param name       the name of the property to get.
     * @param hostObject the host object object to get the property value for.
     *                   Can be used to calculate a host specific value on the fly.
     *
     * @return the value of the specified property, or the specified defaultValue if it doesn't exist.
     */
    protected abstract Object delegatedGetExistingPropertyValue(final String name, final H hostObject);

    /**
     * Checks that the content of this set of properties meet the requirements of the metadat, and throw an
     * IllegalArgumentException if they don't.
     *
     * @param message the message to prepend to the exception, before the error reports.
     */
    protected final void checkContentValidity(final String message) {
        final PropertyMetadatas metadatas = getMetadatas();
        if (metadatas != null) {
            final List<PropertyValidationReport> reports = metadatas.validate(this);
            if (!reports.isEmpty()) {
                throw new IllegalArgumentException(message + "  " + Arrays.toString(reports.toArray()));
            }
        }
    }

    private void checkIsList(final String name) {
        if (myMetadatas != null) {
            final PropertyMetadata metadata = myMetadatas.getPropertyMetadata(name);
            if (metadata != null) {
                if (!metadata.isList()) {
                    throw new IllegalArgumentException("Can not get list property '" + name + "' as a list, " + "because it is not a list property according to the metadata.");
                }
                ;
            }
        }
    }

    private <L> void checkIsListOType(final String name, final Class<L> type) {
        checkIsList(name);
        if (myMetadatas != null) {
            final PropertyMetadata metadata = myMetadatas.getPropertyMetadata(name);
            if (metadata != null) {
                Class listType = metadata.getListType();
                if (!type.isAssignableFrom(listType)) {
                    throw new IllegalArgumentException("Can not get list property '" + name + "' as a list containing " + "'" + type + "' instances, because it contains " + "'" + listType + "' instances");
                }
                ;
            }
        }
    }

    private Object getMetadataDefaultValue(final String name, final Object defaultValue) {
        Object value = defaultValue;
        if (myMetadatas != null) {
            final PropertyMetadata propertyMetadata = myMetadatas.getPropertyMetadata(name);
            if (propertyMetadata != null) {
                value = propertyMetadata.getDefaultValue();
            }
        }
        return value;
    }
}
