package siouxsie.desktop.events;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Main bus for desktop scoped events.
 * The bus receives events from differents sources
 * in the desktop and forwards them to listeners.
 * This centralized implementation of the observer
 * pattern is used instead of using scattered
 * event generators and listeners, which could
 * lead to very messy event path and hard to debug
 * code.</p>
 * <p>This solution is inspired of the
 * <a href="http://jedit.org">JEdit</a> text editor, where
 * plugins register to an <a href="http://www.jedit.org/api/org/gjt/sp/jedit/EditBus.html">
 * edit bus</a> to receive events.</p>  
 * @author Arnaud Cogoluegnes
 * @version $Id: DesktopBus.java 188 2008-10-21 14:14:04Z acogo $
 */
public class DesktopBus {

    private Collection<DesktopListener> listeners = new ArrayList<DesktopListener>();

    private Collection<IDesktopActor> desktopActors = new ArrayList<IDesktopActor>();

    public DesktopBus(Collection<IDesktopActor> desktopActors) {
        super();
        this.desktopActors = desktopActors;
        init();
    }

    public void addDesktopListener(DesktopListener desktopListener) {
        listeners.add(desktopListener);
    }

    public void removeDesktopListener(DesktopListener desktopListener) {
        listeners.remove(desktopListener);
    }

    public void fireDesktopEvent(DesktopEvent event) {
        for (DesktopListener listener : listeners) {
            listener.handle(event);
        }
    }

    /**
	 * Init the actors with the bus.
	 *
	 */
    public void init() {
        for (IDesktopActor actor : desktopActors) {
            actor.init(this);
        }
    }
}
