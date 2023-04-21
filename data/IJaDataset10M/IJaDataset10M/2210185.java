package sdljava.event;

import java.util.EventListener;

/**
 * Interface for handling SDLEvents.
 *
 * @author  Bart Lebeouf
 * @version $Id: SDLEventListener.java,v 1.2 2005/01/29 05:09:14 ivan_ganza Exp $
 */
public interface SDLEventListener extends EventListener {

    /**
     * Called when an SDLEvent occurs
     *
     * @param event a <code>SDLEvent</code> value
     */
    public void handleEvent(SDLEvent event);
}
