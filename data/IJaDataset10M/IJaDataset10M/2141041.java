package com.volantis.mcs.runtime.plugin.markup;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.runtime.plugin.IntegrationPluginManager;

/**
 * MarkupPluginManager is provided to ensure that the IAPI invoke element does
 * not need to be aware of where application, session and "page" scope plugins
 * are stored and how they are managed.
 */
public interface MarkupPluginManager extends IntegrationPluginManager {

    /**
     * Get the {@link MarkupPlugin} defined by the specified attributes.
     *
     * <p>If the named plugin was either not configured, or was configured but
     * an error occurred when instantiating it then an error handling plugin
     * is returned.</p>
     *
     * @param context The MarinerRequestContext from which we will retrieve
     * the appropriate scope to retrieve (or store) the MarkupPlugin.
     * @param pluginName The name of the {@link MarkupPlugin} to return.
     *
     * @return the {@link MarkupPlugin} defined by the specified attributes.
     */
    MarkupPlugin getMarkupPlugin(MarinerRequestContext context, String pluginName);
}
