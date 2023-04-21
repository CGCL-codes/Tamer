package org.peertrust.event;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;

/**
 * <p>
 * PeerTrust event dispatcher.
 * </p><p>
 * $Id: PTEventDispatcher.java,v 1.5 2005/05/22 17:56:46 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:46 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class PTEventDispatcher implements EventDispatcher, Configurable {

    private static Logger log = Logger.getLogger(PTEventDispatcher.class);

    Hashtable registry;

    public PTEventDispatcher() {
        super();
        log.debug("$Id: PTEventDispatcher.java,v 1.5 2005/05/22 17:56:46 dolmedilla Exp $");
    }

    public void init() throws ConfigurationException {
        registry = new Hashtable();
    }

    public void register(PTEventListener listener) {
        register(listener, PTEvent.class);
    }

    public boolean unregister(PTEventListener listener) {
        boolean res = false;
        Iterator it = registry.keySet().iterator();
        while (it.hasNext()) {
            Vector vector = (Vector) registry.get(it.next());
            if (vector.remove(listener) == true) res = true;
        }
        return res;
    }

    public void register(PTEventListener listener, Class event) {
        log.debug(".registering " + listener.getClass().getName() + " to event " + event.getName());
        Vector vector = (Vector) registry.get(event);
        if (vector == null) {
            vector = new Vector();
            registry.put(event, vector);
        }
        if (vector.contains(listener)) {
            return;
        }
        vector.addElement(listener);
    }

    public void event(PTEvent event) {
        log.debug("Distributing event " + event.getClass().getName() + " from " + event.getSource().getClass().getName());
        Class currentClass = event.getClass();
        Vector vector;
        while ((currentClass != Object.class) && (currentClass != null)) {
            vector = (Vector) registry.get(currentClass);
            if (vector == null) log.debug("No listeners registered to catch event " + currentClass.getName()); else {
                log.debug(vector.size() + " elements registered for the event " + currentClass.getName());
                log.debug("Broadcasting event to listeners registered for the event " + currentClass.getName());
                Iterator it = vector.iterator();
                dispatchEvent(event, it);
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    private void dispatchEvent(PTEvent event, Iterator it) {
        while (it.hasNext()) {
            Object listener = it.next();
            if (listener instanceof PTEventListener) {
                if (listener != event.getSource()) ((PTEventListener) listener).event(event);
            } else {
                log.error("Unknown object " + listener.getClass().getName());
            }
        }
    }
}
