package org.jmesa.core.message;

import org.jmesa.web.SpringWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * The Spring specific messages. Will use the default messages if they are not defined in Spring.
 *
 * @since 2.3.3
 * @author Oscar Perez
 */
public class SpringMessages implements Messages {

    private static Logger logger = LoggerFactory.getLogger(SpringMessages.class);

    private Messages defaultMessages;

    private SpringWebContext springWebContext;

    private MessageSource messageSource;

    public SpringMessages(Messages defaultMessages, SpringWebContext springWebContext) {
        this.defaultMessages = defaultMessages;
        this.springWebContext = springWebContext;
        this.messageSource = springWebContext.getApplicationContext();
    }

    /**
     * Try to get the messages from Spring first or else retrieve from the default messages.
     */
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * Try to get the messages from Spring first or else retrieve from the default messages.
     */
    public String getMessage(String code, Object[] args) {
        if (messageSource == null) {
            logger.warn("There is no Spring MessageSource defined. Will get the default messages instead.");
            return defaultMessages.getMessage(code, args);
        }
        String message = null;
        try {
            message = messageSource.getMessage(code, args, springWebContext.getLocale());
        } catch (NoSuchMessageException ex) {
            message = defaultMessages.getMessage(code, args);
        }
        return message;
    }
}
