package net.sf.xpontus.plugins;

import org.java.plugin.Plugin;

/**
 * XPontus plugins have all a  method called init
 * @author Yves Zoundi
 */
public abstract class XPontusPlugin extends Plugin {

    /**
     * Default constructor nothing to do for now
     */
    public XPontusPlugin() {
    }

    /**
     * <p>Initialize the plugin. This method exists because the main
     * window must be created before anything happens</p>
     *
     * @throws java.lang.Exception An exception
     */
    public abstract void init() throws Exception;
}
