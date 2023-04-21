package org.jomc.model;

/**
 * Object management and configuration model provider interface.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id: ModelProvider.java 1546 2010-03-03 22:19:28Z schulte2005 $
 * @see ModelContext#findModules()
 */
public interface ModelProvider {

    /**
     * Searches a given context for modules.
     *
     * @param context The context to search for modules.
     *
     * @return The modules found in the context or {@code null} if no modules are found.
     *
     * @throws NullPointerException if {@code context} is {@code null}.
     * @throws ModelException if searching the context fails.
     */
    Modules findModules(ModelContext context) throws NullPointerException, ModelException;
}
