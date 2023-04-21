package com.thoughtworks.xstream.converters.javabean;

import java.util.Map;

/**
 * A sorter that keeps the natural order of the bean properties as they are returned by the
 * JavaBean introspection.
 * 
 * @author J&ouml;rg Schaible
 * @since 1.4
 */
public class NativePropertySorter implements PropertySorter {

    public Map sort(final Class type, final Map nameMap) {
        return nameMap;
    }
}
