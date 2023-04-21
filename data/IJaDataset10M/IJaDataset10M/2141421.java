package org.dwgsoftware.raistlin.composition.model;

import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;

/**
 * Definition of an extension handler that handles the Contextualize
 * stage of a component lifecycle.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 */
public interface ContextualizationHandler {

    /**
     * Handle the contextualization stage of a component lifecycle.
     *
     * @param context the context to apply
     * @param object the object to contextualize
     * @exception ContextException if a contextualization error occurs
     */
    void contextualize(Object object, Context context) throws ContextException;
}
