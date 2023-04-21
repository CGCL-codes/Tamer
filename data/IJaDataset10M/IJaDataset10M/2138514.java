package com.crowdsourcing.framework.context;

/**
 * This inteface should be implemented by components that need
 * a Context to work. Context contains runtime generated object
 * provided by the Container to this Component.
 *
 * @link <a href="mailto:chikaiwang@hotmail.com">chikai</a>
 * @version CVS $Revision:  $ $Date:  $
 */
public interface Contextualizable {

    /**
     * Pass the Context to the component.
     * This method is called after the Loggable.setLogger() (if present)
     * method and before any other method.
     *
     * @param context the context. Must not be <code>null</code>.
     * @throws ContextException if context is invalid
     */
    void contextualize(Context context) throws ContextException;
}
