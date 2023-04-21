package org.compiere.install;

import java.io.*;
import java.net.*;

/**
 * 	Tomcat 5.5.9 Configuration
 *	
 *  @author Jorg Janke
 *  @version $Id: ConfigTomcat.java,v 1.3 2006/07/30 00:57:42 jjanke Exp $
 */
public class ConfigTomcat extends Config {

    /**
	 * 	ConfigJBoss
	 * 	@param data configuration
	 */
    public ConfigTomcat(ConfigurationData data) {
        super(data);
    }

    /**
	 * 	Initialize
	 */
    public void init() {
        p_data.setAppsServerDeployDir(getDeployDir());
        p_data.setAppsServerDeployDir(true);
        p_data.setAppsServerJNPPort("1099");
        p_data.setAppsServerJNPPort(false);
        p_data.setAppsServerWebPort("80");
        p_data.setAppsServerWebPort(true);
        p_data.setAppsServerSSLPort("443");
        p_data.setAppsServerSSLPort(true);
    }

    /**
	 * 	Get Notes
	 *	@return notes
	 */
    public String getNotes() {
        return "Adempiere requires Tomcat 5.5.9" + "\nPlease set the Web Port in $CATALINA_HOME//conf//server.xml" + "\n";
    }

    /**
	 * 	Get Deployment Dir
	 *	@return deployment dir
	 */
    private String getDeployDir() {
        return "C:" + File.separator + "Program Files" + File.separator + "Apache Software Foundation" + File.separator + "Tomcat 5.5";
    }

    /**
	 * 	Test
	 *	@return error message or null if OK
	 */
    public String test() {
        String server = p_data.getAppsServer();
        boolean pass = server != null && server.length() > 0 && server.toLowerCase().indexOf("localhost") == -1 && !server.equals("127.0.0.1");
        InetAddress appsServer = null;
        String error = "Not correct: AppsServer = " + server;
        try {
            if (pass) appsServer = InetAddress.getByName(server);
        } catch (Exception e) {
            error += " - " + e.getMessage();
            pass = false;
        }
        if (getPanel() != null) signalOK(getPanel().okAppsServer, "ErrorAppsServer", pass, true, error);
        if (!pass) return error;
        log.info("OK: AppsServer = " + appsServer);
        setProperty(ConfigurationData.ADEMPIERE_APPS_SERVER, appsServer.getHostName());
        setProperty(ConfigurationData.ADEMPIERE_APPS_TYPE, p_data.getAppsServerType());
        File deploy = new File(p_data.getAppsServerDeployDir());
        pass = deploy.exists();
        error = "CATALINA_HOME Not found: " + deploy;
        if (getPanel() != null) signalOK(getPanel().okDeployDir, "ErrorDeployDir", pass, true, error);
        if (!pass) return error;
        setProperty(ConfigurationData.ADEMPIERE_APPS_DEPLOY, p_data.getAppsServerDeployDir());
        log.info("OK: Deploy Directory = " + deploy);
        String baseDir = p_data.getAppsServerDeployDir();
        if (!baseDir.endsWith(File.separator)) baseDir += File.separator;
        String sharedLib = baseDir + "shared" + File.separator + "lib";
        File sharedLibDir = new File(sharedLib);
        pass = sharedLibDir.exists();
        error = "Not found (shared library): " + sharedLib;
        if (getPanel() != null) signalOK(getPanel().okDeployDir, "ErrorDeployDir", pass, true, error);
        if (!pass) return error;
        String webApps = baseDir + "webapps";
        File webAppsDir = new File(webApps);
        pass = webAppsDir.exists();
        error = "Not found (webapps): " + sharedLib;
        if (getPanel() != null) signalOK(getPanel().okDeployDir, "ErrorDeployDir", pass, true, error);
        if (!pass) return error;
        return null;
    }
}
