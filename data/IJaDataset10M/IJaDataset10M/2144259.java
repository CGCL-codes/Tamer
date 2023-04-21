package com.ibm.tuningfork.infra.filter;

import com.ibm.tuningfork.infra.data.TimeInterval;
import com.ibm.tuningfork.infra.event.EventType;
import com.ibm.tuningfork.infra.feed.IDataSource;
import com.ibm.tuningfork.infra.units.Unit;

public abstract class TimeIntervalFilter extends Filter implements ITimeIntervalFilter {

    protected TimeIntervalFilter(String name, IDataSource dataSource, Unit unit) {
        this(name, new IDataSource[] { dataSource }, unit);
    }

    protected TimeIntervalFilter(String name, IDataSource[] dataSources, Unit unit) {
        super(name, dataSources, unit);
    }

    public EventType getEventType() {
        return TimeInterval.eventType;
    }
}
