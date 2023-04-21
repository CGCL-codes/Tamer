package de.etqw.openranked.service.impl;

import java.security.InvalidParameterException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import de.etqw.openranked.service.ConfigService;

public class ConfigServiceImpl implements ConfigService {

    private static final Logger log = Logger.getLogger(ConfigServiceImpl.class);

    public ConfigurableListableBeanFactory createBeanFactory(String[] _xmlFiles, String _pathToConfiguration) {
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext(_xmlFiles, false);
        PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
        Resource resource = getConfigurationLocation(_pathToConfiguration, ac);
        if (resource == null) {
            throw new InvalidParameterException("Could not load OpenRanked configuration");
        }
        cfg.setLocation(resource);
        ac.getBeanFactoryPostProcessors().add(cfg);
        ac.refresh();
        return ac.getBeanFactory();
    }

    /**
     * Returns the configuration location. If not specified,
     * {@link ConfigService#OPENRANKED_DEFAULT_CONFIGURATION} will be used
     * 
     * @param _path
     * @param _applicationContext
     * @return
     */
    public Resource getConfigurationLocation(String _path, ApplicationContext _applicationContext) {
        Resource r = null;
        if (!_path.startsWith("file:")) {
            _path = "file:" + _path;
        }
        r = _applicationContext.getResource(_path);
        if (!r.exists()) {
            log.error("specified OpenRanked configuration [" + _path + "] does not exist");
            _path = OPENRANKED_DEFAULT_CONFIGURATION;
            log.info("using internal configuration [" + _path + "]");
            r = _applicationContext.getResource(_path);
            if (!r.exists()) {
                throw new InvalidParameterException("Could not load internal property file [" + _path + "]!");
            }
        }
        return r;
    }
}
