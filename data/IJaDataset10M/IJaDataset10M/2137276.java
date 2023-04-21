package org.progeeks.meta;

import java.util.*;
import org.progeeks.util.ClassIterator;
import org.progeeks.util.ObjectUtils;

/**
 *  Class for relating objects to types.  This is slightly
 *  more involved than a simple map since some types can be
 *  broken down further to find better values.
 *
 *  @version   $Revision: 1.9 $
 *  @author    Paul Speed
 */
public class TypeRegistry<V> {

    /**
     *  A registry to check when values aren't found in
     *  this registry.
     */
    private TypeRegistry<V> delegate;

    /**
     *  Maps property types to values.
     */
    private Map<PropertyType, V> types = new LinkedHashMap<PropertyType, V>();

    /**
     *  Maps base types to values.  This map is checked
     *  when a type is not found in the types map.
     */
    private Map<Class, V> baseTypes = new LinkedHashMap<Class, V>();

    public TypeRegistry() {
    }

    public TypeRegistry(TypeRegistry<V> delegate) {
        this.delegate = delegate;
    }

    /**
     *  Sets all of the specified entries to this registry,
     *  clearing any previous registrations.
     */
    public void setEntries(List<TypeEntry<V>> entries) {
        for (TypeEntry<V> entry : entries) addEntry(entry);
    }

    /**
     *  Adds a single TypeEntry to the existing mappings.
     */
    public void addEntry(TypeEntry<V> entry) {
        entry.apply(this);
    }

    /**
     *  Maps the value to the specified type.
     */
    public void put(PropertyType type, V value) {
        types.put(type, value);
    }

    /**
     *  Maps the value with a default base type.  The defaults
     *  are checked when a specific property type is not found.
     */
    public void putDefault(Class baseType, V value) {
        baseTypes.put(baseType, value);
    }

    /**
     *  Returns the object associated with the specified type.
     *  Search order goes: regular, delegate's regular, compatible
     *  super-types, defaults, delegate's defaults.
     */
    public V get(PropertyType type, boolean searchDefaults) {
        if (type == null) return (null);
        V result = getDirectMatch(type);
        if (result != null) return (result);
        for (Iterator i = type.getSuperTypes(); i.hasNext(); ) {
            PropertyType superType = (PropertyType) i.next();
            result = getDirectMatch(superType);
            if (result != null) return (result);
        }
        if (!searchDefaults) return (result);
        return (getDefault(type));
    }

    public V get(PropertyType type) {
        return (get(type, true));
    }

    /**
     *  Returns the object associated with the specified type's
     *  super types.  This can be used to continue a search if
     *  the caller knows where the last search left off and
     *  decided it didn't like the previous result.
     */
    public V getNext(PropertyType type, PropertyType skipPastType, boolean searchDefaults) {
        if (type == null) return (null);
        boolean skipping = true;
        if (ObjectUtils.areEqual(type, skipPastType)) skipping = false;
        V result = null;
        for (Iterator i = type.getSuperTypes(); i.hasNext(); ) {
            PropertyType superType = (PropertyType) i.next();
            result = getDirectMatch(superType);
            if (result != null) {
                if (!skipping) return (result); else if (ObjectUtils.areEqual(superType, skipPastType)) skipping = false;
            }
        }
        if (!searchDefaults) return (result);
        return (getNextDefault(type, skipPastType, skipping));
    }

    public V getNext(PropertyType type, PropertyType skipPastType) {
        return (getNext(type, skipPastType, true));
    }

    /**
     *  Convenience method that will wrap the specified Class in
     *  a ClassPropertyType before searching.
     */
    public V get(Class<? extends Object> classType) {
        return (get(new ClassPropertyType(classType), true));
    }

    /**
     *  Convenience method that will wrap the specified MetaClass in
     *  a MetaClassPropertyType before searching.
     */
    public V get(MetaClass metaClassType) {
        return (get(new MetaClassPropertyType(metaClassType), true));
    }

    /**
     *  Convenience method that will lookup a value for the type
     *  of the specified object by either wrapping its meta-class in
     *  a MetaClassPropertyType or wrapping its class in a ClassPropertyType.
     */
    public V getValueForObject(Object obj) {
        if (obj instanceof MetaObject) {
            return (get(((MetaObject) obj).getMetaClass()));
        }
        if (obj == null) return (get(new ClassPropertyType(Object.class)));
        return (get(new ClassPropertyType(obj.getClass())));
    }

    public V getDefault(PropertyType type) {
        if (type == null) return (null);
        V result = baseTypes.get(type.getBaseClass());
        if (result != null) return (result);
        for (Iterator i = new ClassIterator(type.getBaseClass()); i.hasNext(); ) {
            result = baseTypes.get(i.next());
            if (result != null) return (result);
        }
        if (delegate != null) {
            result = delegate.getDefault(type);
        }
        return (result);
    }

    protected V getNextDefault(PropertyType type, PropertyType skipPastType, boolean skipping) {
        if (type == null) return (null);
        Class skipPast = skipPastType.getBaseClass();
        V result = null;
        for (Iterator i = new ClassIterator(type.getBaseClass()); i.hasNext(); ) {
            Class c = (Class) i.next();
            result = baseTypes.get(c);
            if (result != null) {
                if (skipping && ObjectUtils.areEqual(c, skipPast)) skipping = false; else if (!skipping) return (result);
            }
        }
        if (delegate != null) {
            result = delegate.getDefault(type);
        }
        return (result);
    }

    /**
     *  Returns the object associated with the specified type without
     *  doing any special type searches.  This registry is checked
     *  first and then the delegate registry.
     */
    public V getDirectMatch(PropertyType type) {
        if (type == null) return (null);
        V result = types.get(type);
        if (result != null) return (result);
        if (delegate != null) {
            result = delegate.getDirectMatch(type);
            if (result != null) return (result);
        }
        return (result);
    }
}
