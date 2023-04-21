package com.google.inject.tools.ideplugin.intellij;

import com.intellij.openapi.components.ApplicationComponent;

/**
 * Created by IntelliJ IDEA.
 * User: d
 * Date: Sep 21, 2007
 * Time: 8:39:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Plugin implements ApplicationComponent {

    private static IntellijGuicePlugin guicePlugin;

    private static Thread initThread;

    public void initComponent() {
        initThread = new Thread() {

            @Override
            public void run() {
                guicePlugin = new IntellijGuicePlugin();
                initThread = null;
            }
        };
        initThread.start();
    }

    public static IntellijGuicePlugin getGuicePlugin() {
        if (initThread != null) {
            try {
                initThread.join();
            } catch (InterruptedException e) {
            }
        }
        return guicePlugin;
    }

    public void disposeComponent() {
    }

    public String getComponentName() {
        return "com.google.inject.tools.ideplugin.intellij.guiceplugin.plugin";
    }
}
