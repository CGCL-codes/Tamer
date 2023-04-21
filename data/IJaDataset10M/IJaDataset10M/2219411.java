package com.unicont.cardio.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import com.unicont.cardio.CardioTerminal;
import com.unicont.cardio.CardioTerminalInitializationException;
import com.unicont.cardio.CardioTerminalListener;
import com.unicont.cardio.Cardiogram;
import com.unicont.cardio.CardiogramReceivingException;
import com.unicont.cardio.ConfigurationException;
import com.unicont.cardio.TransmissionManager;
import com.unicont.cardio.model.ConnectProperties;
import com.unicont.cardio.model.Metadata;

public abstract class CardioTerminalImpl implements CardioTerminal {

    private String port;

    protected Metadata metadata = new Metadata(1, 0xffff);

    public static String LOGGER_NAME = "cardio.terminal";

    private boolean open = false;

    protected Logger logger = Logger.getLogger(LOGGER_NAME);

    private ConnectProperties connectProperties;

    public CardioTerminalImpl(String aPort) {
        port = aPort;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public boolean isOpen() {
        return open;
    }

    /**
	 * Set status open to the terminal.
	 * 
	 * This method usually get called from inheritants to indicate that pager was connected or disconnected 
	 * @param isOpen boolean true - terminal gets open, false - {@link #close()} is invoked
	 */
    protected void setOpen(boolean isOpen) {
        open = isOpen;
        if (open) {
            fireTerminalWasConnected();
        } else {
            close();
        }
    }

    public void init(ConnectProperties aConnectProperties) throws CardioTerminalInitializationException {
    }

    public void init() throws CardioTerminalInitializationException {
    }

    public void setConnectProperties(ConnectProperties connectProperties) {
        this.connectProperties = connectProperties;
    }

    public ConnectProperties getConnectProperties() {
        return connectProperties;
    }

    public void close() {
        if (!isOpen()) return;
        open = false;
        fireTerminalWasClosed();
    }

    public boolean isOnlineSupported() {
        return false;
    }

    public Cardiogram receiveCardiogram(int number) throws CardiogramReceivingException {
        return null;
    }

    public void prepareReceivingCardiogramOnline() throws CardiogramReceivingException {
    }

    public void receiveNextLeadPair(int aLeadPair) throws CardiogramReceivingException {
    }

    public Cardiogram receiveCardiogram() throws CardiogramReceivingException {
        throw new CardiogramReceivingException("Not supported.");
    }

    public String getPort() {
        return port;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger aLogger) {
        logger = aLogger;
        LOGGER_NAME = aLogger.getName();
    }

    public Object getAdapter(Class anAdapterClass) {
        return null;
    }

    public boolean isConfigurable() {
        return false;
    }

    public void setConfiguration(Map aConfiguration) throws ConfigurationException {
    }

    public Map getConfiguration() throws ConfigurationException {
        return null;
    }

    public TransmissionManager getTransmissionManager() {
        return null;
    }

    protected List<CardioTerminalListener> listeners = new LinkedList<CardioTerminalListener>();

    public void addCardioTerminalListener(CardioTerminalListener aListener) {
        synchronized (listeners) {
            listeners.add(aListener);
        }
    }

    public void removeCardioTerminalListener(CardioTerminalListener aListener) {
        synchronized (listeners) {
            listeners.remove(aListener);
        }
    }

    protected void fireTerminalWasConnected() {
        synchronized (listeners) {
            for (CardioTerminalListener l : listeners) {
                l.terminalConnected(this);
            }
        }
    }

    protected void fireTerminalWasClosed() {
        synchronized (listeners) {
            for (CardioTerminalListener l : listeners) {
                l.terminalClosed(this);
            }
            listeners.clear();
        }
    }
}
