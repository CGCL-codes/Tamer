package org.ignition.blojsom.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ignition.blojsom.blog.BlogEntry;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Hyperlink HREFing Plugin
 *
 * @author Mark Lussier
 * @version $Id: HyperlinkURLPlugin.java,v 1.2 2003-03-01 19:29:09 intabulas Exp $
 */
public class HyperlinkURLPlugin implements BlojsomPlugin {

    /**
     * Protocol support for HREF links
     */
    private static final String URL_PROTOCOLS = "((ftp|http|https|gopher|mailto|news|nntp|telnet|wais|file)";

    /**
     * Regular expression to identify URLs in the entry
     */
    private static final String URL_REGEX = "(^|[ t\rn])" + URL_PROTOCOLS + ":[A-Za-z0-9/](([A-Za-z0-9$_.+!*(),;/?:@&~=-])|%[A-Fa-f0-9]{2})+)";

    /**
     * The resulting replace string for formatting the href
     */
    private static final String HREF_EXPRESSION = " <a href=\"$2\" target=\"_blank\">$2</a>";

    private Log _logger = LogFactory.getLog(HyperlinkURLPlugin.class);

    /**
     * Initialize this plugin. This method only called when the plugin is instantiated.
     *
     * @param servletConfig Servlet config object for the plugin to retrieve any initialization parameters
     * @param blogProperties Read-only properties for the Blog
     * @throws BlojsomPluginException If there is an error initializing the plugin
     */
    public void init(ServletConfig servletConfig, HashMap blogProperties) throws BlojsomPluginException {
    }

    /**
     * Process the blog entries
     *
     * @param httpServletRequest Request
     * @param entries Blog entries retrieved for the particular request
     * @return Modified set of blog entries
     * @throws BlojsomPluginException If there is an error processing the blog entries
     */
    public BlogEntry[] process(HttpServletRequest httpServletRequest, BlogEntry[] entries) throws BlojsomPluginException {
        if (entries == null) {
            return null;
        }
        for (int i = 0; i < entries.length; i++) {
            BlogEntry entry = entries[i];
            entry.setDescription(entry.getDescription().replaceAll(URL_REGEX, HREF_EXPRESSION));
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
}
