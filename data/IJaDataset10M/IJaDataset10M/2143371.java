package junit.extensions;

import junit.framework.Test;

/**
 * Test that provides access to configuration parameters.
 *
 * @author Siegfried GOESCHL
 * @author Dima STADNIK
 */
public interface ConfigurableTest extends Test {

    /**
     * Is the property defined ?!
     *
     * @param key name of the configuration parameter
     */
    boolean contains(String key);

    /**
     * Retrieves boolean number from configuration.
     *
     * @param key Configuration parameter name.
     * @return Boolean value of configuration parameter.
     * @exception IllegalArgumentException Parameter was not found or can't be retrieved.
     */
    boolean getBoolean(String key) throws IllegalArgumentException;

    /**
     * Retrieves byte from configuration.
     *
     * @param key Configuration parameter name.
     * @return Byte value of configuration parameter.
     * @exception IllegalArgumentException Parameter was not found or can't be retrieved.
     */
    byte getByte(String key) throws IllegalArgumentException;

    /**
     * Retrieves character from configuration.
     *
     * @param key Configuration parameter name.
     * @return Char value of configuration parameter.
     * @exception IllegalArgumentException Parameter was not found or can't be retrieved.
     */
    char getChar(String key) throws IllegalArgumentException;

    /**
     * Retrieves double number from configuration.
     *
     * @param key Configuration parameter name.
     * @return Double value of configuration parameter.
     * @exception IllegalArgumentException Parameter was not found or can't be retrieved.
     */
    double getDouble(String key) throws IllegalArgumentException;

    /**
     * Retrieves float number from configuration.
     *
     * @param name Configuration parameter name.
     * @return Float value of configuration parameter.
     * @exception IllegalArgumentException Parameter was not found or can't be retrieved.
     */
    float getFloat(String key) throws IllegalArgumentException;

    /**
     * Retrieves integer number from configuration.
     *
     * @param key Configuration parameter name.
     * @return Integer value of configuration parameter.
     * @exception IllegalArgumentException Parameter was not found or can't be retrieved.
     */
    int getInteger(String key) throws IllegalArgumentException;

    /**
     * Retrieves long number from configuration.
     *
     * @param key Configuration parameter name.
     * @return Long value of configuration parameter.
     * @exception IllegalArgumentException Parameter was not found or can't be retrieved.
     */
    long getLong(String key) throws IllegalArgumentException;

    /**
     * Retrieves short number from configuration.
     *
     * @param key Configuration parameter name.
     * @return Short value of configuration parameter.
     * @exception IllegalArgumentException Parameter was not found or can't be retrieved.
     */
    short getShort(String key) throws IllegalArgumentException;

    /**
     * Retrieves string from configuration.
     *
     * @param key Configuration parameter name.
     * @return String value of configuration parameter.
     * @exception IllegalArgumentException Parameter was not found or can't be retrieved.
     */
    String getString(String key) throws IllegalArgumentException;

    /**
     * Retrieves array of strings from configuration.
     *
     * @param key Configuration parameter name.
     * @return Value of configuration parameter as array of strings.
     * @exception IllegalArgumentException Parameter was not found or can't be retrieved.
     */
    String[] getStrings(String key) throws IllegalArgumentException;
}
