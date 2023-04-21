package org.meshcms.core;

import java.io.Serializable;
import org.meshcms.util.Path;

/**
 * Manages the configuration parameters of a website.
 */
public class Configuration implements Serializable {

    /**
   * The length of a hour in milliseconds.
   */
    public static final long LENGTH_OF_HOUR = 60 * 60 * 1000;

    /**
   * The length of a day in milliseconds.
   */
    public static final long LENGTH_OF_DAY = 24 * LENGTH_OF_HOUR;

    /**
   * Contains the extensions of files that are visually editable by default.
   */
    public static final String[] DEFAULT_VISUAL_EXTENSIONS = { "html", "htm" };

    /**
   * Value used to disable page caching.
   */
    public static final int NO_CACHE = 0;

    /**
   * Value used to cache pages in memory.
   */
    public static final int IN_MEMORY_CACHE = 1;

    /**
   * Value used to cache pages on disk.
   */
    public static final int ON_DISK_CACHE = 2;

    /**
   * Value used to cache pages on disk and, if possible, in memory.
   */
    public static final int MIXED_CACHE = IN_MEMORY_CACHE | ON_DISK_CACHE;

    public static final int TIDY_NO = 0;

    public static final int TIDY_YES = 1;

    public static final int TIDY_ASK = 2;

    private boolean useAdminTheme;

    private boolean preventHotlinking;

    private boolean alwaysRedirectWelcomes;

    private boolean alwaysDenyDirectoryListings;

    private boolean hideExceptions;

    private boolean editorModulesCollapsed;

    private boolean highQualityThumbnails;

    private boolean replaceThumbnails;

    private boolean exportCheckDates;

    private boolean searchMovedPages;

    private boolean redirectRoot;

    private boolean passwordProtected;

    private int backupLife;

    private int statsLength;

    private int updateInterval;

    private int cacheType;

    private int tidy;

    private int excerptLength;

    private String mailServer;

    private String smtpUsername;

    private String smtpPassword;

    private String siteName;

    private String siteHost;

    private String siteDescription;

    private String siteKeywords;

    private String siteAuthor;

    private String siteAuthorURL;

    private String exportBaseURL;

    private String exportDir;

    private String exportCommand;

    private String[] visualExtensions;

    private Configuration() {
        setUseAdminTheme(true);
        setPreventHotlinking(false);
        setAlwaysRedirectWelcomes(true);
        setAlwaysDenyDirectoryListings(true);
        setHideExceptions(false);
        setEditorModulesCollapsed(false);
        setHighQualityThumbnails(true);
        setReplaceThumbnails(true);
        setExportCheckDates(true);
        setSearchMovedPages(false);
        setRedirectRoot(true);
        setPasswordProtected(false);
        setBackupLife(15);
        setStatsLength(3);
        setUpdateInterval(2);
        setCacheType(NO_CACHE);
        setTidy(TIDY_NO);
        setExcerptLength(400);
        setMailServer("localhost");
        setSmtpUsername("");
        setSmtpPassword("");
        setSiteAuthor("your name");
        setSiteAuthorURL("http://www.yoursite.com/yourpage");
        setSiteDescription("A short description of your site");
        setSiteHost("www.thissite.com");
        setSiteKeywords("your, keywords, here");
        setSiteName("Site Name");
        setExportBaseURL("");
        setExportDir("");
        setExportCommand("");
        setVisualExtensions(DEFAULT_VISUAL_EXTENSIONS);
    }

    /**
   * Returns true if the default MeshCMS theme is always used for the pages
   * of the control panel.
   */
    public boolean isUseAdminTheme() {
        return useAdminTheme;
    }

    /**
   * Sets if the default MeshCMS theme is always used for the pages
   * of the control panel.
   */
    public void setUseAdminTheme(boolean useAdminTheme) {
        this.useAdminTheme = useAdminTheme;
    }

    /**
   * Returns true if the option to prevent hotlinking is enabled.
   */
    public boolean isPreventHotlinking() {
        return preventHotlinking;
    }

    /**
   * Enables or disables hotlinking prevention.
   */
    public void setPreventHotlinking(boolean preventHotlinking) {
        this.preventHotlinking = preventHotlinking;
    }

    /**
   * Returns the minimum time before deleting a backup file,
   * measured in days.
   */
    public int getBackupLife() {
        return backupLife;
    }

    /**
   * Sets the minimum time before deleting a backup file,
   * measured in days.
   */
    public void setBackupLife(int backupLife) {
        this.backupLife = Math.max(backupLife, 0);
    }

    /**
   * Returns the length of stats (hit counts) measured in days.
   */
    public int getStatsLength() {
        return statsLength;
    }

    /**
   * Sets the length of stats (hit counts) measured in days. Please note that
   * this value is fixed when the web application is initialized, so if the
   * value is changed, the new value won't be used until the next restart of the
   * web application.
   */
    public void setStatsLength(int statsLength) {
        this.statsLength = Math.max(statsLength, 1);
    }

    /**
   * Returns the minimum interval between two updates of the site map,
   * measured in hours.
   */
    public int getUpdateInterval() {
        return updateInterval;
    }

    /**
   * Sets the minimum interval between two updates of the site map,
   * measured in hours.
   */
    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = Math.max(updateInterval, 1);
    }

    /**
   * Returns the type of cache to be used for pages.
   *
   * @see #setCacheType
   */
    public int getCacheType() {
        return cacheType;
    }

    /**
   * Sets the type of cache to be used for pages. Possible values are
   * {@link #NO_CACHE}, {@link #IN_MEMORY_CACHE} and {@link #ON_DISK_CACHE}.
   */
    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }

    /**
   * Returns the name of the mail server (SMTP).
   */
    public String getMailServer() {
        return mailServer;
    }

    /**
   * Sets the name of the mail server (SMTP).
   */
    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    /**
   * Returns the SMTP username.
   */
    public String getSmtpUsername() {
        return smtpUsername;
    }

    /**
   * Sets the SMTP username.
   */
    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    /**
   * Returns the SMTP password.
   */
    public String getSmtpPassword() {
        return smtpPassword;
    }

    /**
   * Sets the SMTP password.
   */
    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    /**
   * Loads the configuration from file or creates a new configuration with
   * default values if the file doesn't exist.
   */
    public static Configuration load(WebSite webSite) {
        Configuration c = null;
        Path configFilePath = webSite.getConfigFilePath();
        try {
            c = (Configuration) webSite.loadFromXML(configFilePath);
        } catch (Exception ex) {
        }
        if (c == null) {
            c = new Configuration();
            if (!webSite.getFile(configFilePath).exists()) {
                c.store(webSite);
            }
        }
        return c;
    }

    /**
   * Saves the current configuration to file.
   */
    public boolean store(WebSite webSite) {
        return webSite.storeToXML(this, webSite.getConfigFilePath());
    }

    /**
   * Returns the minimum interval between two updates of the site map,
   * measured in milliseconds.
   */
    public long getUpdateIntervalMillis() {
        return getUpdateInterval() * LENGTH_OF_HOUR;
    }

    /**
   * Returns the minimum time before deleting a backup file,
   * measured in milliseconds.
   */
    public long getBackupLifeMillis() {
        return getBackupLife() * LENGTH_OF_DAY;
    }

    /**
   * Returns the extensions that denote file types that can be edited
   * using the wysiwyg editor.
   */
    public String[] getVisualExtensions() {
        return visualExtensions;
    }

    /**
   * Sets the extensions that denote file types that can be edited
   * using the wysiwyg editor.
   */
    public void setVisualExtensions(String[] visualExtensions) {
        this.visualExtensions = visualExtensions;
    }

    /**
   * Returns the state of the automatic redirection to welcome files.
   */
    public boolean isAlwaysRedirectWelcomes() {
        return alwaysRedirectWelcomes;
    }

    /**
   * Enables or disables automatic redirection to welcome files.
   */
    public void setAlwaysRedirectWelcomes(boolean alwaysRedirectWelcomes) {
        this.alwaysRedirectWelcomes = alwaysRedirectWelcomes;
    }

    /**
   * Returns the state of directory list blocking.
   */
    public boolean isAlwaysDenyDirectoryListings() {
        return alwaysDenyDirectoryListings;
    }

    /**
   * Enables or disables blocking of directory listings.
   */
    public void setAlwaysDenyDirectoryListings(boolean alwaysDenyDirectoryListings) {
        this.alwaysDenyDirectoryListings = alwaysDenyDirectoryListings;
    }

    /**
   * Returns the main host name of this website.
   */
    public String getSiteHost() {
        return siteHost;
    }

    /**
   * Sets the main host name of this website.
   */
    public void setSiteHost(String siteHost) {
        this.siteHost = siteHost;
    }

    /**
   * Returns the website description.
   */
    public String getSiteDescription() {
        return siteDescription;
    }

    /**
   * Sets the website description.
   */
    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }

    /**
   * Returns the keywords related to the website.
   */
    public String getSiteKeywords() {
        return siteKeywords;
    }

    /**
   * Sets the keywords related to the website.
   */
    public void setSiteKeywords(String siteKeywords) {
        this.siteKeywords = siteKeywords;
    }

    /**
   * Returns the author name.
   */
    public String getSiteAuthor() {
        return siteAuthor;
    }

    /**
   * Sets the author name.
   */
    public void setSiteAuthor(String siteAuthor) {
        this.siteAuthor = siteAuthor;
    }

    /**
   * Returns the site name.
   */
    public String getSiteName() {
        return siteName;
    }

    /**
   * Sets the site name.
   */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
   * Returns the author's URL.
   */
    public String getSiteAuthorURL() {
        return siteAuthorURL;
    }

    /**
   * Sets the author's URL. Can be a website URL or a mailto. It is expected
   * to be a full URL.
   */
    public void setSiteAuthorURL(String siteAuthorURL) {
        this.siteAuthorURL = siteAuthorURL;
    }

    /**
   * Returns the state of exception hiding.
   */
    public boolean isHideExceptions() {
        return hideExceptions;
    }

    /**
   * Enables or disables hiding of Java exceptions. If enabled, exception will
   * be catched and not rethrown.
   */
    public void setHideExceptions(boolean hideExceptions) {
        this.hideExceptions = hideExceptions;
    }

    /**
   * Returns the state of whether modules are collapsed in the editor.
   */
    public boolean isEditorModulesCollapsed() {
        return editorModulesCollapsed;
    }

    /**
   * Sets whether modules are collapsed in the editor or not.
   */
    public void setEditorModulesCollapsed(boolean editorModulesCollapsed) {
        this.editorModulesCollapsed = editorModulesCollapsed;
    }

    public boolean isHighQualityThumbnails() {
        return highQualityThumbnails;
    }

    public void setHighQualityThumbnails(boolean highQualityThumbnails) {
        this.highQualityThumbnails = highQualityThumbnails;
    }

    public boolean isExportCheckDates() {
        return exportCheckDates;
    }

    public void setExportCheckDates(boolean exportCheckDates) {
        this.exportCheckDates = exportCheckDates;
    }

    public String getExportBaseURL() {
        return exportBaseURL;
    }

    public void setExportBaseURL(String exportBaseURL) {
        this.exportBaseURL = exportBaseURL;
    }

    public String getExportDir() {
        return exportDir;
    }

    public void setExportDir(String exportDir) {
        this.exportDir = exportDir;
    }

    public String getExportCommand() {
        return exportCommand;
    }

    public void setExportCommand(String exportCommand) {
        this.exportCommand = exportCommand;
    }

    public boolean isSearchMovedPages() {
        return searchMovedPages;
    }

    public void setSearchMovedPages(boolean searchMovedPages) {
        this.searchMovedPages = searchMovedPages;
    }

    public boolean isRedirectRoot() {
        return redirectRoot;
    }

    public void setRedirectRoot(boolean redirectRoot) {
        this.redirectRoot = redirectRoot;
    }

    public boolean isReplaceThumbnails() {
        return replaceThumbnails;
    }

    public void setReplaceThumbnails(boolean replaceThumbnails) {
        this.replaceThumbnails = replaceThumbnails;
    }

    public int getTidy() {
        return tidy;
    }

    public void setTidy(int tidy) {
        this.tidy = tidy;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    /**
   * @return the excerptLength
   */
    public int getExcerptLength() {
        return excerptLength;
    }

    /**
   * @param excerptLength the excerptLength to set
   */
    public void setExcerptLength(int excerptLength) {
        this.excerptLength = excerptLength;
    }
}
