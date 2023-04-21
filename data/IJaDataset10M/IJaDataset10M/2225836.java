package com.estontorise.plugin.test;

import java.util.Map;
import com.estontorise.plugin.interfaces.ActionProcessor;
import com.estontorise.plugin.interfaces.Plugin;
import com.estontorise.plugin.interfaces.PluginManager;

public class Plugin2 implements Plugin {

    @Override
    public void init(final PluginManager pm) {
        System.out.println("plugin2 initialized!");
        pm.registerService("testService", new TestService());
        pm.addActionProcessor("test_action", new ActionProcessor() {

            @Override
            public void call(Map<String, Object> context) {
                System.out.println("Before test_hook ...");
                pm.callAction("test_hook", context);
                System.out.println("After test_hook ...");
            }
        });
    }
}
