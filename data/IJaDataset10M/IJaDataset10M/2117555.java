package org.tcpfile.update;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcpfile.fileio.Hasher;
import org.tcpfile.fileio.RFile;
import org.tcpfile.gui.settingsmanager.HookToSetting;
import org.tcpfile.gui.settingsmanager.SettingHookGenerator;
import org.tcpfile.main.EntryPoint;
import org.tcpfile.main.Misc;
import org.tcpfile.net.ByteArray;
import org.tcpfile.update.updatescripts.Versions;
import version.VersionDependant;

public class Updater {

    private static Logger log = LoggerFactory.getLogger(Updater.class);

    @HookToSetting
    static String UpdaterPath;

    @HookToSetting
    static boolean UpdaterIsEnabled;

    @HookToSetting
    static boolean UpdatePlugins;

    static {
        SettingHookGenerator.generateHooks(Updater.class);
    }

    private static String list;

    public static String getConcreteURL() {
        return UpdaterPath + "update" + VersionDependant.getJavaVersion() + "/";
    }

    public static void updateCheck() {
        if (!UpdaterIsEnabled) return;
        list = getList();
        if (list == null) {
            log.info("Update lookup failed");
            return;
        }
        updateUpdater(list);
        String[] bla = list.split(Misc.NEWLINE);
        if (!bla[0].trim().equals(Misc.VERSIONNUMBER)) {
            if (Versions.current.before(new Versions(bla[0].trim()))) {
                if (Misc.askUser("Do you want to update?\nThis will close the program.", "Update?")) update(true);
            }
        }
    }

    public static void update(boolean doexit) {
        if (doexit) Misc.exit(false);
        EntryPoint.runCommand("java", new String[] { "-jar", "updater.jar", "/restart", "/url" + getConcreteURL(), UpdatePlugins ? "/updatePlugins" : "" }, new File("").getAbsolutePath(), 0);
        System.exit(0);
    }

    public static String getList() {
        String out = Misc.downloadpage(getConcreteURL() + "list.txt");
        return out;
    }

    public static void updateUpdater(String list) {
        for (String s : list.replaceAll("\r", "").split("\n")) {
            if (s.startsWith("updater.jar")) {
                String onlinehash = s.trim().replace("updater.jar:", "").trim();
                File updater = new RFile("updater.jar");
                if (!updater.exists()) download();
                String hash = ByteArray.dumpBytes16(Hasher.hashonce(new RFile("updater.jar").getAbsolutePath()));
                if (!hash.equalsIgnoreCase(onlinehash)) {
                    download();
                }
                break;
            }
        }
    }

    private static void download() {
        Misc.downloadToHD(getConcreteURL() + "updater.jar", new RFile("updater.jar").getAbsolutePath());
        log.info("New version of updater downloaded");
    }
}
