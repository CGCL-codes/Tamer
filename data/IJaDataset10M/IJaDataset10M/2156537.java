package relex.parser_old;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class RelexProperties {

    public static int verbosity = 1;

    private static Properties props;

    public static Properties getProps() {
        return props;
    }

    public static String getProperty(String prop) {
        return props.getProperty(prop);
    }

    public static void setProperty(String prop, String val) {
        props.setProperty(prop, val);
    }

    public static int getIntProperty(String prop) {
        return Integer.parseInt(props.getProperty(prop));
    }

    private static void setDefaultValues() {
        setProperty("relex.parser.LinkParserSocketClient.parseCountBetweenKills", "0");
        setProperty("relex.parser.LinkParserSocketClient.millisecondsBetweenConnectionAttempts", "5000");
        setProperty("relex.parser.LinkParserSocketClient.failedRequestRepeatLimit", "3");
        String pathname = System.getProperty("relex.parser.LinkParser.pathname");
        if (pathname != null) setProperty("relex.parser.LinkParser.pathname", pathname);
    }

    private static void loadPropertiesIfRequired() {
        String loadFilename = System.getProperty("relex.RelexProperties.loadFilename");
        if (loadFilename != null && loadFilename.length() > 0) {
            if (verbosity >= 1) System.err.println("RelexProperties: loading properties from " + loadFilename);
            try {
                props.load(new FileInputStream(new File(loadFilename)));
                if (verbosity >= 1) System.err.println(RelexProperties.toPropsString());
            } catch (Exception e) {
                System.err.println("Property load failed.");
                e.printStackTrace();
            }
        }
    }

    private static void storePropertiesIfRequired() {
        String storeFilename = System.getProperty("relex.RelexProperties.storeFilename");
        if (storeFilename != null && storeFilename.length() > 0) {
            if (verbosity >= 1) System.err.println("RelexProperties: storing properties in " + storeFilename);
            try {
                props.store(new FileOutputStream(new File(storeFilename)), "RelexProperties");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String toPropsString() {
        return props.toString();
    }

    static {
        props = new Properties();
        setDefaultValues();
        loadPropertiesIfRequired();
        storePropertiesIfRequired();
    }
}
