package org.ogce.gfac.services.impl;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Properties;
import org.ogce.gfac.context.InvocationContext;
import org.ogce.gfac.exception.GfacException;
import org.ogce.gfac.exception.GfacException.FaultCode;
import org.ogce.gfac.extension.DataServiceChain;
import org.ogce.gfac.extension.ExitableChain;
import org.ogce.gfac.extension.PostExecuteChain;
import org.ogce.gfac.extension.PreExecuteChain;
import org.ogce.gfac.registry.RegistryService;
import org.ogce.gfac.registry.impl.XregistryServiceWrapper;
import org.ogce.gfac.scheduler.Scheduler;

/**
 * This generic service implementation will load Registry service and Data
 * Catalog from property file. It selects provider and execute it base on
 * execution context.
 * 
 * @author Patanachai Tangchaisin
 * 
 */
public class PropertiesBasedServiceImpl extends AbstractSimpleService {

    private static final String FILENAME = "service.properties";

    public static final String REGISTY_URL_NAME = "registryURL";

    public static final String SSL_TRUSTED_CERTS_FILE = "ssl.trustedCertsFile";

    public static final String SSL_HOSTCERTS_KEY_FILE = "ssl.hostcertsKeyFile";

    public static final String SCHEDULER_CLASS = "scheduler.class";

    public static final String DATA_CHAIN_CLASS = "datachain.classes";

    public static final String PRE_CHAIN_CLASS = "prechain.classes";

    public static final String POST_CHAIN_CLASS = "postchain.classes";

    private Properties properties;

    private Scheduler scheduler;

    private PreExecuteChain[] preChain;

    private PostExecuteChain[] postChain;

    private DataServiceChain[] dataChain;

    private RegistryService registryService;

    public void init() throws GfacException {
        try {
            URL url = ClassLoader.getSystemResource(FILENAME);
            this.properties = new Properties();
            this.properties.load(url.openStream());
            String registryURL = loadFromProperty(REGISTY_URL_NAME, true);
            String trustcerts = loadFromProperty(SSL_TRUSTED_CERTS_FILE, true);
            String hostcerts = loadFromProperty(SSL_HOSTCERTS_KEY_FILE, true);
            this.registryService = new XregistryServiceWrapper(registryURL, trustcerts, hostcerts);
        } catch (Exception e) {
            throw new GfacException("Error initialize the generic service", e);
        }
    }

    public void dispose() throws GfacException {
    }

    @Override
    public void preProcess(InvocationContext context) throws GfacException {
        context.getExecutionContext().setRegistryService(this.registryService);
    }

    @Override
    public void postProcess(InvocationContext context) throws GfacException {
    }

    public Scheduler getScheduler(InvocationContext context) throws GfacException {
        String className = null;
        if (this.scheduler == null) {
            className = loadFromProperty(SCHEDULER_CLASS, true);
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Class<? extends Scheduler> spiClass;
                if (classLoader == null) {
                    spiClass = Class.forName(className).asSubclass(Scheduler.class);
                } else {
                    spiClass = classLoader.loadClass(className).asSubclass(Scheduler.class);
                }
                this.scheduler = spiClass.newInstance();
            } catch (ClassNotFoundException ex) {
                throw new GfacException("Scheduler " + className + " not found", ex);
            } catch (Exception ex) {
                throw new GfacException("Scheduler " + className + " could not be instantiated: " + ex, ex);
            }
        }
        return this.scheduler;
    }

    public PreExecuteChain[] getPreExecutionSteps(InvocationContext context) throws GfacException {
        if (this.preChain == null) {
            this.preChain = loadClassFromProperties(PRE_CHAIN_CLASS, PreExecuteChain.class);
        }
        return preChain;
    }

    public PostExecuteChain[] getPostExecuteSteps(InvocationContext context) throws GfacException {
        if (this.postChain == null) {
            this.postChain = loadClassFromProperties(POST_CHAIN_CLASS, PostExecuteChain.class);
        }
        return postChain;
    }

    public DataServiceChain[] getDataChains(InvocationContext context) throws GfacException {
        if (this.dataChain == null) {
            this.dataChain = loadClassFromProperties(DATA_CHAIN_CLASS, DataServiceChain.class);
        }
        return dataChain;
    }

    /**
	 * 
	 * @param propertyName
	 * @param required
	 * @return
	 * @throws GfacException
	 */
    private String loadFromProperty(String propertyName, boolean required) throws GfacException {
        String propValue = this.properties.getProperty(propertyName);
        if (propValue == null) {
            if (required) throw new GfacException("Property \"" + propertyName + "\" is not found", FaultCode.InvalidConfig);
            return null;
        }
        return propValue;
    }

    /**
	 * 
	 */
    @SuppressWarnings("unchecked")
    private <T> T[] loadClassFromProperties(String propertyName, Class<? extends ExitableChain> type) throws GfacException {
        String propValue = loadFromProperty(propertyName, false);
        if (propValue == null) {
            return null;
        }
        String classNames[] = this.properties.getProperty(propertyName).split(",");
        T[] chain = (T[]) Array.newInstance(type, classNames.length);
        for (int i = 0; i < classNames.length; i++) {
            String className = classNames[i];
            System.out.println(className);
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Class<? extends ExitableChain> spiClass;
                if (classLoader == null) {
                    spiClass = Class.forName(className).asSubclass(ExitableChain.class);
                } else {
                    spiClass = classLoader.loadClass(className).asSubclass(ExitableChain.class);
                }
                chain[i] = (T) spiClass.newInstance();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return chain;
    }
}
