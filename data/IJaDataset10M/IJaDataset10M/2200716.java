package com.volantis.shared.net.url.http;

import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.dependency.DependencyContext;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * URLConfiguration implementation for the HTTP(S) protocol.
 */
public class HttpUrlConfiguration implements URLConfiguration {

    /**
     * Request headers.
     */
    private Map headers;

    /**
     * The currently active dependency context.
     */
    private final DependencyContext dependencyContext;

    /**
     * Creates an HTTP URL configuration with the active dependency context.
     *
     * @param dependencyContext the active dependency context
     */
    public HttpUrlConfiguration(final DependencyContext dependencyContext) {
        this.dependencyContext = dependencyContext;
    }

    /**
     * Sets the value of the specified header. Overwrites previous value, if
     * present.
     *
     * @param name name of the header
     * @param value value of the header
     */
    public void addHeader(final String name, final String value) {
        if (headers == null) {
            headers = new LinkedHashMap();
        }
        final String key = name.toLowerCase();
        List values = (List) headers.get(key);
        if (values == null) {
            values = new LinkedList();
            headers.put(key, values);
        }
        values.add(value);
    }

    /**
     * Returns an iterator over the header names as String objects.
     *
     * @return the iterator for the header names
     */
    public Iterator getHeaderNames() {
        final Iterator iterator;
        if (headers == null) {
            iterator = Collections.EMPTY_LIST.iterator();
        } else {
            iterator = headers.keySet().iterator();
        }
        return iterator;
    }

    /**
     * Returns the list of header values for the given header name.
     *
     * @param headerName the name of the header
     * @return the values of the headers or null
     */
    public Iterator getHeaderValues(final String headerName) {
        final List values;
        if (headers == null) {
            values = null;
        } else {
            values = (List) headers.get(headerName.toLowerCase());
        }
        return values == null ? null : values.iterator();
    }

    /**
     * Returns true if this HttpUrlConfiguration contains the specified header.
     *
     * @param headerName the name of the header
     * @return true if the configuration contains the header
     */
    public boolean containsHeader(final String headerName) {
        return headers.containsKey(headerName.toLowerCase());
    }

    /**
     * Returns the currently active dependency context.
     */
    public DependencyContext getDependencyContext() {
        return dependencyContext;
    }
}
