package org.peaseplate.domain.locator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import org.peaseplate.TemplateLocatorException;
import org.peaseplate.Messages;
import org.peaseplate.MessagesLocator;
import org.peaseplate.TemplateEngine;
import org.peaseplate.domain.template.PropertyBasedMessages;
import org.peaseplate.locator.AbstractURLBasedLocator;

/**
 * A messages locator that is based on an URL
 * 
 * @author Manfred HANTSCHEL
 */
public class URLBasedMessagesLocator extends AbstractURLBasedLocator implements MessagesLocator {

    public URLBasedMessagesLocator(URL url, String name, Locale locale, String encoding) {
        super(url, name, locale, encoding);
    }

    /**
	 * @see org.peaseplate.MessagesLocator#load(org.peaseplate.TemplateEngine)
	 */
    public Messages load(TemplateEngine engine) throws TemplateLocatorException {
        Messages result = null;
        try {
            Properties properties = new Properties();
            InputStream in = getUrl().openStream();
            try {
                properties.load(in);
            } finally {
                in.close();
            }
            updateMetadata();
            result = new PropertyBasedMessages(this, properties);
        } catch (IOException e) {
            throw new TemplateLocatorException("Could not load messages from \"" + this + "\"");
        }
        return result;
    }
}
