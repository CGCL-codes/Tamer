package org.beanlet;

/**
 * Thrown if an instance for the specified beanlet could not be created, or 
 * obtained for some reason.
 *
 * @author Leon van Zantvoort
 */
public class BeanletCreationException extends BeanletException {

    private static final long serialVersionUID = 1183456034632463344L;

    public BeanletCreationException(String beanletName) {
        super(beanletName, "Failed to create beanlet instance.");
    }

    public BeanletCreationException(String beanletName, String message) {
        super(beanletName, "Failed to create beanlet instance. " + message.toString());
    }

    public BeanletCreationException(String beanletName, Throwable cause) {
        super(beanletName, "Failed to create beanlet instance.", cause);
    }

    public BeanletCreationException(String beanletName, String message, Throwable cause) {
        super(beanletName, "Failed to create beanlet instance. " + message.toString(), cause);
    }
}
