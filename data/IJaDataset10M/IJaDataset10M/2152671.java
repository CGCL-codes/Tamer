package at.filemonkey.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import at.filemonkey.data.Bookmark;
import at.filemonkey.data.Config;
import at.filemonkey.data.FTPSite;
import at.filemonkey.model.FtpDirectoryNavigator;
import at.filemonkey.model.LocalDirectoryNavigator;
import at.filemonkey.model.transferhandler.FtpTransferHandler;
import at.filemonkey.model.transferhandler.LocalTransferHandler;
import at.filemonkey.view.AboutWindow;
import at.filemonkey.view.FtpSiteDialog;
import at.filemonkey.view.MonkeyMainView;
import at.filemonkey.view.StatusBar;

/**
 * The main controller class for the project
 * 
 * @author Markus Skergeth
 * 
 */
public class MonkeyMainController {

    private Properties settings;

    private Config config;

    private FtpDirectoryNavigator ftpNavigator;

    private LocalDirectoryNavigator localNavigator;

    private MonkeyMainView mainView;

    private FtpSiteDialog ftpSiteDialog;

    private AboutWindow about;

    public MonkeyMainController(String configfile) throws ParserConfigurationException, SAXException, IOException {
        config = new Config(configfile);
        ftpNavigator = new FtpDirectoryNavigator();
        localNavigator = new LocalDirectoryNavigator();
        settings = new Properties();
        settings.load(new FileInputStream("conf/settings.properties"));
        about = new AboutWindow(this);
        ftpSiteDialog = new FtpSiteDialog(this);
        mainView = new MonkeyMainView(this);
        mainView.setVisible(true);
    }

    public Config getConfig() {
        return config;
    }

    public AboutWindow getAboutWindow() {
        return about;
    }

    public FtpDirectoryNavigator getFtpNavigator() {
        return ftpNavigator;
    }

    public LocalDirectoryNavigator getLocalNavigator() {
        return localNavigator;
    }

    public TransferHandler getFtpTransferHandler() {
        return new FtpTransferHandler(this);
    }

    public TransferHandler getLocalTransferHandler() {
        return new LocalTransferHandler(this);
    }

    public void showSiteDialog() {
        ftpSiteDialog.setVisible(true);
    }

    public boolean connect(FTPSite site) {
        boolean b = false;
        try {
            b = ftpNavigator.connect(site);
            if (b) {
                setStatus("Successfully connected to: " + site.getName(), StatusBar.SUCCESS);
                mainView.setConnected(site);
            }
        } catch (Exception e) {
            setStatus(e.getClass().getName() + ": " + e.getMessage(), StatusBar.ERROR);
        } finally {
            mainView.updateFilePanels();
        }
        return b;
    }

    public boolean connect(String site, String user, String password, int port) {
        try {
            FTPSite ftpsite = new FTPSite(site, site, port, user, password, "");
            return connect(ftpsite);
        } catch (IllegalArgumentException e) {
            mainView.setStatus("Please fill in all fields before clicking connect", StatusBar.ERROR);
            return false;
        }
    }

    public boolean connect(Bookmark bookmark) {
        FTPSite ftpsite = bookmark.getSite();
        boolean b = connect(ftpsite);
        try {
            ftpNavigator.cd(bookmark.getRemotepath());
            localNavigator.cd(bookmark.getLocalpath());
            mainView.updateFilePanels();
        } catch (IOException e) {
            setStatus(e.getClass().getName() + ": " + e.getMessage(), StatusBar.ERROR);
        }
        return b;
    }

    /**
	 * disconnect current ftp connection
	 */
    public void disconnect() {
        try {
            ftpNavigator.disconnect();
            mainView.updateFilePanels();
            setStatus("Successfully disconnected from FTP", StatusBar.SUCCESS);
        } catch (IOException e) {
            setStatus(e.getClass().getName() + ": " + e.getMessage(), StatusBar.ERROR);
        }
    }

    /**
	 * get a property from settings
	 * 
	 * @param name
	 * @return
	 */
    public String getProperty(String name) {
        return settings.getProperty(name);
    }

    /**
	 * set the firststart property form settings
	 * 
	 * @param value
	 */
    public void disableFirstTimeNote() {
        try {
            settings.setProperty("firsttime", "false");
            File tempfile = new File("conf/settings.properties");
            FileOutputStream fos = new FileOutputStream(tempfile);
            settings.store(fos, null);
        } catch (IOException e) {
            this.setStatus(e.getMessage(), StatusBar.ERROR);
        }
    }

    /**
	 * set a status message in the statusbar
	 * 
	 * @param message
	 * @param textcolor
	 */
    public void setStatus(String message, Color textcolor) {
        mainView.setStatus(message, textcolor);
    }

    public void setStatus(String message) {
        mainView.setStatus(message, StatusBar.MESSAGE);
    }

    public void exit() {
        try {
            if (ftpNavigator.isConnected()) {
                if (JOptionPane.showConfirmDialog(mainView, "A connection is currently active. Do you really want to exit?", "Active Connection", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    config.store();
                    ftpNavigator.disconnect();
                    System.exit(0);
                }
            } else {
                config.store();
                ftpNavigator.disconnect();
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            setStatus("The configuration file could not be saved", StatusBar.ERROR);
        }
    }
}
