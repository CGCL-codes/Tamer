package org.blojsom.plugin.common;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * CollectionUtilities plugin
 *
 * @author David Czarnecki
 * @since blojsom 3.0
 * @version $Id: CollectionUtilitiesPlugin.java,v 1.1 2006/03/20 21:30:54 czarneckid Exp $
 */
public class CollectionUtilitiesPlugin implements Plugin {

    private static final String BLOJSOM_PLUGIN_COLLECTION_UTILITIES = "BLOJSOM_PLUGIN_COLLECTION_UTILITIES";

    /**
     * Construct a new instance of the Collection Utilities plugin
     */
    public CollectionUtilitiesPlugin() {
    }

    /**
     * Initialize this plugin. This method only called when the plugin is instantiated.
     *
     * @throws org.blojsom.plugin.PluginException
     *          If there is an error initializing the plugin
     */
    public void init() throws PluginException {
    }

    /**
     * Process the blog entries
     *
     * @param httpServletRequest  Request
     * @param httpServletResponse Response
     * @param blog                {@link Blog} instance
     * @param context             Context
     * @param entries             Blog entries retrieved for the particular request
     * @return Modified set of blog entries
     * @throws PluginException If there is an error processing the blog entries
     */
    public Entry[] process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Blog blog, Map context, Entry[] entries) throws PluginException {
        context.put(BLOJSOM_PLUGIN_COLLECTION_UTILITIES, new CollectionUtilities());
        return entries;
    }

    /**
     * Perform any cleanup for the plugin. Called after {@link #process}.
     *
     * @throws org.blojsom.plugin.PluginException
     *          If there is an error performing cleanup for this plugin
     */
    public void cleanup() throws PluginException {
    }

    /**
     * Called when BlojsomServlet is taken out of service
     *
     * @throws org.blojsom.plugin.PluginException
     *          If there is an error in finalizing this plugin
     */
    public void destroy() throws PluginException {
    }

    /**
     * Utility class for collection utility functions to make available to templates
     */
    public class CollectionUtilities {

        /**
         * Construct a new instance of CollectionUtilities
         */
        public CollectionUtilities() {
        }

        /**
         * Reverse a list
         *
         * @param input List to be reversed
         * @return List in reverse order
         */
        public List reverse(List input) {
            if (input == null || input.size() == 0) {
                return input;
            }
            Collections.reverse(input);
            return input;
        }
    }
}
