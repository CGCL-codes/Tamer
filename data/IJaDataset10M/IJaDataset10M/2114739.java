package com.volantis.synergetics.reporting.impl.exclusions;

import com.volantis.synergetics.reporting.Event;

/**
 * Excluder for update reporting events
 *
 */
public class UpdateEventExcluder extends EqualsConditionExcluder {

    /**
     * Constructor
     *
     */
    public UpdateEventExcluder() {
        super(com.volantis.synergetics.reporting.impl.Metric.EVENT, Event.UPDATE, false);
    }
}
