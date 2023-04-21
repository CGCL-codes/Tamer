package org.chon.cms.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.common.configuration.ConfigSon;
import org.chon.common.configuration.ConfigurationFactory;
import org.chon.core.common.util.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;

public abstract class JCRAppConfgEnabledActivator extends JCRApplicationActivator {

    private static Log log = LogFactory.getLog(JCRAppConfgEnabledActivator.class);

    protected abstract String getName();

    public ConfigSon getConfigSonService() {
        return ConfigurationFactory.getConfigSonInstance();
    }

    private void copyNonExistingConfigsToDistConfigDir() {
        String __CONFIG = "/__config/";
        @SuppressWarnings("unchecked") Enumeration<URL> configFiles = getBundleContext().getBundle().findEntries(__CONFIG, null, false);
        if (configFiles != null) {
            while (configFiles.hasMoreElements()) {
                URL configFileURL = configFiles.nextElement();
                String fileName = configFileURL.getFile();
                fileName = fileName.substring(__CONFIG.length());
                File cfgFile = getConfigSonService().getFile(fileName);
                if (!cfgFile.exists()) {
                    copyFile(configFileURL, cfgFile);
                }
            }
        }
    }

    private void copyFile(URL from, File to) {
        try {
            InputStream is = from.openStream();
            IOUtils.copy(is, new FileOutputStream(to));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onAppAdded(BundleContext context, JCRApplication app) {
        copyNonExistingConfigsToDistConfigDir();
    }

    @Override
    protected void onAppRemoved(BundleContext context) {
    }

    public JSONObject getConfig() {
        try {
            JSONObject cfgJson = getConfigSonService().getConfig(getName());
            return cfgJson;
        } catch (FileNotFoundException e) {
            log.fatal("Config File for " + getName() + " not found!");
        } catch (JSONException e) {
            log.fatal("Invalid JSON structure for configuration " + getName());
        }
        return null;
    }
}
