package org.apache.nutch.parse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a natural ordering for which parsing plugin should get
 * called for a particular mimeType. It provides methods to store the
 * parse-plugins.xml data, and methods to retreive the name of the appropriate
 * parsing plugin for a contentType.
 *
 * @author mattmann
 * @version 1.0
 */
class ParsePluginList {

    private Map fMimeTypeToPluginMap = null;

    private Map aliases = null;

    /**
   * Constructs a new ParsePluginList
   */
    ParsePluginList() {
        fMimeTypeToPluginMap = new HashMap();
        aliases = new HashMap();
    }

    List getPluginList(String mimeType) {
        return (List) fMimeTypeToPluginMap.get(mimeType);
    }

    void setAliases(Map aliases) {
        this.aliases = aliases;
    }

    Map getAliases() {
        return aliases;
    }

    void setPluginList(String mimeType, List l) {
        fMimeTypeToPluginMap.put(mimeType, l);
    }

    List getSupportedMimeTypes() {
        return Arrays.asList(fMimeTypeToPluginMap.keySet().toArray(new String[] {}));
    }
}
