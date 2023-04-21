package org.datanucleus.store;

import org.datanucleus.ObjectManagerFactoryImpl;
import org.datanucleus.util.Localiser;

/**
 * Abstract representation of an autostart mechanism.
 */
public abstract class AbstractAutoStartMechanism implements AutoStartMechanism {

    /** Localisation of messages */
    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.Localisation", ObjectManagerFactoryImpl.class.getClassLoader());

    /** AutoStart "mode" */
    protected Mode mode;

    /** Flag whether the starter is open. */
    protected boolean open = false;

    /**
     * Constructor.
     */
    public AbstractAutoStartMechanism() {
        super();
    }

    /**
     * Accessor for the mode of operation
     * @return The mode of operation
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Mutator for the mode of operation
     * @param mode The mode of operation
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * Starts a transaction for writting (add/delete) classes to the auto start mechanism.
     * Simply sets the open flag to true.
     */
    public void open() {
        open = true;
    }

    /**
     * Whether it's open for writing (add/delete) classes to the auto start mechanism
     * @return whether this is open for writing 
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Closes a transaction for writing (add/delete) classes to the auto start mechanism.
     * Set the open flag to false.
     */
    public void close() {
        open = false;
    }
}
