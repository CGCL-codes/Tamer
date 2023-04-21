package org.opennms.netmgt.inventory;

/** 
 * <P>This exception is generated when a method needs to lock an
 * element, but cannot obtain a lock within an acceptable time.
 * </P>
 *
 * @author <A HREF="mailto:justis@opennms.org">Justis Peters</A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS</A>
 *
 */
public class LockUnavailableException extends IllegalStateException {

    /**
	 * Constructs a new exception instance.
	 */
    public LockUnavailableException() {
        super();
    }

    /**
	 * Constructs a new exception instance with the specific message
	 *
	 * @param msg	The exception message.
	 *
	 */
    public LockUnavailableException(String msg) {
        super(msg);
    }
}
