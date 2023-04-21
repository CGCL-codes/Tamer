package org.jbfilter.core;

/**
 * Interface for {@link FilterComponent}s which provide special support for testing {@code null} values.
 * Special {@code null} testing behavior is switched on by calling the {@link #setNullTestingEnabled(boolean)}
 * method. 
 * @author Marcus Adrian
 */
public interface NullTestingEnableable {

    /**
	 * Method to switch on/off the special {@code null} testing behavior.
	 * @param enabled {@code true} switches on, {@code false} switches off
	 */
    void setNullTestingEnabled(boolean enabled);

    /**
	 * Indicates if the special {@code null} testing behavior is on or off.
	 * @return
	 */
    boolean isNullTestingEnabled();
}
