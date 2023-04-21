package com.ideo.sweetdevria.util;

import java.io.Serializable;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Messages resources abstract class.
 * Initially from org.apache.struts.util.MessageResourcesFactory.
 */
public abstract class MessageResources implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5613984709393251012L;

    /**
     * Logger.
     */
    protected static final Log LOG = LogFactory.getLog(MessageResources.class);

    /**
     * Properties file name.
     */
    protected String propertiesFileName;

    /**
     * Factory.
     */
    protected MessageResourcesFactory factory;

    /**
     * Return null ? (not used here)
     */
    protected boolean returnNull;

    /**
     * Default factory.
     */
    protected static MessageResourcesFactory defaultFactory = null;

    /**
     * Constructor.
     * @param factory Factory used.
     * @param propertiesFileName Properties file name.
     */
    public MessageResources(final MessageResourcesFactory factory, final String propertiesFileName) {
        this(factory, propertiesFileName, false);
    }

    /**
     * Constructor.
     * @param factory Factory used.
     * @param propertiesFileName Properties file name.
     * @param returnNull Return null ? (not used here)
     */
    public MessageResources(final MessageResourcesFactory factory, final String propertiesFileName, final boolean returnNull) {
        this.factory = factory;
        this.propertiesFileName = propertiesFileName;
        this.returnNull = returnNull;
    }

    /**
     * Get message from the specific locale.
     * @param locale locale in wich get message.
     * @param messageKey message key.
     * @return message value in specified locale or in default locale (English) if not available.
     */
    public abstract String getMessage(final Locale locale, final String messageKey);

    /**
     * Get message from the default locale (English).
     * @param messageKey message key.
     * @return message value in default locale (English).
     */
    public abstract String getMessage(final String messageKey);

    /**
     * Get message by using object in argument.
     * @param key message key.
     * @param object Object used to get the locale (i.e. PageContext from where extract the locale).
     * @return messave value.
     */
    public abstract String getMessage(final String key, final Object object);

    /**
     * Get the config.
     * @return  Returns the config.
     */
    public String getConfig() {
        return propertiesFileName;
    }

    /**
     * Get the factory.
     * @return  Returns the factory.
     */
    public MessageResourcesFactory getFactory() {
        return factory;
    }

    /**
     * Get the returnNull.
     * @return  Returns the returnNull.
     */
    public boolean getReturnNull() {
        return returnNull;
    }

    /**
     * Set the returnNull.
     * @param returnNull  The returnNull to set.
     */
    public void setReturnNull(final boolean returnNull) {
        this.returnNull = returnNull;
    }

    /**
     * Get messages resources.
     * @param propertiesFileName Properties file name.
     * @return a message resources object corresponding to a properties file
     */
    public static synchronized MessageResources getMessageResources(final String propertiesFileName) {
        if (defaultFactory == null) {
            defaultFactory = MessageResourcesFactory.createFactory();
        }
        return defaultFactory.createResources(propertiesFileName);
    }
}
