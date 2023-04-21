package fr.imag.adele.escoffier.script.command;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.osgi.framework.BundleContext;

/**
 * @author Clement Escoffier <clement.escoffier@gmail.com>
 *
 */
public class BundleContextPropertiesWrapper implements Map {

    private BundleContext bundleContext;

    BundleContextPropertiesWrapper(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /**
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(Object key) {
        return bundleContext.getProperty((String) key);
    }

    public int size() {
        return -1;
    }

    public void clear() {
    }

    public boolean isEmpty() {
        return false;
    }

    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object arg0) {
        return (bundleContext.getProperty((String) arg0) != null);
    }

    public boolean containsValue(Object arg0) {
        return false;
    }

    public Collection values() {
        return null;
    }

    public void putAll(Map arg0) {
    }

    public Set entrySet() {
        return null;
    }

    public Set keySet() {
        return null;
    }

    public Object remove(Object arg0) {
        return null;
    }

    public Object put(Object arg0, Object arg1) {
        return null;
    }
}
