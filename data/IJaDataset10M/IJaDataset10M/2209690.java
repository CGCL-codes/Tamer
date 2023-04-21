package com.ecyrd.jspwiki.plugin;

import java.util.Map;
import com.ecyrd.jspwiki.WikiContext;

/**
 *  Defines an interface for plugins.  Any instance of a wiki plugin
 *  should implement this interface.
 *
 */
public interface WikiPlugin {

    /**
     *  Name of the default plugin resource bundle.
     */
    static final String CORE_PLUGINS_RESOURCEBUNDLE = "plugin.PluginResources";

    /**
     *  This is the main entry point for any plugin.  The parameters are parsed,
     *  and a special parameter called "_body" signifies the name of the plugin
     *  body, i.e. the part of the plugin that is not a parameter of
     *  the form "key=value".  This has been separated using an empty
     *  line.
     *  <P>
     *  Note that it is preferred that the plugin returns
     *  XHTML-compliant HTML (i.e. close all tags, use &lt;br /&gt;
     *  instead of &lt;br&gt;, etc.
     *
     *  @param context The current WikiContext.
     *  @param params  A Map which contains key-value pairs.  Any
     *                 parameter that the user has specified on the
     *                 wiki page will contain String-String
     *  parameters, but it is possible that at some future date,
     *  JSPWiki will give you other things that are not Strings.
     *
     *  @return HTML, ready to be included into the rendered page.
     *
     *  @throws PluginException In case anything goes wrong.
     */
    public String execute(WikiContext context, Map params) throws PluginException;
}
