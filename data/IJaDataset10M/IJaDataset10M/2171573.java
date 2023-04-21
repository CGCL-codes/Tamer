package sdljava.event;

import sdljava.x.swig.*;

/**
 * Platform-dependent window manager event.  <P> The system window
 * manager event contains a pointer to system-specific information
 * about unknown window manager events. If you enable this event using
 * SDL_EventState, it will be generated whenever unhandled events are
 * received from the window manager. This can be used, for example, to
 * implement cut-and-paste in your application.
 * <P>
 * If you want to obtain system-specific information about the window
 * manager, you can fill in the version member of a SDL_SysWMinfo
 * structure (details can be found in SDL_syswm.h, which must be
 * included) using the SDL_VERSION() macro found in SDL_version.h, and
 * pass it to the function:
 *
 * @author Ivan Z. Ganza
 * @version $Id: SDLSysWMEvent.java,v 1.4 2005/01/25 02:50:45 ivan_ganza Exp $
 */
public class SDLSysWMEvent extends SDLEvent {

    /**
     * The type of the this event
     *
     * @return The type of event
     */
    public int getType() {
        return -1;
    }
}
