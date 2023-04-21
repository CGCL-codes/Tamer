package org.simplextensions.registry.exceptions;

/**
 * @author Tomasz Krzyzak, <a
 *         href="mailto:tomasz.krzyzak@gmail.com">tomasz.krzyzak@gmail.com</a>
 * @since 2009-07-30 23:05:26
 * 
 */
public class ExtensionsConfigurationException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -238042436676793960L;

    public ExtensionsConfigurationException(String message) {
        super(message);
    }

    public ExtensionsConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
