package org.alliance.core.settings;

/**
 * Created by IntelliJ IDEA.
 * User: maciek
 * Date: 2008-jun-05
 * Time: 19:03:00
 * To change this template use File | Settings | File Templates.
 */
public class Plugin {

    private String jar, pluginclass;

    public String getJar() {
        return jar;
    }

    public void setJar(String jar) {
        this.jar = jar;
    }

    public String getPluginclass() {
        return pluginclass;
    }

    public void setPluginclass(String pluginclass) {
        this.pluginclass = pluginclass;
    }
}
