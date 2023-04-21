package com.genia.toolbox.web.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.genia.toolbox.basics.bean.EnumerationAggregator;
import com.genia.toolbox.basics.bean.EnumerationIterator;

/**
 * class containing useful methods for proxy classes.
 */
class ProcessProxy {

    /**
   * Retrieve an <code>Enumeration</code> containing all the key of either the
   * parameters or the attributes.
   * 
   * @param currentValues
   *          the values of the proxy
   * @param removedValues
   *          the values removed from the proxy
   * @param upValues
   *          the values of the wrapped request
   * @return an <code>Enumeration</code> containing all the key of either the
   *         parameters or the attributes
   */
    @SuppressWarnings("unchecked")
    public static Enumeration<String> getNames(final Map<String, ?> currentValues, final Set<String> removedValues, final Enumeration<String> upValues) {
        final Collection<String> names = new ArrayList<String>();
        while (upValues.hasMoreElements()) {
            final String upName = upValues.nextElement();
            if (!removedValues.contains(upName) && !currentValues.containsKey(upName)) {
                names.add(upName);
            }
        }
        return new EnumerationAggregator<String>(new EnumerationIterator<String>(currentValues.keySet()), new EnumerationIterator<String>(names));
    }

    /**
   * Retrieve a value from a proxied Map.
   * 
   * @param <K>
   *          the type of the value
   * @param name
   *          the name of the value
   * @param values
   *          the proxied values
   * @param removedValues
   *          the removed values from the proxy
   * @param upValue
   *          the value of the original object
   * @return the value from a proxied Map
   */
    public static <K> K getValue(final String name, final Map<String, K> values, final Set<String> removedValues, final K upValue) {
        if (removedValues.contains(name)) {
            return null;
        }
        final K res = values.get(name);
        if (res != null) {
            return res;
        }
        return upValue;
    }

    /**
   * returns a {@link Map} representing the current state of the proxy.
   * 
   * @param <K>
   *          the type of the values
   * @param values
   *          the proxied values
   * @param removedValues
   *          the removed values from the proxy
   * @param upValues
   *          the values of the original object
   * @return the values from a proxied Map
   */
    public static <K> Map<String, K> getValueMap(final Map<String, K> values, final Set<String> removedValues, final Map<String, K> upValues) {
        final Map<String, K> res = new TreeMap<String, K>();
        res.putAll(upValues);
        res.putAll(values);
        for (final String removedParameter : removedValues) {
            res.remove(removedParameter);
        }
        return Collections.unmodifiableMap(res);
    }
}
