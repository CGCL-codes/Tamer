package uk.ac.lkl.migen.system.expresser.ui.uievent;

import java.util.ArrayList;

/**
 * This class manages user interface events.
 * When events occur this is notified.
 * Listeners may be added to respond.
 * 
 * 
 * @author Ken Kahn
 *
 */
public class UIEventManager {

    private static ArrayList<UIEventListener> uIEventListeners = new ArrayList<UIEventListener>();

    public static void processEvent(UIEvent<?> event) {
        for (UIEventListener uIEventListener : new ArrayList<UIEventListener>(uIEventListeners)) {
            uIEventListener.processEvent(event);
        }
    }

    public static void addUIEventListener(UIEventListener uIEventListener) {
        uIEventListeners.add(uIEventListener);
    }

    public static void removeUIEventListener(UIEventListener uIEventListener) {
        uIEventListeners.remove(uIEventListener);
    }
}
