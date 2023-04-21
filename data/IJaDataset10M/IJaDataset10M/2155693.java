package net.sf.webconsole.sql.cmd;

import net.sf.webconsole.cmd.AbstractCommand;
import net.sf.webconsole.sql.JDBCUtility;

/**
 * The <CODE>SetJDBCInfo</CODE> class provides a 
 * command to set the used JDBC's detail information.
 * 
 * <UL>
 *  <LI>scoprio.jdbc.name
 *  <LI>scorpio.jdbc.driver
 *  <LI>scorpio.jdbc.url
 *  <LI>scorpio.jdbc.username
 *  <LI>scorpio.jdbc.password
 *  <LI>scorpio.jdbc.sql
 * </UL>
 * 
 * @version 1.0.0 06 Jul 2001
 * @author <A HREF='mailto:chyxiang@yahoo.com'>Chen Xiang</A>
 */
public class SetJDBCInfo extends AbstractCommand {

    /**
     * The constant for JDBC information, which will be 
     * stored as system properties.
     */
    public static final String JDBC_NAME = "scorpio.jdbc.name";

    public static final String JDBC_DRIVER = "scorpio.jdbc.driver";

    public static final String JDBC_URL = "scorpio.jdbc.url";

    public static final String JDBC_USER = "scorpio.jdbc.username";

    public static final String JDBC_PASSWORD = "scorpio.jdbc.password";

    public static final String JDBC_SQL = "scorpio.jdbc.sql";

    /**
     * Using the JDBCUtility to get the JDBC information stored 
     * in jdbcprops.properties file.
     */
    private JDBCUtility jdbcUtil = JDBCUtility.getInstance();

    private String[] args;

    /**
     * Override the abstract runCommand method inherited from 
     * parent class.
     */
    public void runCommand() {
        args = getArguments();
        if (args == null || args.length == 0) {
            getOut().println("====Current JDBC's properties====");
            getOut().println(this.JDBC_NAME + "=" + System.getProperty(this.JDBC_NAME));
            getOut().println(this.JDBC_DRIVER + "=" + System.getProperty(this.JDBC_DRIVER));
            getOut().println(this.JDBC_URL + "=" + System.getProperty(this.JDBC_URL));
            getOut().println(this.JDBC_USER + "=" + System.getProperty(this.JDBC_USER));
            getOut().println(this.JDBC_PASSWORD + "=" + System.getProperty(this.JDBC_PASSWORD));
            getOut().println(this.JDBC_SQL + "=" + System.getProperty(this.JDBC_SQL));
        } else {
            for (int i = 0; i < args.length; i++) {
                processArgument(args[i]);
                if (args[i].startsWith("sql=")) {
                    break;
                }
            }
        }
    }

    /**
     * Process a command line argument.
     * 
     * @param arg A command line argrment.
     */
    private void processArgument(String arg) {
        if (arg.equals("")) {
        } else if (arg.indexOf("=") > 0) {
            int i = arg.indexOf("=");
            String name = arg.substring(0, i);
            String value = arg.substring(i + 1);
            setJDBCProperty(name, value);
        } else {
            showJDBCProperty(arg);
        }
    }

    /**
     * Sets a JDBC property
     * 
     * @param name The property name
     * @param value The property's value
     */
    private void setJDBCProperty(String name, String value) {
        String message = "Property set successfully.";
        if (name.equals("name")) {
            System.setProperty(this.JDBC_NAME, value);
            String driver = jdbcUtil.getJdbcDriverName(value);
            if (driver != null) {
                System.setProperty(this.JDBC_DRIVER, driver);
            }
            String urlPattern = jdbcUtil.getJdbcUrlPattern(value);
            if (urlPattern != null) {
                System.setProperty(this.JDBC_URL, urlPattern);
            }
        } else if (name.equals("driver")) {
            System.setProperty(this.JDBC_DRIVER, value);
        } else if (name.equals("url")) {
            System.setProperty(this.JDBC_URL, value);
        } else if (name.equals("user")) {
            System.setProperty(this.JDBC_USER, value);
        } else if (name.equals("password")) {
            System.setProperty(this.JDBC_PASSWORD, value);
        } else if (name.equals("sql")) {
            String commandLine = getCommandLine();
            int i = commandLine.indexOf("sql=");
            String sql = commandLine.substring(i + 4);
            if (sql.startsWith("$sql;")) {
                sql = sql.substring(4);
                sql = System.getProperty(this.JDBC_SQL) + sql;
            }
            System.setProperty(this.JDBC_SQL, sql);
        } else {
            String urlPattern = System.getProperty(this.JDBC_URL);
            message = buildURL(urlPattern, name, value);
        }
        getOut().println(message);
    }

    /**
     * Construct the url from url pattern.
     * 
     * @param pattern The url pattern
     * @param name The attribute's name
     * @param value The attribute's value
     * 
     * @return The message to user.
     */
    private String buildURL(String pattern, String name, String value) {
        if (pattern == null) {
            return "Set the url first.";
        }
        String retMessage = "";
        int i = pattern.indexOf("<" + name + ">");
        if (i >= 0) {
            String url = pattern.substring(0, i);
            url = url + value;
            url = url + pattern.substring(i + 1 + name.length() + 1);
            System.setProperty(this.JDBC_URL, url);
            retMessage = "Set " + name + " successfully.";
        } else {
            retMessage = "No such attribute in URL.";
        }
        return retMessage;
    }

    /**
     * Shows a JDBC property
     * 
     * @param name The property name.
     */
    private void showJDBCProperty(String name) {
        String pName = "";
        String value = "";
        if (name.equals("name")) {
            pName = this.JDBC_NAME;
        } else if (name.equals("driver")) {
            pName = this.JDBC_DRIVER;
        } else if (name.equals("url")) {
            pName = this.JDBC_URL;
        } else if (name.equals("user")) {
            pName = this.JDBC_USER;
        } else if (name.equals("password")) {
            pName = this.JDBC_PASSWORD;
        } else if (name.equals("sql")) {
            pName = this.JDBC_SQL;
        } else {
            pName = "No such property!";
            value = " ";
        }
        value = value.equals(" ") ? value : ("=" + System.getProperty(pName));
        getOut().println(pName + value);
    }
}
