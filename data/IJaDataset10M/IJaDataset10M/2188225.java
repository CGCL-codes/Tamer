package net.taylor.seam.ext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jboss.seam.Seam;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.contexts.Lifecycle;
import org.jboss.seam.core.Init;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.core.ResourceLoader;
import org.jboss.seam.util.IteratorEnumeration;

public class CompositeResourceBundle extends ResourceBundle {

    protected Map<Init, Map<Locale, Map<String, String>>> cache = new ConcurrentHashMap<Init, Map<Locale, Map<String, String>>>();

    @Override
    protected Object handleGetObject(String key) {
        Object value = getCachedBundle().get(key);
        if (value != null) {
            value = Interpolator.instance().interpolate((String) value);
        }
        return value;
    }

    @Override
    public Enumeration<String> getKeys() {
        return new IteratorEnumeration(getCachedBundle().keySet().iterator());
    }

    @Override
    public Locale getLocale() {
        return org.jboss.seam.core.Locale.instance();
    }

    protected Map<String, String> getCachedBundle() {
        Map<String, String> bundle = getCachedBundles().get(getLocale());
        if (bundle == null) {
            bundle = new HashMap<String, String>();
            getCachedBundles().put(getLocale(), bundle);
            initialize(bundle);
        }
        return bundle;
    }

    protected void initialize(Map<String, String> bundle) {
        if (!Contexts.isApplicationContextActive()) {
            return;
        }
        for (ResourceBundle littleBundle : loadBundlesForCurrentLocale()) {
            Set<String> keys = littleBundle.keySet();
            for (String key : keys) {
                bundle.put(key, littleBundle.getString(key));
            }
        }
    }

    protected List<ResourceBundle> loadBundlesForCurrentLocale() {
        ResourceLoader resourceLoader = ResourceLoader.instance();
        List<String> bundleNames = new ArrayList<String>(Arrays.asList(resourceLoader.getBundleNames()));
        bundleNames.add("ValidatorMessages");
        bundleNames.add("org/hibernate/validator/resources/DefaultValidatorMessages");
        bundleNames.add("javax.faces.Messages");
        Collections.reverse(bundleNames);
        List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
        for (String bundleName : bundleNames) {
            ResourceBundle bundle = resourceLoader.loadBundle(bundleName);
            if (bundle != null) {
                bundles.add(bundle);
            }
        }
        return Collections.unmodifiableList(bundles);
    }

    protected Map<Locale, Map<String, String>> getCachedBundles() {
        Init init;
        if (Contexts.isApplicationContextActive()) {
            init = (Init) Contexts.getApplicationContext().get(Seam.getComponentName(Init.class));
        } else {
            init = (Init) Lifecycle.getApplication().get(Seam.getComponentName(Init.class));
        }
        if (!cache.containsKey(init)) {
            cache.put(init, new ConcurrentHashMap<Locale, Map<String, String>>());
        }
        return cache.get(init);
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle(CompositeResourceBundle.class.getName(), org.jboss.seam.core.Locale.instance());
    }

    public static ResourceBundle getBundleNamed(String bundleName) {
        return ResourceBundle.getBundle(bundleName, org.jboss.seam.core.Locale.instance());
    }

    public static void clear() {
        CompositeResourceBundle bundle = (CompositeResourceBundle) getBundle();
        bundle.cache.clear();
    }
}
