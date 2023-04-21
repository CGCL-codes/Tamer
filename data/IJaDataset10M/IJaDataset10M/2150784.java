package org.coury.jfilehelpers.events;

import org.coury.jfilehelpers.engines.EngineBase;

/**
 * @author Robert Eccardt
 *
 */
public interface BeforeReadRecordHandler<T> {

    public void handleBeforeReadRecord(EngineBase<T> engine, BeforeReadRecordEventArgs<T> e);
}
