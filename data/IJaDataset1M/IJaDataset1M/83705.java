package org.ourgrid.aggregator;

import java.util.Map;
import org.ourgrid.common.OurGridContextFactory;
import org.ourgrid.reqtrace.Req;
import br.edu.ufcg.lsd.commune.ApplicationProperties;
import br.edu.ufcg.lsd.commune.container.contextfactory.ContextParser;

public class AggregatorComponentContextFactory extends OurGridContextFactory {

    private final String CONF_DIR = findConfDir();

    public AggregatorComponentContextFactory(ContextParser parser) {
        super(parser);
    }

    @Override
    public Map<Object, Object> getDefaultProperties() {
        Map<Object, Object> properties = super.getDefaultProperties();
        properties.put(ApplicationProperties.PROP_CONFDIR, CONF_DIR);
        properties.put(AggregatorConfiguration.PROP_LOG_PROPERTIES_FILE, "aggregator.log.properties");
        properties.put(AggregatorConfiguration.PROP_LOGFILE, "aggregator.log");
        properties.put(AggregatorConfiguration.PROP_DS_USERNAME, AggregatorConfiguration.DEFAULT_DS_USERNAME);
        properties.put(AggregatorConfiguration.PROP_DS_SERVERNAME, AggregatorConfiguration.DEFAULT_DS_SERVERNAME);
        return properties;
    }

    /**
	 * Returns the configuration directory.
	 * 
	 * @return The directory.
	 */
    @Req("REQ010")
    public String findConfDir() {
        String prop = System.getProperty("OGROOT");
        if (prop == null || prop.equals("")) {
            prop = System.getProperty("user.dir");
        }
        return prop;
    }
}
