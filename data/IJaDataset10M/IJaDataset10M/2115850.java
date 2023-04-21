package main;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * User: wallb
 */
public class UpdateChecker {

    private Frame parentFrame;

    private boolean onlyShowMessageWhenUpdateAvailable;

    private String downloadURL = "http://pixelace.sourceforge.net/download/PixelAce.exe";

    public UpdateChecker(Frame parentFrame, boolean onlyShowMessageWhenUpdateAvailable) {
        this.parentFrame = parentFrame;
        this.onlyShowMessageWhenUpdateAvailable = onlyShowMessageWhenUpdateAvailable;
        String osName = System.getProperty("os.name").toUpperCase();
        if (!osName.startsWith("WINDOWS")) {
            downloadURL = "http://pixelace.sourceforge.net/download/PixelAce_NoInstaller.zip";
        }
    }

    public void checkForUpdate() {
        URL url;
        try {
            url = new URL("http://pixelace.sourceforge.net/updateCheck_PixelAce.html");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            InputStream in = urlConn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String version = reader.readLine();
            String type = reader.readLine();
            if (isProgramUpToDate(version)) {
                if (!onlyShowMessageWhenUpdateAvailable) {
                    JOptionPane.showMessageDialog(parentFrame, "Program is up to date.");
                }
            } else {
                if (type.trim().equalsIgnoreCase("critical")) {
                    int result = JOptionPane.showConfirmDialog(parentFrame, "There is a critical update available.\n Would you like to download the update now?", "Critical update available", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == 0) {
                        downloadUpdate();
                    }
                } else {
                    int result = JOptionPane.showConfirmDialog(parentFrame, "There is an update available.\n Would you like to download the update now?", "Update available", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == 0) {
                        downloadUpdate();
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, "Problem checking for update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isProgramUpToDate(String latestVersionOnline) {
        boolean isUpToDate = false;
        String version = latestVersionOnline.trim();
        String programVersion = ProgramConstants.VERSION;
        while (version.length() < programVersion.length()) {
            version += ".0";
        }
        while (version.length() > programVersion.length()) {
            programVersion += ".0";
        }
        String[] versionArray = version.split("\\.");
        String[] programVersionArray = programVersion.split("\\.");
        for (int i = 0; i < versionArray.length; i++) {
            if (Integer.parseInt(programVersionArray[i]) > Integer.parseInt(versionArray[i])) {
                isUpToDate = true;
                break;
            } else if (Integer.parseInt(programVersionArray[i]) == Integer.parseInt(versionArray[i])) {
                isUpToDate = true;
            } else {
                isUpToDate = false;
                break;
            }
        }
        return isUpToDate;
    }

    private void downloadUpdate() {
        try {
            URI uri = new URI(downloadURL);
            Desktop.getDesktop().browse(new URI(downloadURL));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, "Problem getting download.\nDownload the update manually from http://www.walltechsoftware.com");
            e.printStackTrace();
        }
    }
}
