package org.argouml.application.security;

/** Argo Awt Exception handler.
 *
 *  This allows us to manage and hide exceptions which occur
 *  in the AWT event queue.
 *  Refer to {@link java.awt.EventDispatchThread} for details.
 * 
 *  @author Thierry Lach
 *  @since 0.9.4
 */
public final class ArgoAwtExceptionHandler {

    public ArgoAwtExceptionHandler() {
    }

    /** Called from within {@link java.awt.EventDispatchThread}
     *  when an unhandled exception occurs in the Awt event queue.
     *
     *  @param t The uncaught exception.
     *
     *  @throws Throwable to repost the exception if we do not want
     *                    to "eat" it.
     */
    public void handle(Throwable t) throws Throwable {
        if (t.getClass().equals(org.argouml.application.security.ArgoSecurityException.class)) {
        } else {
            throw t;
        }
    }
}
