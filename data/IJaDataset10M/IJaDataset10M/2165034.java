package org.webical.web.event;

/**
 * Interface for any class that wants to add extensions to a Component
 * @author ivo
 *
 */
public interface ExtensionListener {

    /**
	 * Called when its time to setup the extensions
	 * @param extensionEvent an Event with the source ExtensionHandler
	 * 
	 */
    public void addExtensionsBeforeComponentSetup(ExtensionEvent extensionEvent);

    /**
	 * Called when its time to setup the extensions
	 * @param extensionEvent an Event with the source ExtensionHandler
	 * 
	 */
    public void addExtensionsAfterComponentSetup(ExtensionEvent extensionEvent);
}
