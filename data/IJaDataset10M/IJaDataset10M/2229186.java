package core.config;

import gui.MainSystray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import javax.swing.UIManager;

/**
 * @author Glauber
 *
 */
public final class Configuration {

    /**
     * File that will store the configuration on the hard drive
     */
    private static final File CONFIGURATION_FILE = new File(System.getProperty("user.home"), "OpenP2M.cfg");

    /**
     * The only instance of configuration (Singleton)
     */
    private static Configuration configuration;

    private String fileVersion = "17";

    private int numberOfDownloadAttempts;

    private int timeBetweenAttempts;

    private int numberOfAccountsToSimultaneouslyDownload;

    private int numberOfSimultaneousDownloadPerAccount;

    private boolean turnOffAfterDownload;

    private boolean turnOffOnError;

    private boolean closeAfterDownload;

    private boolean beepOnDownloadError;

    private String openFileAfterEachDownload;

    private String openFileAfterAllDownloads;

    private String openFileAfterListing;

    private String proxyHost;

    private int proxyPort;

    private String proxyUsername;

    private String proxyPassword;

    private int timeoutTime;

    private String language;

    private String theme;

    private boolean useSystrayIcon;

    private File lastChoosenFolderMerge;

    private File lastChoosenFolderSplit;

    private File lastChoosenFolderRename;

    private File lastChoosenFolderDownload;

    private File lastChoosenFolderUpload;

    private File lastChoosenFolderCompare;

    private int lastSplitSize;

    private boolean sawHowToHelp;

    private String shortcutLogin;

    private String shortcutEncryptPassword;

    private String shortcutUploadFile;

    private String shortcutExit;

    private String shortcutSplitFile;

    private String shortcutMergeFiles;

    private String shortcutCompareFiles;

    private String shortcutGenerateLogins;

    private String shortcutRenameFiles;

    private String shortcutFavoritesManager;

    private String shortcutFavoritesOpenAll;

    private String shortcutOptions;

    private String shortcutCleanHistory;

    private Configuration() {
        if (CONFIGURATION_FILE.exists() || CONFIGURATION_FILE.canRead()) loadFile(); else loadDefault();
    }

    /**
     * @return a singleton instance of the Configuration.
     */
    public static synchronized Configuration getInstance() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    /**
     * Load the Configuration File from the Hard Disk
     *
     */
    private final void loadFile() {
        FileInputStream in = null;
        try {
            in = new FileInputStream(CONFIGURATION_FILE);
            Properties properties = new Properties();
            properties.load(in);
            this.numberOfDownloadAttempts = Integer.parseInt(properties.getProperty("download.numeroTentativas", "15"));
            this.timeBetweenAttempts = Integer.parseInt(properties.getProperty("download.tempoEntreTentativas", "10"));
            this.numberOfAccountsToSimultaneouslyDownload = Integer.parseInt(properties.getProperty("download.numberOfAccountsToSimultaneouslyDownload", "1"));
            this.numberOfSimultaneousDownloadPerAccount = Integer.parseInt(properties.getProperty("download.numberOfSimultaneousDownloadPerAccount", "1"));
            this.turnOffAfterDownload = properties.getProperty("turnOffAfterDownload", "false").equals("true");
            this.turnOffOnError = properties.getProperty("turnOffOnError", "false").equals("true");
            this.closeAfterDownload = properties.getProperty("closeAfterDownload", "false").equals("true");
            this.beepOnDownloadError = properties.getProperty("beepOnDownloadError", "false").equals("true");
            this.openFileAfterEachDownload = properties.getProperty("openFileAfterEachDownload", "");
            this.openFileAfterAllDownloads = properties.getProperty("openFileAfterAllDownloads", "");
            this.openFileAfterListing = properties.getProperty("openFileAfterListing", "");
            this.proxyHost = properties.getProperty("connection.proxyHost", "");
            this.proxyPort = Integer.parseInt(properties.getProperty("connection.proxyPort", "0"));
            this.proxyUsername = properties.getProperty("connection.proxyUsername", "");
            this.proxyPassword = properties.getProperty("connection.proxyPassword", "");
            this.timeoutTime = Integer.parseInt(properties.getProperty("connection.timeoutTime", "30"));
            this.language = properties.getProperty("geral.language", Locale.getDefault().getLanguage());
            this.theme = properties.getProperty("geral.theme", UIManager.getLookAndFeel().getClass().getName());
            this.useSystrayIcon = properties.getProperty("useSystrayIcon", "true").equals("true");
            this.lastChoosenFolderMerge = new File(properties.getProperty("folder.lastChoosenFolderMerge", "."));
            if (!lastChoosenFolderMerge.exists()) this.lastChoosenFolderMerge = new File(".");
            this.lastChoosenFolderSplit = new File(properties.getProperty("folder.lastChoosenFolderSplit", "."));
            if (!lastChoosenFolderSplit.exists()) this.lastChoosenFolderSplit = new File(".");
            this.lastChoosenFolderRename = new File(properties.getProperty("folder.lastChoosenFolderRename", "."));
            if (!lastChoosenFolderRename.exists()) this.lastChoosenFolderRename = new File(".");
            this.lastChoosenFolderDownload = new File(properties.getProperty("folder.lastChoosenFolderDownload", "."));
            if (!lastChoosenFolderDownload.exists()) this.lastChoosenFolderDownload = new File(".");
            this.lastChoosenFolderUpload = new File(properties.getProperty("folder.lastChoosenFolderUpload", "."));
            if (!lastChoosenFolderUpload.exists()) this.lastChoosenFolderUpload = new File(".");
            this.lastChoosenFolderCompare = new File(properties.getProperty("folder.lastChoosenFolderCompare", "."));
            if (!lastChoosenFolderCompare.exists()) this.lastChoosenFolderCompare = new File(".");
            this.lastSplitSize = Integer.parseInt(properties.getProperty("lastSplitSize", "1000"));
            this.sawHowToHelp = properties.getProperty("sawHowToHelp", "false").equals("true");
            this.shortcutLogin = properties.getProperty("shortcut.login", "");
            this.shortcutEncryptPassword = properties.getProperty("shortcut.encryptPassword", "");
            this.shortcutUploadFile = properties.getProperty("shortcut.uploadFile", "");
            this.shortcutExit = properties.getProperty("shortcut.exit", "");
            this.shortcutSplitFile = properties.getProperty("shortcut.splitFile", "");
            this.shortcutMergeFiles = properties.getProperty("shortcut.mergeFiles", "");
            this.shortcutCompareFiles = properties.getProperty("shortcut.compareFiles", "");
            this.shortcutGenerateLogins = properties.getProperty("shortcut.generateLogins", "");
            this.shortcutRenameFiles = properties.getProperty("shortcut.renameFiles", "");
            this.shortcutFavoritesManager = properties.getProperty("shortcut.favoritesManager", "");
            this.shortcutFavoritesOpenAll = properties.getProperty("shortcut.favoritesOpenAll", "");
            this.shortcutOptions = properties.getProperty("shortcut.options", "");
            this.shortcutCleanHistory = properties.getProperty("shortcut.cleanHistory", "");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Load the Default Configuration
     *
     */
    private final void loadDefault() {
        this.numberOfDownloadAttempts = 15;
        this.timeBetweenAttempts = 10;
        this.numberOfAccountsToSimultaneouslyDownload = 1;
        this.numberOfSimultaneousDownloadPerAccount = 1;
        this.turnOffAfterDownload = false;
        this.turnOffOnError = false;
        this.closeAfterDownload = false;
        this.beepOnDownloadError = false;
        this.openFileAfterEachDownload = "";
        this.openFileAfterAllDownloads = "";
        this.openFileAfterListing = "";
        this.proxyHost = "";
        this.proxyPort = 0;
        this.proxyUsername = "";
        this.proxyPassword = "";
        this.timeoutTime = 30;
        this.language = Locale.getDefault().getLanguage();
        this.theme = UIManager.getLookAndFeel().getClass().getName();
        this.useSystrayIcon = true;
        this.lastChoosenFolderMerge = new File(".");
        this.lastChoosenFolderSplit = new File(".");
        this.lastChoosenFolderRename = new File(".");
        this.lastChoosenFolderDownload = new File(".");
        this.lastChoosenFolderUpload = new File(".");
        this.lastChoosenFolderCompare = new File(".");
        this.lastSplitSize = 1000;
        this.sawHowToHelp = false;
        this.shortcutLogin = "control L";
        this.shortcutEncryptPassword = "";
        this.shortcutUploadFile = "";
        this.shortcutExit = "";
        this.shortcutSplitFile = "control S";
        this.shortcutMergeFiles = "control M";
        this.shortcutCompareFiles = "";
        this.shortcutGenerateLogins = "";
        this.shortcutRenameFiles = "";
        this.shortcutFavoritesManager = "";
        this.shortcutFavoritesOpenAll = "";
        this.shortcutOptions = "control O";
        this.shortcutCleanHistory = "";
    }

    /**
     * Save the Configuration in the Hard Disk
     *
     */
    public final void saveConfiguration() {
        try {
            Properties properties = new Properties();
            properties.setProperty("versao", configuration.fileVersion);
            properties.setProperty("download.numberOfDownloadAttempts", String.valueOf(configuration.numberOfDownloadAttempts));
            properties.setProperty("download.timeBetweenAttempts", String.valueOf(configuration.timeBetweenAttempts));
            properties.setProperty("download.numberOfAccountsToSimultaneouslyDownload", String.valueOf(configuration.numberOfAccountsToSimultaneouslyDownload));
            properties.setProperty("download.numberOfSimultaneousDownloadPerAccount", String.valueOf(configuration.numberOfSimultaneousDownloadPerAccount));
            properties.setProperty("turnOffAfterDownload", String.valueOf(configuration.turnOffAfterDownload));
            properties.setProperty("turnOffOnError", String.valueOf(configuration.turnOffOnError));
            properties.setProperty("closeAfterDownload", String.valueOf(configuration.closeAfterDownload));
            properties.setProperty("beepOnDownloadError", String.valueOf(configuration.beepOnDownloadError));
            properties.setProperty("openFileAfterEachDownload", String.valueOf(configuration.openFileAfterEachDownload));
            properties.setProperty("openFileAfterAllDownloads", String.valueOf(configuration.openFileAfterAllDownloads));
            properties.setProperty("openFileAfterListing", String.valueOf(configuration.openFileAfterListing));
            properties.setProperty("connection.proxyHost", String.valueOf(configuration.proxyHost));
            properties.setProperty("connection.proxyPort", String.valueOf(configuration.proxyPort));
            properties.setProperty("connection.proxyUsername", String.valueOf(configuration.proxyUsername));
            properties.setProperty("connection.proxyPassword", String.valueOf(configuration.proxyPassword));
            properties.setProperty("connection.timeoutTime", String.valueOf(configuration.timeoutTime));
            properties.setProperty("geral.language", configuration.language);
            properties.setProperty("geral.theme", configuration.theme);
            properties.setProperty("useSystrayIcon", String.valueOf(configuration.useSystrayIcon));
            properties.setProperty("folder.lastChoosenFolderMerge", configuration.lastChoosenFolderMerge.getAbsolutePath());
            properties.setProperty("folder.lastChoosenFolderSplit", configuration.lastChoosenFolderSplit.getAbsolutePath());
            properties.setProperty("folder.lastChoosenFolderRename", configuration.lastChoosenFolderRename.getAbsolutePath());
            properties.setProperty("folder.lastChoosenFolderDownload", configuration.lastChoosenFolderDownload.getAbsolutePath());
            properties.setProperty("folder.lastChoosenFolderUpload", configuration.lastChoosenFolderUpload.getAbsolutePath());
            properties.setProperty("folder.lastChoosenFolderCompare", configuration.lastChoosenFolderCompare.getAbsolutePath());
            properties.setProperty("lastSplitSize", String.valueOf(configuration.getLastSplitSize()));
            properties.setProperty("sawHowToHelp", String.valueOf(configuration.sawHowToHelp));
            properties.setProperty("shortcut.login", this.shortcutLogin);
            properties.setProperty("shortcut.encryptPassword", this.shortcutEncryptPassword);
            properties.setProperty("shortcut.uploadFile", this.shortcutUploadFile);
            properties.setProperty("shortcut.exit", this.shortcutExit);
            properties.setProperty("shortcut.splitFile", this.shortcutSplitFile);
            properties.setProperty("shortcut.mergeFiles", this.shortcutMergeFiles);
            properties.setProperty("shortcut.compareFiles", this.shortcutCompareFiles);
            properties.setProperty("shortcut.generateLogins", this.shortcutGenerateLogins);
            properties.setProperty("shortcut.renameFiles", this.shortcutRenameFiles);
            properties.setProperty("shortcut.favoritesManager", this.shortcutFavoritesManager);
            properties.setProperty("shortcut.favoritesOpenAll", this.shortcutFavoritesOpenAll);
            properties.setProperty("shortcut.options", this.shortcutOptions);
            properties.setProperty("shortcut.cleanHistory", this.shortcutCleanHistory);
            properties.store(new FileOutputStream(CONFIGURATION_FILE), "Configuration File of OpenP2M");
            MainSystray.guiFactory.updateDisplay();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return Returns the lastChoosenFolderMerge.
     */
    public File getLastChoosenFolderMerge() {
        return lastChoosenFolderMerge;
    }

    /**
     * @param lastChoosenFolderMerge The lastChoosenFolderMerge to set.
     */
    public void setLastChoosenFolderMerge(File lastChoosenFolderMerge) {
        this.lastChoosenFolderMerge = lastChoosenFolderMerge;
    }

    /**
     * @return Returns the lastChoosenFolderSplit.
     */
    public File getLastChoosenFolderSplit() {
        return lastChoosenFolderSplit;
    }

    /**
     * @param lastChoosenFolderSplit The lastChoosenFolderSplit to set.
     */
    public void setLastChoosenFolderSplit(File lastChoosenFolderSplit) {
        this.lastChoosenFolderSplit = lastChoosenFolderSplit;
    }

    /**
     * @return Returns the lastChoosenFolderRename.
     */
    public File getLastChoosenFolderRename() {
        return lastChoosenFolderRename;
    }

    /**
     * @param lastChoosenFolderRename The lastChoosenFolderRename to set.
     */
    public void setLastChoosenFolderRename(File lastChoosenFolderRename) {
        this.lastChoosenFolderRename = lastChoosenFolderRename;
    }

    /**
     * @return Returns the lastChoosenFolderDownload.
     */
    public File getLastChoosenFolderDownload() {
        return lastChoosenFolderDownload;
    }

    /**
     * @param lastChoosenFolderDownload The lastChoosenFolderDownload to set.
     */
    public void setLastChoosenFolderDownload(File lastChoosenFolderDownload) {
        this.lastChoosenFolderDownload = lastChoosenFolderDownload;
    }

    /**
     * @return Returns the lastChoosenFolderUpload.
     */
    public File getLastChoosenFolderUpload() {
        return lastChoosenFolderUpload;
    }

    /**
     * @param lastChoosenFolderUpload The lastChoosenFolderUpload to set.
     */
    public void setLastChoosenFolderUpload(File lastChoosenFolderUpload) {
        this.lastChoosenFolderUpload = lastChoosenFolderUpload;
    }

    /**
     * @return Returns the lastChoosenFolderCompare.
     */
    public File getLastChoosenFolderCompare() {
        return lastChoosenFolderCompare;
    }

    /**
     * @param lastChoosenFolderCompare The lastChoosenFolderCompare to set.
     */
    public void setLastChoosenFolderCompare(File lastChoosenFolderCompare) {
        this.lastChoosenFolderCompare = lastChoosenFolderCompare;
    }

    /**
     * @return Returns the beepOnDownloadError.
     */
    public boolean isBeepOnDownloadError() {
        return beepOnDownloadError;
    }

    /**
     * @param beepOnDownloadError The beepOnDownloadError to set.
     */
    public void setBeepOnDownloadError(boolean beepOnDownloadError) {
        this.beepOnDownloadError = beepOnDownloadError;
    }

    /**
     * @return Returns the turnOffOnError.
     */
    public boolean isTurnOffOnError() {
        return turnOffOnError;
    }

    /**
     * @param turnOffOnError The turnOffOnError to set.
     */
    public void setTurnOffOnError(boolean turnOffOnError) {
        this.turnOffOnError = turnOffOnError;
    }

    /**
     * @return Returns the proxyPassword.
     */
    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * @param proxyPassword The proxyPassword to set.
     */
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    /**
     * @return Returns the proxyUsername.
     */
    public String getProxyUsername() {
        return proxyUsername;
    }

    /**
     * @param proxyUsername The proxyUsername to set.
     */
    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    /**
     * @return Returns the useSystrayIcon.
     */
    public boolean isUseSystrayIcon() {
        return useSystrayIcon;
    }

    /**
     * @param useSystrayIcon The useSystrayIcon to set.
     */
    public void setUseSystrayIcon(boolean useSystrayIcon) {
        this.useSystrayIcon = useSystrayIcon;
    }

    /**
     * @return Returns the theme.
     */
    public String getTheme() {
        return theme;
    }

    /**
     * @param theme The theme to set.
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * @return Returns the openFileAfterListing.
     */
    public String getOpenFileAfterListing() {
        return openFileAfterListing;
    }

    /**
     * @param openFileAfterListing The openFileAfterListing to set.
     */
    public void setOpenFileAfterListing(String openFileAfterListing) {
        this.openFileAfterListing = openFileAfterListing;
    }

    /**
     * @return Returns the openFileAfterEachDownload.
     */
    public String getOpenFileAfterEachDownload() {
        return openFileAfterEachDownload;
    }

    /**
     * @param openFileAfterEachDownload The openFileAfterEachDownload to set.
     */
    public void setOpenFileAfterEachDownload(String openFileAfterEachDownload) {
        this.openFileAfterEachDownload = openFileAfterEachDownload;
    }

    /**
     * @return Returns the openFileAfterAllDownloads.
     */
    public String getOpenFileAfterAllDownloads() {
        return openFileAfterAllDownloads;
    }

    /**
     * @param openFileAfterAllDownloads The openFileAfterAllDownloads to set.
     */
    public void setOpenFileAfterAllDownloads(String openFileAfterAllDownloads) {
        this.openFileAfterAllDownloads = openFileAfterAllDownloads;
    }

    /**
     * @return Returns the turnOffAfterDownload.
     */
    public boolean isTurnOffAfterDownload() {
        return turnOffAfterDownload;
    }

    /**
     * @param turnOffAfterDownload The turnOffAfterDownload to set.
     */
    public void setTurnOffAfterDownload(boolean turnOffAfterDownload) {
        this.turnOffAfterDownload = turnOffAfterDownload;
    }

    /**
     * @return Returns the closeAfterDownload.
     */
    public boolean isCloseAfterDownload() {
        return closeAfterDownload;
    }

    /**
     * @param closeAfterDownload The closeAfterDownload to set.
     */
    public void setCloseAfterDownload(boolean closeAfterDownload) {
        this.closeAfterDownload = closeAfterDownload;
    }

    /**
     * @return Returns the language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language The language to set.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return Returns the numberOfDownloadAttempts.
     */
    public int getNumberOfDownloadAttempts() {
        return numberOfDownloadAttempts;
    }

    /**
     * @param numberOfDownloadAttempts The numberOfDownloadAttempts to set.
     */
    public void setNumberOfDownloadAttempts(int numberOfDownloadAttempts) {
        this.numberOfDownloadAttempts = numberOfDownloadAttempts;
    }

    /**
     * @return Returns the proxyHost.
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * @param proxyHost The proxyHost to set.
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * @return Returns the proxyPort.
     */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * @param proxyPort The proxyPort to set.
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * @return Returns the timeBetweenAttempts.
     */
    public int getTimeBetweenAttempts() {
        return timeBetweenAttempts;
    }

    /**
     * @param timeBetweenAttempts The timeBetweenAttempts to set.
     */
    public void setTimeBetweenAttempts(int timeBetweenAttempts) {
        this.timeBetweenAttempts = timeBetweenAttempts;
    }

    /**
     * @return Returns the timeoutTime.
     */
    public int getTimeoutTime() {
        return timeoutTime;
    }

    /**
     * @param timeoutTime The timeoutTime to set.
     */
    public void setTimeoutTime(int timeoutTime) {
        this.timeoutTime = timeoutTime;
    }

    /**
     * @param sawHowToHelp The sawHowToHelp to set.
     */
    public void setSawHowToHelp(boolean sawHowToHelp) {
        this.sawHowToHelp = sawHowToHelp;
    }

    /**
     * @return Returns the sawHowToHelp.
     */
    public boolean isSawHowToHelp() {
        return sawHowToHelp;
    }

    public String getShortcutLogin() {
        return shortcutLogin;
    }

    public void setShortcutLogin(String shortcutLogin) {
        this.shortcutLogin = shortcutLogin;
    }

    public String getShortcutEncryptPassword() {
        return shortcutEncryptPassword;
    }

    public void setShortcutEncryptPassword(String shortcutEncryptPassword) {
        this.shortcutEncryptPassword = shortcutEncryptPassword;
    }

    public String getShortcutUploadFile() {
        return shortcutUploadFile;
    }

    public void setShortcutUploadFile(String shortcutUploadFile) {
        this.shortcutUploadFile = shortcutUploadFile;
    }

    public String getShortcutExit() {
        return shortcutExit;
    }

    public void setShortcutExit(String shortcutExit) {
        this.shortcutExit = shortcutExit;
    }

    public String getShortcutSplitFile() {
        return shortcutSplitFile;
    }

    public void setShortcutSplitFile(String shortcutSplitFile) {
        this.shortcutSplitFile = shortcutSplitFile;
    }

    public String getShortcutMergeFiles() {
        return shortcutMergeFiles;
    }

    public void setShortcutMergeFiles(String shortcutMergeFiles) {
        this.shortcutMergeFiles = shortcutMergeFiles;
    }

    public String getShortcutCompareFiles() {
        return shortcutCompareFiles;
    }

    public void setShortcutCompareFiles(String shortcutCompareFiles) {
        this.shortcutCompareFiles = shortcutCompareFiles;
    }

    public String getShortcutGenerateLogins() {
        return shortcutGenerateLogins;
    }

    public void setShortcutGenerateLogins(String shortcutGenerateLogins) {
        this.shortcutGenerateLogins = shortcutGenerateLogins;
    }

    public String getShortcutRenameFiles() {
        return shortcutRenameFiles;
    }

    public void setShortcutRenameFiles(String shortcutRenameFiles) {
        this.shortcutRenameFiles = shortcutRenameFiles;
    }

    public String getShortcutFavoritesManager() {
        return shortcutFavoritesManager;
    }

    public void setShortcutFavoritesManager(String shortcutFavoritesManager) {
        this.shortcutFavoritesManager = shortcutFavoritesManager;
    }

    public String getShortcutFavoritesOpenAll() {
        return shortcutFavoritesOpenAll;
    }

    public void setShortcutFavoritesOpenAll(String shortcutFavoritesOpenAll) {
        this.shortcutFavoritesOpenAll = shortcutFavoritesOpenAll;
    }

    public String getShortcutOptions() {
        return shortcutOptions;
    }

    public void setShortcutOptions(String shortcutOptions) {
        this.shortcutOptions = shortcutOptions;
    }

    public String getShortcutCleanHistory() {
        return shortcutCleanHistory;
    }

    public void setShortcutCleanHistory(String shortcutCleanHistory) {
        this.shortcutCleanHistory = shortcutCleanHistory;
    }

    public int getLastSplitSize() {
        return lastSplitSize;
    }

    public void setLastSplitSize(int lastSplitSize) {
        this.lastSplitSize = lastSplitSize;
    }

    /**
     * @return Returns the numberOfSimultaneousDownloadPerAccount.
     */
    public int getNumberOfSimultaneousDownloadPerAccount() {
        return numberOfSimultaneousDownloadPerAccount;
    }

    /**
     * @param numberOfSimultaneousDownloadPerAccount The numberOfSimultaneousDownloadPerAccount to set.
     */
    public void setNumberOfSimultaneousDownloadPerAccount(int numberOfSimultaneousDownloadPerAccount) {
        this.numberOfSimultaneousDownloadPerAccount = numberOfSimultaneousDownloadPerAccount;
    }

    /**
     * @return Returns the numberOfAccountsToSimultaneouslyDownload.
     */
    public int getNumberOfAccountsToSimultaneouslyDownload() {
        return numberOfAccountsToSimultaneouslyDownload;
    }

    /**
     * @param numberOfAccountsToSimultaneouslyDownload The numberOfAccountsToSimultaneouslyDownload to set.
     */
    public void setNumberOfAccountsToSimultaneouslyDownload(int numberOfAccountsToSimultaneouslyDownload) {
        this.numberOfAccountsToSimultaneouslyDownload = numberOfAccountsToSimultaneouslyDownload;
    }
}
