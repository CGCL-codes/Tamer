package org.peaseplate.templateengine.messages;

import java.util.Locale;
import org.peaseplate.utils.message.Messages;
import org.peaseplate.utils.resource.ResourceKey;

/**
 * The main services for message bundles
 * 
 * @author Manfred HANTSCHEL
 */
public interface MessagesService {

    /**
	 * Adds a global message bundle with the specified name. The name will be resolved via the resolver service.
	 * 
	 * @param name the name of the bundle
	 * @throws MessagesException if the bundle was not found
	 */
    public void addGlobal(String name) throws MessagesException;

    /**
	 * Adds a global message bundle with the specified name. The name will be resolved via the resolver service. The
	 * local will be ignored!
	 * 
	 * @param key the key of the bundle
	 * @throws MessagesException if the bundle was not found
	 */
    public void addGlobal(ResourceKey key) throws MessagesException;

    /**
	 * Returns the default message bundle for the specified locale
	 * 
	 * @param locale the locale
	 * @return the default message bundle, at least an empty one if not found
	 * @throws MessagesException on occasion
	 */
    public Messages getGlobal(Locale locale) throws MessagesException;

    /**
	 * Returns the message bundle with the specified name.
	 * 
	 * @param name the name of the bundle
	 * @param locale the locale
	 * @return the message bundle, at least an empty one if not found
	 * @throws MessagesException on occasion
	 */
    public Messages get(String name, Locale locale) throws MessagesException;

    /**
	 * Returns the message bundle with the resource key.
	 * 
	 * @param key the key for the bundle
	 * @return the message bundle, at least an empty none if not found
	 * @throws MessagesException on occasion
	 */
    public Messages get(ResourceKey key) throws MessagesException;
}
