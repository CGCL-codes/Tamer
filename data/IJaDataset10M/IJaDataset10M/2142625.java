package com.apelon.selext;

import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.LogConfigLoader;
import com.apelon.common.sql.ConnectionParams;
import com.apelon.common.sql.SQL;
import com.apelon.common.util.db.ParseConfig;
import com.apelon.matchpack.MatchPackException;
import com.apelon.selext.dtd.DTD;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Starts up SearchSpecHandler to execute selection and extraction
 * from UMLS and ontylog into the match_db table.
 *
 * @author        Daniel O'Connor
 * @version       SelectorExtractor 1.0
 */
public class SelectorExtractor {

    private static final String FILTER_SPEC_FILE = "filterSpec.xml";

    private static String filterSpecFile = "filterSpec.xml";

    protected static LogConfigLoader logConfig = null;

    static final int DEFAULT_CACHE_SIZE = 900000;

    static int cacheSize = DEFAULT_CACHE_SIZE;

    private static boolean test = false;

    static String filterSpec = "";

    private Connection conn;

    private String connectionType;

    private String searchSpecAction;

    private Map specs;

    private SpecProcess ssp;

    /**
   * Sets the connection to the Apelon Knowledge Base
   *
   * @since             SelectorExtractor 1.0
   */
    private SelectorExtractor(Connection conn, String type) throws Exception {
        this.conn = conn;
        connectionType = type;
    }

    /**
   * Gets the static LogConfigLoader and creates as needed
   * Looks in the selext directory first for the log config file and
   * if not found looks in the current working directory.
   *
   * @return LogConfigLoader
   */
    protected static LogConfigLoader logCfgLoader() {
        if (logConfig == null) {
            File logConfigFile = new File("selext/selectorlog.xml");
            if (logConfigFile.exists()) {
                logConfig = new LogConfigLoader("selext/selectorlog.xml", SelectorExtractor.class);
            } else {
                logConfig = new LogConfigLoader("selectorlog.xml", SelectorExtractor.class);
            }
        }
        return logConfig;
    }

    private static ConnectionParams makeConnection(String fileName) throws Exception {
        ParseConfig parseConfig = new ParseConfig(DTD.CONNECTIONSPEC, com.apelon.selext.dtd.DTD.class, DTD.CONNECTIONSPEC_FILE);
        parseConfig.addToEntityResolver(com.apelon.common.util.db.dtd.DTD.CONNECTION_URL, com.apelon.common.util.db.dtd.DTD.class, com.apelon.common.util.db.dtd.DTD.CONNECTION_FILE);
        Map connectionParamsMap = parseConfig.parseForConnection(fileName);
        ConnectionParams params = (ConnectionParams) connectionParamsMap.get(new Integer(ParseConfig.TARGET_INDEX));
        return params;
    }

    private static void displayUsage() {
        System.out.println();
        System.out.println("Run Selectors & Extractors utility to create, modify, or delete silos.");
        System.out.println("Usage: ");
        System.out.println("selext -cf FilterSpecFileName");
        System.out.println("selext -cf FilterSpecFileName");
        System.out.println("usage: -cf <filterSpecName> -cp <connection parameter filename>");
        System.out.println();
    }

    /**
   * Gets parameters for the connection to the Apelon Knowledge Base
   * <p>
   *
   * @param args        String Array holds user, pass, host, port and instance.
   *                    If this array is empty then the user will be prompted
   *                    for these values.
   *
   * @since             SelectorExtractor 1.0
   */
    public static void main(String[] args) {
        String type = null;
        String[] strings = null;
        logCfgLoader().loadDefault();
        String connectionFileName = null;
        ConnectionParams p = null;
        if ((args.length >= 1) && (args[0].equals("-?") || args[0].equals("-help"))) {
            displayUsage();
            return;
        }
        try {
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("-cf")) {
                        filterSpecFile = args[++i];
                    } else if (args[i].equals("-cs")) {
                        cacheSize = Integer.parseInt(args[++i]);
                    } else if (args[i].equals("-cp")) {
                        connectionFileName = args[++i];
                        p = makeConnection(connectionFileName);
                    } else {
                        strings = args;
                        break;
                    }
                }
                if ((connectionFileName == null) && (strings == null)) {
                    strings = SQL.promptFullConnectionArgsMultiPlatform();
                    p = new ConnectionParams(strings);
                }
            } else {
                strings = SQL.promptFullConnectionArgsMultiPlatform();
                p = new ConnectionParams(strings);
            }
            Connection conn = SQL.getConnection(p);
            Categories.dataIo().info("Successfully connected to : " + conn.getMetaData().getURL());
            SelectorExtractor dba = new SelectorExtractor(conn, p.getType());
            if (dba.startSearchSpecSaxHandler()) {
                dba.perform();
                dba.logout();
            } else {
                dba.logout();
            }
        } catch (Exception ex) {
            displayUsage();
            Categories.dataDb().error("Error in selector/extractor.", ex);
        }
    }

    /**
   * Creates an instance of searchSpecDb, starts the SearchSpecHandler
   * and imports filterSpec.xml.  Then selection and extraction is done by
   * searchSpecDb.
   * <p>
   *
   * @since           SelectorExtractor 1.0
   */
    private boolean startSearchSpecSaxHandler() {
        if (!(conn == null)) {
            try {
                Categories.dataXml().info("Setting up handler for parsing ..");
                SpecSaxHandler handler = null;
                if (!test) {
                    handler = new SpecSaxHandler();
                } else {
                    handler = new SpecSaxHandler();
                }
                handler.parse(filterSpecFile);
                specs = handler.getSpecs();
            } catch (org.xml.sax.SAXParseException ex) {
                Categories.dataXml().error("Problem parsing specfile. " + "Ensure that XML is well-formed and valid and try again.", ex);
                return false;
            } catch (Exception e) {
                Categories.dataXml().error("Problem importing/reading xml file", e);
                return false;
            }
        } else {
            Categories.dataXml().info("Extraction & Selection not done as there is no database connection.");
        }
        return true;
    }

    private void perform() throws SQLException, MatchPackException {
        int length = specs.keySet().size();
        SpecProcess[] tasks = new SpecProcess[length];
        Set sortedSet = new TreeSet(specs.keySet());
        Iterator it = sortedSet.iterator();
        int index = 0;
        while (it.hasNext()) {
            Integer key = (Integer) it.next();
            List l = (List) specs.get(key);
            tasks[index++] = new SpecProcess(conn, connectionType, searchSpecAction, l);
        }
        try {
            SpecProcess.prepareUpdate(conn, connectionType);
            for (int i = 0; i < length; i++) {
                tasks[i].process();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                SpecProcess.finishUpdate();
            } catch (SQLException e) {
                System.out.println("SpecProcess: error in finishMatchUpdate");
            }
        }
    }

    /**
   * Cleanup connections and other stuff
   */
    private void logout() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            Categories.dataXml().error("Problem encountered while logging off!!", e);
        }
    }

    /**
   * Method to be used by Junit tests only
   * @param args
   */
    public static void test(String[] args, String spec) throws Exception {
        test = true;
        SelectorExtractor.filterSpec = spec;
        SelectorExtractor.main(args);
    }
}
