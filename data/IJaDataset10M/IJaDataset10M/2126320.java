package org.enerj.apache.commons.beanutils;

/** 
 * Thrown to indicate that the <em>Bean Access Language</em> cannot execute query
 * against given bean since a nested bean referenced is null.
 *
 * @author Robert Burrell Donkin
 * @since 1.7
 */
public class NestedNullException extends BeanAccessLanguageException {

    /** 
     * Constructs a <code>NestedNullException</code> without a detail message.
     */
    public NestedNullException() {
        super();
    }

    /**
     * Constructs a <code>NestedNullException</code> without a detail message.
     * 
     * @param message the detail message explaining this exception
     */
    public NestedNullException(String message) {
        super(message);
    }
}
