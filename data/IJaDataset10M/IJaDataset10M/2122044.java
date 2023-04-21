package org.timothyb89.jtelirc.plugins.javatest;

import org.timothyb89.jtelirc.plugin.Plugin;
import org.timothyb89.jtelirc.server.Server;

/**
 *
 * @author tim
 */
public class JavaTestPlugin extends Plugin {

    public JavaTestPlugin() {
        System.out.println("[Info] JavaTestPlugin loaded!");
    }

    @Override
    public String getName() {
        return "Java Test Plugin";
    }

    @Override
    public String getDescription() {
        return "A pure-Java plugin for JTelIRC";
    }

    @Override
    public String getURL() {
        return "http://jtelirc.sf.net/";
    }

    @Override
    public void registerListeners(Server server) {
        registerListener(new JavaCommand(server));
    }
}
