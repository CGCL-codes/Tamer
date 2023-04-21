package com.i3sp.jboss;

import org.mortbay.util.Code;
import java.io.File;
import javax.management.*;
import org.jboss.logging.Log;
import org.jboss.util.ServiceMBeanSupport;

public class OpenLdapService extends ServiceMBeanSupport implements OpenLdapServiceMBean {

    Log log = Log.createLog(getName());

    private String configFile;

    private String url;

    private String runningConfigFile;

    private String runningURL;

    private String slapd;

    private Process slapdProcess = null;

    private int startWaitTime = 1000;

    public OpenLdapService() {
        Code.debug("Created OpenLdapService");
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile_) {
        configFile = configFile_;
    }

    public String getRunningConfigFile() {
        return runningConfigFile;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url_) {
        url = url_;
    }

    public String getRunningURL() {
        return runningURL;
    }

    public String getSlapd() {
        return slapd;
    }

    public void setSlapd(String slapd_) {
        slapd = slapd_;
    }

    public int getStartWaitTime() {
        return startWaitTime;
    }

    public void setStartWaitTime(int startWaitTime_) {
        startWaitTime = startWaitTime_;
    }

    public boolean isRunning() {
        if (slapdProcess == null) return false;
        try {
            slapdProcess.exitValue();
        } catch (IllegalThreadStateException ex) {
            return true;
        }
        return false;
    }

    public ObjectName getObjectName(MBeanServer server, ObjectName name) throws javax.management.MalformedObjectNameException {
        return new ObjectName(OBJECT_NAME);
    }

    public String getName() {
        return "OpenLdapServer";
    }

    public void startService() throws Exception {
        if (url == null) throw new Exception("Must initialise URL attribute");
        if (configFile == null) throw new Exception("Must initialise ConfigFile attribute");
        File cfg = new File(configFile);
        if (!cfg.canRead()) throw new Exception("ConfigFile must be readable");
        if (slapd == null) throw new Exception("Must initialise Slapd attribute");
        String cmd = slapd + " -d 0 -s 0 -f " + configFile + " -h " + url;
        log.log("Starting:" + cmd);
        try {
            slapdProcess = Runtime.getRuntime().exec(cmd);
            runningURL = url;
            runningConfigFile = configFile;
            Thread.sleep(startWaitTime);
        } catch (Exception ex) {
            log.log("Error starting openldap:" + ex.getMessage());
            ex.printStackTrace(System.err);
            throw ex;
        }
    }

    public void stopService() {
        slapdProcess.destroy();
        try {
            slapdProcess.waitFor();
        } catch (InterruptedException ex) {
        }
    }
}
