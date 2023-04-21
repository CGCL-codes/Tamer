package jws.core.config;

import org.dom4j.Document;
import org.dom4j.Element;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.io.ByteArrayInputStream;
import jws.util.*;

/**
 * Manages module-wide configuration.<br></br><br></br>
 * The module-wide configuration must be provided in an XML file that is loaded as
 * resource the first time a {@link jws.core.config.JwsConfiguration} method is called.
 * The actual name of the resource may be provided via the Java system property
 * <code>jws.module.config</code> like this:
 * <pre>
 * java -Djws.module.config=config/module.xml org.your.MainClass
 * </pre>
 * In the example above the configuration will be loaded from resource with name
 * <code>config/module.xml</code>.<br></br><br></br>
 * If the <code>jws.module.config</code> property is not specified, than the default
 * resource name <code>jws-module.xml</code> is used.<br></br><br></br>
 * Please, refer to files <code>jws-module.xsd</code> and <code>jws-module.xml.sample</code>
 * located in the <code>jws-core/resources</code> folder of your JWS framework installation
 * to get the idea of how to configure your module.
 */
public final class JwsConfiguration {

    private static String _name;

    private static HashMap<String, Object> _parameters;

    private static LocalServicesConf _local;

    private static ArrayList<RemoteModuleRefConf> _remote;

    private static ArrayList<JobConfiguration> _jobs = new ArrayList<JobConfiguration>();

    private static HashMap<String, ServiceConfiguration> _services;

    private JwsConfiguration() {
    }

    /**
     * Returns the name of the module.
     * @return Name of this module
     * @throws ConfigurationException If there was an error loading module configuration
     */
    public static String getModuleName() throws ConfigurationException {
        if (_name == null) {
            loadConfiguration();
        }
        return _name;
    }

    /**
     * Returns the names of defined module-wide configuration parameters.
     * @return Array strings defining the list of defined module-wide configuration parameters 
     * @throws ConfigurationException If there was an error loading module configuration
     */
    public static Map<String, Object> getParameters() throws ConfigurationException {
        if (_parameters == null) {
            loadConfiguration();
        }
        return new HashMap<String, Object>(_parameters);
    }

    /**
     * Returns the value of a module-wide configuration parameter.
     * @param name Name of the parameter
     * @return The value of module-wide configuration parameter with name <code>name</code>, or
     * <code>null</code> if no parameter with such name is defined
     * @throws ConfigurationException If there was an error loading module configuration
     */
    public static Object getParameter(String name) throws ConfigurationException {
        if (_parameters == null) {
            loadConfiguration();
        }
        return _parameters.get(name);
    }

    /**
     * Returns the value of a module-wide configuration parameter.
     * @param name Name of the parameter
     * @param def Dafault value to return if there is no such parameter
     * @return The value of module-wide configuration parameter with name <code>name</code>, or
     * <code>def</code> if no parameter with such name is defined
     * @throws ConfigurationException If there was an error loading module configuration
     */
    public static Object getParameter(String name, Object def) throws ConfigurationException {
        Object result = getParameter(name);
        return result == null ? def : result;
    }

    /**
	 * Returns configured service by its name.
	 * @param name The name of service wich configuration to be returned of.
	 * @return The {@link jws.core.config.ServiceConfiguration} object that represents configuration of loaded service,
	 * specified by <i>name</i>
	 * @throws ConfigurationException If there was an error loading module configuration
	 */
    public static ServiceConfiguration getServiceConfiguration(String name) throws ConfigurationException {
        if (_services == null) {
            loadConfiguration();
        }
        return _services.get(name);
    }

    /**
     * Returns the list of configured services.
     * @return Array of {@link ServiceConfiguration} objects that describe
     * configured services
     * @throws ConfigurationException If there was an error loading module configuration
     */
    public static ServiceConfiguration[] getServiceConfigurations() throws ConfigurationException {
        if (_services == null) {
            loadConfiguration();
        }
        return _services.values().toArray(new ServiceConfiguration[_services.size()]);
    }

    /**
     * Returns the list of configured scheduled jobs.
     * @return Array of {@link JobConfiguration} objects that describe
     * configured scheduled jobs
     * @throws ConfigurationException If there was an error loading module configuration
     */
    public static JobConfiguration[] getJobConfigurations() throws ConfigurationException {
        if (_jobs == null) {
            loadConfiguration();
        }
        return _jobs.toArray(new JobConfiguration[_jobs.size()]);
    }

    private static void loadConfiguration() throws ConfigurationException {
        Document doc;
        String conf_res_name = System.getProperty("jws.module.config", "jws-module.xml");
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(ResourceUtils.loadBinaryResource(conf_res_name));
            doc = XmlUtils.readXML(input, new ResourceEntityResolver());
        } catch (Throwable th) {
            throw new ConfigurationException("Unable to read " + conf_res_name, th);
        }
        Element root = doc.getRootElement();
        _name = root.element("module-name").getTextTrim();
        Element pars = root.element("module-properties");
        _parameters = new HashMap<String, Object>();
        if (pars != null) {
            _parameters.putAll(ConfigurationObject.readParameters(pars));
        }
        _local = null;
        Element elem = root.element("local-services");
        if (elem != null) {
            _local = new LocalServicesConf(elem);
        }
        _remote = new ArrayList<RemoteModuleRefConf>();
        for (Object obj : root.elements("remote-module-ref")) {
            elem = (Element) obj;
            RemoteModuleRefConf conf = new RemoteModuleRefConf(elem);
            _remote.add(conf);
        }
        _services = new HashMap<String, ServiceConfiguration>();
        for (ServiceConf conf : _local.getServices()) {
            ServiceConfiguration info = new ServiceConfiguration(conf);
            if (_services.containsKey(info.getServiceName())) {
                continue;
            }
            _services.put(info.getServiceName(), info);
        }
        for (RemoteModuleRefConf ref : _remote) {
            for (ServiceProxyConf conf : ref.getProxies()) {
                ServiceConfiguration info = new ServiceConfiguration(conf);
                if (_services.containsKey(info.getServiceName())) {
                    continue;
                }
                _services.put(info.getServiceName(), info);
            }
        }
        ArrayList<JobConfiguration> jobs = new ArrayList<JobConfiguration>();
        Element jobsElem = root.element("scheduled-jobs");
        if (jobsElem != null) {
            for (Object obj : jobsElem.elements("job")) {
                elem = (Element) obj;
                JobConfiguration conf = new JobConfiguration(elem);
                jobs.add(conf);
            }
        }
        _jobs = jobs;
    }
}
