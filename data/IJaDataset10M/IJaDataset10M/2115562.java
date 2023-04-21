package com.monad.homerun.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Dictionary;
import java.util.Properties;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;
import org.osgi.framework.BundleEvent;
import com.monad.homerun.config.ConfigService;
import com.monad.homerun.core.GlobalProps;
import com.monad.homerun.repo.RepositoryAdmin;
import com.monad.homerun.repo.impl.PackageManager;

/**
 * OSGi Activator class
 */
public class Activator implements BundleActivator, BundleListener {

    private static BundleContext bc;

    private static ConfigService cfgSvc = null;

    private static RepositoryAdmin repoSvc = null;

    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        bc = context;
        cfgSvc = new XmlConfigService();
        context.registerService(ConfigService.class.getName(), cfgSvc, new Hashtable());
        repoSvc = new PackageManager();
        context.registerService(RepositoryAdmin.class.getName(), repoSvc, new Hashtable());
        bc.addBundleListener(this);
    }

    public void stop(BundleContext context) throws Exception {
        bc.removeBundleListener(this);
        bc = null;
    }

    public void bundleChanged(BundleEvent event) {
        int type = event.getType();
        if (GlobalProps.DEBUG) {
            Bundle b = event.getBundle();
            Dictionary dict = b.getHeaders();
            String bName = (String) dict.get("Bundle-Name");
            if (GlobalProps.DEBUG) {
                System.out.println("Bundle Changed - Bundle: " + bName + " type: " + type);
            }
        }
        if (BundleEvent.INSTALLED == type) {
            Bundle b = event.getBundle();
            Dictionary dict = b.getHeaders();
            if (dict.get("Bundle-Activator") == null) {
                installBundle(b);
            }
        } else if (BundleEvent.RESOLVED == type || BundleEvent.STARTED == type) {
            installBundle(event.getBundle());
        }
    }

    private void installBundle(Bundle b) {
        Dictionary dict = b.getHeaders();
        String bName = (String) dict.get("Bundle-Name");
        String version = (String) dict.get("Bundle-Version");
        if (!canInstall(bName, version)) {
            return;
        }
        String confNames = (String) dict.get("Install-Resource");
        if (confNames != null) {
            String instPath = "conf" + File.separator + (String) dict.get("Bundle-Category");
            String confName = "conf/" + bName + ".xml";
            URL resUrl = b.getResource(confName);
            if (resUrl != null) {
                try {
                    installFile(resUrl.openStream(), instPath, bName + ".xml", false);
                } catch (IOException ioe) {
                    if (GlobalProps.DEBUG) {
                        System.out.println("Install failure!");
                        ioe.printStackTrace();
                    }
                }
            }
        }
    }

    public static ConfigService getConfigService() {
        return cfgSvc;
    }

    private boolean canInstall(String name, String version) {
        if (GlobalProps.DEBUG) {
            System.out.println("CanInstall - testing: " + name);
        }
        String instPath = GlobalProps.getHomeDir() + File.separator + "conf" + File.separator + "installed";
        Properties instProps = new Properties();
        try {
            File instFile = new File(instPath);
            if (instFile.exists()) {
                instProps.load(new FileInputStream(instFile));
            } else {
                instFile.createNewFile();
            }
            if (instProps.containsKey(name)) {
                return false;
            }
            instProps.put(name, version);
            instProps.store(new FileOutputStream(instFile), "Do not edit!");
            return true;
        } catch (Exception e) {
            if (GlobalProps.DEBUG) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void installFile(InputStream in, String instPath, String fileName, boolean update) throws IOException {
        instPath = instPath.replaceAll("/", File.separator);
        String path = GlobalProps.getHomeDir() + File.separator + instPath;
        File instDir = new File(path);
        if (!instDir.isDirectory()) {
            instDir.mkdirs();
        }
        byte[] buf = new byte[1024];
        String inst = path + File.separator + fileName;
        File instFile = new File(inst);
        if (update && instFile.exists()) {
            instFile.delete();
        }
        FileOutputStream fileOut = new FileOutputStream(instFile);
        int read = 0;
        while ((read = in.read(buf)) != -1) {
            fileOut.write(buf, 0, read);
        }
        in.close();
        fileOut.close();
    }
}
