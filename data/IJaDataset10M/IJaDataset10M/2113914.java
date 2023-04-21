package org.blaps.erazer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author François JACOBS 26/04/2009
 */
public class Configuration {

    private static final Configuration instance = new Configuration();

    private Map<String, EraseMethod> eraseDefinition;

    private Configuration() {
        eraseDefinition = new HashMap<String, EraseMethod>();
        initialize();
    }

    private void initialize() {
    }
}
