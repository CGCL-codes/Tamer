package com.hyk.proxy.framework.admin;

import java.io.Console;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.hyk.proxy.framework.Framework;
import com.hyk.proxy.framework.common.Constants;
import com.hyk.proxy.framework.common.Version;
import com.hyk.proxy.framework.management.UDPManagementServer;
import com.hyk.proxy.framework.plugin.Plugin;
import com.hyk.proxy.framework.plugin.PluginManager;
import com.hyk.proxy.framework.plugin.PluginManager.InstalledPlugin;
import com.hyk.proxy.framework.prefs.Preferences;
import com.hyk.proxy.framework.shell.tui.TUITrace;

/**
 *
 */
public class Admin {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Preferences.init();
        TUITrace trace = new TUITrace();
        PluginManager pm = PluginManager.getInstance();
        pm.loadPlugins(trace);
        Collection<InstalledPlugin> plugins = pm.getAllInstalledPlugins();
        Console console = System.console();
        while (true) {
            System.out.println("==============Framework&Plugins===========");
            System.out.println("[1] Framework V" + Version.value);
            int i = 2;
            Map<Integer, InstalledPlugin> table = new HashMap<Integer, InstalledPlugin>();
            for (InstalledPlugin plugin : plugins) {
                table.put(i, plugin);
                System.out.println("[" + i + "] " + plugin.desc.name + " V" + plugin.desc.version);
                i++;
            }
            System.out.println("[" + i + "] Stop hyk-proxy V" + Version.value);
            System.out.println("[0] Exit");
            System.out.print("Please enter 0-" + i + ":");
            String s = console.readLine();
            try {
                int choice = Integer.parseInt(s);
                if (choice >= 0 && choice <= i) {
                    if (choice == 0) {
                        System.exit(1);
                    }
                    if (choice == 1) {
                        System.out.println("Not support in framework now.");
                        continue;
                    }
                    if (choice == i) {
                        UDPManagementServer.sendUDPCommand(Constants.FRAMEWORK_NAME, Constants.STOP_CMD);
                        continue;
                    }
                    InstalledPlugin p = table.get(choice);
                    if (null == p.plugin.getAdmin()) {
                        System.out.println("Not support in plugin:" + p.desc.name + " now.");
                    } else {
                        p.plugin.getAdmin().start();
                    }
                    continue;
                }
            } catch (Exception e) {
            }
            System.err.println("Wrong input:" + s);
        }
    }
}
