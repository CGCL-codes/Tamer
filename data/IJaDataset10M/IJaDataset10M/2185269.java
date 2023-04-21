package org.zxframework;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Settings that affect the behaviour of zX at runtime.
 * 
 * These settings are cached for the duration of the application.
 */
public final class Settings {

    /** The name of the current application. **/
    private String appName;

    /** The version of this application. **/
    private String appVersion;

    /** The language of the current user, this defaults to EN and can be overridden by the office. **/
    private String language = "EN";

    /** Message directory. **/
    private String statusFileDir;

    /** The base directory of zx. **/
    private String baseDir;

    /** The directory of where the Business objects are stored. **/
    private String boDir;

    /** The runmode of the application. **/
    private String appMode;

    /** The directory where the pageflow objects are stored. NOTE : The main menu is also stored there. **/
    private String pageflowDir;

    /** The directory where the query definitions are stored. **/
    private String queryDir;

    /** The directory where templates are stored. **/
    private String templatesDir;

    /** The temp directory. **/
    private String tmpDir;

    private int sessionTimeOut;

    /**
     * Trace/Log settings
     */
    private String logFileName;

    private boolean logActive;

    private boolean logAppend;

    private String logLevel;

    private String logFactory;

    private String logFormat;

    private String traceFileName;

    private boolean traceActive;

    private boolean traceAppend;

    private int traceLevel;

    private String traceFormat;

    /** Not set explicitly so assume 1.4 **/
    private int requiredzXVersionMajor = 1;

    private int requiredzXVersionMinor = 4;

    /**
     * Date format for presentation.
     */
    private String strDateFormat;

    private String strTimeFormat;

    private String strTimestampFormat;

    private String appDate;

    private Map tags = new HashMap();

    /**
     * Default to the datbase for session storage as this will always be
     * available.
     */
    private zXType.sessionSouce sessionSource = zXType.sessionSouce.ssDB;

    /** The current runmode of this application. **/
    private zXType.runMode runMode;

    /** A collection of Auditing attributes used by Business objects with Auditing turned on. */
    private AttributeCollection auditAttributes;

    /** zX Settings for the web. */
    private WebSettings webSettings;

    /**
     * Default constructor.
     */
    public Settings() {
        super();
    }

    /**
     * @return Returns the requiredzXVersionMajor.
     */
    public int getRequiredzXVersionMajor() {
        return requiredzXVersionMajor;
    }

    /**
     * @param requiredzXVersionMajor The requiredzXVersionMajor to set.
     */
    public void setRequiredzXVersionMajor(int requiredzXVersionMajor) {
        this.requiredzXVersionMajor = requiredzXVersionMajor;
    }

    /**
     * @return Returns the requiredzXVersionMinor.
     */
    public int getRequiredzXVersionMinor() {
        return requiredzXVersionMinor;
    }

    /**
     * @param requiredzXVersionMinor The requiredzXVersionMinor to set.
     */
    public void setRequiredzXVersionMinor(int requiredzXVersionMinor) {
        this.requiredzXVersionMinor = requiredzXVersionMinor;
    }

    /**
     * @return Returns the sessionSource.
     */
    public zXType.sessionSouce getSessionSource() {
        return sessionSource;
    }

    /**
     * @param sessionSource The sessionSource to set.
     */
    public void setSessionSource(String sessionSource) {
        this.sessionSource = zXType.sessionSouce.getEnum(sessionSource);
    }

    /**
     * @param sessionSource The sessionSource to set.
     */
    public void setSessionSource(zXType.sessionSouce sessionSource) {
        this.sessionSource = sessionSource;
    }

    /**
     * @return Returns the dateFormat.
     */
    public String getStrDateFormat() {
        return strDateFormat;
    }

    /**
     * @param dateFormat The dateFormat to set.
     */
    public void setStrDateFormat(String dateFormat) {
        this.strDateFormat = dateFormat;
    }

    /**
     * @return Returns the timeFormat.
     */
    public String getStrTimeFormat() {
        return strTimeFormat;
    }

    /**
     * @param timeFormat The timeFormat to set.
     */
    public void setStrTimeFormat(String timeFormat) {
        this.strTimeFormat = timeFormat;
    }

    /**
     * @return Returns the timestampFormat.
     */
    public String getStrTimestampFormat() {
        return strTimestampFormat;
    }

    /**
     * @param timestampFormat The timestampFormat to set.
     */
    public void setStrTimestampFormat(String timestampFormat) {
        this.strTimestampFormat = timestampFormat;
    }

    /**
     * @return Returns the logActive.
     */
    public boolean isLogActive() {
        return logActive;
    }

    /**
     * @param logActive The logActive to set.
     */
    public void setLogActive(boolean logActive) {
        this.logActive = logActive;
    }

    /**
     * @return Returns the logAppend.
     */
    public boolean isLogAppend() {
        return logAppend;
    }

    /**
     * @param logAppend The logAppend to set.
     */
    public void setLogAppend(boolean logAppend) {
        this.logAppend = logAppend;
    }

    /**
     * @return Returns the logFactory.
     */
    public String getLogFactory() {
        return logFactory;
    }

    /**
     * @param logFactory The logFactory to set.
     */
    public void setLogFactory(String logFactory) {
        this.logFactory = logFactory;
    }

    /**
     * @return Returns the logFileName.
     */
    public String getLogFileName() {
        return logFileName;
    }

    /**
     * @param logFileName The logFileName to set.
     */
    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    /**
     * @return Returns the logFormat.
     */
    public String getLogFormat() {
        return logFormat;
    }

    /**
     * @param logFormat The logFormat to set.
     */
    public void setLogFormat(String logFormat) {
        this.logFormat = logFormat;
    }

    /**
     * @return Returns the logLevel.
     */
    public String getLogLevel() {
        return logLevel;
    }

    /**
     * @param logLevel The logLevel to set.
     */
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * @return Returns the appName.
     */
    public String getAppName() {
        return this.appName;
    }

    /**
     * @param appName The appName to set.
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return Returns the appVersion.
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * @param appVersion The appVersion to set.
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * The directory where the pageFlows are stored.
     * @return Returns the pageflowDir.
     */
    public String getPageflowDir() {
        return pageflowDir;
    }

    /**
     * @param pageflowDir The pageflowDir to set.
     */
    public void setPageflowDir(String pageflowDir) {
        this.pageflowDir = pageflowDir;
    }

    /**
     * The director where the queryDefs are stored.
     * 
     * @return Returns the queryDir.
     */
    public String getQueryDir() {
        return queryDir;
    }

    /**
     * @param queryDir The queryDir to set.
     */
    public void setQueryDir(String queryDir) {
        this.queryDir = queryDir;
    }

    /**
     * Time in minutes before a idle session expires.
     * 
     * @return Returns the sessionTimeOut.
     */
    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    /**
     * @param sessionTimeOut The sessionTimeOut to set.
     */
    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    /**
     * This is where the word templates are stored. This will be replaced by xml templates.
     * 
     * @return Returns the templatesDir.
     */
    public String getTemplatesDir() {
        return templatesDir;
    }

    /**
     * @param templatesDir The templatesDir to set.
     */
    public void setTemplatesDir(String templatesDir) {
        this.templatesDir = templatesDir;
    }

    /**
     * Directory where temp data is store. This is often used by the repository editor
     * 
     * @return Returns the tmpDir.
     */
    public String getTmpDir() {
        return tmpDir;
    }

    /**
     * @param tmpDir The tmpDir to set.
     */
    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }

    /**
     * Message directory. The full path to the directory where "zXLeft" polls for
     * new messages. This should be served by on effient webserver. This is required
     * for the instant message service to work.
     * 
	 * @return Returns the statusFileDir.
	 */
    public String getStatusFileDir() {
        return statusFileDir;
    }

    /**
	 * @param statusFileDir The statusFileDir to set.
	 */
    public void setStatusFileDir(String statusFileDir) {
        this.statusFileDir = statusFileDir;
    }

    /**
     * @return Returns the traceActive.
     */
    public boolean isTraceActive() {
        return traceActive;
    }

    /**
     * @param traceActive The traceActive to set.
     */
    public void setTraceActive(boolean traceActive) {
        this.traceActive = traceActive;
    }

    /**
     * @return Returns the traceAppend.
     */
    public boolean isTraceAppend() {
        return traceAppend;
    }

    /**
     * @param traceAppend The traceAppend to set.
     */
    public void setTraceAppend(boolean traceAppend) {
        this.traceAppend = traceAppend;
    }

    /**
     * @return Returns the traceFileName.
     */
    public String getTraceFileName() {
        return traceFileName;
    }

    /**
     * @param traceFileName The traceFileName to set.
     */
    public void setTraceFileName(String traceFileName) {
        this.traceFileName = traceFileName;
    }

    /**
     * @return Returns the traceFormat.
     */
    public String getTraceFormat() {
        return traceFormat;
    }

    /**
     * @param traceFormat The traceFormat to set.
     */
    public void setTraceFormat(String traceFormat) {
        this.traceFormat = traceFormat;
    }

    /**
     * @return Returns the traceLevel.
     */
    public int getTraceLevel() {
        return traceLevel;
    }

    /**
     * @param traceLevel The traceLevel to set.
     */
    public void setTraceLevel(int traceLevel) {
        this.traceLevel = traceLevel;
    }

    /**
     * @return Returns the language.
     */
    public String getLanguage() {
        if (this.language == null) {
            this.language = "EN";
        }
        return this.language;
    }

    /**
     * @param language The language to set.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return Returns the appMode.
     */
    public String getAppMode() {
        return appMode;
    }

    /**
     * @param appMode The appMode to set.
     */
    public void setAppMode(String appMode) {
        this.appMode = appMode;
        if (this.appMode != null) {
            this.runMode = zXType.runMode.getEnum(this.appMode);
        } else {
            this.runMode = zXType.runMode.rmProduction;
        }
    }

    /**
     * @return Returns the boDir.
     */
    public String getBoDir() {
        return boDir;
    }

    /**
     * @param boDir The boDir to set.
     */
    public void setBoDir(String boDir) {
        this.boDir = boDir;
    }

    /**
     * @return Returns the runMode.
     */
    public zXType.runMode getRunMode() {
        return this.runMode;
    }

    /**
     * @param runMode The runMode to set.
     */
    public void setRunMode(zXType.runMode runMode) {
        this.runMode = runMode;
    }

    /**
     * @return Returns the appDate.
     */
    public String getAppDate() {
        return appDate;
    }

    /**
     * @param appDate The appDate to set.
     */
    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    /**
     * @return Returns the baseDir.
     */
    public String getBaseDir() {
        return baseDir;
    }

    /**
     * @param baseDir The baseDir to set.
     */
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * @return Returns the tags.
     */
    public Map getTags() {
        return tags;
    }

    /**
     * @param tags The tags to set.
     */
    public void setTags(Map tags) {
        this.tags = tags;
    }

    /**
     * @return Returns the auditAttributes.
     */
    public AttributeCollection getAuditAttributes() {
        return this.auditAttributes;
    }

    /**
     * @param auditAttributes The auditAttributes to set.
     */
    public void setAuditAttributes(AttributeCollection auditAttributes) {
        this.auditAttributes = auditAttributes;
    }

    private static DateFormat timeFormat;

    private static DateFormat timestampFormat;

    private static DateFormat dateFormat;

    /**
     * @return Returns the dateFormatter for Times.
     */
    public DateFormat getTimeFormat() {
        if (timeFormat == null) {
            timeFormat = new SimpleDateFormat(this.strTimeFormat);
        }
        return timeFormat;
    }

    /**
     * @return Returns the dateformatter for Timestamps.
     */
    public DateFormat getTimestampFormat() {
        if (timestampFormat == null) {
            timestampFormat = new SimpleDateFormat(this.strTimestampFormat);
        }
        return timestampFormat;
    }

    /**
     * @return Returns the dateFormatter for Dates.
     */
    public DateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(this.strDateFormat);
        }
        return dateFormat;
    }

    /**
     * @return Returns the webSettings.
     */
    public WebSettings getWebSettings() {
        return webSettings;
    }

    /**
     * @param webSettings The webSettings to set.
     */
    public void setWebSettings(WebSettings webSettings) {
        this.webSettings = webSettings;
    }
}
