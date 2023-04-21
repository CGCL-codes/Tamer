package org.ignition.blojsom.plugin.textile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ignition.blojsom.blog.Blog;
import org.ignition.blojsom.blog.BlogEntry;
import org.ignition.blojsom.plugin.BlojsomPlugin;
import org.ignition.blojsom.plugin.BlojsomPluginException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import net.sf.textile4j.Textile;

/**
 * Textile Plugin
 *
 * An implementation of the Textism's Textile. See http://www.textism.com/tools/textile
 *
 * @author Mark Lussier
 * @since blojsom 1.9
 * @version $Id: TextilePlugin.java,v 1.7 2003-06-11 00:11:27 intabulas Exp $
 */
public class TextilePlugin implements BlojsomPlugin {

    /**
     * MetaData Key to identify a Textile post
     */
    public static final String METADATA_ISTEXTILE = "TEXTILE";

    /**
     * Extension of Textile Post
     */
    public static final String TEXTILE_EXTENSION = ".textile";

    /**
     * Textile Instance
     */
    private Textile _textile;

    /**
     * Logger instance
     */
    private Log _logger = LogFactory.getLog(TextilePlugin.class);

    /**
     * Initialize this plugin. This method only called when the plugin is instantiated.
     *
     * @param servletConfig Servlet config object for the plugin to retrieve any initialization parameters
     * @param blog {@link Blog} instance
     * @throws BlojsomPluginException If there is an error initializing the plugin
     */
    public void init(ServletConfig servletConfig, Blog blog) throws BlojsomPluginException {
        _textile = new Textile();
    }

    /**
     * Process the blog entries
     *
     * @param httpServletRequest Request
     * @param httpServletResponse Response
     * @param context Context
     * @param entries Blog entries retrieved for the particular request
     * @return Modified set of blog entries
     * @throws BlojsomPluginException If there is an error processing the blog entries
     */
    public BlogEntry[] process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Map context, BlogEntry[] entries) throws BlojsomPluginException {
        for (int x = 0; x < entries.length; x++) {
            BlogEntry entry = entries[x];
            if (entry.getPermalink().endsWith(TEXTILE_EXTENSION)) {
                _logger.debug("Textile Processing: " + entry.getTitle());
                entry.setDescription(_textile.process(entry.getDescription()));
            }
        }
        return entries;
    }

    /**
     * Perform any cleanup for the plugin. Called after {@link #process}.
     *
     * @throws BlojsomPluginException If there is an error performing cleanup for this plugin
     */
    public void cleanup() throws BlojsomPluginException {
    }

    /**
     * Called when BlojsomServlet is taken out of service
     *
     * @throws BlojsomPluginException If there is an error in finalizing this plugin
     */
    public void destroy() throws BlojsomPluginException {
    }
}
