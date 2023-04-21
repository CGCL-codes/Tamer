package de.mguennewig.pobjects.event;

import java.util.EventListener;

/** A listener for database changes caused by the application itself.
 *
 * @author Michael G�nnewig
 */
public interface PObjContainerListener extends EventListener {

    /** Called when the container data has changed. */
    void containerDataChanged(PObjContainerEvent event);
}
