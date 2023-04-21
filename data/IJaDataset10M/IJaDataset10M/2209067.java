package org.ourgrid.aggregator;

import static java.io.File.separator;
import static org.ourgrid.common.interfaces.Constants.LINE_SEPARATOR;
import org.ourgrid.common.config.Configuration;

public class AggregatorConfiguration extends Configuration {

    private static final long serialVersionUID = 1L;

    public static final String AGGREGATOR = AggregatorConfiguration.class.getName();

    public static final String PREFIX = "aggregator.";

    public static final String CONF_DIR = findConfDir();

    public static final String PROPERTIES_FILENAME = CONF_DIR + separator + "aggregator.properties";

    public static final String DEFAULT_AGG_USERNAME = "aggregator";

    public static final String DEFAULT_AGG_SERVERNAME = "xmpp.ourgrid.org";

    public static final String PROP_AGG_USERNAME = "username";

    public static final String PROP_AGG_SERVERNAME = "servername";

    public static final String PROP_DS_USERNAME = PREFIX + "ds.username";

    public static final String PROP_DS_SERVERNAME = PREFIX + "ds.servername";

    public static final String DEFAULT_DS_USERNAME = "lsd-ds";

    public static final String DEFAULT_DS_SERVERNAME = "xmpp.ourgrid.org";

    @Override
    protected String getPrefix() {
        return PREFIX;
    }

    @Override
    public String getConfDir() {
        return CONF_DIR;
    }

    @Override
    public String getLogPath() {
        String logfile = getProperty(PROP_LOGFILE);
        if (logfile == null) {
            logfile = getConfDir() + separator + "log" + separator + "aggregator.log";
        }
        return logfile;
    }

    public static String findConfDir() {
        String property = System.getProperty("OGROOT");
        return property == null ? "." : property;
    }

    @Override
    public String toString() {
        StringBuilder conf = new StringBuilder(super.toString());
        conf.append("\tAggregator username: ");
        conf.append(this.getProperty(PROP_AGG_USERNAME));
        conf.append(LINE_SEPARATOR);
        conf.append("\tAggregator servername: ");
        conf.append(this.getProperty(PROP_AGG_SERVERNAME));
        conf.append(LINE_SEPARATOR);
        conf.append("\tDiscoveryService username: ");
        conf.append(this.getProperty(PROP_DS_USERNAME));
        conf.append(LINE_SEPARATOR);
        conf.append("\tDiscoveryService servername: ");
        conf.append(this.getProperty(PROP_DS_SERVERNAME));
        conf.append(LINE_SEPARATOR);
        return conf.toString();
    }
}
