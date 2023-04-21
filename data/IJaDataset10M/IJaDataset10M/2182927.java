package org.codecompany.jeha.populator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.codecompany.jeha.core.Handler;
import org.codecompany.jeha.util.OrderedProperties;
import org.codecompany.jeha.util.Utils;

public class PropertiesPopulator implements Populator {

    private static Logger log = LoggerFactory.getLogger(PropertiesPopulator.class);

    private String propertiesFile;

    public PropertiesPopulator() {
        propertiesFile = "";
    }

    public PropertiesPopulator(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public void populate(Handler handler) {
        if (propertiesFile == null || "".equals(propertiesFile.trim())) {
            propertiesFile = handler.getClass().getSimpleName() + ".properties";
        }
        if (!propertiesFile.endsWith(".properties")) {
            propertiesFile += ".properties";
        }
        Map<String, Object> properties = beanProperties(handler, propertiesFile);
        try {
            BeanUtils.populate(handler, properties);
        } catch (Exception e) {
            log.error("Error populating " + handler, e);
        }
    }

    private Map<String, Object> beanProperties(Handler handler, String propertiesFile) {
        Map<String, Object> map = new HashMap<String, Object>();
        OrderedProperties properties = Utils.load(propertiesFile);
        String beanClass = handler.getClass().getName();
        for (Entry<String, String> entry : properties.getProperties().entrySet()) {
            String property = entry.getKey();
            if (property.startsWith(beanClass)) {
                property = property.substring(beanClass.length() + 1);
            } else {
            }
            log.debug("Property definition found for {} [{}={}]", beanClass, property);
            Object value = instantiate(entry.getValue());
            map.put(property, value);
        }
        return map;
    }

    private Object instantiate(String value) {
        Object result = value;
        if (value.contains(".")) {
            try {
                Class<?> clazz = Utils.forName(value);
                result = clazz.newInstance();
                log.debug("Class '{}' instantiated: {}", value, result);
            } catch (InstantiationException e) {
                log.error("Can't instantiate class '" + value + "'", e);
            } catch (IllegalAccessException e) {
                log.error("Can't instantiate class '" + value + "'", e);
            }
        }
        return result;
    }
}
