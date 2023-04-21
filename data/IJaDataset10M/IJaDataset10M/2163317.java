package javax.util.jcache;

import java.util.Map;
import org.fjank.jcache.AttributesImpl;

/**
 * Interface with extra methods for special features of FKache.
 * 
 * @author Frank Karlstr�m
 */
public interface CacheMap extends Map {

    /**
	 * Will return an attribute object describing the current attributes
	 * associated for the region for this CacheAccess.
	 * 
	 * @return an AttributesImpl object for the region of this CacheAccess.
	 */
    AttributesImpl getAttributes();

    /**
	 * will return an attribute object describing the current attributes
	 * associated with the object name.
	 * 
	 * @param name the name of the object to get the attributes for.
	 * @return an AttributesImpl object for the named object.
	 * @throws CacheException if an error occurs.
	 */
    AttributesImpl getAttributes(Object name) throws CacheException;

    /**
	 * Gets the object from the cache. If the object is not currently in the
	 * cache and a loader object has been registered, the object will be loaded
	 * into the cache with the arguments specified.
	 * 
	 * @param name the name of the object to get.
	 * @param arguments the arguments wich is passed to the load method of the
	 *            CacheLoader, if registered.
	 * @return a reference to a shared object.
	 */
    Object get(Object name, Object arguments);

    /**
	 * is used to specify the attributes to associate with an object when it is
	 * loaded. This can include the loader object, cache event handlers, and
	 * spesific attributes for the object such as distribute, spool, etc.
	 * AttributesImpl (with the exception of the CacheLoader object itself) can
	 * also be specified within the load method of the CacheLoader object using
	 * the setAttributes method, if the attributes for an object are not
	 * defined, the attributes of the region will be used.
	 * 
	 * @param name the name of the object to associate the attributes with.
	 * @param attributes the attributes to associate.
	 */
    public void defineObject(Object name, AttributesImpl attributes);

    /**
	 * Puts a object into the cache, and attach it to a group. If the group does
	 * not existthe group is autocreated. If an object with the specified name,
	 * in the specified group already exists, this object is repleaced with the
	 * new object, and the old object is returned. Otherwise <code>null</code>
	 * is returned.
	 * 
	 * @param key the key of the object
	 * @param group the group to put it in to.
	 * @param value the actual object to put into the cache.
	 * @return if an object already was in the cache in the specified group,
	 *         this object is returned, otherwise <code>null</code> is returned.
	 */
    public Object put(Object key, String group, Object value);

    /**
	 * Removes an object from a group.
	 * 
	 * @param key The object to remove.
	 * @param group The group to remove the object from.
	 * @return The removed object if any.
	 */
    public Object remove(Object key, String group);
}
