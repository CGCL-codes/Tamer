package tpac.lib.DAPSpider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
* The entry point to the spider.
* @author Ian C (ian@insight4.com), Pauline M (pauline@insight4.com)
**/
public class SpiderMain {

    private static String configFilename = "<no filename specified>";

    private static ReportWriter report;

    private static boolean useProxy, useDatabase;

    private static CrawlerConfig config;

    /**
    * Begins the spidering.
    * @param args - command line arguments.  The spider has the following command structure:
    * <full path to config file> <wait_time> <log_level> [-d] (where -d is used for database logging of spider).
    **/
    public SpiderMain(String[] args) {
        SpiderShutdown sh = new SpiderShutdown(this);
        Runtime.getRuntime().addShutdownHook(sh);
        String resourceContent;
        if (args.length < 4) {
            printUsage();
            return;
        }
        int cleanupBeforeDays = 0;
        resourceContent = readConfig();
        if (resourceContent != null) {
            if (useDatabase) {
                String[] params = resourceContent.split("\n");
                String cleanupStr = getParam("deleteBefore", params);
                config.cleanupBeforeDays = Integer.parseInt(cleanupStr);
                DigitalLibrarySpider spider = new DigitalLibrarySpider(config);
                spider.setReportWriter(report);
                MsgLog.setReportWriter(report);
                spider.setTimeoutMilliseconds(config.timeoutMilliseconds);
                if (spider.init()) {
                    String SQL = "SET ROLE aodaac";
                    spider.getConnector().executeUpdate(SQL);
                    spider.begin();
                } else {
                    System.err.println("Cannot connect to Digital Library database.  Please make sure that:\n\t- configuration for accessing database is correct.\n\t- The MySQL connector library location has been compiled into this Java library.");
                    System.err.println("Error message was: " + spider.getLastError());
                }
            } else {
                System.err.println("Cannot find database configuration in '" + configFilename + "'.");
            }
        }
    }

    public static void processArgs(String[] args) {
        config.fileWaitTime = CrawlerConfig.FILE_NO_WAIT;
        for (int i = 0; i < args.length; i++) {
            if ((report == null) && args[i].equalsIgnoreCase("-d")) {
                report = new ReportWriterDatabase(config.databaseHost, config.databaseName, config.databaseUsername, config.databasePassword, config.logLevel);
            } else if (args[i].equalsIgnoreCase("-showsql")) {
                config.isDebug = true;
            } else if (args[i].startsWith("-wait=")) {
                config.fileWaitTime = Integer.parseInt(args[i].substring(args[i].indexOf("=") + 1, args[i].length()));
            } else if (args[i].equalsIgnoreCase("-wait-random")) {
                config.fileWaitTime = CrawlerConfig.FILE_WAIT_RANDOM;
            }
        }
        if (report == null) report = report = new ReportWriterConsole(config.logLevel);
        if (config.mode == DigitalLibrarySpider.SEARCH_MODE) report.setWriterPrefix("SEARCH"); else if (config.mode == DigitalLibrarySpider.RETRIEVE_MODE) report.setWriterPrefix("RETRIEVE");
    }

    /**
    * Entry point to the spider.
    **/
    public static void main(String[] args) {
        report = null;
        config = new CrawlerConfig();
        if (args.length < 4) {
            printUsage();
            return;
        }
        config.waitTime = Integer.parseInt(args[2]);
        config.logLevel = Integer.parseInt(args[3]);
        configFilename = args[1];
        if (args[0].equalsIgnoreCase("SEARCH")) {
            config.mode = DigitalLibrarySpider.SEARCH_MODE;
        } else if (args[0].equalsIgnoreCase("RETRIEVE")) {
            config.mode = DigitalLibrarySpider.RETRIEVE_MODE;
        } else {
            System.err.println("Please enter a valid crawler mode.  This can either be SEARCH, for searching out OPeNDAP files, or RETRIEVE for retrieving the DAS of found files.");
            printUsage();
            return;
        }
        String resourceContent = readConfig();
        String[] params = resourceContent.split("\n");
        useProxy = getProxyConfig(params);
        useDatabase = getDatabaseConfig(params);
        if (getRegexConfig(params)) {
            return;
        }
        config.timeoutMilliseconds = 4000;
        getTimeoutConfig(params);
        getDebugConfig(params);
        if (!useDatabase) {
            System.err.println("There was problem reading the database connectivity settings.");
            return;
        }
        try {
            processArgs(args);
            new SpiderMain(args);
        } catch (Exception e) {
            MsgLog.error("Uncaught exception Error: " + e.toString());
            e.printStackTrace(System.out);
        }
    }

    private static boolean getRegexConfig(String[] params) {
        config.baseFileRegex = getParam("base_file_regex", params);
        config.filenameRegex = getParam("filename_regex", params);
        boolean error = false;
        if (config.baseFileRegex != null) {
            try {
                Pattern p = Pattern.compile(config.baseFileRegex);
            } catch (PatternSyntaxException e) {
                System.err.println("The expression you've entered: '" + config.baseFileRegex + "' cannot be compiled.  Please enter a valid regular expression.");
                error = true;
            }
        }
        if (config.filenameRegex != null) {
            try {
                Pattern p = Pattern.compile(config.filenameRegex);
            } catch (PatternSyntaxException e) {
                System.err.println("The expression you've entered: '" + config.filenameRegex + "' cannot be compiled.  Please enter a valid regular expression.");
                error = true;
            }
        }
        if (!error) {
            if (config.baseFileRegex == null) {
                config.baseFileRegex = ".*[.](nc|cdf|hdf)(.gz)?";
            }
            if (config.filenameRegex == null) {
                config.filenameRegex = ".*[.]((nc|hdf)(.gz)?)|.*[.]((nc|hdf)(.gz)?)[.](das|dds|dods|info|html|ver|help)";
            }
        }
        return error;
    }

    /**
    * Read the configuration file (.spider_rc or whatever file is specified in the command line).
    * @return the configuration file as a string.  Null if there is an error.
    **/
    public static String readConfig() {
        String resContent = "";
        try {
            FileInputStream configFile = new FileInputStream(configFilename);
            byte[] buffer = new byte[1024];
            int numRead = configFile.read(buffer);
            while (numRead > -1) {
                resContent += new String(buffer);
                numRead = configFile.read(buffer);
            }
            configFile.close();
        } catch (FileNotFoundException fnfe) {
            MsgLog.serious("Cannot find DAP spider resource file:  '" + configFilename + "' in the current directory. Error: " + fnfe.toString());
            return (String) null;
        } catch (java.io.IOException ioe) {
            MsgLog.serious("Problems reading DAP spider resouce file: '" + configFilename + "'. Error: " + ioe.toString());
            return (String) null;
        }
        return resContent;
    }

    /**
    * Get the parameter with a given key.
    * @param param - parameter key.
    * @param params - a list of non-comment parameters found.
    * @return the value of the parameter if it exists, Null otherwise.
    **/
    public static String getParam(String param, String[] params) {
        for (int i = 0; i < params.length; i++) {
            if ((params[i].indexOf("//") == -1) && (params[i].length() > 0)) {
                String[] singleParam = (params[i]).split(":");
                if (singleParam.length > 1) {
                    if (singleParam[0].equals(param)) {
                        return (singleParam[1]).trim();
                    }
                }
            }
        }
        return (String) null;
    }

    public static boolean getTimeoutConfig(String[] params) {
        String tmp = getParam("timeout_milliseconds", params);
        if (tmp != null) {
            config.timeoutMilliseconds = Integer.parseInt(tmp);
            return true;
        }
        return false;
    }

    public static int getDatabaseType(String[] params) throws Exception {
        String type = getParam("database_type", params);
        if (type.equalsIgnoreCase("MYSQL")) {
            return CrawlerConfig.DB_MYSQL;
        } else if (type.equalsIgnoreCase("PGSQL")) {
            return CrawlerConfig.DB_PGSQL;
        }
        throw (new Exception("Databset type:" + type + " not supported, only supports MYSQL and PGSQL"));
    }

    /**
    * Get Proxy settings
    * @param params - the content of the config file.
    * @return true = proxy configuration can be read, false = failed to read.
    **/
    public static boolean getProxyConfig(String[] params) {
        config.proxyHost = getParam("proxy_host", params);
        config.proxyPort = getParam("proxy_port", params);
        config.proxyUsername = getParam("proxy_username", params);
        config.proxyPassword = getParam("proxy_password", params);
        if ((config.proxyHost != null) && (config.proxyPort != null) && (config.proxyUsername != null) && (config.proxyPassword != null)) {
            return true;
        }
        return false;
    }

    /**
    * Get Database settings
    * @param params - List of tokenized parameters.
    * @return true = database configuration can be read, false = failed to read.
    **/
    public static boolean getDatabaseConfig(String[] params) {
        config.databaseHost = getParam("database_host", params);
        config.databaseName = getParam("database_name", params);
        config.databaseUsername = getParam("database_username", params);
        config.databasePassword = getParam("database_password", params);
        config.databasePort = getParam("database_port", params);
        try {
            config.databasetType = getDatabaseType(params);
        } catch (Exception e) {
            System.err.println("Unable to get the dataset type.  Aborting");
            return false;
        }
        if ((config.databaseHost != null) && (config.databaseName != null) && (config.databaseUsername != null) && (config.databasePassword != null)) return true;
        return false;
    }

    public static boolean getDebugConfig(String[] params) {
        String tmp = getParam("debug", params);
        if (tmp != null) {
            if (tmp.equalsIgnoreCase("true")) config.isDebug = true; else if (tmp.equalsIgnoreCase("false")) config.isDebug = false;
            return true;
        }
        return false;
    }

    public static void printUsage() {
        System.err.println("Usage: java -jar tpac_dlp-lib.jar <SEARCH|RETRIEVE> <full path to config file> <wait_time> <log_level> [-d] [-showsql] [-wait=<n seconds>] [-wait-random]\n Please note that proxy and database settings are found in the file '" + configFilename + "'\n -d is optional for turning on database logging of the spider status.  Default logger prints to console." + "\n -wait specifies the number of seconds to wait between each DAS retrieval.  Only works in RETRIEVE mode." + "\n -wait-random specifies that there will be a random number of seconds (up to 20 seconds) of wait between each DAS retireval");
    }

    /**
    * Shutdow code by Mark Watson (markw@markwatson.com)
    **/
    public void freeResources() {
        if (report != null) MsgLog.critical("Shutting down in progress.");
        try {
            if (report != null) MsgLog.critical("Shutdown by user");
        } catch (Exception ee) {
            if (report != null) MsgLog.critical("Error freeing resources: " + ee);
        }
    }

    protected void finalize() throws Throwable {
        try {
            freeResources();
        } finally {
            super.finalize();
        }
    }
}
