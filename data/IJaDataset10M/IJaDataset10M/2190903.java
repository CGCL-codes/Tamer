package com.amazon.carbonado.spi;

import com.amazon.carbonado.Storable;
import com.amazon.carbonado.Trigger;

/**
 *
 *
 * @author Brian S O'Neill
 */
public interface TriggerSupport<S extends Storable> extends StorableSupport<S> {

    /**
     * Returns a trigger which must be run for all insert operations.
     *
     * @return null if no trigger
     */
    Trigger<? super S> getInsertTrigger();

    /**
     * Returns a trigger which must be run for all update operations.
     *
     * @return null if no trigger
     */
    Trigger<? super S> getUpdateTrigger();

    /**
     * Returns a trigger which must be run for all delete operations.
     *
     * @return null if no trigger
     */
    Trigger<? super S> getDeleteTrigger();
}
