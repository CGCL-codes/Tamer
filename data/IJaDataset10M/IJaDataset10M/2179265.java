package org.datanucleus.store.types.sco.simple;

import java.io.ObjectStreamException;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import javax.jdo.spi.PersistenceCapable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.state.FetchPlanState;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.types.sco.SCOMap;
import org.datanucleus.store.types.sco.SCOUtils;
import org.datanucleus.util.Localiser;
import org.datanucleus.util.NucleusLogger;

/**
 * A mutable second-class SortedMap object.
 * This is the simplified form that intercepts mutators and marks the field as dirty.
 * It also handles cascade-delete triggering for persistable elements.
 */
public class SortedMap extends AbstractMap implements java.util.SortedMap, SCOMap, Cloneable, java.io.Serializable {

    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.Localisation", org.datanucleus.ClassConstants.NUCLEUS_CONTEXT_LOADER);

    protected transient ObjectProvider ownerSM;

    protected transient String fieldName;

    protected transient int fieldNumber;

    /** The internal "delegate". */
    protected java.util.TreeMap delegate;

    /**
     * Constructor
     * @param ownerSM the owner StateManager
     * @param fieldName the field name
     */
    public SortedMap(ObjectProvider ownerSM, String fieldName) {
        this.ownerSM = ownerSM;
        this.fieldName = fieldName;
        this.fieldNumber = ownerSM.getClassMetaData().getMetaDataForMember(fieldName).getAbsoluteFieldNumber();
    }

    /**
     * Method to initialise the SCO from an existing value.
     * @param o Object to set value using.
     * @param forInsert Whether the object needs inserting in the datastore with this value
     * @param forUpdate Whether to update the datastore with this value
     */
    public void initialise(Object o, boolean forInsert, boolean forUpdate) {
        java.util.Map m = (java.util.Map) o;
        if (m != null) {
            initialiseDelegate();
            delegate.putAll(m);
        } else {
            initialiseDelegate();
        }
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023003", ownerSM.toPrintableID(), fieldName, "" + size(), SCOUtils.getSCOWrapperOptionsMessage(true, false, false, false)));
        }
    }

    /**
     * Method to initialise the SCO for use.
     */
    public void initialise() {
        initialiseDelegate();
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023003", ownerSM.toPrintableID(), fieldName, "" + size(), SCOUtils.getSCOWrapperOptionsMessage(true, false, false, false)));
        }
    }

    /**
     * Convenience method to set up the delegate respecting any comparator specified in MetaData.
     */
    protected void initialiseDelegate() {
        AbstractMemberMetaData fmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
        Comparator comparator = SCOUtils.getComparator(fmd, ownerSM.getExecutionContext().getClassLoaderResolver());
        if (comparator != null) {
            this.delegate = new java.util.TreeMap(comparator);
        } else {
            this.delegate = new java.util.TreeMap();
        }
    }

    /**
     * Accessor for the unwrapped value that we are wrapping.
     * @return The unwrapped value
     */
    public Object getValue() {
        return delegate;
    }

    /**
     * Method to effect the load of the data in the SCO.
     * Used when the SCO supports lazy-loading to tell it to load all now.
     */
    public void load() {
    }

    /**
     * Method to return if the SCO has its contents loaded. Returns true.
     * @return Whether it is loaded
     */
    public boolean isLoaded() {
        return true;
    }

    /**
     * Method to flush the changes to the datastore when operating in queued mode.
     * Does nothing in "direct" mode.
     */
    public void flush() {
    }

    /**
     * Method to update an embedded key in this map.
     * @param key The key
     * @param fieldNumber Number of field in the key
     * @param newValue New value for this field
     */
    public void updateEmbeddedKey(Object key, int fieldNumber, Object newValue) {
        makeDirty();
    }

    /**
     * Method to update an embedded value in this map.
     * @param value The value
     * @param fieldNumber Number of field in the value
     * @param newValue New value for this field
     */
    public void updateEmbeddedValue(Object value, int fieldNumber, Object newValue) {
        makeDirty();
    }

    /**
     * Accessor for the field name that this SortedMap relates to.
     * @return The field name
     **/
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Accessor for the owner that this SortedMap relates to.
     * @return The owner
     **/
    public Object getOwner() {
        return (ownerSM != null ? ownerSM.getObject() : null);
    }

    /**
     * Method to unset the owner and field details.
     **/
    public synchronized void unsetOwner() {
        if (ownerSM != null) {
            ownerSM = null;
        }
    }

    /**
     * Utility to mark the object as dirty
     **/
    public void makeDirty() {
        if (ownerSM != null) {
            ((PersistenceCapable) ownerSM.getObject()).jdoMakeDirty(fieldName);
        }
    }

    /**
     * Method to return a detached copy of the container.
     * Recurse sthrough the keys/values so that they are likewise detached.
     * @param state State for detachment state
     * @return The detached container
     */
    public Object detachCopy(FetchPlanState state) {
        java.util.Map detached = new java.util.TreeMap();
        SCOUtils.detachCopyForMap(ownerSM, entrySet(), state, detached);
        return detached;
    }

    /**
     * Method to return an attached copy of the passed (detached) value. The returned attached copy
     * is a SCO wrapper. Goes through the existing keys/values in the store for this owner field and
     * removes ones no longer present, and adds new keys/values. All keys/values in the (detached)
     * value are attached.
     * @param value The new (map) value
     */
    public void attachCopy(Object value) {
        java.util.Map m = (java.util.Map) value;
        AbstractMemberMetaData fmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
        boolean keysWithoutIdentity = SCOUtils.mapHasKeysWithoutIdentity(fmd);
        boolean valuesWithoutIdentity = SCOUtils.mapHasValuesWithoutIdentity(fmd);
        java.util.Map attachedKeysValues = new java.util.TreeMap();
        SCOUtils.attachCopyForMap(ownerSM, m.entrySet(), attachedKeysValues, keysWithoutIdentity, valuesWithoutIdentity);
        SCOUtils.updateMapWithMapKeysValues(ownerSM.getExecutionContext().getApiAdapter(), this, attachedKeysValues);
    }

    /**
     * Creates and returns a copy of this object.
     *
     * <P>Mutable second-class Objects are required to provide a public
     * clone method in order to allow for copying PersistenceCapable
     * objects. In contrast to Object.clone(), this method must not throw a
     * CloneNotSupportedException.
     * @return The cloned object
     */
    public Object clone() {
        return delegate.clone();
    }

    /**
     * Accessor for the comparator.
     * @return The comparator
     */
    public Comparator comparator() {
        return delegate.comparator();
    }

    /**
     * Method to return if the map contains this key
     * @param key The key
     * @return Whether it is contained
     **/
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    /**
     * Method to return if the map contains this value.
     * @param value The value
     * @return Whether it is contained
     **/
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    /**
     * Accessor for the set of entries in the Map.
     * @return Set of entries
     **/
    public java.util.Set entrySet() {
        return delegate.entrySet();
    }

    /**
     * Method to check the equality of this map, and another.
     * @param o The map to compare against.
     * @return Whether they are equal.
     **/
    public synchronized boolean equals(Object o) {
        return delegate.equals(o);
    }

    /**
     * Accessor for the first key in the sorted map.
     * @return The first key
     **/
    public Object firstKey() {
        return delegate.firstKey();
    }

    /**
     * Accessor for the last key in the sorted map.
     * @return The last key
     **/
    public Object lastKey() {
        return delegate.lastKey();
    }

    /**
     * Method to retrieve the head of the map up to the specified key.
     * @param toKey the key to return up to.
     * @return The map meeting the input
     */
    public java.util.SortedMap headMap(Object toKey) {
        return delegate.headMap(toKey);
    }

    /**
     * Method to retrieve the subset of the map between the specified keys.
     * @param fromKey The start key
     * @param toKey The end key
     * @return The map meeting the input
     */
    public java.util.SortedMap subMap(Object fromKey, Object toKey) {
        return delegate.subMap(fromKey, toKey);
    }

    /**
     * Method to retrieve the part of the map after the specified key.
     * @param fromKey The start key
     * @return The map meeting the input
     */
    public java.util.SortedMap tailMap(Object fromKey) {
        return delegate.headMap(fromKey);
    }

    /**
     * Accessor for the value stored against a key.
     * @param key The key
     * @return The value.
     **/
    public Object get(Object key) {
        return delegate.get(key);
    }

    /**
     * Method to generate a hashcode for this Map.
     * @return The hashcode.
     **/
    public synchronized int hashCode() {
        return delegate.hashCode();
    }

    /**
     * Method to return if the Map is empty.
     * @return Whether it is empty.
     **/
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    /**
     * Accessor for the set of keys in the Map.
     * @return Set of keys.
     **/
    public java.util.Set keySet() {
        return delegate.keySet();
    }

    /**
     * Method to return the size of the Map.
     * @return The size
     **/
    public int size() {
        return delegate.size();
    }

    /**
     * Accessor for the set of values in the Map.
     * @return Set of values.
     **/
    public Collection values() {
        return delegate.values();
    }

    /**
     * Method to clear the SortedMap.
     */
    public void clear() {
        if (ownerSM != null && !delegate.isEmpty()) {
            AbstractMemberMetaData mmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
            if (SCOUtils.hasDependentKey(mmd) || SCOUtils.hasDependentValue(mmd)) {
                Iterator<Map.Entry> entryIter = delegate.entrySet().iterator();
                while (entryIter.hasNext()) {
                    Map.Entry entry = entryIter.next();
                    if (SCOUtils.hasDependentKey(mmd)) {
                        ownerSM.getExecutionContext().deleteObjectInternal(entry.getKey());
                    }
                    if (SCOUtils.hasDependentValue(mmd)) {
                        ownerSM.getExecutionContext().deleteObjectInternal(entry.getValue());
                    }
                }
            }
        }
        delegate.clear();
        makeDirty();
        if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
            ownerSM.getExecutionContext().processNontransactionalUpdate();
        }
    }

    /**
     * Method to add a value against a key to the SortedMap.
     * @param key The key
     * @param value The value
     * @return The previous value for the specified key.
     */
    public Object put(Object key, Object value) {
        Object oldValue = delegate.put(key, value);
        makeDirty();
        if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
            ownerSM.getExecutionContext().processNontransactionalUpdate();
        }
        return oldValue;
    }

    /**
     * Method to add the specified Map's values under their keys here.
     * @param m The map
     **/
    public void putAll(java.util.Map m) {
        delegate.putAll(m);
        makeDirty();
        if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
            ownerSM.getExecutionContext().processNontransactionalUpdate();
        }
    }

    /**
     * Method to remove the value for a key from the SortedMap.
     * @param key The key to remove
     * @return The value that was removed from this key.
     **/
    public Object remove(Object key) {
        Object value = delegate.remove(key);
        if (ownerSM != null) {
            AbstractMemberMetaData mmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
            if (SCOUtils.hasDependentKey(mmd) || SCOUtils.hasDependentValue(mmd)) {
                if (SCOUtils.hasDependentKey(mmd)) {
                    ownerSM.getExecutionContext().deleteObjectInternal(key);
                }
                if (SCOUtils.hasDependentValue(mmd)) {
                    ownerSM.getExecutionContext().deleteObjectInternal(value);
                }
            }
        }
        makeDirty();
        if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
            ownerSM.getExecutionContext().processNontransactionalUpdate();
        }
        return value;
    }

    /**
     * The writeReplace method is called when ObjectOutputStream is preparing
     * to write the object to the stream. The ObjectOutputStream checks whether
     * the class defines the writeReplace method. If the method is defined, the
     * writeReplace method is called to allow the object to designate its
     * replacement in the stream. The object returned should be either of the
     * same type as the object passed in or an object that when read and
     * resolved will result in an object of a type that is compatible with all
     * references to the object.
     * 
     * @return the replaced object
     * @throws ObjectStreamException
     */
    protected Object writeReplace() throws ObjectStreamException {
        return new java.util.TreeMap(delegate);
    }
}
