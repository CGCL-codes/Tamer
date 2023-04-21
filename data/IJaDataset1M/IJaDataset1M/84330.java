package org.jazzteam.poc.jpatterns.patterns.factory.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jazzteam.poc.jpatterns.core.configuration.IPatternConfigurationProvider;
import org.jazzteam.poc.jpatterns.patterns.factory.ConfigForFactoryPattern;
import org.jazzteam.poc.jpatterns.patterns.factory.FactoryPatternConstants;
import com.zmicer.utils.InputArgumentUtils;

/**
 * This class just helper for more comfortable working with Factory
 * 
 * $Author:: $<br/>
 * $Rev:: $<br/>
 * $Date:: $<br/>
 */
public class FactoryPatternHelper {

    /**
	 * Logger instance.
	 */
    public static final Logger LOG = Logger.getLogger(FactoryPatternHelper.class);

    private static IPatternConfigurationProvider<ConfigForFactoryPattern, String, String> configProvider = null;

    private static boolean isConfigSet = false;

    public static String getJVMParamFactoryClassName(final String factoryClassName) {
        return FactoryPatternConstants.JVM_FACTORY_BASE + factoryClassName;
    }

    private static void isInitialized() throws IllegalStateException {
        if (!FactoryPatternHelper.isConfigSet) {
            throw new IllegalStateException("ConfigProvider should be set before setting factory name");
        }
    }

    public static String getJVMParamFactoryIsInstanceSettins(final String factoryClassName) {
        return FactoryPatternHelper.getJVMParamFactoryClassName(factoryClassName) + FactoryPatternConstants.CREATION_USING_GETINSTANCE_METHOD_SUFFIX;
    }

    public static void setConfigurationProvider(final IPatternConfigurationProvider<ConfigForFactoryPattern, String, String> configurationProvider) {
        InputArgumentUtils.checkObjects(configurationProvider);
        FactoryPatternHelper.configProvider = configurationProvider;
        FactoryPatternHelper.isConfigSet = true;
    }

    /**
	 * Main method of this class
	 * 
	 * @param className
	 */
    public static void setClassName(final String className) {
        FactoryPatternHelper.isInitialized();
        final Map<String, String> settings = new HashMap<String, String>();
        settings.put("", FactoryPatternHelper.getJVMParamFactoryClassName(className));
        FactoryPatternHelper.configProvider.applyConfiguration(settings);
    }
}
