package ch.trackedbean.copier;

/**
 * Context used in {@link IBeanMapper}.
 * 
 * @author M. Hautle
 */
public interface IMappingContext {

    /**
     * Returns true if the property represented by the given holder should be copied.<br>
     * This check is for {@link IBeanMapper#copySrc2Dst(Object, Object, IMappingContext)}
     * 
     * @param holder The mapping to check
     * @return True if the value should be copied
     */
    boolean copySrc2Dst(IMappingHolder holder);

    /**
     * Returns true if the property represented by the given holder should be copied.<br>
     * This check is for {@link IBeanMapper#copyDst2Src(Object, Object, IMappingContext)}
     * 
     * @param holder The mapping to check
     * @return True if the value should be copied
     */
    boolean copyDst2Src(IMappingHolder holder);

    /**
     * Looks up a previously converted/mapped instance (previously stored using {@link #putElement(Object, Object)}).
     * 
     * @param <T> The desired type
     * @param key The source instance
     * @param type The desired type
     * @return The converted instance or null
     */
    <T> T getElement(Object key, Class<T> type);

    /**
     * Stores a converted/mapped instance for the given source element.<br>
     * Use {@link #getElement(Object, Class)} to retrieve the stored instances.<br>
     * This method must be called before any mappings were executed on the converted instance, otherwise we risk to create an instance several times (nested
     * elements referencing the same instance).
     * 
     * @param <T> The type of the value
     * @param key The source instance
     * @param value The converted instance
     * @return The converted instance
     */
    <T> T putElement(Object key, T value);

    /**
     * Returns the value for the given key (previously stored using {@link #putValue(Object, Object)}).
     * 
     * @param <T> The type
     * @param key The key
     * @return The value for the given key or null
     */
    <T> T getValue(Object key);

    /**
     * Stores the given value using the passed key.<br>
     * Stored value can be retrieved using {@link #getValue(Object)}.
     * 
     * @param key The key
     * @param value The value
     * @return The previously value of the specified key
     */
    Object putValue(Object key, Object value);

    /**
     * Call this method if you step down into an object.<br>
     * Call {@link #stepOutOfObject()} if you leave the object.<br>
     * Custom implementations may use this feature to maintain the current working path.
     * 
     * @param value The object your stepping in
     */
    void stepIntoObject(Object value);

    /**
     * Call this method if your leaving an object ({@link #stepIntoObject(Object)} must be called first).<br>
     * Custom implementations may use this feature to maintain the current working path.
     */
    void stepOutOfObject();
}
