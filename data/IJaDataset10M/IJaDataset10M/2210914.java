package org.tm4j.topicmap.index;

/** Indicates that an attempt was made to access an {@link Index} that is not
 * supported by a particular {@link IndexProvider}.
 *
 * @author    <a href="mailto:kal@techquila.com">Kal Ahmed</a>
 * @since     0.7.0
 */
public class UnsupportedIndexException extends Exception {

    /** Creates a new unsupported index exception without a detail message. */
    public UnsupportedIndexException() {
        super();
    }

    /** Creates a new unsupported index exception exception with the specified detail
    * message.
    *
    * @param    msg The detail message to be associated with this
    *               exception.
    */
    public UnsupportedIndexException(String msg) {
        super(msg);
    }
}
