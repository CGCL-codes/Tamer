package lt.baltic_amadeus.jqbridge.providers.jms;

import javax.jms.QueueConnectionFactory;
import org.apache.log4j.Logger;
import lt.baltic_amadeus.jqbridge.Version;
import lt.baltic_amadeus.jqbridge.providers.Port;
import lt.baltic_amadeus.jqbridge.providers.Provider;
import lt.baltic_amadeus.jqbridge.providers.ProviderEnv;
import lt.baltic_amadeus.jqbridge.server.BridgeException;
import lt.baltic_amadeus.jqbridge.server.Config;
import lt.baltic_amadeus.jqbridge.server.MessageLogger;
import lt.baltic_amadeus.jqbridge.server.ProviderConfigurationException;

/**
 * 
 * @author Baltic Amadeus, JSC
 * @author Antanas Kompanas
 *
 */
public class JMSProvider implements Provider {

    private static final Version providerVersion = new Version("1.4");

    private static final Logger log = Logger.getLogger(JMSProvider.class);

    private ProviderEnv env;

    private Class factoryClass;

    public JMSProvider() {
        log.info("JMS Provider plugin version " + JMSProvider.getVersion() + " initializing");
    }

    public static Version getVersion() {
        return providerVersion;
    }

    public Config getConfig() {
        return env.getConfig();
    }

    public String getName() {
        return env.getName();
    }

    public MessageLogger getMessageLogger() {
        return env.getMessageLogger();
    }

    public void init(ProviderEnv env) throws BridgeException {
        this.env = env;
        Config conf = env.getConfig();
        String name = env.getName();
        String pfx = "provider." + name + ".";
        try {
            String factoryClassName = conf.getString(pfx + "factory");
            factoryClass = Class.forName(factoryClassName);
            QueueConnectionFactory factory = (QueueConnectionFactory) factoryClass.newInstance();
            if (factory == null) throw new ProviderConfigurationException(name, "factory class instance is null");
        } catch (ClassNotFoundException ex) {
            throw new ProviderConfigurationException(name, "factory class not found", ex);
        } catch (InstantiationException ex) {
            throw new ProviderConfigurationException(name, "factory cannot be created", ex);
        } catch (IllegalAccessException ex) {
            throw new ProviderConfigurationException(name, "factory is not accessible", ex);
        }
    }

    public Port createPort(String name) throws BridgeException {
        return new JMSPort(this, name);
    }

    public QueueConnectionFactory createConnectionFactory(String portName) throws BridgeException {
        try {
            return (QueueConnectionFactory) factoryClass.newInstance();
        } catch (InstantiationException ex) {
            throw new ProviderConfigurationException(getName(), "factory cannot be created for port " + portName, ex);
        } catch (IllegalAccessException ex) {
            throw new ProviderConfigurationException(getName(), "factory is not accessible for port " + portName, ex);
        }
    }
}
