package org.pixory.pxfoundation;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class for localizing Strings. It is conceptually similar to
 * java.util.ResourceBundle. However, unlike ResourceBundle, this class will
 * load StringBundles from XML properties files, not classic java properties
 * files. Classic java properties files are hardcoded to the ISO-8859-1
 * character encoding, but this class supports any encoding, for example UTF-8
 * or UTF-16.
 * 
 * All This does not pretend to be a high performance implementation, like
 * util.ResourceBundle In particular, it does not worry about concurrent
 * getBundle() calls for values that have not yet been cached-- concurrent
 * threads will trigger concurrent construction and caching of Bundle values,
 * and the last one in will overwrite previous writes. But after the first one
 * completes, subsequent calls will go to the cache
 * 
 * TODO should introduce a softcache here
 */
public class PXStringBundle extends Object {

    private static final Log LOG = LogFactory.getLog(PXStringBundle.class);

    public static final String BUNDLE_PROPERTIES_SUFFIX = ".properties.xml";

    private static final String NOT_FOUND_STRING = "__NOT_FOUND__";

    private static final PXStringBundle NOT_FOUND_BUNDLE = new PXStringBundle(null, null);

    /**
	 * because of the fallback hierarchy, the bundle ultimately matched to a
	 * request might not exactly match the request locale. so the least subtle
	 * method for handling this situation is to maintain two caches-- one for
	 * perceived locales (_answerCache) and one for actual locales (_loadCache).
	 */
    private static Map _answerCache = new HashMap();

    private static Map _loadCache = new HashMap();

    private Properties _strings;

    private PXStringBundle _parent;

    private Locale _locale;

    private PXStringBundle(Properties strings_, Locale locale_) {
        _strings = strings_;
        _locale = locale_;
    }

    /**
	 * @param target_
	 *           the object that the bundle is for. The FQ className of target_
	 *           is used as the bundle baseName, and the ClassLoader of target_
	 *           is used to locate the bundle
	 */
    public static PXStringBundle getBundle(Object target_, Locale locale_) {
        String message = "target_: " + target_ + " locale_: " + locale_;
        LOG.debug(message);
        Validate.notNull(target_);
        Validate.notNull(locale_);
        Class targetClass = (target_ instanceof Class) ? (Class) target_ : target_.getClass();
        ClassLoader targetClassLoader = targetClass.getClassLoader();
        String baseName = targetClass.getName().replace('.', '/');
        return getBundle(baseName, locale_, targetClassLoader);
    }

    /**
	 * uses loader_ to locate the resource
	 */
    public static PXStringBundle getBundle(String baseName_, Locale locale_, ClassLoader loader_) {
        PXStringBundle getBundle = null;
        String message = "baseName_: " + baseName_ + " locale_: " + locale_ + " loader_: " + loader_;
        LOG.debug(message);
        LOG.debug("user.language: " + System.getProperty("user.language"));
        LOG.debug("user.region: " + System.getProperty("user.region"));
        LOG.debug("default locale: " + Locale.getDefault());
        Validate.notNull(baseName_);
        Validate.notNull(locale_);
        Validate.notNull(loader_);
        String bundleCacheKey = getBundleCacheKey(baseName_, locale_, loader_);
        LOG.debug("answer cache key: " + bundleCacheKey);
        getBundle = (PXStringBundle) _answerCache.get(bundleCacheKey);
        LOG.debug("from cache: " + getBundle);
        if (getBundle == null) {
            getBundle = findBundle(baseName_, locale_, loader_);
            LOG.debug("found bundle: " + getBundle);
            _answerCache.put(bundleCacheKey, getBundle);
            LOG.debug("cached bundle: " + getBundle + " under key: " + bundleCacheKey);
        }
        if (getBundle == NOT_FOUND_BUNDLE) {
            throw new MissingResourceException("could not find bundle", PXStringBundle.class.getName(), baseName_);
        }
        return getBundle;
    }

    public PXStringBundle getParent() {
        return _parent;
    }

    private void setParent(PXStringBundle parent_) {
        _parent = parent_;
    }

    public Locale getLocale() {
        return _locale;
    }

    public String getString(String key_) {
        String getString = null;
        if ((_strings != null) && (key_ != null)) {
            getString = _strings.getProperty(key_);
            LOG.debug("getString from _strings: " + getString);
            if ((getString == null) && (_parent != null)) {
                getString = _parent.getString(key_);
                LOG.debug("getString from _parent: " + getString);
            }
            if (getString == null) {
                _strings.setProperty(key_, NOT_FOUND_STRING);
            }
            if (ObjectUtils.equals(getString, NOT_FOUND_STRING)) {
                getString = null;
            }
        }
        return getString;
    }

    public String toString() {
        Locale parentLocale = (_parent != null) ? _parent._locale : null;
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("locale", _locale);
        builder.append("parent locale", parentLocale);
        builder.append("strings", _strings);
        return builder.toString();
    }

    /**
	 * used only for testing-- clears all the caches
	 */
    public static void reset() {
        _answerCache.clear();
        _loadCache.clear();
    }

    /**
	 * not only finds a matching bundle, but climbs the hierarchy from the match
	 * to set all the parents
	 */
    private static PXStringBundle findBundle(String baseName_, Locale locale_, ClassLoader loader_) {
        PXStringBundle findBundle = null;
        LOG.debug("baseName_: " + baseName_ + " locale_: " + locale_ + " loader_: " + loader_);
        if ((baseName_ != null) && (locale_ != null) && (loader_ != null)) {
            PXResourceLocation[] bundleLocations = PXResourceLocator.locateAll(baseName_ + BUNDLE_PROPERTIES_SUFFIX, locale_, loader_);
            if ((bundleLocations != null) && (bundleLocations.length > 0)) {
                PXStringBundle lastLoadedBundle = null;
                for (int i = 0; i < bundleLocations.length; i++) {
                    String cacheKey = getBundleCacheKey(baseName_, bundleLocations[i].getLocale(), loader_);
                    LOG.debug("cacheKey: " + cacheKey);
                    PXStringBundle cachedBundle = (PXStringBundle) _loadCache.get(cacheKey);
                    LOG.debug("cachedBundle: " + cachedBundle);
                    if (cachedBundle != null) {
                        if (findBundle == null) {
                            findBundle = cachedBundle;
                        }
                        if ((lastLoadedBundle != null)) {
                            lastLoadedBundle.setParent(cachedBundle);
                        }
                        break;
                    } else {
                        PXStringBundle loadedBundle = loadBundle(bundleLocations[i]);
                        LOG.debug("loadedBundle: " + loadedBundle);
                        if (findBundle == null) {
                            findBundle = loadedBundle;
                        }
                        if (lastLoadedBundle != null) {
                            lastLoadedBundle.setParent(loadedBundle);
                        }
                        lastLoadedBundle = loadedBundle;
                        _loadCache.put(cacheKey, loadedBundle);
                    }
                }
            }
            if (findBundle == null) {
                findBundle = NOT_FOUND_BUNDLE;
            }
        }
        return findBundle;
    }

    private static PXStringBundle loadBundle(PXResourceLocation location_) {
        PXStringBundle loadBundle = null;
        LOG.debug(location_);
        URL url = location_.getUrl();
        LOG.debug("url: " + url);
        if (url != null) {
            InputStream bundleStream = null;
            try {
                bundleStream = url.openStream();
                LOG.debug("bundleStream: " + bundleStream);
                if (bundleStream != null) {
                    PXXmlProperties bundleProperties = new PXXmlProperties();
                    bundleProperties.loadFromXML(bundleStream);
                    loadBundle = new PXStringBundle(bundleProperties, location_.getLocale());
                }
            } catch (Exception e) {
                LOG.error(null, e);
            } finally {
                PXStreamUtility.close(bundleStream);
            }
        }
        return loadBundle;
    }

    /**
	 * hashCode of the loader_ is used in the cacheKey, which might be a bit
	 * dicey.
	 */
    private static String getBundleCacheKey(String baseName_, Locale locale_, ClassLoader loader_) {
        LOG.debug("baseName_: " + baseName_ + " locale_: " + locale_);
        Validate.notNull(baseName_);
        Validate.notNull(loader_);
        StringBuffer keyBuffer = new StringBuffer();
        keyBuffer.append(baseName_);
        if (locale_ != null) {
            keyBuffer.append("|" + locale_.toString());
        }
        String loaderKey = Integer.toString(loader_.hashCode());
        keyBuffer.append("|" + loaderKey);
        LOG.debug("cacheKey: " + keyBuffer);
        return keyBuffer.toString();
    }
}
