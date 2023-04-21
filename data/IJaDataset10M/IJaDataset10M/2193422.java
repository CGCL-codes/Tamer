package org.aladdinframework.event;

import java.util.List;
import java.util.Vector;
import org.aladdinframework.contextplugin.api.*;
import org.aladdinframework.contextplugin.api.ContextPluginRuntime;
import org.aladdinframework.contextplugin.api.IPluginContextListener;
import org.aladdinframework.contextplugin.api.IPluginEventHandler;
import org.aladdinframework.contextplugin.api.security.*;

/**
 * Simple implementation of the IPluginEventHandler interface. Uses a thread-safe Vector for maintaining the list of
 * listeners. Automatically creates a snapshot of the current listener list before sending events.
 * 
 * @author Darren Carlson
 */
public class SimpleEventHandler implements IPluginEventHandler {

    private List<IPluginContextListener> listeners = new Vector<IPluginContextListener>();

    /**
     * Adds a IPluginContextListener if it has not already been added
     */
    public void addContextListener(IPluginContextListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    /**
     * Removes a previously added IPluginContextListener
     */
    public void removeContextListener(IPluginContextListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sends the specified SecuredEvents to registered listeners using the specified ContextPluginRuntime as the event
     * sender.
     */
    @Override
    public void sendEvent(ContextPluginRuntime sender, ContextInfoSet infoSet) {
        List<IPluginContextListener> snapshot = new Vector<IPluginContextListener>(listeners);
        for (IPluginContextListener l : snapshot) {
            l.onPluginContextEvent(infoSet, sender.getSessionId());
        }
        snapshot.clear();
        snapshot = null;
    }
}
