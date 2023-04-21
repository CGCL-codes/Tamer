package com.od.jtimeseries.source.impl;

import com.od.jtimeseries.identifiable.Identifiable;
import com.od.jtimeseries.identifiable.IdentifiableBase;
import com.od.jtimeseries.source.*;
import com.od.jtimeseries.util.time.TimePeriod;

public class DefaultValueSourceFactory extends IdentifiableBase implements ValueSourceFactory {

    public DefaultValueSourceFactory() {
        super(ID, ID);
        setDescription(getClass().getName());
    }

    protected ValueRecorder createValueRecorder(Identifiable parent, String path, String id, String description, Object... parameters) {
        return new DefaultValueRecorder(id, description);
    }

    protected Counter createCounter(Identifiable parent, String path, String id, String description, Object... parameters) {
        return new DefaultCounter(id, description);
    }

    protected EventTimer createEventTimer(Identifiable parent, String path, String id, String description, Object... parameters) {
        return new DefaultEventTimer(id, description);
    }

    protected TimedValueSupplier createTimedValueSupplier(Identifiable parent, String path, String id, String description, ValueSupplier valueSupplier, TimePeriod timePeriod, Object... parameters) {
        return new DefaultTimedValueSupplier(id, description, valueSupplier, timePeriod);
    }

    public ValueSource createValueSource(Identifiable parent, String path, String id, String description, Class classType, Object... parameters) {
        if (classType.isAssignableFrom(ValueRecorder.class)) {
            return createValueRecorder(parent, path, id, description, parameters);
        } else if (classType.isAssignableFrom(Counter.class)) {
            return createCounter(parent, path, id, description, parameters);
        } else if (classType.isAssignableFrom(EventTimer.class)) {
            return createEventTimer(parent, path, id, description, parameters);
        } else if (classType.isAssignableFrom(TimedValueSupplier.class)) {
            return createTimedValueSupplier(parent, path, id, description, (ValueSupplier) parameters[0], (TimePeriod) parameters[1], parameters);
        } else {
            throw new UnsupportedOperationException("Cannot create ValueSource of class type " + classType);
        }
    }
}
