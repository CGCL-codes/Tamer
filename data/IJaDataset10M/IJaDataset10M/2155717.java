package com.sts.webmeet.pluginmanager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.sts.webmeet.api.PluginInfo;
import com.sts.webmeet.api_impl.PluginContextImpl;
import com.sts.webmeet.common.IOUtil;
import com.sts.webmeet.common.ParticipantInfo;
import com.sts.webmeet.content.server.AbstractPluginServer;
import com.sts.webmeet.server.util.ClassLoaderUtil;
import com.sts.webmeet.server.util.FileUtil;
import com.sts.webmeet.server.util.Zip;
import com.sts.webmeet.server.interfaces.*;

public class PluginManager {

    public static final String WEBHUDDLE_PLUGIN_PROPERTIES_BASE_FILE_NAME = "webhuddle-plugin";

    private PluginInfo[] pluginInfos = new PluginInfo[0];

    private File fileDir = new File("plugins");

    private static PluginManager _instance;

    private Hashtable hashClientArchives = new Hashtable();

    private static Logger logger = Logger.getLogger(PluginManager.class);

    public static synchronized PluginManager getInstance() {
        if (null == _instance) {
            _instance = new PluginManager();
            try {
                _instance.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return _instance;
    }

    public PluginInfo[] getPluginInfos() {
        return this.pluginInfos;
    }

    public boolean isPluginClientArchiveRequest(String uri) {
        return findClientArchiveBytes(uri) != null;
    }

    private byte[] findClientArchiveBytes(String uri) {
        Enumeration enumer = this.hashClientArchives.keys();
        while (enumer.hasMoreElements()) {
            String key = (String) enumer.nextElement();
            if (uri.indexOf(key) > -1) {
                return (byte[]) this.hashClientArchives.get(key);
            }
        }
        return null;
    }

    public void writeClientArchive(String uri, java.io.OutputStream os) throws IOException {
        byte[] bytes = findClientArchiveBytes(uri);
        IOUtil.copyStream(new ByteArrayInputStream(bytes), os);
    }

    public String getCommaSeparatedClientJars() {
        String jars = ",";
        String[] clients = getClientBaseNames();
        for (int i = 0; i < clients.length; i++) {
            jars = jars + base2Jar(clients[i]) + ",";
        }
        return jars.substring(0, jars.length() - 1);
    }

    public String getCommaSeparatedClientCabs() {
        String cabs = ",";
        String[] clients = getClientBaseNames();
        for (int i = 0; i < clients.length; i++) {
            cabs = cabs + base2Cab(clients[i]) + ",";
        }
        return cabs.substring(0, cabs.length() - 1);
    }

    private String base2Jar(String base) {
        return base + "-signed.jar";
    }

    private String base2Cab(String base) {
        return base + ".cab";
    }

    private String[] getClientBaseNames() {
        Vector archives = new Vector();
        for (int i = 0; i < this.pluginInfos.length; i++) {
            if (this.pluginInfos[i].getClientBaseName() != null) {
                archives.add(this.pluginInfos[i].getClientBaseName());
            }
        }
        return (String[]) archives.toArray(new String[0]);
    }

    public String getCommaSeparatedClientPackages() {
        String jars = ",";
        for (int i = 0; i < this.pluginInfos.length; i++) {
            if (this.pluginInfos[i].getPluginPackageName() != null) {
                jars = jars + this.pluginInfos[i].getPluginPackageName() + ".client,";
            }
        }
        return jars.substring(0, jars.length() - 1);
    }

    public AbstractPluginServer[] getServerPluginClassInstances() {
        Vector servers = new Vector();
        for (int i = 0; i < this.pluginInfos.length; i++) {
            try {
                AbstractPluginServer server = (AbstractPluginServer) pluginInfos[i].getServerClass().newInstance();
                server.setPluginInfo(this.pluginInfos[i]);
                servers.add(server);
            } catch (Exception e) {
                logger.error("problem instantiating server for " + pluginInfos[i], e);
            }
        }
        return (AbstractPluginServer[]) servers.toArray(new AbstractPluginServer[0]);
    }

    private void persistParticipationSetting(String participationId, String pluginName, String settingName, String value) {
        try {
            logger.info("persisting " + settingName + ": " + value);
            PluginSettingsManagerUtil.getLocalHome().create().setPluginParticipationSetting(participationId, pluginName, settingName, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PluginManager() {
    }

    private void init() throws Exception {
        logger.info(".init");
        File[] pluginArchiveFiles = fileDir.listFiles(FileUtil.FILENAME_FILTER_ZIP);
        if (null == pluginArchiveFiles) {
            logger.info("No plugins found.");
            return;
        }
        List infos = new ArrayList();
        for (int i = 0; i < pluginArchiveFiles.length; i++) {
            PluginInfo info = infoForPlugin(pluginArchiveFiles[i]);
            infos.add(info);
            logger.info(".init: loaded plugin '" + info + "'");
        }
        this.pluginInfos = (PluginInfo[]) infos.toArray(new PluginInfo[0]);
        initClientArchives();
    }

    private void initClientArchives() throws IOException {
        for (int i = 0; i < this.pluginInfos.length; i++) {
            if (this.pluginInfos[i].getClientBaseName() != null) {
                storeBytes(pluginInfos[i], base2Jar(pluginInfos[i].getClientBaseName()));
                storeBytes(pluginInfos[i], base2Cab(pluginInfos[i].getClientBaseName()));
            }
        }
    }

    private void storeBytes(PluginInfo info, String file) throws IOException {
        logger.info("storing bytes for '" + file + "' in " + info.getBaseDir().getAbsolutePath());
        File archive = new File(info.getBaseDir(), file);
        FileInputStream fis = new FileInputStream(archive);
        byte[] archiveBytes = IOUtil.stream2bytes(fis);
        fis.close();
        logger.info("putting " + archiveBytes.length + " bytes for " + file);
        this.hashClientArchives.put(file, archiveBytes);
    }

    private PluginInfo infoForPlugin(File filePlugin) throws Exception {
        File filePluginSubdir = new File(filePlugin.getAbsolutePath() + ".d/");
        filePluginSubdir.mkdir();
        logger.info(".infoForPlugin created dir: " + filePluginSubdir.getAbsolutePath());
        Zip.unzip(filePlugin.getAbsolutePath(), filePluginSubdir.getAbsolutePath());
        ClassLoader loader = ClassLoaderUtil.classLoaderForBaseDir(filePluginSubdir, this.getClass().getClassLoader());
        ResourceBundle bundle = ResourceBundle.getBundle(WEBHUDDLE_PLUGIN_PROPERTIES_BASE_FILE_NAME, new Locale("en"), loader);
        String pluginPackageName = bundle.getString("plugin.package");
        String clientBaseName = bundle.getString("plugin.client.base.name");
        String serverClassName = pluginPackageName + ".server.Server";
        logger.info(".init: trying to load '" + serverClassName + "'");
        Class classServer = loader.loadClass(serverClassName);
        return new PluginInfo(filePluginSubdir, pluginPackageName, classServer, clientBaseName);
    }
}
