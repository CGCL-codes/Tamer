package sk.yw.azetclient.preferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author error216
 */
public class Preferences {

    private static final Logger logger = Logger.getLogger(Preferences.class);

    private Map<String, String> initialValuesMap = new HashMap<String, String>();

    private Map<String, String> valuesMap = new HashMap<String, String>();

    private Set<Handler> handlers = new HashSet<Handler>();

    private Map<String, Handler> handlersMap = new HashMap<String, Handler>();

    public void add(String name, String initialValue, Handler handler) {
        initialValuesMap.put(name, initialValue);
        valuesMap.put(name, initialValue);
        handlers.add(handler);
        handlersMap.put(name, handler);
    }

    public void remove(String name) {
        valuesMap.remove(name);
        handlersMap.remove(name);
    }

    public void setValue(String name, String newValue) {
        valuesMap.put(name, newValue);
    }

    public Map<String, String> getChanged() {
        Map<String, String> changed = new HashMap<String, String>();
        for (String name : valuesMap.keySet()) {
            if (!valuesMap.get(name).equals(initialValuesMap.get(name))) {
                changed.put(name, valuesMap.get(name));
            }
        }
        return changed;
    }

    public void apply() {
        logger.info("Applying settings.");
        for (Handler handler : handlers) {
            handler.beginApply();
        }
        Map<String, String> changed = getChanged();
        logger.info("Changed values are: " + changed);
        for (String name : changed.keySet()) {
            String oldValue = initialValuesMap.get(name);
            String newValue = changed.get(name);
            handlersMap.get(name).preferenceChanged(name, oldValue, newValue);
        }
        for (Handler handler : handlers) {
            handler.endApply();
        }
        for (String name : valuesMap.keySet()) {
            initialValuesMap.put(name, valuesMap.get(name));
        }
    }
}
