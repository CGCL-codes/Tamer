package org.jpublish.view;

import org.jpublish.JPublishException;

/** Exception which is thrown when an error occurs during view rendering.

    @author Anthony Eden
    @since 2.0
*/
public class ViewRenderException extends JPublishException {

    /** Construct a new ViewRenderException.
    
        @param message The message
    */
    public ViewRenderException(String message) {
        super(message);
    }

    /** Construct a new ViewRenderException.
    
        @param t The nested exception
    */
    public ViewRenderException(Throwable t) {
        super(t);
    }

    /** Construct a new ViewRenderException.
    
        @param message The message
        @param t The nested exception
    */
    public ViewRenderException(String message, Throwable t) {
        super(message, t);
    }
}
