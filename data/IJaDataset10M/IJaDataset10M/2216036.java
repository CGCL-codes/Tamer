package com.crowdsourcing.framework.context;

/**
 * This interface is used to indicate objects that need to be
 * resolved in some particular context.
 *
 * @link <a href="mailto:chikaiwang@hotmail.com">chikai</a>
 * @version CVS $Revision:  $ $Date:  $
 */
public interface Resolvable {

    /**
     * Resolve a object to a value.
     *
     * @param context the contextwith respect which to resolve
     * @return the resolved object
     * @throws ContextException if an error occurs
     */
    Object resolve(Context context) throws ContextException;
}
