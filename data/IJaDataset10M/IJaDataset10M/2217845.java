package de.beas.explicanto.server;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;
import de.beas.explicanto.ExplicantoProperties;
import de.beas.explicanto.types.WSRoleType;

/**
 * @author AlexanderS
 */
public class ExplicantoServerProperties extends ExplicantoProperties {

    private static final String DBPROP_FILENAME = "xpcdb.properties";

    private static final String DBPROP_TYPEKEY = "database_type";

    private static final String DBPROP_NAMEKEY = "database_name";

    private static final String DBPROP_USERKEY = "database_user";

    private static final String DBPROP_PWKEY = "database_pw";

    private static final String DBPROP_HOST = "database_host";

    private static final String DB_MYSQL = "mysql";

    private static final String DB_DRIVERCLASS_MYSQL = "com.mysql.jdbc.Driver";

    private static final String DB_DIALECT_MYSQL = "net.sf.hibernate.dialect.MySQLDialect";

    private static final String DB_POSTGRESQL = "postgresql";

    private static final String DB_DRIVERCLASS_POSTGRESQL = "org.postgresql.Driver";

    private static final String DB_DIALECT_POSTGRESQL = "net.sf.hibernate.dialect.PostgreSQLDialect";

    private static final String[] VALID_DB_TYPES = new String[] { DB_MYSQL, DB_POSTGRESQL };

    public static String DB_TYPE = "<undefined>", DB_NAME = "<undefined>", DB_USER = "<undefined>", DB_PASSWD = "<undefined>", DB_HOST = "<undefined>";

    public final int roleCount = 6;

    public final int numOfStati = 2;

    private String baseDir, propFile;

    private String[] nodenames = { WSRoleType.PROJECT, WSRoleType.COURSENAME, WSRoleType.COURSE, WSRoleType.LESSON, WSRoleType.UNIT };

    private String[] booleans = { "true", "false" };

    public ExplicantoServerProperties(String dir) {
        super(dir + "/explicantoServer.properties");
        baseDir = dir;
        propFile = new File(baseDir + "/explicantoServer.properties").getAbsolutePath();
    }

    /**
	 * To be called after logging etc. has been set up
	 */
    public String verify() {
        String erg;
        for (int i = 1; i <= roleCount; i++) {
            erg = verifyValue("role" + i + "_isextended", booleans);
            if (erg.compareTo("OK") != 0) return "error in property file \"" + propFile + "\"\n*** error: " + erg + "\n\nbye.";
            erg = verifyValue("role" + i + "_single_node", nodenames);
            if (erg.compareTo("OK") != 0) return "error in property file \"" + propFile + "\"\n*** error: " + erg + "\n\nbye.";
            erg = verifyValue("role" + i, null);
            if (erg.compareTo("OK") != 0) return "error in property file \"" + propFile + "\"\n*** error: " + erg + "\n\nbye.";
        }
        return null;
    }

    public String getPath(String property) {
        String prop = getProperty(property);
        if (Pattern.matches("^\\w:\\\\.*", prop) || Pattern.matches("^\\w:/.*", prop) || Pattern.matches("^/.*", prop)) return prop; else return baseDir + "/" + prop;
    }

    public String getExplicantoHome() {
        return baseDir;
    }

    public void initDefaults() {
        defaultVals.put("LogDir", "log");
        defaultVals.put("LogMaxSize", "1000KB");
        defaultVals.put("LogMaxFiles", "25");
        defaultVals.put("medialib_storage_location", "MediaLib");
        defaultVals.put("customer_files_location", "MediaLib/Customer");
        defaultVals.put("course_target_location", "Courses");
        defaultVals.put("preview_directory", "Course");
        defaultVals.put("extra_directories", "10");
        defaultVals.put("unescapedHTML", "false");
        defaultVals.put("role1", "Rolle1");
        defaultVals.put("role2", "Rolle2");
        defaultVals.put("role3", "Rolle3");
        defaultVals.put("role4", "Rolle4");
        defaultVals.put("role5", "Rolle5");
        defaultVals.put("role6", "Rolle6");
        defaultVals.put("role1_single_node", "project");
        defaultVals.put("role2_single_node", "project");
        defaultVals.put("role3_single_node", "project");
        defaultVals.put("role4_single_node", "project");
        defaultVals.put("role5_single_node", "project");
        defaultVals.put("role6_single_node", "project");
        defaultVals.put("role1_isextended", "false");
        defaultVals.put("role2_isextended", "false");
        defaultVals.put("role3_isextended", "false");
        defaultVals.put("role4_isextended", "false");
        defaultVals.put("role5_isextended", "false");
        defaultVals.put("role6_isextended", "false");
    }

    public static String loadDBSettings() throws Exception {
        String dbtype = "", dbName = "", dbUser = "", dbPw = "", dbHost = "";
        boolean ok = false;
        Properties dbprop = new Properties();
        InputStream dbpropStream = XPCUtil.readClasspathInputStream(DBPROP_FILENAME);
        if (dbpropStream != null) {
            dbprop.load(dbpropStream);
            dbtype = dbprop.getProperty(DBPROP_TYPEKEY);
            if (dbtype == null) return "could not find value for key '" + DBPROP_TYPEKEY + "' in " + DBPROP_FILENAME; else {
                for (int i = 0; i < VALID_DB_TYPES.length; i++) if (dbtype.equals(VALID_DB_TYPES[i])) {
                    ok = true;
                    break;
                }
                if (!ok) {
                    String invalidDB = "invalid value for key '" + DBPROP_TYPEKEY + "': '" + dbtype + "'. allowed values are: (";
                    for (int i = 0; i < VALID_DB_TYPES.length; i++) invalidDB += VALID_DB_TYPES[i] + ", ";
                    invalidDB = invalidDB.substring(0, invalidDB.length() - (VALID_DB_TYPES.length > 0 ? 2 : 0)) + ")";
                    return invalidDB;
                }
            }
            dbHost = dbprop.getProperty(DBPROP_HOST);
            if (dbHost == null) dbHost = "localhost";
            dbName = dbprop.getProperty(DBPROP_NAMEKEY);
            if (dbName == null) return "could not find value for key '" + DBPROP_NAMEKEY + "' in " + DBPROP_FILENAME;
            dbUser = dbprop.getProperty(DBPROP_USERKEY);
            if (dbUser == null) return "could not find value for key '" + DBPROP_USERKEY + "' in " + DBPROP_FILENAME;
            dbPw = dbprop.getProperty(DBPROP_PWKEY);
            if (dbPw == null) return "could not find value for key '" + DBPROP_PWKEY + "' in " + DBPROP_FILENAME;
        } else return "could not find property file '" + DBPROP_FILENAME + "'";
        DB_TYPE = dbtype;
        DB_NAME = dbName;
        DB_USER = dbUser;
        DB_PASSWD = dbPw;
        DB_HOST = dbHost;
        return "";
    }

    public static String getDBDriverClass() {
        if (DB_TYPE.equals(DB_MYSQL)) return DB_DRIVERCLASS_MYSQL; else if (DB_TYPE.equals(DB_POSTGRESQL)) return DB_DRIVERCLASS_POSTGRESQL; else return "?";
    }

    public static String getDBDialect() {
        if (DB_TYPE.equals(DB_MYSQL)) return DB_DIALECT_MYSQL; else if (DB_TYPE.equals(DB_POSTGRESQL)) return DB_DIALECT_POSTGRESQL; else return "?";
    }

    public static String getDBConnUrl() {
        if (DB_TYPE.equals(DB_MYSQL)) return "jdbc:mysql://" + DB_HOST + "/" + DB_NAME + "?useUnicode=true&characterEncoding=utf-8&useServerPrepStmts=false&autoReconnect=true"; else if (DB_TYPE.equals(DB_POSTGRESQL)) return "jdbc:postgresql://" + DB_HOST + "/" + DB_NAME; else return "?";
    }
}
