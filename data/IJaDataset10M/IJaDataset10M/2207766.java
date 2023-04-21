package org.jpublish;

/** Exception which is thrown when a particular peice of content can not
    be found.
    
    @author Anthony Eden
    @since 2.0
*/
public class ContentNotFoundException extends JPublishRuntimeException {

    /** Construct a new ContentNotFoundException.
    
        @param message The error message
    */
    public ContentNotFoundException(String message) {
        super(message);
    }

    /** Construct a new ContentNotFoundException.
    
        @param message The error message
        @param t The nested error
    */
    public ContentNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
