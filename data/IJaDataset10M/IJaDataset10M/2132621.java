package io;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class responsible for handling the gruntwork communication.
 * 
 * @author Staubli
 *
 */
public abstract class IOClient {

    private String name;

    private List<CommunicationListener> listeners;

    public IOClient(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract boolean isConnected();

    public abstract boolean connect();

    public abstract boolean disconnect();

    public abstract boolean send(byte[] data);

    public abstract byte[] receive();

    protected List<CommunicationListener> getListeners() {
        if (listeners == null) listeners = new ArrayList<CommunicationListener>();
        return listeners;
    }

    public void addListener(CommunicationListener listen) {
        getListeners().add(listen);
    }

    public void removeListener(CommunicationListener listen) {
        getListeners().remove(listen);
    }

    public String toString() {
        return getName();
    }
}
