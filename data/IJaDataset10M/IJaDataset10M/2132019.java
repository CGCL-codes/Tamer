package com.xohm.upgrades.autoupdate;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;
import com.xohm.base.logging.XohmLogger;
import com.xohm.platform.api.OSAPIFactory;

/**
 * <B>This class handles the software installations. It launches the downloaded
 * software installers based on the operating system. If the file is a zip file then 
 * unzips the file first and then launches the installer.
 * </B><br><br>
 *
 * <font size=-1>Open source WiMAX connection manager<br>
 * � Copyright Sprint Nextel Corp. 2008</font><br><br>
 *
 * @author Sachin Kumar 
 */
public class Installer implements Runnable {

    private String softwareName = null;

    private String filename = null;

    private UpdateProgressListener listener = null;

    /**
	 * Constructs an installer object to install the software using
	 * specified file name
	 * 
	 * @param filename - installer file name
	 */
    public Installer(String softwareName, String filename) {
        this.softwareName = softwareName;
        this.filename = filename;
    }

    /**
	 * This methods adds the listener for the installation progress notifications.
	 * 
	 * @param listener UpdateProgressListener - listener object
	 */
    public void setProgressListener(UpdateProgressListener listener) {
        this.listener = listener;
    }

    /**
	 * This method installs the software based on the operating system.
	 */
    public void run() {
        boolean success = false;
        String installerFile = "";
        try {
            if (filename.endsWith(".zip")) {
                unzipFile(filename);
            }
            String cmd = "";
            if (OSAPIFactory.getConnectionManager().isWindows()) {
                installerFile = cmd = findInstallerFile(".exe").getPath();
            } else if (OSAPIFactory.getConnectionManager().isMac()) {
                installerFile = findInstallerFile(".app").getPath();
                Runtime.getRuntime().exec("chmod -R a+x " + installerFile);
                cmd = "open " + installerFile;
            } else {
                installerFile = findInstallerFile(".pkg").getPath();
                Runtime.getRuntime().exec("chmod -R a+x " + installerFile);
                cmd = "open " + installerFile;
            }
            XohmLogger.debugPrintln("Launching the installer " + cmd);
            Runtime.getRuntime().exec(cmd);
            success = true;
        } catch (Exception ex) {
            XohmLogger.warningPrintln("Software Update Install failed - " + ex.getMessage(), null, null);
        }
        listener.installationStarted(success, softwareName);
    }

    /**
	 * This method finds the file with the specified file type.
	 * 
	 * @param fileType - file type
	 * @return File - File with the specified type.
	 */
    private File findInstallerFile(String fileType) {
        File file = null;
        File downloadFolder = new File(Properties.downloadFolder);
        File[] unzippedFiles = downloadFolder.listFiles();
        for (int i = 0; i < unzippedFiles.length; i++) {
            if (unzippedFiles[i].getPath().endsWith(fileType)) {
                file = unzippedFiles[i];
                break;
            }
        }
        return file;
    }

    /**
	 * This method unzips the specified zip file in the downloads folder.
	 * 
	 * @param filename - zip file name
	 * @return - unzipped file name.
	 */
    private void unzipFile(String filename) {
        File unzipFolder = new File(Properties.downloadFolder);
        try {
            File file = new File(filename);
            if (file.exists()) {
                ZipFile zipFile = new ZipFile(filename);
                Enumeration entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    File entryFile = new File(unzipFolder + File.separator + entry.getName());
                    if (entry.isDirectory()) {
                        if (!entryFile.exists()) entryFile.mkdir();
                        continue;
                    } else {
                        File parent = new File(entryFile.getParent());
                        if (!parent.exists()) parent.mkdirs();
                    }
                    copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(entryFile)));
                }
                zipFile.close();
            }
        } catch (Exception ex) {
            XohmLogger.debugPrintln("Failed to unzip the file " + filename);
            ex.printStackTrace();
        }
    }

    /**
	 * Utility method to copy the stream. Used by the unzipFile method
	 * for exracting the files. 
	 * 
	 * @param in - input stream
	 * @param out - output stream
	 * @throws Exception
	 */
    private void copyInputStream(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
        out.flush();
        out.close();
        in.close();
    }
}
