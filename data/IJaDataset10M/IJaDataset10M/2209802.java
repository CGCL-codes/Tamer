package se.sics.mspsim.core;

import se.sics.mspsim.util.ArrayUtils;

public class PortListenerProxy implements PortListener {

    private PortListener[] portListeners;

    public PortListenerProxy(PortListener listen1, PortListener listen2) {
        portListeners = new PortListener[] { listen1, listen2 };
    }

    public static PortListener addPortListener(PortListener portListener, PortListener listener) {
        if (portListener == null) {
            return listener;
        }
        if (portListener instanceof PortListenerProxy) {
            return ((PortListenerProxy) portListener).add(listener);
        }
        return new PortListenerProxy(portListener, listener);
    }

    public static PortListener removePortListener(PortListener portListener, PortListener listener) {
        if (portListener == listener) {
            return null;
        }
        if (portListener instanceof PortListenerProxy) {
            return ((PortListenerProxy) portListener).remove(listener);
        }
        return portListener;
    }

    public PortListener add(PortListener mon) {
        portListeners = (PortListener[]) ArrayUtils.add(PortListener.class, portListeners, mon);
        return this;
    }

    public PortListener remove(PortListener listener) {
        PortListener[] listeners = (PortListener[]) ArrayUtils.remove(portListeners, listener);
        if (listeners == null) {
            return null;
        }
        if (listeners.length == 1) {
            return listeners[0];
        }
        portListeners = listeners;
        return this;
    }

    @Override
    public void portWrite(IOPort source, int data) {
        PortListener[] listeners = this.portListeners;
        for (PortListener l : listeners) {
            l.portWrite(source, data);
        }
    }
}
