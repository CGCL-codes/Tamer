package org.jjazz.util;

/**
 * A map which uses Class object as keys.
 * A subclass is considered as an equivalent key.
 */
public class SillyClassMap<V> extends SillyMap<Class, V> {

    /**
     * Overridden so that aClass key can be a subclass of the actual key.
     * @param aClass
     * @return
     */
    @Override
    public V getValue(Class aClass) {
        for (Class cl : getKeys()) {
            if (cl.isAssignableFrom(aClass)) {
                int index = keys.indexOf(cl);
                return values.get(index);
            }
        }
        return null;
    }
}
