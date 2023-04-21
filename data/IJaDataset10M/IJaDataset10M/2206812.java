package br.gov.frameworkdemoiselle.message;

import java.util.Collection;
import java.util.Locale;

/**
 * Message context abstraction.
 * 
 * @see Message
 */
public interface MessageContext {

    /**
	 * Add specified message to the context.
	 * 
	 * @param message
	 * @param args (optional)
	 */
    public void addMessage(Message message, Object... args);

    /**
	 * Returns all messages from context.
	 * 
	 * @return List of message
	 */
    public Collection<Message> getMessages();

    /**
	 * Returns all messages from context and translate them to specified locale.
	 * 
	 * @param locale to translate
	 * @return List of message
	 */
    public Collection<Message> getMessages(Locale locale);

    /**
	 * Remove all messages from context.
	 */
    public void clear();
}
