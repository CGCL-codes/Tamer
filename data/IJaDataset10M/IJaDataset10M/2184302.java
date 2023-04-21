package jtmsmon.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author th
 */
public class DatabaseConfiguration {

    private static final DatabaseConfiguration singleton = new DatabaseConfiguration();

    /** Creates a new instance of DatabaseFactory */
    private DatabaseConfiguration() {
        configPath = System.getProperty("user.home");
        configPath += System.getProperty("file.separator");
        boolean addDot = true;
        String osName = System.getProperty("os.name");
        if (osName != null) {
            addDot = !osName.toUpperCase().startsWith("WINDOWS");
        }
        if (addDot) {
            configPath += ".";
        }
        configPath += "JTMSMon";
        System.out.println("JTMSMon configuration directory path = " + configPath);
        try {
            File configDir = new File(configPath);
            File configPropertiesFile = new File(configDir, "jtmsmon.db.xml");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            dbProperties = new Properties();
            if (configPropertiesFile.exists()) {
                dbProperties.loadFromXML(new FileInputStream(configPropertiesFile));
            } else {
                String password = getRandomPassword(16);
                dbProperties.put("jtmsmon.db.class", "jtmsmon.javadb.JavaDB");
                dbProperties.put("jtmsmon.db.user", "jtmsdb");
                dbProperties.put("jtmsmon.db.password", password);
                dbProperties.put("jtmsmon.db.host", "localhost");
                dbProperties.put("jtmsmon.db.port", "1527");
                dbProperties.put("jtmsmon.db.url", "*undefined*");
                dbProperties.put("jtmsmon.rmi.port", "3099");
                dbProperties.storeToXML(new FileOutputStream(configPropertiesFile), "JTMSMON Database configuration", "ISO-8859-1");
            }
            user = dbProperties.getProperty("jtmsmon.db.user", "jtmsdb");
            password = dbProperties.getProperty("jtmsmon.db.password", password);
            host = dbProperties.getProperty("jtmsmon.db.host", "localhost");
            dbClassName = dbProperties.getProperty("jtmsmon.db.class", "jtmsmon.javadb.JavaDB");
            url = dbProperties.getProperty("jtmsmon.db.url", "*undefined*");
            port = 0;
            try {
                port = Integer.parseInt(dbProperties.getProperty("jtmsmon.db.port", "1527"));
            } catch (NumberFormatException ne) {
            }
            rmiPort = 0;
            try {
                rmiPort = Integer.parseInt(dbProperties.getProperty("jtmsmon.rmi.port", "3099"));
            } catch (NumberFormatException ne) {
            }
            System.out.println("     db class = " + dbClassName);
            System.out.println("      db user = " + user);
            System.out.println("  db password = " + password);
            System.out.println("      db host = " + host);
            System.out.println("      db port = " + port);
            System.out.println("       db url = " + url);
            System.out.println("     rmi port = " + rmiPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public static String getConfigurationDirectoryPath() {
        return singleton.configPath;
    }

    /**
   * Method description
   *
   *
   * @return
   *
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
    public static JTMSDB getDatabase() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class dbClass = Class.forName(singleton.dbClassName);
        return (JTMSDB) dbClass.newInstance();
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public static String getDbClassName() {
        return singleton.dbClassName;
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public static String getHost() {
        return singleton.host;
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public static String getPassword() {
        return singleton.user;
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public static int getPort() {
        return singleton.port;
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public static int getRMIPort() {
        return singleton.rmiPort;
    }

    /**
   * Method description
   *
   *
   * @param numberOfChars
   *
   * @return
   */
    public static String getRandomPassword(int numberOfChars) {
        final char[] pwChars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
        Random random = new Random(System.currentTimeMillis());
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < numberOfChars; i++) {
            buffer.append(pwChars[random.nextInt(pwChars.length)]);
        }
        return buffer.toString();
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public static String getUrlString() {
        return singleton.url;
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public static String getUser() {
        return singleton.user;
    }

    private String configPath;

    private String dbClassName;

    private Properties dbProperties;

    private String host;

    private String password;

    private int port;

    private int rmiPort;

    private String url;

    private String user;
}
