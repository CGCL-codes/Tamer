package org.formproc.message;

import java.util.Locale;
import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.ConfigurationException;

/**
 * Abstract base class which implements the MessageProvider interface.
 *
 * @author Anthony Eden
 */
public abstract class AbstractMessageProvider implements MessageProvider {

    /**
     * Get the message for the given locale.
     *
     * @param locale The Locale
     * @return The message
     * @throws Exception
     */
    public abstract String getMessage(Locale locale) throws Exception;

    /**
     * Load the message provider's configuration.  The default implementation provided here does nothing.  Subclasses
     * can override this method to load configuration information for the message provider.
     *
     * @param configuration The Configuration object
     * @throws ConfigurationException
     */
    public void loadConfiguration(Configuration configuration) throws ConfigurationException {
    }
}
