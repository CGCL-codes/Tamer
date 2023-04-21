package org.rapla.facade;

import java.util.EventListener;
import org.rapla.framework.RaplaException;

/** Classes implementing this interface will be notified when changes to
 *  reservations or resources occurred. The listener can be registered by calling
 *  <code>addModificationListener</code> of the <code>UpdateModule</code> <br>
 *  Don't forget to remove the listener by calling <code>removeModificationLister</code>
 *  when no longer needed.
 *  @author Christopher Kohlhaas
 *  @see UpdateModule
 *  @see ModificationEvent
 */
public interface ModificationListener extends EventListener {

    /** this notifies all listeners that data in the rapla-backend has changed.
     *  The {@link ModificationEvent} describes these changes.
     */
    void dataChanged(ModificationEvent evt) throws RaplaException;

    /**
     * Return true if you want the notification to
     * be synchronized with the awt event-queue using {@link
     * javax.swing.SwingUtilities#invokeLater}. Use this to avaoid
     * synchronization problems with swing guis.
     *
     */
    boolean isInvokedOnAWTEventQueue();
}
