package org.peaseplate.templateengine.locator.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import org.peaseplate.templateengine.locator.MessagesLocator;
import org.peaseplate.templateengine.locator.TemplateLocatorException;
import org.peaseplate.templateengine.messages.internal.PropertyBasedMessages;
import org.peaseplate.utils.message.Messages;

/**
 * A messages locator that is based on an URL
 * 
 * @author Manfred HANTSCHEL
 */
public class URLBasedMessagesLocator extends AbstractURLBasedLocator implements MessagesLocator {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates a new instance of the locator
	 * 
	 * @param url the url
	 * @param name the name
	 * @param locale the locale
	 * @param encoding the encoding
	 */
    public URLBasedMessagesLocator(final URL url, final String name, final Locale locale, final String encoding) {
        super(url, name, locale, encoding);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Messages load() throws TemplateLocatorException {
        Messages result = null;
        try {
            final Properties properties = new Properties();
            final InputStream in = getUrl().openReader();
            try {
                properties.load(in);
            } finally {
                in.close();
            }
            updateMetadata();
            result = new PropertyBasedMessages(this, properties);
        } catch (final IOException e) {
            throw new TemplateLocatorException("Could not load messages from \"" + this + "\"");
        }
        return result;
    }
}
