package com.textflex.txtfl;

import java.io.*;
import org.eclipse.swt.browser.*;

/** Update notification system.
 * Checks for latest version online and notifies user if new
 * version available.  Future version may also automatically
 * perform this update, if possible.
 */
public class Updater implements Runnable {

    private static final String NEWLINE = System.getProperty("line.separator");

    public String version = "";

    public String versionWebStats = "";

    public Browser browser = null;

    /** Creates empty updater.
	 */
    public Updater() {
    }

    /** Starts the updateer.
	 */
    public void startUpdater() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /** Entry point to test the updater.
	 */
    public static void main(String[] args) {
        Updater updater = new Updater();
        updater.startUpdater();
    }

    /** Runs the update mechanism, comparing the latest version
	 * specified online with the current version of the simulator
	 * and the web stats file.
	 * Notifies the user if the simulator version has an update
	 * available.  Future version may also show updates fo teh webstats or
	 * even perform this update automatically.
	 */
    public void run() {
        try {
            BufferedReader reader = LibTxtfl.getWebPage(LibTxtfl.URL_UPDATE);
            if (reader != null) {
                String line = "";
                while ((line = LibTxtfl.getNextLine(reader)) != null) {
                    line = line.trim();
                    String[] splitLine = line.split("=");
                    if (splitLine.length < 2) return;
                    String keyword = splitLine[0].trim();
                    if (keyword.equalsIgnoreCase(LibTxtfl.VERSION)) {
                        setVersion(splitLine[1]);
                    } else if (keyword.equalsIgnoreCase(LibTxtfl.VERSION_WEBSTATS)) {
                        setVersionWebStats(splitLine[1]);
                    }
                }
                txtflUpdate();
            }
        } catch (AbortException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /** Checks for a tXtFL simulator update and notifies the 
	 * user if a new version is available.
	 */
    public void txtflUpdate() {
        if (getVersion().equals("")) return;
        if (LibTxtfl.VERSION_NUM.compareTo(getVersion()) < 0) {
            System.out.println("A new version of the tXtFL simulator is available." + NEWLINE + "Please download the latest version at http://textflex.com/txtfl");
            if (getBrowser() != null) {
                LibTxtfl.openIntegratedBrowserPage(getBrowser(), Txtfl.getManualURLStr() + Txtfl.MANUAL_DIR_UPDATE);
            }
        }
    }

    /** Gets the simulator version number.
	 * Usually the latest version available as reported online.
	 * @return version
	 */
    public String getVersion() {
        return version;
    }

    /** Gets the set web stats version number.
	 * Usually the latest web stats version available as reported online.
	 * @return the web stats version
	 */
    public String getVersionWebStats() {
        return versionWebStats;
    }

    /** Gets the set browser.
	 * This browser is usually used for update notifications.
	 */
    public Browser getBrowser() {
        return browser;
    }

    /** Sets the simulator version number.
	 * Usually the latest version available as reported online.
	 */
    public void setVersion(String aVersion) {
        version = aVersion;
    }

    /** Sets the web stats version number.
	 * Usually the latest web stats version available as reported online.
	 */
    public void setVersionWebStats(String aVersionWebStats) {
        versionWebStats = aVersionWebStats;
    }

    /** Sets the browser for displaying update notifications.
	 * This browser is usually used for update notifications.
	 */
    public void setBrowser(Browser aBrowser) {
        browser = aBrowser;
    }
}
