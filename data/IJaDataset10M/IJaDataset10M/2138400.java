package org.jpublish.util.encoding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.ConfigurationException;
import org.jpublish.util.PathUtilities;

/**
 * Manager which controls mapping character encodings to request paths.
 *
 * @author Anthony Eden
 */
public class CharacterEncodingManager {

    /**
     * The default page encoding (ISO-8859-1).
     */
    public static final String DEFAULT_PAGE_ENCODING = "ISO-8859-1";

    /**
     * The default template encoding (ISO-8859-1).
     */
    public static final String DEFAULT_TEMPLATE_ENCODING = "ISO-8859-1";

    /**
     * The default request encoding (ISO-8859-1).
     */
    public static final String DEFAULT_REQUEST_ENCODING = "ISO-8859-1";

    /**
     * The default response encoding (ISO-8859-1).
     */
    public static final String DEFAULT_RESPONSE_ENCODING = "ISO-8859-1";

    private List characterEncodingMaps;

    private CharacterEncodingMap defaultMap;

    /**
     * Construct a new CharacterEncodingManager.
     */
    public CharacterEncodingManager() {
        characterEncodingMaps = new ArrayList();
        loadDefaultCharacterEncodingMap();
    }

    /**
     * Get the CharacterEncodingMap for the given path.  If the no map is found for that path then the default map is
     * used.
     *
     * @param path The path
     * @return The CharacterEncodingMap
     */
    public CharacterEncodingMap getMap(String path) {
        Iterator iter = characterEncodingMaps.iterator();
        while (iter.hasNext()) {
            CharacterEncodingMap map = (CharacterEncodingMap) iter.next();
            if (PathUtilities.match(path, map.getPath())) {
                return map;
            }
        }
        return getDefaultMap();
    }

    /**
     * Get the default character encoding map.
     *
     * @return The default character encoding map
     */
    public CharacterEncodingMap getDefaultMap() {
        return defaultMap;
    }

    /**
     * Set the default character encoding map.
     *
     * @param defaultMap The default character encoding map
     */
    public void setDefaultMap(CharacterEncodingMap defaultMap) {
        this.defaultMap = defaultMap;
    }

    /**
     * Load the character encoding configuration.
     *
     * @param configuration The configuration object
     * @throws ConfigurationException
     */
    public void loadConfiguration(Configuration configuration) throws ConfigurationException {
        loadCharacterEncodingMaps(configuration.getChildren("character-encoding-map"));
    }

    /**
     * Load all character encodings.
     *
     * @param configurationElements The Configuration element list
     */
    private void loadCharacterEncodingMaps(List configurationElements) {
        characterEncodingMaps.clear();
        Iterator iter = configurationElements.iterator();
        while (iter.hasNext()) {
            CharacterEncodingMap characterEncodingMap = new CharacterEncodingMap();
            Configuration mapElement = (Configuration) iter.next();
            characterEncodingMap.setPath(mapElement.getAttribute("path"));
            characterEncodingMap.setPageEncoding(mapElement.getChildValue("page-encoding"));
            characterEncodingMap.setTemplateEncoding(mapElement.getChildValue("template-encoding"));
            characterEncodingMap.setRequestEncoding(mapElement.getChildValue("request-encoding"));
            characterEncodingMap.setResponseEncoding(mapElement.getChildValue("response-encoding"));
            characterEncodingMaps.add(characterEncodingMap);
        }
    }

    /**
     * Load the default character encoding map.  The default map uses ISO-8859-1 as the encoding for all data.
     */
    private void loadDefaultCharacterEncodingMap() {
        defaultMap = new CharacterEncodingMap();
        defaultMap.setPageEncoding(DEFAULT_PAGE_ENCODING);
        defaultMap.setTemplateEncoding(DEFAULT_TEMPLATE_ENCODING);
        defaultMap.setRequestEncoding(DEFAULT_REQUEST_ENCODING);
        defaultMap.setResponseEncoding(DEFAULT_RESPONSE_ENCODING);
    }
}
