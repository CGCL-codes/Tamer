package com.hszt.util;

import com.hszt.util.log.Log;
import java.io.File;
import java.util.StringTokenizer;

/**
 * Specifies the log procedure for the two log levels: INFO and DEBUG.
 *
 * @author danielroth
 * @author adrianchristen
 * @author matthiasschmid
 * @see #getInstance()
 * @see #getInstance(String[])
 * @see #initDisplayLogLevel()
 * @see #initLogLevel()
 * @see #initLogPath()
 * @see #initCli()
 * @see #initSetting()
 */
public class RunConfiguration {

    private Log.LOG_LEVEL displayLogLevel = Log.LOG_LEVEL.INFO;

    private Log.LOG_LEVEL logLevel = Log.LOG_LEVEL.DEBUG;

    private boolean cli = false;

    private String logPath = null;

    private static RunConfiguration config;

    /**
     * Implements the RunConfiguration by implementing the tokenizer and initiating settingName and settingValue.
     *
     * @param settings
     */
    private RunConfiguration(String[] settings) {
        for (String setting : settings) {
            if (setting.startsWith("-D")) {
                StringTokenizer tokenizer = new StringTokenizer(setting, "=");
                if (tokenizer.countTokens() == 2) {
                    String settingName = tokenizer.nextToken().substring(2);
                    String settingValue = tokenizer.nextToken();
                    initSetting(settingName, settingValue);
                }
            } else {
            }
        }
    }

    /**
     * Reinitiating config after checking if config was initiated with null.
     *
     * @return config
     * @throws Exception "can not reinitiate" if config not null.
     */
    public static RunConfiguration getInstance(String[] args) throws Exception {
        if (config == null) {
            config = new RunConfiguration(args);
            return config;
        }
        throw new Exception("can not reinitiate");
    }

    /**
     * @return config
     * @throws Exception if config is null
     */
    public static RunConfiguration getInstance() throws Exception {
        if (config == null) throw new Exception("not initiated yet");
        return config;
    }

    /**
     * Getter of the property <tt>displayLogLevel</tt>.
     *
     * @return the displayLogLevel
     */
    public Log.LOG_LEVEL getDisplayLogLevel() {
        return displayLogLevel;
    }

    /**
     * Getter of the property <tt>logLevel</tt>.
     *
     * @return the logLevel
     */
    public Log.LOG_LEVEL getLogLevel() {
        return logLevel;
    }

    /**
     * Getter of the property <tt>logPath</tt>.
     *
     * @return the logPath or null if there is none
     */
    public String getLogPath() {
        return logPath;
    }

    /**
     * Getter of the Command Line Interface.
     *
     * @return the Command Line Interface
     */
    public boolean getClis() {
        return cli;
    }

    private void initDisplayLogLevel(String settingValue) {
        try {
            displayLogLevel = Log.LOG_LEVEL.valueOf(settingValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            displayLogLevel = Log.LOG_LEVEL.INFO;
        }
    }

    /**
     * Setting the initial Log Level as a sting in upper cases.
     *
     * @param settingValue
     * @throws IllegalArgumentException
     */
    private void initLogLevel(String settingValue) {
        try {
            logLevel = Log.LOG_LEVEL.valueOf(settingValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            logLevel = Log.LOG_LEVEL.DEBUG;
        }
    }

    /**
     * Setting the initial Log Path after checking if it's directory is valid and writable.
     *
     * @param settingValue
     */
    private void initLogPath(String settingValue) {
        File testPath = new File(settingValue);
        if (testPath.isDirectory()) {
            if (testPath.canWrite()) {
                logPath = settingValue;
            }
        }
    }

    /**
     * Setting the initial Command Line Interface true or false.
     *
     * @param settingValue
     */
    private void initCli(String settingValue) {
        cli = Boolean.valueOf(settingValue);
    }

    /**
     * Initializes the settingValue according to the settingName.
     *
     * @param settingName
     * @param settingValue
     */
    private void initSetting(String settingName, String settingValue) {
        if ("LogLevel".equalsIgnoreCase(settingName)) {
            initLogLevel(settingValue);
        } else if ("displayLogLevel".equalsIgnoreCase(settingName)) {
            initDisplayLogLevel(settingValue);
        } else if ("LogPath".equalsIgnoreCase(settingName)) {
            initLogPath(settingValue);
        } else if ("CLI".equalsIgnoreCase(settingName)) {
            initCli(settingValue);
        }
    }
}
