package com.apelon.common.util.db;

import java.io.*;
import java.sql.*;
import java.util.*;
import com.apelon.common.log4j.*;
import com.apelon.common.sql.*;
import com.apelon.common.util.*;
import com.apelon.common.util.db.dtd.*;

/**
 * The primary class for a console application that migrates a DTS3 KB.
 * This class contains both UI (console-level) and database interaction code.
 */
public class DbMigrate {

    /**
   * load test properties
   * @throws IOException
   */
    private void loadProperties(String fileName) throws IOException {
        String temp = fileName;
        try {
            ParseConfig parseConfig = new ParseConfig(ParseConfig.DTD_URL, ParseConfig.class, ParseConfig.DTD_NAME);
            dbMigrateMap = parseConfig.getProperties();
            Class cl = Class.forName(systemClass);
            systemConfig = (DBSystemConfig) cl.newInstance();
            logCfgLoader().loadDefault();
            connectionParamsMap = parseConfig.parseForConnection(fileName);
            connectionParamsMap = parseConfig.parseForConnection(fileName);
            ParseConfig connParseConfig = new ParseConfig(DTD.CONNECTION_URL, DTD.class, DTD.CONNECTION_FILE);
            if (targetConnFileName != null) {
                connParseConfig.parseForConnection(targetConnFileName, connectionParamsMap);
            }
            parseConfig.addToEntityResolver(systemConfig.getDaoDTDURL(), systemConfig.getDaoDTDClass(), systemConfig.getDaoDTDFileName());
            tableList = parseConfig.parseForTable(systemConfig.getTableLists());
            prefix = parseConfig.getPrefix();
            propertyMap = parseConfig.getProperties();
            systemConfig.validateInput(dbMigrateMap);
        } catch (IllegalArgumentException e) {
            Categories.dataDb().error("DbMigrate: loadProperties", e);
            throw new IllegalArgumentException("Invalid property configuration: " + e.getMessage());
        } catch (Exception e) {
            Categories.dataDb().error("DbMigrate: loadProperties", e);
            throw new IOException("cannot load properties: " + e.getMessage());
        }
    }

    private void makeConnection() throws IOException, SQLException {
        ConnectionParams params = (ConnectionParams) connectionParamsMap.get(new Integer(ParseConfig.TARGET_INDEX));
        targetConn = SQL.getConnection(params);
        targetType = params.getType();
        Categories.dataDb().info("**** Successfully connected to : " + "[database-user=<" + params.getUserName() + ">, hostname=<" + params.getHostName() + ">, port=<" + params.getPort() + ">]");
    }

    private void init() throws Exception {
        new BaseTable(targetConn, targetType, systemConfig, dbMigrateMap);
        systemConfig.init(null, targetConn);
    }

    public void cleanup() throws SQLException {
        if (targetConn != null) {
            Categories.dataDb().info("Closing connection..");
            targetConn.close();
            targetConn = null;
        }
    }

    /**
   * Gets the static LogConfigLoader, creating it if need be
   *
   * @return LogConfigLoader
   */
    protected LogConfigLoader logCfgLoader() {
        if (logConfig == null) {
            logConfig = systemConfig.logConfigLoader();
        }
        return logConfig;
    }

    /**
   * Constructor for use with JUnit test.
   * @param fileName
   * @throws SQLException
   */
    public DbMigrate(String fileName) throws IOException, SQLException, Exception {
        loadProperties(fileName);
        makeConnection();
        init();
    }

    public void run() {
        try {
            long beg;
            long end;
            beg = System.currentTimeMillis();
            Categories.app().info("buildTables");
            buildTables();
            end = System.currentTimeMillis();
            Categories.app().info("Elapsed time:  " + (end - beg) / 1000 + " secs");
            targetConn.close();
        } catch (Exception ex) {
            Categories.app().error("Exception running DbAdmin", ex);
        }
    }

    /**
   * Builds all the tables provided in the tables configuration file
   * @throws Exception
   */
    private void buildTables() throws Exception {
        int len = tableList.size();
        for (int i = 0; i < len; i++) {
            String tableName = (String) tableList.get(i);
            BaseTable.create(tableName.trim(), prefix);
        }
    }

    public static void main(String[] args) {
        try {
            String fileName = PROPERTY_XML;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-p")) {
                    fileName = args[++i];
                    continue;
                }
                if (args[i].equals("-c")) {
                    systemClass = args[++i];
                    continue;
                }
                if (args[i].equals("-t")) {
                    targetConnFileName = args[++i];
                    continue;
                }
                if (args[i].equals("-startSeq")) {
                    startSequenceNum = Long.valueOf(args[++i]).longValue();
                    continue;
                }
                if (args[i].equals("-skipPause")) {
                    defaultSkipPause = true;
                    continue;
                }
                if (args[i].equals("-saveDTSSequences")) {
                    saveDTSSequences = true;
                    continue;
                }
            }
            if (systemClass == null) {
                System.out.println("Options: -c <system configuration class name> which is mandatory");
                System.out.println("-p <user property name>");
                System.out.println("-t <target connection config file>");
                return;
            }
            DbMigrate dba = new DbMigrate(fileName);
            dba.run();
        } catch (Exception ex) {
            Categories.data().error(ex.getMessage());
        } finally {
        }
    }

    public static boolean defaultSkipPause = false;

    protected static LogConfigLoader logConfig = null;

    protected static long startSequenceNum = 1;

    protected static boolean saveDTSSequences = false;

    private String targetType = null;

    private boolean defaultAutoCommit = true;

    private static final String PROPERTY_XML = "kbmigrate.xml";

    private Map dbMigrateMap;

    private Map connectionParamsMap;

    private Map propertyMap;

    private String MASTERKB_TAG = "masterKb";

    private List tableList;

    private DBSystemConfig systemConfig;

    private static String systemClass;

    private static String targetConnFileName;

    private String prefix;

    private Connection targetConn;
}
