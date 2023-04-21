package com.od.jtimeseries.context.impl;

import com.od.jtimeseries.context.ContextFactory;
import com.od.jtimeseries.context.TimeSeriesContext;
import com.od.jtimeseries.identifiable.IdentifiableBase;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 17-Feb-2009
 * Time: 14:03:35
 *
 * A factory for contexts, which can be added to a context to handle the creation of child contexts
 */
public class DefaultContextFactory extends IdentifiableBase implements ContextFactory {

    public DefaultContextFactory() {
        super(ID, ID);
        setDescription(getClass().getName());
    }

    public TimeSeriesContext createContext(TimeSeriesContext parent, String id, String description, Class classType, Object... parameters) {
        if (classType.isAssignableFrom(SeriesContext.class)) {
            return new SeriesContext(id, description, false);
        } else {
            throw new UnsupportedOperationException("Cannot create a context of type " + classType);
        }
    }
}
