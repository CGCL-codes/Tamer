package updater.model;

import static updater.LanguageContent.*;
import static updater.UpdaterConstants.*;
import updater.model.utility.ZipArchiveExtractor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Properties;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.swing.SwingUtilities;
import updater.model.downloader.Downloader;
import updater.model.downloader.VersionChecker;

/**
 * Implements and handles the application flow for the Updater tool.
 * 
 * @author Dominik Schaufelberger <dominik.schaufelberger@web.de>
 */
public class UpdateManager extends Observable {

    private static UpdateManager updateManager = new UpdateManager();

    private VersionChecker versionCheck;

    private String localVersion;

    private String latestVersion;

    private String progressMessage;

    private float updatePercentage;

    private boolean updatable;

    private boolean downloading;

    /**
     * Singleton pattern prevents multiple object creation.
     */
    private UpdateManager() {
        this.localVersion = "";
        this.latestVersion = "";
        this.progressMessage = "";
        this.updatePercentage = 0.0f;
        this.versionCheck = new VersionChecker(CONFIG_FILE_URL, URL_LATEST_VERSION_FILE);
        this.updatable = false;
        this.downloading = false;
    }

    /**
     * Returns the unique instance of this class.
     * Singleton pattern.
     * 
     * @return
     *      unique class instance
     */
    public static UpdateManager getUpdateManager() {
        return updateManager;
    }

    /**
     * Checks whether there is a new version of the cookbook program available on
     * the project web page at {@link http://sourceforge.net/projects/kochbuchsoft/}.
     * 
     * The local version is read from the program config file through a {@code VersionChecker}
     * and the latest version is retrieved from a file, located at projects web page,
     * which is downloaded while checking.
     * 
     * @return
     *      true iff there's a  new version available.
     * @throws MalformedURLException
     *      thrown if the url for the latest version was invalid.
     * @throws FileNotFoundException
     *      thrown if the latest version file couldn't be located.
     * @throws IOException
     *      thrown if an I/O error occurs while retrieving latest version.
     */
    public boolean checkForVersion() throws MalformedURLException, FileNotFoundException, IOException {
        localVersion = versionCheck.retrieveLocalVersion();
        progressUpdate(0.0f, progress_message_download_version, true);
        latestVersion = versionCheck.retrieveLatestVersion();
        progressUpdate(0.0f, PROGRESS_MESSAGE_DIVIDER, false);
        updatable = VersionChecker.isUpdateAvailable(localVersion, latestVersion);
        String message;
        if (updatable) {
            message = progress_message_newVersion;
        } else {
            message = progress_message_newVersion_na;
        }
        progressUpdate(0.0f, message, false);
        progressUpdate(0.0f, PROGRESS_MESSAGE_DIVIDER, false);
        return updatable;
    }

    /**
     * Performs the update.
     * It retrieves the files to update from the version file, unzips them and replaces
     * the local files.
     * 
     */
    public void update() throws FileNotFoundException, MalformedURLException, ZipException, IOException {
        String fileName;
        ZipFile zFile = null;
        ZipArchiveExtractor zipExtractor;
        String[] updatableFileNames = retrieveFilesToUpdate(versionCheck.getLatestVersionFile());
        int length = updatableFileNames.length;
        float percentageIncrease = 100.0f / (length * 2);
        File[] files = new File[length];
        progressUpdate(0.0f, progress_message_download_zip, true);
        zFile = new ZipFile(downloadLatestZip());
        zipExtractor = new ZipArchiveExtractor(zFile);
        progressUpdate(0.0f, PROGRESS_MESSAGE_DIVIDER, false);
        for (int i = 0; i <= length - 1; i++) {
            fileName = updatableFileNames[i];
            progressUpdate(percentageIncrease, progress_message_unzip + fileName + PROGRESS_MESSAGE_RUNNING_SUFFIX, false);
            files[i] = zipExtractor.extractZipEntry(fileName, TMP_FILE_FOLDER);
        }
        progressUpdate(0.0f, PROGRESS_MESSAGE_DIVIDER, false);
        for (File file : files) {
            fileName = file.getName();
            progressUpdate(percentageIncrease, progress_message_replace + fileName + PROGRESS_MESSAGE_RUNNING_SUFFIX, false);
            if (fileName.equals(CONFIG_FILE_NAME)) {
                updateConfig();
            } else {
                updateFile(file);
            }
        }
        progressUpdate(0.0f, PROGRESS_MESSAGE_DIVIDER, false);
        progressUpdate(percentageIncrease, progress_message_finish, false);
        progressUpdate(0.0f, PROGRESS_MESSAGE_DIVIDER, false);
        updatable = false;
    }

    /**
     * Restarts the updated cookbook software.
     * @throws IOException
     * @throws InterruptedException 
     */
    public void restartProgram() throws IOException {
        progressUpdate(0.0f, progress_message_restart, false);
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", COOKBOOK_PROGRAM_PATH);
        builder.start();
    }

    private void updateConfig() throws FileNotFoundException, IOException {
        Properties config = new Properties();
        FileReader reader = null;
        FileWriter writer = null;
        try {
            reader = new FileReader(CONFIG_FILE_URL);
            config.load(reader);
            config.setProperty(PROPERTY_VERSION_KEY, latestVersion);
            writer = new FileWriter(CONFIG_FILE_URL);
            config.store(writer, null);
        } finally {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
        }
    }

    private void updateFile(File file) throws FileNotFoundException, IOException {
        File destFile = new File(file.getPath().replace(URL_UNZIPPED_PREFIX + latestVersion, ""));
        FileChannel in = null;
        FileChannel out = null;
        try {
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
                destFile.createNewFile();
            }
            in = new FileInputStream(file).getChannel();
            out = new FileOutputStream(destFile).getChannel();
            in.transferTo(0, in.size(), out);
        } finally {
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }

    private String[] retrieveFilesToUpdate(File versionFile) throws FileNotFoundException, IOException {
        String input;
        ArrayList<String> files = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(versionFile));
        while ((input = reader.readLine()) != null) {
            if (!input.equals(latestVersion)) {
                files.add(input);
            }
        }
        return files.toArray(new String[0]);
    }

    private File downloadLatestZip() throws MalformedURLException, FileNotFoundException, IOException {
        return Downloader.downloadFileAsTmp(URL_LATEST_VERSION_ZIP_PREFIX + latestVersion + ZIP_FILE_SUFFIX, TMP_FILE_PREFIX);
    }

    private void progressUpdate(final float increase, final String action, final boolean isDownloading) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                updatePercentage += increase;
                progressMessage = action;
                downloading = isDownloading;
                setChanged();
                notifyObservers();
            }
        });
    }

    public String getLocalVersion() {
        return localVersion;
    }

    public String getNewestVersion() {
        return latestVersion;
    }

    public float getUpdatePercentage() {
        return updatePercentage;
    }

    public String getUpdateAction() {
        return progressMessage;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public boolean isDownloading() {
        return downloading;
    }
}
