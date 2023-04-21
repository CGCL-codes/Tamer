package omschaub.azcvsupdater.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import omschaub.azcvsupdater.main.View;
import omschaub.azcvsupdater.utilities.download.AzVerGet;
import omschaub.azcvsupdater.utilities.download.AzchangelogGet;
import omschaub.azcvsupdater.utilities.download.PluginCacheGet;
import org.eclipse.swt.widgets.List;
import org.gudy.azureus2.plugins.PluginInterface;

public class DownloaderGeneric {

    public static void plugin_Downloader() {
        Thread downloadThread = new Thread() {

            public void run() {
                String version_plugin = AZCVSUpdaterVersionUtils.readAZCVSUPversion(View.getPluginInterface());
                String sourceDir = View.getPluginInterface().getPluginDirectoryName() + System.getProperty("file.separator");
                PluginCacheGet plugincache_get = new PluginCacheGet();
                plugincache_get.setURL("http://prdownloads.sourceforge.net/azcvsupdater/azcvsupdater_" + version_plugin + ".jar?downloads");
                plugincache_get.setDir(sourceDir);
                plugincache_get.setFileName("plugin.cache");
                plugincache_get.initialize();
                plugincache_get.start();
            }
        };
        downloadThread.setDaemon(true);
        downloadThread.start();
    }

    public static void azcvsChangeLog_Downloader() {
        Thread downloadThread = new Thread() {

            public void run() {
                AzchangelogGet azclogget = new AzchangelogGet();
                azclogget.setURL("http://azcvsupdater.sourceforge.net/azcvsupdater/Changelog.txt");
                azclogget.setDir(View.getPluginInterface().getPluginDirectoryName() + System.getProperty("file.separator"));
                azclogget.setFileName("azcvsupdater_changelog.cache");
                azclogget.initialize();
                azclogget.start();
            }
        };
        downloadThread.setDaemon(true);
        downloadThread.start();
    }

    public static void azcvsChangelogReader(final PluginInterface pluginInterface, final List changelogList) {
        if (View.getDisplay() == null && View.getDisplay().isDisposed()) return;
        View.getDisplay().asyncExec(new Runnable() {

            public void run() {
                try {
                    if (changelogList != null && !changelogList.isDisposed()) changelogList.removeAll();
                    String sourceDir = pluginInterface.getPluginDirectoryName() + System.getProperty("file.separator");
                    String sourceFile = "azcvsupdater_changelog.cache";
                    File infile = new File(sourceDir + sourceFile);
                    if (!infile.exists()) infile.createNewFile();
                    if (changelogList != null && !changelogList.isDisposed()) changelogList.removeAll();
                    BufferedReader in = new BufferedReader(new FileReader(infile));
                    String line_temp;
                    while ((line_temp = in.readLine()) != null) {
                        if (changelogList != null && !changelogList.isDisposed()) changelogList.add(line_temp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void azVer_Downloader() {
        Thread downloadThread = new Thread() {

            public void run() {
                AzVerGet azverget = new AzVerGet();
                azverget.setURL("http://azcvsupdater.sourceforge.net/azcvsupdater/current_version.txt");
                azverget.setDir(View.getPluginInterface().getPluginDirectoryName() + System.getProperty("file.separator"));
                azverget.setFileName("azcvsupdater_version.cache");
                azverget.initialize();
                azverget.start();
            }
        };
        downloadThread.setDaemon(true);
        downloadThread.start();
    }
}
