package org.blojsom.plugin.macro;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.util.BlojsomUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Macro Expansion Plugin
 *
 * @author David Czarnecki
 * @author Mark Lussier
 * @since blojsom 3.0
 * @version $Id: MacroExpansionPlugin.java,v 1.1 2006/03/20 21:30:56 czarneckid Exp $
 */
public class MacroExpansionPlugin implements Plugin {

    /**
     * Regular expression to identify macros as $MACRO$ and DOES NOT ignore escaped $'s
     */
    private static final String MACRO_REGEX = "(\\$[^\\$]*\\$)";

    /**
     * Default constructor. Compiles the macro regular expression pattern, $MACRO$
     */
    public MacroExpansionPlugin() {
    }

    /**
     * Initialize this plugin. This method only called when the plugin is instantiated.
     *
     * @throws PluginException If there is an error initializing the plugin
     */
    public void init() throws PluginException {
    }

    /**
     * Expand macro tokens in an entry
     *
     * @param content Entry to process
     * @param macros  Macros to expand in the content
     * @return The macro expanded string
     */
    private String replaceMacros(String content, Map macros) {
        if (BlojsomUtils.checkNullOrBlank(content)) {
            return content;
        }
        Pattern macroPattern = Pattern.compile(MACRO_REGEX);
        Matcher matcher = macroPattern.matcher(content);
        while (matcher.find()) {
            String token = matcher.group();
            String macro = token.substring(1, token.length() - 1);
            if (macros.containsKey(macro)) {
                content = BlojsomUtils.replace(content, token, (String) macros.get(macro));
            }
        }
        return content;
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
        Map macros = MacroExpansionUtilities.readMacros(blog);
        for (int i = 0; i < entries.length; i++) {
            Entry entry = entries[i];
            entry.setTitle(replaceMacros(entry.getTitle(), macros));
            entry.setDescription(replaceMacros(entry.getDescription(), macros));
        }
        return entries;
    }

    /**
     * Perform any cleanup for the plugin. Called after {@link #process}.
     *
     * @throws PluginException If there is an error performing cleanup for this plugin
     */
    public void cleanup() throws PluginException {
    }

    /**
     * Called when BlojsomServlet is taken out of service
     *
     * @throws PluginException If there is an error in finalizing this plugin
     */
    public void destroy() throws PluginException {
    }
}
