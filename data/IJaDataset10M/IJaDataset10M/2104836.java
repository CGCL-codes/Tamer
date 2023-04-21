package org.datanucleus.sco.simple;

import java.io.ObjectStreamException;
import java.util.Collection;
import javax.jdo.spi.PersistenceCapable;
import org.datanucleus.ObjectManagerFactoryImpl;
import org.datanucleus.StateManager;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.sco.SCOMap;
import org.datanucleus.sco.SCOUtils;
import org.datanucleus.state.FetchPlanState;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.Localiser;
import org.datanucleus.util.StringUtils;

/**
 * A mutable second-class HashMap object.
 * This is the simplified form that intercepts mutators and marks the field as dirty.
 */
public class HashMap extends java.util.HashMap implements SCOMap, Cloneable {

    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.Localisation", ObjectManagerFactoryImpl.class.getClassLoader());

    protected transient Object owner;

    protected transient StateManager ownerSM;

    protected transient String fieldName;

    protected transient int fieldNumber;

    /** The internal "delegate". */
    protected java.util.HashMap delegate;

    /**
     * Constructor
     * @param ownerSM the owner StateManager
     * @param fieldName the field name
     */
    public HashMap(StateManager ownerSM, String fieldName) {
        this.ownerSM = ownerSM;
        this.owner = ownerSM.getObject();
        this.fieldName = fieldName;
        if (ownerSM != null) {
            fieldNumber = ownerSM.getClassMetaData().getMetaDataForMember(fieldName).getAbsoluteFieldNumber();
        }
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
            delegate = new java.util.HashMap(m);
        } else {
            delegate = new java.util.HashMap();
        }
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023003", StringUtils.toJVMIDString(ownerSM.getObject()), fieldName, "" + size(), SCOUtils.getSCOWrapperOptionsMessage(true, false, true, false)));
        }
    }

    /**
     * Method to initialise the SCO for use.
     */
    public void initialise() {
        delegate = new java.util.HashMap();
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023003", StringUtils.toJVMIDString(ownerSM.getObject()), fieldName, "" + size(), SCOUtils.getSCOWrapperOptionsMessage(true, false, true, false)));
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
    }

    /**
     * Method to update an embedded value in this map.
     * @param value The value
     * @param fieldNumber Number of field in the value
     * @param newValue New value for this field
     */
    public void updateEmbeddedValue(Object value, int fieldNumber, Object newValue) {
    }

    /**
     * Accessor for the field name that this HashMap relates to.
     * @return The field name
     **/
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Accessor for the owner that this HashMap relates to.
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
        if (owner != null) {
            ((PersistenceCapable) owner).jdoMakeDirty(fieldName);
        }
    }

    /**
     * Method to return a detached copy of the container.
     * Recurse sthrough the keys/values so that they are likewise detached.
     * @param state State for detachment process
     * @return The detached container
     */
    public Object detachCopy(FetchPlanState state) {
        java.util.Map detached = new java.util.HashMap();
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
        java.util.Map attachedKeysValues = new java.util.HashMap(m.size());
        SCOUtils.attachCopyForMap(ownerSM, m.entrySet(), attachedKeysValues, keysWithoutIdentity, valuesWithoutIdentity);
        SCOUtils.updateMapWithMapKeysValues(ownerSM.getObjectManager().getApiAdapter(), this, attachedKeysValues);
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
        return size() == 0;
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
     * Method to clear the HashMap.
     */
    public void clear() {
        delegate.clear();
        makeDirty();
    }

    /**
     * Method to add a value against a key to the HashMap.
     * @param key The key
     * @param value The value
     * @return The previous value for the specified key.
     */
    public Object put(Object key, Object value) {
        Object oldValue = delegate.put(key, value);
        makeDirty();
        return oldValue;
    }

    /**
     * Method to add the specified Map's values under their keys here.
     * @param m The map
     */
    public void putAll(java.util.Map m) {
        delegate.putAll(m);
        makeDirty();
    }

    /**
     * Method to remove the value for a key from the HashMap.
     * @param key The key to remove
     * @return The value that was removed from this key.
     */
    public Object remove(Object key) {
        Object value = delegate.remove(key);
        makeDirty();
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
        return new java.util.HashMap(delegate);
    }
}
