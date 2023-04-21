package org.ignition.blojsom.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ignition.blojsom.blog.BlogEntry;
import org.ignition.blojsom.util.BlojsomUtils;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Macro Expansion Plugin
 *
 * @author Mark Lussier
 * @version $Id: MacroExpansionPlugin.java,v 1.2 2003-03-01 18:04:32 czarneckid Exp $
 */
public class MacroExpansionPlugin implements BlojsomPlugin {

    private static final String BLOG_MACRO_CONFIGURATION_IP = "plugin-macros-expansion";

    private Log _logger = LogFactory.getLog(MacroExpansionPlugin.class);

    private Map _macros;

    /**
     * Regular expression to identify macros as $MACRO$ and DOES NOT ignore escaped $'s
     */
    private static final String MACRO_REGEX = "(\\$[^\\$]*\\$)";

    private Pattern _macro;

    /**
     * Default constructor. Compiles the macro regular expression pattern, $MACRO$
     */
    public MacroExpansionPlugin() {
        _macro = Pattern.compile(MACRO_REGEX);
    }

    /**
     * Load the macro mappings
     *
     * @param servletConfig Servlet config object for the plugin to retrieve any initialization parameters
     */
    private void loadMacros(ServletConfig servletConfig) throws BlojsomPluginException {
        String macroConfiguration = servletConfig.getInitParameter(BLOG_MACRO_CONFIGURATION_IP);
        Properties macroProperties = new Properties();
        InputStream is = servletConfig.getServletContext().getResourceAsStream(macroConfiguration);
        try {
            macroProperties.load(is);
            Iterator handlerIterator = macroProperties.keySet().iterator();
            while (handlerIterator.hasNext()) {
                String keyword = (String) handlerIterator.next();
                _macros.put(keyword, macroProperties.get(keyword));
                _logger.info("Adding macro [" + keyword + "] with value of [" + macroProperties.get(keyword) + "]");
            }
        } catch (IOException e) {
            throw new BlojsomPluginException(e);
        }
    }

    /**
     * Initialize this plugin. This method only called when the plugin is instantiated.
     *
     * @param servletConfig Servlet config object for the plugin to retrieve any initialization parameters
     * @param blogProperties Read-only properties for the Blog
     * @throws BlojsomPluginException If there is an error initializing the plugin
     */
    public void init(ServletConfig servletConfig, HashMap blogProperties) throws BlojsomPluginException {
        _macros = new HashMap(10);
        loadMacros(servletConfig);
    }

    /**
     * Expand macro tokens in an entry
     *
     * @param content Entry to process
     * @return The macro expanded string
     */
    private String replaceMacros(String content) {
        Matcher _matcher = _macro.matcher(content);
        while (_matcher.find()) {
            String _token = _matcher.group();
            String _macro = _token.substring(1, _token.length() - 1).toLowerCase();
            if (_macros.containsKey(_macro)) {
                content = BlojsomUtils.replace(content, _token, (String) _macros.get(_macro));
            }
        }
        return content;
    }

    /**
     * Process the blog entries. Expands any macros in title and body.
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
            entry.setTitle(replaceMacros(entry.getTitle()));
            entry.setDescription(replaceMacros(entry.getDescription()));
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
