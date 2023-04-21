package com.javector.adaptive.framework;

import com.javector.soaj.SoajRuntimeException;
import java.util.Map;
import java.util.HashMap;

public class ConfiguratorFactory {

    private static Map<String, Configurator> configurationMapping = new HashMap<String, Configurator>();

    public static Configurator DEFAULT_JAXB_CONFIGURATOR;

    static {
        DEFAULT_JAXB_CONFIGURATOR = createConfigurator("config/SOAJaxbHandlerMapping.xml");
    }

    public static Configurator createConfigurator(String filePath) {
        Configurator configurator;
        configurator = configurationMapping.get(filePath);
        if (configurator != null) {
            return configurator;
        }
        configurator = ConfigMapper.parseConfigurationDetail(filePath);
        configurationMapping.put(filePath, configurator);
        return configurator;
    }
}
