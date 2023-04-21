package edu.umn.cs5115.scheduler.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * ManagedData is intended to be subclassed.  In your concrete subclasses, you
 * can implement data accessors, as you normally would, or you can choose not 
 * to.  If you implement data accessors, rather than storing the values of the 
 * data inside the class as you can use ManagedData's interface instead.  You
 * can use getPrimitiveValueForKey and setPrimitiveValueForKey to read and write
 * data that is stored inside the class.  These methods automatically provide
 * undo and notification support.  In the future, support could be provided
 * for automatic serialization of ManagedData classes, as well.<p>
 * Since getValueForKey and setValueForKey are public, everybody can also use
 * these methods to read and write data in your class.  If you do some kind of
 * data validation in your custom accessors, that is fine because setValueForKey
 * and getValueForKey will call your accessors for keys, if they are available.
 * @author grant
 */
public abstract class ManagedData extends KeyValueCodingBase {

    /** The document this managed data is contained within.  Needed for undo registration. */
    private Document document;

    /** The data storage for this ManagedData. */
    private HashMap data;

    /** Creates a new instance of ManagedData */
    public ManagedData(Document document) {
        this.document = document;
        HashMap initialMap = SubclassData.getSubclassDataForClass(getClass()).getInitialDataMap();
        if (initialMap == null) data = new HashMap(); else data = (HashMap) initialMap.clone();
    }

    /**
     * Call this method to set the default values for newly-created copies of
     * your class.
     * @param subclass Your custom subclass.  If the name of your subclass is
     * MyManagedData, you should pass MyManagedData.class.
     * @param initialValues The new default values. This should be a map of keys
     * to whatever their default values should be.  When new classes are 
     * initialized, they will use a copy of this map as their initial data.
     * The copy that performed is a shallow copy, so do NOT include mutable 
     * values in this list; instead set these manually in your constructor.
     */
    protected static void setDefaultValues(Class subclass, Map initialValues) {
        SubclassData.getSubclassDataForClass(subclass).setInitialDataMap(initialValues);
    }

    /**
     * Called by KeyValueCodingBase when it can't find a custom accessor.  
     * Retrieves the value from the store. You generally shouldn't need to call 
     * this method or override it.
     * @param key The key that KeyValueCodingBase couldn't find.
     * @return The value of that key from the ManagedData store.
     */
    protected Object getValueForUndefinedKey(String key) {
        return getPrimitiveValueForKey(key);
    }

    /**
     * Called by KeyValueCodingBase when it can't find a custom accessor.  
     * Sets the value in the store. You generally shouldn't need to call 
     * this method or override it.
     * @param key The key that KeyValueCodingBase couldn't find.
     * @param value The value to set that key to.
     */
    protected void setValueForUndefinedKey(String key, Object value) {
        setPrimitiveValueForKey(key, value);
    }

    /**
     * Set the value for a key without using custom setters.  Call this method 
     * if you have defined your own custom setters to do data validation, but
     * are using the ManagedData class to handle storage, undo support, and 
     * notifications.  This method changes the value stored for the key, 
     * registers an undo, and sends out notifications that the key has changed.
     * <b>Important:</b> Because setValueForKey will call your custom setter if 
     * it is available, you MUST call this method rather than setValueForKey in 
     * order to store data from a setter.
     * @param key The key that has changed.
     * @param value The object value to store.
     */
    protected void setPrimitiveValueForKey(final String key, Object value) {
        final Object oldValue = data.put(key, value);
        if (oldValue != null) {
            if (oldValue instanceof KeyValueCodingSet && value instanceof Collection) {
                data.put(key, oldValue);
                ((KeyValueCodingSet) oldValue).replace((Collection) value);
            }
        }
        if (oldValue == null || !oldValue.equals(value)) {
            document.getUndoManager().registerUndo(new Undoable() {

                public void undo() {
                    setValueForKey(key, oldValue);
                }
            });
            didChangeValueForKey(key);
        }
    }

    /**
     * Get the value for a key without using custom getters.  Call this method
     * if you have defined your own custom getter for convenience, but you are
     * using the ManagedData class to handle storage, undo support, and 
     * notifications.  This method retrieves the value for the key and returns 
     * it.
     * <b>Important:</b> Because getValueForKey will call your custom getter if
     * it is available, you MUST call this method rather than getValueForKey in
     * order to get data from a getter.
     * @return The value retrieved from the store.  If no value is associated
     * with this key, this method returns null.
     */
    protected Object getPrimitiveValueForKey(String key) {
        return data.get(key);
    }

    /**
     * Get the document that this managed object is in.
     * @return The document containing this ManagedObject.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Produces a string containing all the data stored in this managed data.
     */
    public String toString() {
        return super.toString() + "{data=" + data + "}";
    }

    /**
     * This is similar to the class in KeyValueCodingBase, but it is instead 
     * used to store the initial values for newly-initialized instances of 
     * ManagedData subclasses.
     * I should really pull out a common super class for these two, but I don't
     * have time to worry about that right now.  If anybody else wants to tackle
     * that task, go for it.
     */
    private static class SubclassData {

        /** Stores a list of known subclasses that have been retrieved using
         * getSubclassDataForClass().
         */
        private static HashMap knownSubclasses = new HashMap();

        /** Subclass that this data is for. */
        private Class subclass;

        /** Map containing the initial values. */
        private HashMap initialMap = null;

        /**
         * Protected constructor, used by getSubclassDataForClass().
         * @param subclass The subclass to create a new instance of SubclassData
         * for.
         * @see getSubclassDataForClass(Class)
         */
        protected SubclassData(Class subclass) {
            this.subclass = subclass;
        }

        /**
         * Gets the subclass data for the given subclass.  If this is the first
         * time the data has been requested, a new data object is created for
         * it.
         * @param subclass The class to get the data for.
         * @return A SubclassData object that can be used to store the subclass
         * data for the given subclass.
         */
        public static SubclassData getSubclassDataForClass(Class subclass) {
            SubclassData data = (SubclassData) knownSubclasses.get(subclass);
            if (data == null) {
                data = new SubclassData(subclass);
                knownSubclasses.put(subclass, data);
            }
            return data;
        }

        /**
         * Sets the initial data map used for instances of this subclass.
         * @param map The new map to use.  It will be copied.
         * @see #getInitialDataMap()
         */
        public void setInitialDataMap(Map map) {
            initialMap = new HashMap(map);
        }

        /**
         * Fetches and returns the initial data for this class.  Be sure to
         * copy it before using it.
         * @return The initialMap for this class.  If you change this map, 
         * changes will occur in the map returned by future calls to this
         * function.  If that's not what you want, be sure to clone it first.
         */
        public HashMap getInitialDataMap() {
            return initialMap;
        }
    }
}
