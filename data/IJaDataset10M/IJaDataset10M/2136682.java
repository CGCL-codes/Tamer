package de.miethxml.toolkit.conf;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 *
 */
public class StoreableConfigurationImpl implements StoreableConfiguration {

    private DefaultConfiguration conf;

    public StoreableConfigurationImpl(Configuration conf) {
        this.conf = new DefaultConfiguration("hawron");
        this.conf.addAll(conf);
    }

    public StoreableConfigurationImpl() {
        this.conf = new DefaultConfiguration("hawron");
        wrapConfig();
    }

    public void setConfiguration(Class clazz, Configuration conf) {
        this.conf.addChild(conf);
    }

    public void setConfiguration(String key, Hashtable parameters) {
    }

    public void store() {
    }

    public Configuration getConfiguration() {
        return conf;
    }

    private void wrapConfig() {
        Properties props = ConfigManager.getInstance().getProperties();
        Enumeration e = props.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            conf.setAttribute(key, (String) props.get(key));
        }
    }
}
