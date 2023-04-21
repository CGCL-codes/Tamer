package pt.gotham.gardenia.desktop;

import thinlet.Thinlet;
import thinlet.FrameLauncher;
import org.apache.log4j.Logger;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.awt.FileDialog;
import java.awt.Container;
import java.awt.Frame;
import java.io.File;

/**
 *  GUI for GARDENIA (using http://thinlet.sf.net)
 *
 * @author     appj
 * @created    September 5, 2005
 */
public class WebAppRunnerGui extends Thinlet {

    Object aboutDialog;

    WebAppRunner wAppRunner = null;

    Logger logger = Logger.getLogger(WebAppRunnerGui.class);

    Object backupDialog;

    Object restoreDialog;

    /**
     *  Constructor for the WebAppRunnerGui object
     *
     * @param  wAppRunner1    Description of the Parameter
     * @exception  Exception  Description of the Exception
     */
    public WebAppRunnerGui(WebAppRunner wAppRunner1) throws Exception {
        wAppRunner = wAppRunner1;
        setResourceBundle(wAppRunner.getResourceBundle());
        add(parse("/GardeniaDesktopGUI/main.xml"));
    }

    /**
     *  Creates a frame including this thinlet demo
     *
     * @param  args           The command line arguments
     * @exception  Exception  Description of the Exception
     */
    public static void main(String[] args) throws Exception {
        WebAppRunnerGui gui = new WebAppRunnerGui(null);
        gui.showMainGUI();
    }

    /**  Description of the Method */
    public void showMainGUI() {
        new FrameLauncher("GARDENIA Gestao Empresarial - www.gotham.pt", this, 370, 142);
    }

    /**
     *  Shows the modal About dialog, creates only one dialog instance
     *
     * @exception  Exception  Description of the Exception
     */
    public void actionAboutOpen() throws Exception {
        if (aboutDialog == null) {
            aboutDialog = parse("/GardeniaDesktopGUI/about.xml");
        }
        add(aboutDialog);
    }

    /**  Closes the dialog  */
    public void actionAboutClose() {
        remove(aboutDialog);
    }

    /**  Description of the Method */
    public void actionOpenBrowser() {
        String url = wAppRunner != null ? wAppRunner.getDefaultURL() : "http://localhost:9889/gardenia/";
        logger.debug("Opening URL:" + url);
        BrowserControl.displayURL(url);
    }

    /**  Description of the Method */
    public void actionExitApplication() {
        logger.info("Exiting application... ");
        try {
            if (wAppRunner != null) {
                wAppRunner.stopServers();
            }
        } catch (Exception ex) {
            logger.error("Exception on exiting: " + ex, ex);
        }
        System.exit(0);
    }

    /**
     *  Shows the modal find dialog, creates only one dialog instance
     *
     * @exception  Exception  Description of the Exception
     */
    public void actionBackupOpen() throws Exception {
        if (backupDialog == null) {
            backupDialog = parse("/GardeniaDesktopGUI/backup.xml");
        }
        add(backupDialog);
        String gardeniaHome = System.getProperty("gardenia.home", ".");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String fileName = gardeniaHome + "/backup/gardenia-backup-" + sdf.format(new java.util.Date()) + ".zip";
        setString(find("backupFileName"), "text", fileName);
    }

    public void actionBackupChangeFile() throws Exception {
        String fileName = getFileNameDialog("File to backup:", false);
        if (fileName != null && !fileName.trim().equals("")) {
            setString(find("backupFileName"), "text", fileName);
        }
    }

    /**
     *  Backups the application
     *
     * @exception  Exception  Description of the Exception
     */
    public void actionBackupOk() throws Exception {
        String gardeniaHome = System.getProperty("gardenia.home", ".");
        String bckFile = getString(find("backupFileName"), "text");
        wAppRunner.stopDatabaseServer();
        GardeniaDesktopSetup.backupGardenia(gardeniaHome, bckFile);
        wAppRunner.runDatabaseServer();
        remove(backupDialog);
    }

    /**  Closes the dialog  */
    public void actionBackupClose() {
        remove(backupDialog);
    }

    /**
     *  Shows the modal find dialog, creates only one dialog instance
     *
     * @exception  Exception  Description of the Exception
     */
    public void actionRestoreOpen() throws Exception {
        if (restoreDialog == null) {
            restoreDialog = parse("/GardeniaDesktopGUI/restore.xml");
        }
        add(restoreDialog);
    }

    /**  Closes the dialog  */
    public void actionRestoreClose() {
        remove(restoreDialog);
    }

    public void actionRestoreChangeFile() throws Exception {
        String fileName = getFileNameDialog("Backup File to Restore:", true);
        if (fileName != null && !fileName.trim().equals("")) {
            setString(find("restoreFileName"), "text", fileName);
        }
    }

    /**
     *  Restore a previous backup
     *
     * @exception  Exception  Description of the Exception
     */
    public void actionRestoreOk() throws Exception {
        String gardeniaHome = System.getProperty("gardenia.home", "");
        String rstFile = getString(find("restoreFileName"), "text");
        if (rstFile != null) {
            File rstFile1 = new File(rstFile);
            if (rstFile1.exists()) {
                wAppRunner.stopDatabaseServer();
                GardeniaDesktopSetup.restoreGardenia(gardeniaHome, rstFile);
                wAppRunner.runDatabaseServer();
            }
        }
        remove(restoreDialog);
    }

    protected String getFileNameDialog(String diagTitle, boolean isLoad) throws Exception {
        Container frame = this;
        while (!(frame instanceof Frame)) {
            frame = frame.getParent();
        }
        FileDialog fd = null;
        if (isLoad) fd = new FileDialog((Frame) frame, diagTitle, FileDialog.LOAD); else fd = new FileDialog((Frame) frame, diagTitle, FileDialog.SAVE);
        fd.show();
        return (fd.getFile() == null) ? null : fd.getDirectory() + "/" + fd.getFile();
    }
}
