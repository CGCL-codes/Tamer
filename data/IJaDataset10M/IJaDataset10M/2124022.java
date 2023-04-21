package org.progeeks.util;

import java.util.*;
import org.progeeks.util.log.Log;

/**
 *  Utility methods that can be used by configurator implementations
 *  to perform common operations.
 *
 *  @version   $Revision: 1.1 $
 *  @author    Paul Speed
 */
public class ConfiguratorUtils {

    static Log log = Log.getLog();

    /**
     *  Returns a new object created from the factory or a previously
     *  created object if the factory results can be reused
     *  (factory.isReusable()) and if the factory cache contains a
     *  previous result for the factory.  If a new object is created
     *  and the factory results are reusable then it is added to the cache.
     *  If the factoryCache is null then no cache resolution is done and
     *  a new object is created every time.
     */
    @SuppressWarnings("unchecked")
    public static Object resolveConfigurator(ObjectConfigurator factory, Map factoryCache) {
        if (log.isDebugEnabled()) log.debug("resolveFactory(" + factory + ", " + factoryCache + ")");
        if (factoryCache != null && factory.isReusable()) {
            Object result = factoryCache.get(factory);
            if (result != null) {
                if (log.isDebugEnabled()) log.debug("Reusing result:" + result);
                return (result);
            }
        }
        Object result = factory.createObject(factoryCache);
        if (factoryCache != null && factory.isReusable()) factoryCache.put(factory, result);
        return (result);
    }

    /**
     *  Returns a collection with any factory elements resolved into
     *  a new collection if necessary.  If the collection contains any
     *  factories, then the factories are resolved and a new collection
     *  is returned.  Otherwise, the supplied collection is returned directly.
     *  Individual factories are resolved against the factoryCache as per
     *  resolveConfigurator().
     *  If target type is null then the class of the values collection
     *  is used for any newly created collections.
     */
    @SuppressWarnings("unchecked")
    public static Collection resolveCollection(Collection values, Class targetType, Map factoryCache) {
        if (values == null) return (null);
        boolean noFactories = true;
        for (Iterator i = values.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof ObjectConfigurator) {
                noFactories = false;
                break;
            }
        }
        if (noFactories) return (values);
        if (targetType == null) targetType = values.getClass();
        Collection newValues = InspectionUtils.createCollection(values.getClass());
        for (Iterator i = values.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof ObjectConfigurator) {
                o = resolveConfigurator((ObjectConfigurator) o, factoryCache);
                if (log.isDebugEnabled()) log.debug("   created:" + o);
            }
            newValues.add(o);
        }
        return (newValues);
    }

    /**
     *  Appends the specified value to any pre-existing value in the target
     *  for the specified key.  This is a straight value append that does
     *  not resolve configurators into objects.  This is used to merge
     *  configurator values prior to creating an object from the configurator.
     *  Generally, overwrite/append rules are governed in the same way as
     *  configureObject() would do as per the source's flags.
     *  There is a logic to using the source flags as opposed to the target
     *  flags that may not be readily apparent.  Appending configurator
     *  values in this way is supposed to be the same as if target.configureObject()
     *  and source.configureObject() were called in that order.  In that case,
     *  the source flags would determine if existing values were overwritten, collections
     *  appended, etc..
     */
    @SuppressWarnings("unchecked")
    public static void mergeConfiguratorValue(ObjectConfigurator source, ObjectConfigurator target, String key, Object value) {
        if (log.isDebugEnabled()) log.debug("appendConfiguratorValue(" + key + ", " + value + ")");
        boolean overwrite = source.getOverwriteExistingValues();
        boolean append = source.getAppendCollections();
        boolean appendFactories = source.getAppendFactories();
        Object existing = target.get(key);
        if (log.isDebugEnabled()) log.debug("existing:" + existing);
        if (existing == null) {
            target.put(key, value);
            return;
        }
        if (existing instanceof Collection) {
            Collection c = (Collection) existing;
            if (c.size() == 0) {
                target.put(key, value);
                return;
            }
            if (!overwrite && !append) {
                return;
            }
            if (overwrite && !append) c.clear();
            if (value instanceof Collection) c.addAll((Collection) value); else c.add(value);
        } else if (existing instanceof ObjectConfigurator) {
            ObjectConfigurator oc = (ObjectConfigurator) existing;
            if (oc.size() == 0 || (overwrite && !appendFactories)) {
                target.put(key, value);
                return;
            }
            if (!overwrite && !appendFactories) {
                return;
            }
            if (value instanceof ObjectConfigurator) {
                oc.appendFactory((ObjectConfigurator) value);
            } else {
                throw new RuntimeException("Cannot update factory value for key[" + key + "] with non-factory value:" + value);
            }
        } else if (existing instanceof Map) {
            Map m = (Map) existing;
            if (m.size() == 0) {
                target.put(key, value);
                return;
            }
            if (!overwrite && !append) {
                return;
            }
            if (overwrite && !append) m.clear();
            if (value instanceof Map) m.putAll((Map) value); else throw new RuntimeException("Cannot update map for key[" + key + "] with non-map value:" + value);
        } else if (overwrite) {
            target.put(key, value);
        }
    }

    /**
     *  Appends the source factory's values to the target factory.  The
     *  net result should be a single factory that would produce the
     *  same results as calling target.configureObject() and
     *  source.configureObject() on the same object in that order.
     */
    public static void mergeFactories(ObjectConfigurator target, ObjectConfigurator source) {
        for (Iterator i = source.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            mergeConfiguratorValue(source, target, (String) e.getKey(), e.getValue());
        }
    }
}
