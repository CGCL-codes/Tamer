package org.xaware.server.updates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.xaware.shared.util.XAClassLoader;
import org.xaware.shared.util.logging.XAwareLogger;
import java.util.prefs.*;

/**
 * @author tferguson
 *
 */
public class ServerUpdater implements IServerUpdater {

    private Timer timer = new Timer();

    private long period = 1000 * 60 * 60 * 24;

    private String uniqueid = null;

    private String version = null;

    private XAwareLogger logger;

    private String className = ServerUpdater.class.getName();

    private ClassLoader dynamicCl;

    private Collection<ServerUpdaterTask> updaters = null;

    private List<ServerUpdaterTask> scheduledUpdaters = new ArrayList<ServerUpdaterTask>();

    public ServerUpdater() {
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void setUpdaters(Collection<ServerUpdaterTask> updaters) {
        this.updaters = updaters;
    }

    public void initialize() {
        logger = XAwareLogger.getXAwareLogger(className);
        logger.entering(className, "initialize");
        uniqueid = this.getUniqueId();
        version = this.getVersion();
        dynamicCl = XAClassLoader.getInstall4jClassLoader(this.getClass().getClassLoader());
        logger.exiting(className, "initialize");
    }

    public void start() {
        logger.entering(className, "start");
        int i = 1;
        for (ServerUpdaterTask task : updaters) {
            task.setController(this);
            if (task.isActive()) {
                this.addUpdater(task, i * 60000);
                i++;
            }
        }
        logger.exiting(className, "start");
    }

    public void scheduleUpdater(ServerUpdaterTask task) {
        if (!scheduledUpdaters.contains(task)) {
            this.addUpdater(task, 0);
        }
    }

    public void unscheduleUpdater(ServerUpdaterTask task) {
    }

    private void addUpdater(ServerUpdaterTask task, long offset) {
        task.setI4jClassLoader(dynamicCl);
        task.setUniqueid(uniqueid);
        task.setVersion(version);
        try {
            timer.schedule(task, offset, period);
            scheduledUpdaters.add(task);
        } catch (IllegalStateException e) {
            logger.info("Unable to schedule the task for auto-updates: " + e.getMessage(), className, "addUpdater");
        }
    }

    /**
     * Get the uniqueid stored in the java prefernces by the installer - if it is not there
     * go to the backup file in the install4j directory
     * @return
     */
    private String getUniqueId() {
        Preferences prefs = null;
        String node = "/xaware/installer";
        try {
            if (Preferences.systemRoot().nodeExists(node)) {
                prefs = Preferences.systemRoot().node(node);
            } else if (Preferences.userRoot().nodeExists(node)) {
                prefs = Preferences.systemRoot().node(node);
            }
        } catch (BackingStoreException e1) {
            logger.warning("Unable to get the uniqueid from the java preferences, this was stored there during installation", className, "getUniqueId", e1);
        }
        if (prefs != null) {
            String uid = prefs.get("uid", "");
            if (uid.length() > 0) {
                return uid;
            }
        }
        logger.debug("Unable to locate the uniqueid in the java preferences store", className, "getUniqueId");
        File f = new File(System.getProperty("xaware.home") + File.separator + ".install4j" + File.separator + "uniqueid");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(f));
            return in.readLine();
        } catch (FileNotFoundException e) {
            logger.warning("Unable to get the uniqueid from " + System.getProperty("xaware.home") + File.separator + ".install4j" + File.separator + "uniqueid, this was stored there during installation", className, "getUniqueId", e);
        } catch (IOException e) {
            logger.warning("Unable to get the uniqueid from " + System.getProperty("xaware.home") + File.separator + ".install4j" + File.separator + "uniqueid, this was stored there during installation", className, "getUniqueId", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.debug("Unable to close stream from " + System.getProperty("xaware.home") + File.separator + ".install4j" + File.separator + "uniqueid, this was stored there during installation", className, "getUniqueId", e);
                }
            }
        }
        return null;
    }

    private String getVersion() {
        File versionFile = new File(System.getProperty("xaware.home") + File.separator + "version.properties");
        Properties props = new Properties();
        String xawareHomeVersion = "";
        if (versionFile.exists()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(versionFile);
                props.load(in);
                xawareHomeVersion = props.getProperty("build.version");
            } catch (IOException e) {
                xawareHomeVersion = "";
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                }
            }
        }
        logger.debug("Returning xawareHomeVersion: " + xawareHomeVersion, className, "getVersion");
        return xawareHomeVersion;
    }
}
