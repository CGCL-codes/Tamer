package kanjitori.abstraction;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @param Content 
 * @author DGronau
 */
public class AbstractFactoryImpl<Content> implements AbstractFactory<Content> {

    private Map<String, Factory<Content>> map = new HashMap<String, Factory<Content>>();

    /**
     * Registers a MapLoader to be called with the given extension.
     * @param ext The File extension
     * @param loader The MapLoader
     */
    public void register(String ext, Factory<Content> loader) {
        map.put(ext.toLowerCase(), loader);
    }

    /**
     * Returns the "known" extensions.
     * @return A String list of all registered extensions (in lowercase)
     */
    public String[] getKeys() {
        String[] extensions = new String[map.size()];
        int i = 0;
        for (String s : map.keySet()) {
            extensions[i++] = s;
        }
        return extensions;
    }

    public void unregister(String key) {
        map.remove(key);
    }

    public void unregisterAll() {
        map.clear();
    }

    public Factory<Content> getFactory(String key) {
        return map.get(key);
    }
}
