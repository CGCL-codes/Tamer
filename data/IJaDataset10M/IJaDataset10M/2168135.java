package com.volantis.mps.message.store;

/**
 * This represents the message store configuration as defined at deployment
 * time.  The various values set in the config file are represented in this
 * class.
 */
public class MessageStoreConfig {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The default location of the log4j configuration file.  This can be
     * overridden by customer configurations in the &lt;environment/&gt;
     * element of the Message Store configuration file.
     */
    public static final String DEFAULT_LOG_LOCATION = "WEB-INF/mps-log4j.xml";

    /**
     * The location of the message store cache.  This is where temporary files
     * will be saved and then used where necesary.
     */
    private String location;

    /**
     * The size of the ID to use for unique name for entries in the message
     * store.  This should have a default minimum size of 10 (according to the
     * configuration xslt).
     */
    private int idSize;

    /**
     * The timeout that should be used when cleaning out the message store
     * entries to determine those that should be removed.
     */
    private int timeout;

    /**
     * Indicates whether there is an unlimited timeout set on the message store
     * entries.  If this is true the value of the {@link #timeout} should be
     * ignored.
     */
    private boolean unlimitedTimeout;

    /**
     * Indicates whether the source xml that is stored should be validated
     * before storage.
     */
    private boolean validate;

    /**
     * The location of the log4j configuration file
     */
    private String log4jConfigurationFile = DEFAULT_LOG_LOCATION;

    /**
     * Initialise a new instance of this class.
     */
    public MessageStoreConfig() {
    }

    /**
     * Reset the state of the configuration object to be default values
     * based on the defaults that are specified in the xslt that validates
     * a config file.  Required attributes are set to invalid values so that
     * if an object is used that is reset it will cause problems early on.
     */
    public void reset() {
        location = null;
        idSize = 0;
        timeout = -1;
        unlimitedTimeout = true;
        validate = false;
        log4jConfigurationFile = DEFAULT_LOG_LOCATION;
    }

    /**
     * Retrieve the id size specified in the config file.
     *
     * @return The id size
     */
    public int getIdSize() {
        return idSize;
    }

    /**
     * Set the id size to the value provided.
     *
     * @param idSize The new id size.
     */
    public void setIdSize(int idSize) {
        this.idSize = idSize;
    }

    /**
     * Retrieve the location of the message store cache directory as
     * specified in the config file.
     *
     * @return The fully qualified path to the message store cache directory
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the location of the message store cache directory to the value
     * provided.
     *
     * @param location The new location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieve the timeout specified in the config file.   This should be
     * used in conjunction with the {@link #isUnlimitedTimeout} as if that
     * method returns true the value returned here is meaningless. The time
     * in seconds before a message is eligible for removal.
     *
     * @return Return the timeout value
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Set the timeout of the entries in the message store cache to be
     * the value provided. This value is the time, in seconds, that must elapse
     * before a message becomes eligible for removal. 
     *
     * @param timeout The new timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
        if (timeout == -1) {
            this.unlimitedTimeout = true;
        }
    }

    /**
     * Determine whether there is an unlimited timeout on entries in the
     * message store cache.
     *
     * @return True if there is an unlimited timeout, false otherwise.
     */
    public boolean isUnlimitedTimeout() {
        return unlimitedTimeout;
    }

    /**
     * Sets whether the entries in the message store have an unlimited timeout.
     *
     * @param unlimitedTimeout True if the entries have unlimited timeout,
     *                         false otherwise.
     */
    public void setUnlimitedTimeout(boolean unlimitedTimeout) {
        this.unlimitedTimeout = unlimitedTimeout;
        this.timeout = -1;
    }

    /**
     * Determine whether the xml that is to be stored should be validated
     * prior to storage.
     *
     * @return True if the xml is to be validated, false otherwise.
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * Sets whether the xml is validated prior to storage.
     *
     * @param validate True if the xml should be validated, false otherwise.
     */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    /**
     * Retrieve the log4j configuration file either as specified in the
     * config file, or if that is not present, defaulting to the location
     * specified by {@link #DEFAULT_LOG_LOCATION}.
     *
     * @return The location of the log4j configuration file
     */
    public String getLog4jConfigurationFile() {
        return log4jConfigurationFile;
    }

    /**
     * Sets the log4j configuration file to be the vlaue provided.
     *
     * @param log4jConfigurationFile The new log4j configuration file to use
     */
    public void setLog4jConfigurationFile(String log4jConfigurationFile) {
        this.log4jConfigurationFile = log4jConfigurationFile;
    }
}
