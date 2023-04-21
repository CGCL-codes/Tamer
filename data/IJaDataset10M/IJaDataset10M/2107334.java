package org.nakedobjects.runtime.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractServletOrFilterMapping {

    private final Class<?> servletOrFilterClass;

    private final List<String> pathSpecs;

    private final Map<String, String> initParams;

    @SuppressWarnings("unchecked")
    public AbstractServletOrFilterMapping(final Class<?> servletOrFilterClass, final String... pathSpecs) {
        this(servletOrFilterClass, Collections.EMPTY_MAP, pathSpecs);
    }

    public AbstractServletOrFilterMapping(final Class<?> servletOrFilterClass, final Map<String, String> initParams, final String... pathSpecs) {
        this.servletOrFilterClass = servletOrFilterClass;
        this.initParams = initParams;
        this.pathSpecs = new ArrayList<String>(Arrays.asList(pathSpecs));
    }

    protected Class<?> getServletOrFilterClass() {
        return servletOrFilterClass;
    }

    public Map<String, String> getInitParams() {
        return Collections.unmodifiableMap(initParams);
    }

    public List<String> getPathSpecs() {
        return Collections.unmodifiableList(pathSpecs);
    }
}
