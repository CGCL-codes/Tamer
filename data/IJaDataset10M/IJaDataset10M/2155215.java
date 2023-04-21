package org.peaseplate;

/**
 * A resolver works on some specified source, like a file system or a class loader.
 * 
 * It looks for the raw template data and messages. If it found some data it creates a
 * {@link TemplateLocator} or a {@link MessagesLocator} that describes how the 
 * data can be loaded.
 */
public interface Resolver {

    /**
	 * Resolves the specified template and returns a {@link TemplateLocator} if found
	 * or null otherwise.
	 * 
	 * @param engine the Pease Plate engine
	 * @param key the key
	 * @return the template locator or null if not found
	 * @throws TemplateException on occasion
	 */
    public TemplateLocator resolveTemplate(TemplateEngine engine, ResourceKey key) throws TemplateException;

    /**
	 * Resolves the specified messages and returns a {@link MessagesLocator} if found
	 * or null otherwise
	 * 
	 * @param engine the Pease Plate engine
	 * @param key the key
	 * @return the messages locator or null if not found
	 * @throws TemplateException on occasion
	 */
    public MessagesLocator resolveMessages(TemplateEngine engine, ResourceKey key) throws TemplateException;
}
