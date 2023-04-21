package org.ofbiz.widget.cache;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.cache.UtilCache;

public class ScreenCache extends AbstractCache {

    public static final String module = ScreenCache.class.getName();

    public ScreenCache() {
        super("screen");
    }

    public GenericWidgetOutput get(String screenName, WidgetContextCacheKey wcck) {
        UtilCache screenCache = getCache(screenName);
        if (screenCache == null) return null;
        return (GenericWidgetOutput) screenCache.get(wcck);
    }

    public GenericWidgetOutput put(String screenName, WidgetContextCacheKey wcck, GenericWidgetOutput output) {
        UtilCache<WidgetContextCacheKey, GenericWidgetOutput> screenCache = getOrCreateCache(screenName);
        return screenCache.put(wcck, output);
    }

    public GenericWidgetOutput remove(String screenName, WidgetContextCacheKey wcck) {
        UtilCache screenCache = getCache(screenName);
        if (Debug.verboseOn()) Debug.logVerbose("Removing from ScreenCache with key [" + wcck + "], will remove from this cache: " + (screenCache == null ? "[No cache found to remove from]" : screenCache.getName()), module);
        if (screenCache == null) return null;
        GenericWidgetOutput retVal = (GenericWidgetOutput) screenCache.remove(wcck);
        if (Debug.verboseOn()) Debug.logVerbose("Removing from ScreenCache with key [" + wcck + "], found this in the cache: " + retVal, module);
        return retVal;
    }
}
