package net.sf.lightbound.online;

import java.util.Map;

/**
 * A wrapper class which presents a Map<String, String> as a PropertiesContainer
 * 
 * @author esa
 *
 */
public class MapPropertiesContainer extends PropertiesContainer {

    private final Map<String, String> propsMap;

    /**
   * Construct a new MapPropertiesContainer which wraps the specified map
   * @param propsMap the map to get the properties from
   */
    public MapPropertiesContainer(Map<String, String> propsMap) {
        this.propsMap = propsMap;
    }

    /**
   * @see PropertiesContainer
   */
    @Override
    public String getProperty(String key) {
        return propsMap.get(key);
    }
}
