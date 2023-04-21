package com.apelon.apps.dts.usermanager;

import com.apelon.beans.apelapp.ApelApp;
import com.apelon.beans.apelapp.ApelApplication;
import com.apelon.beans.apelconfig.ApelConfig;
import com.apelon.beans.apelres.ApelResourceMgr;
import com.apelon.beans.apelsplash.ApelSplash;
import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.LogConfigLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * <p>Application to manage DTS secure socket server users. </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Apelon, Inc.</p>
 * @author Apelon Inc.
 * @version DTS 3.4.1
 * @since   DTS 3.2.0
 */
public class UserManager implements ApelApplication {

    private ApelApp usermanagerApp = null;

    private ApelConfig usermanagerConfig = null;

    private static final String defUserMgrDir = "admin";

    private static final String defUserMgrLogFileName = "usermanagerlog.xml";

    private static final String defUserMgrCfgFileName = "usermanager.xml";

    public UserManager() {
        try {
            ApelSplash splash = new ApelSplash();
            splash.setImageFile("usermanagersplash.gif");
            splash.displaySplash();
        } catch (Exception ex) {
            Categories.uiView().error("Exception displaying splash screen", ex);
        }
        UserManagerPanel frame = new UserManagerPanel(getApelConfig(), getApelApp());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            logCfgLoader().loadDefault();
            UserManager myUserManager = new UserManager();
            ApelApp apelapp = myUserManager.getApelApp();
            apelapp.setJDBCQueryServer(com.apelon.dts.server.OntylogConceptServer.class);
            apelapp.setApelApplication(myUserManager);
            apelapp.runApp(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private com.apelon.beans.apelapp.ApelApp getApelApp() {
        if (usermanagerApp == null) {
            try {
                usermanagerApp = new com.apelon.beans.apelapp.ApelApp();
                usermanagerApp.setApelConfig(getApelConfig());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return usermanagerApp;
    }

    private com.apelon.beans.apelconfig.ApelConfig getApelConfig() {
        if (usermanagerConfig == null) {
            try {
                usermanagerConfig = new com.apelon.beans.apelconfig.ApelConfig();
                usermanagerConfig.setPassword("");
                usermanagerConfig.setJdbcHost("");
                usermanagerConfig.setSocketHost("");
                usermanagerConfig.setUserName("");
                usermanagerConfig.setAppClass(this.getClass());
                usermanagerConfig.setSocketPort(0);
                usermanagerConfig.setConfigName(getUserMgrCfgFile());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return usermanagerConfig;
    }

    protected static String getUserMgrDir() {
        String appRoot = ApelResourceMgr.getAppRootDir().getAbsolutePath();
        return (appRoot + File.separator + "bin" + File.separator + defUserMgrDir + File.separator);
    }

    protected static String getDefLogFile() {
        return defUserMgrLogFileName;
    }

    protected static LogConfigLoader logCfgLoader() {
        return new LogConfigLoader(getUserMgrDir(), getDefLogFile(), UserManager.class);
    }

    protected static String getUserMgrCfgFile() {
        return (defUserMgrDir + File.separator + defUserMgrCfgFileName);
    }

    private void handleException(java.lang.Throwable ex) {
        Categories.ui().error("Exception in DTSEditor.java", ex);
    }

    /**
     * Implement this method to receive notification that the ApelApp bean is attempting to
     * auto-connect. That way, you are able to disable menu items, set status messages, etc.
     */
    public void autoConnecting() {
    }

    public void autoConnectingFailed() {
    }

    /**
     * Override this method to store any end of app properties, etc. If you return false
     * from this method, the properties will NOT be saved by the ApelApp object.
     */
    public boolean endApplication(WindowEvent e) {
        return true;
    }

    /**
     * Implement this method and return true if it is okay to close the application,
     * or false if not. ApelApp will call this method when it receives the window
     * closing event. Here you can check whether or not there are unsaved edits, etc.
     * that would prevent closing the application.
     */
    public boolean isOkayToClose() {
        return true;
    }

    /**
     * This method essentially becomes the "main" for your application. It is called from
     * the runApp method of ApelApp and is wrapped in a try-catch.  This eliminates the need
     * for you to worry about uncaught exceptions. The ApelApp class will catch these and
     * handle them gracefully.
     */
    public void runApplication(String[] args) throws Exception {
    }
}
