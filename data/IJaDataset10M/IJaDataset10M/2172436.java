package de.jassda.core.event;

/**
 * Interface Configurable
 *
 *
 * @author Mark Broerkens
 * @version %I%, %G%
 */
public interface Configurable {

    /**
     * Method configure
     *
     *
     * @param key
     * @param value
     *
     * @throws de.jassda.core.event.BadConfigurationException
     *
     */
    public void configure(Entity key, Entity value) throws BadConfigurationException;
}
