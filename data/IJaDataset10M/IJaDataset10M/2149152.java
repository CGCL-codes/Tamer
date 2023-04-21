package de.excrawler.server.Plugins;

import org.java.plugin.PluginManager;

/**
 * Plugin functions and the static PluginManagers
 * @author Yves Hoppe <info at yves-hoppe.de>
 * @author Karpouzas George <www.webnetsoft.gr>
 */
public class Plugins {

    public static PluginManager HostPluginManager;

    public static PluginManager WebcrawlerPluginManager;

    public static PluginManager ImagecrawlerPluginManager;

    public static PluginManager BasicPluginManager;

    public static PluginManager MinuteScheduledPluginManager;

    public static PluginManager HourlyScheduledPluginManager;

    public static PluginManager DailyScheduledPluginManager;

    public static PluginManager WeeklyScheduledPluginManager;

    public static void ActivateBasicPlugin(String name) throws Exception {
        BasicPluginManager.activatePlugin(name);
    }

    public static void ActivateWecrawlerPlugin(String name) throws Exception {
        WebcrawlerPluginManager.activatePlugin(name);
    }

    public static void ShutdownBasicPlugin() {
        BasicPluginManager.shutdown();
    }

    public static void ShutdownWebcrawlerPlugin() {
        WebcrawlerPluginManager.shutdown();
    }

    public static void ShutdownHostPlugin() {
        HostPluginManager.shutdown();
    }

    public static void ShutdownImagecrawlerPlugin() {
        ImagecrawlerPluginManager.shutdown();
    }

    public static void ShutdownMinuteSchedPlugin() {
        MinuteScheduledPluginManager.shutdown();
    }

    public static void ShutdownHourlySchedPlugin() {
        HourlyScheduledPluginManager.shutdown();
    }

    public static void ShutdownDailySchedPlugin() {
        DailyScheduledPluginManager.shutdown();
    }

    public static void ShutdownWeeklySchedPlugin() {
        WeeklyScheduledPluginManager.shutdown();
    }

    public static void ShutdownPluginSystem() {
        ShutdownBasicPlugin();
        ShutdownHostPlugin();
        ShutdownWebcrawlerPlugin();
        ShutdownImagecrawlerPlugin();
        ShutdownMinuteSchedPlugin();
        ShutdownHourlySchedPlugin();
        ShutdownDailySchedPlugin();
        ShutdownWeeklySchedPlugin();
    }
}
