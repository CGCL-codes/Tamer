package gov.nist.siplite;

import java.util.Hashtable;

/**
    * Configuration properties.
  + * Configuration properties are set in StackConnector before initializing 
  + * a sipstack
  + * Following are mandatory Configuration Properties :
  + *
  + * javax.sip.IP_ADDRESS 
  + * javax.sip.STACK_NAME 
  + *
  + * Following are optional Configuration Properties :
  + *
  + * javax.sip.OUTBOUND_PROXY
  + * javax.sip.EXTENSION_METHODS
  + * gov.nist.javax.sip.LOG_FILE_NAME
  + * gov.nist.javax.sip.TRACE_LEVEL
  + * gov.nist.javax.sip.BAD_MESSAGE_LOG
  + * gov.nist.javax.sip.SERVER_LOG
  + * gov.nist.javax.sip.MAX_CONNECTIONS
  + * gov.nist.javax.sip.THREAD_POOL_SIZE
  + * gov.nist.javax.sip.MAX_SERVER_TRANSACTIONS
    */
public class ConfigurationProperties extends Hashtable {

    /** Default constructor. */
    public ConfigurationProperties() {
        super();
    }

    /**
     * Gets a property value.
     * @param name key for the property
     * @return the value of the property
     */
    public String getProperty(String name) {
        return (String) super.get(name);
    }

    /**
     * Sets a property value.
     * @param name the key for theproperty
     * @param value the value for the property
     */
    public void setProperty(String name, String value) {
        super.put(name, value);
    }
}
