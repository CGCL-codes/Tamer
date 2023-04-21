package net.israfil.markup.plugin.test;

import net.israfil.markup.plugin.Plugin;
import org.apache.tapestry.ioc.ServiceBinder;

public class TestPluginModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(Plugin.class, TestPlugin.class);
    }
}
