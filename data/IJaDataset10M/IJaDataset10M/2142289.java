package org.metastatic.rsync;

import java.util.EventListener;

/**
 * Standard interface for the checksum generator callback, called by
 * {@link GeneratorStream} when new checksum pairs are ready.
 *
 * @version $Revision: 1.2 $
 */
public interface GeneratorListener extends EventListener {

    /**
    * Update with a single, new checksum pair.
    *
    * @param event The event containing the next checksum pair.
    */
    void update(GeneratorEvent event) throws ListenerException;
}
