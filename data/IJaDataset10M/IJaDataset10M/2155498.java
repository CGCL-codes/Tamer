package uk.ac.ncl.cs.instantsoap.wsapi.impl;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import uk.ac.ncl.cs.instantsoap.strategydp.StrategyDispatcher;
import uk.ac.ncl.cs.instantsoap.strategydp.impl.MultiplexingStrategyDispatcher;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A configurator that uses a spring context to find dispatchers.
 *
 * @author Matthew Pocock
 */
public class SpringDispatcherConfigurator implements InitializingBean {

    private static final Logger LOG = Logger.getLogger(SpringDispatcherConfigurator.class.getName());

    private MultiplexingStrategyDispatcher dispatcher;

    public SpringDispatcherConfigurator() {
    }

    public MultiplexingStrategyDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(MultiplexingStrategyDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void afterPropertiesSet() throws Exception {
        LOG.info("Adding dispatchers to: " + dispatcher);
        if (dispatcher == null) {
            return;
        }
        ClassLoader cLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> cfgs = cLoader.getResources("META-INF/spring/uk.ac.ncl.cs.instantsoap.strategydp.impl.SpringDispatcherConfigurator");
        while (cfgs.hasMoreElements()) {
            URL cfg = cfgs.nextElement();
            LOG.info("Processing configuration URL: " + cfg);
            Resource res = new UrlResource(cfg);
            DefaultListableBeanFactory factory = new XmlBeanFactory(res);
            Map<String, StrategyDispatcher> beans = factory.getBeansOfType(StrategyDispatcher.class);
            for (StrategyDispatcher sd : beans.values()) {
                LOG.info("importing strategy dispatcher: " + sd);
                dispatcher.registerHandler(sd);
            }
        }
    }
}
