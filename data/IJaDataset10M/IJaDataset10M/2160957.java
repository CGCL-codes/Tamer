package org.coury.jfilehelpers.events;

import org.coury.jfilehelpers.engines.EngineBase;

/**
 * @author Robert Eccardt
 *
 * @param <T> record type
 */
public interface AfterWriteRecordHandler<T> {

    /**
	 * The name of this method is very misleading because the 
	 * record has not yet been written.  It gives the caller the
	 * opportunity to examine the proposed output and make any desired
	 * modifications.
	 */
    public void handleAfterWriteRecord(EngineBase<T> engine, AfterWriteRecordEventArgs<T> e);
}
