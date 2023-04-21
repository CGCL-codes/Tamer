package pt.igeo.snig.mig.editor.ui.migFrame.updater;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.Attributes;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import fi.mmm.yhteinen.swing.core.YApplicationEvent;
import fi.mmm.yhteinen.swing.core.YController;
import pt.igeo.snig.mig.editor.config.ConfigManager;
import pt.igeo.snig.mig.editor.config.exception.NotRunningFromJarException;
import pt.igeo.snig.mig.editor.constants.Constants;
import pt.igeo.snig.mig.editor.i18n.StringsManager;
import pt.igeo.snig.mig.editor.ui.statusBar.UpdateStatusBarData;
import pt.igeo.snig.mig.editor.ui.statusBar.UpdateStatusBarData.UpdateStatusIconType;

/**
 * Update thread to handle auto-updates.
 * 
 * @author Jos� Pedro Dias
 * @version $Revision: 9654 $
 * @since 1.0
 */
public class Updater extends Thread {

    /** Logger for this class */
    private static Logger logger = Logger.getLogger(Updater.class);

    public void run() {
        try {
            logger.debug("-- UPDATER STARTING --");
            float thisVersion = ConfigManager.getInstance().getLocalVersion();
            logger.debug("This version = " + thisVersion);
            String versionUrl = ConfigManager.getInstance().getVersionURLString();
            float remoteVersion = getRemoteVersion(versionUrl);
            logger.debug("Remote version = " + remoteVersion);
            float minVersion = getRemotePropertyFloat(versionUrl, "Minimum-Supported-Update-Version");
            logger.debug("Minimum version = " + minVersion);
            String md5 = getRemoteProperty(versionUrl, "UpdateData-MD5");
            logger.debug("Update data = " + md5);
            if ((thisVersion < remoteVersion) && (thisVersion < minVersion)) {
                String title = StringsManager.getInstance().getString("unsupportedUpdateTitle");
                String msg = StringsManager.getInstance().getFormattedStringPlain("unsupportedUpdateMsg", Constants.migSourceforgeAddress);
                JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
                throw new Exception();
            } else if (thisVersion < remoteVersion) {
                YController.sendApplicationEvent(new YApplicationEvent(Constants.changeUpdateStatusEvent, new UpdateStatusBarData(Constants.updateStarted, UpdateStatusIconType.DOWNLOADING)));
                logger.debug("remote version " + remoteVersion + " newer than local " + thisVersion);
                logger.debug("Downloading new MIG data from " + ConfigManager.getInstance().getUpdateDataUrlString() + "...");
                downloadFromRemoteSite(getRemoteSize(versionUrl));
                String newMd5 = Md5Util.getMd5FromFile("updateData.jar");
                logger.debug("REMOTE MD5: " + md5);
                logger.debug(" LOCAL MD5: " + newMd5);
                if (!md5.equals(newMd5)) {
                    logger.debug("Digests differ: deleting update data...");
                    new File("updateData.jar").delete();
                    throw new Exception();
                }
                logger.debug("Digest OK");
                logger.debug("Download complete. New version will be used upon restart.");
                YController.sendApplicationEvent(new YApplicationEvent(Constants.changeUpdateStatusEvent, new UpdateStatusBarData(Constants.updateFinished, UpdateStatusIconType.RESTART)));
            } else {
                logger.debug("Mig version up to date (remote=" + remoteVersion + ", local=" + thisVersion + ").");
                YController.sendApplicationEvent(new YApplicationEvent(Constants.changeUpdateStatusEvent, new UpdateStatusBarData(Constants.updateNotNeeded, UpdateStatusIconType.UP_TO_DATE)));
            }
            logger.debug("-- UPDATER FINISHED --");
        } catch (NotRunningFromJarException ex) {
            logger.debug("Update doesn't work when run from jar file.");
            YController.sendApplicationEvent(new YApplicationEvent(Constants.changeUpdateStatusEvent, new UpdateStatusBarData(Constants.updateNotNeeded, UpdateStatusIconType.UP_TO_DATE)));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug("Unable to finish update task.");
            YController.sendApplicationEvent(new YApplicationEvent(Constants.changeUpdateStatusEvent, new UpdateStatusBarData(Constants.updateWentWrong, UpdateStatusIconType.PROBLEM)));
        }
    }

    /**
	 * try to free 100ms for UI update
	 * @param miliS
	 */
    void waitTime(int miliS) {
        try {
            Thread.sleep(miliS);
        } catch (InterruptedException e) {
        }
    }

    /**
	 * 
	 * @param path
	 * @return the version found by inspection in jar file
	 * @throws Exception
	 */
    private static float getRemoteVersion(String path) throws Exception {
        String versionS = getRemoteProperty(path, Attributes.Name.IMPLEMENTATION_VERSION.toString());
        int spaceIndex = versionS.indexOf(' ');
        if (spaceIndex != -1) versionS = versionS.substring(0, spaceIndex);
        return Float.parseFloat(versionS);
    }

    /**
	 * 
	 * @param path
	 * @param property
	 * @return the property float found by inspection in jar file
	 * @throws Exception
	 */
    private static float getRemotePropertyFloat(String path, String property) throws Exception {
        return Float.parseFloat(getRemoteProperty(path, property));
    }

    /**
	 * Get version of jar file.
	 * @param path 
	 * @param property 
	 * 
	 * @return the property found by inspection in jar file
	 * @throws Exception
	 */
    private static String getRemoteProperty(String path, String property) throws Exception {
        URL u = new URL("jar", "", path + "!/");
        JarURLConnection uc = (JarURLConnection) u.openConnection();
        Attributes attr = uc.getMainAttributes();
        if (attr == null) {
            throw new Exception("Unable to read attributes... Corrupted version file??");
        }
        String result = attr.getValue(property);
        return result;
    }

    /**
	 * Return the file size of the remote mig.jar file
	 * @param path
	 * @return the remove mig.jar file's length in bytes
	 * @throws Exception
	 */
    private int getRemoteSize(String path) throws Exception {
        return Integer.parseInt(getRemoteProperty(path, "UpdateData-File-Size"));
    }

    /**
	 * Downloads jar from remote site
	 * @param totalSize the expected number of bytes to obtain the remote mig.jar 
	 * 
	 * @throws Exception 
	 */
    private void downloadFromRemoteSite(int totalSize) throws Exception {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        URL downloadUrl = new URL(ConfigManager.getInstance().getUpdateDataUrlString());
        out = new BufferedOutputStream(new FileOutputStream("updateData.jar"));
        conn = downloadUrl.openConnection();
        in = conn.getInputStream();
        byte[] buffer = new byte[1024];
        int numRead;
        long numWritten = 0;
        int prevPercentage = -1;
        while ((numRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, numRead);
            numWritten += numRead;
            int percentage = (int) (numWritten * 100.0f / totalSize);
            if (percentage != prevPercentage) {
                System.out.println("Bytes: " + numWritten + " / " + totalSize + " (" + percentage + "%)");
                YController.sendApplicationEvent(new YApplicationEvent(Constants.changeUpdateStatusEvent, new UpdateStatusBarData(Constants.updateDownloading, null, percentage)));
                waitTime(5);
            }
        }
        if (in != null) in.close();
        if (out != null) out.close();
    }
}
