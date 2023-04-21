package org.nexopenframework.management.module;

/**
 * @author francesc
 *
 */
public class ModuleNotFoundException extends ModuleException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ModuleNotFoundException() {
        super();
    }

    /**
	 * @param message
	 * @param cause
	 */
    public ModuleNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param message
	 */
    public ModuleNotFoundException(final String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public ModuleNotFoundException(final Throwable cause) {
        super(cause);
    }
}
