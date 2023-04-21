package be.vanvlerken.bert.logmonitor.logging;

import java.util.HashMap;
import java.util.Map;

/**
 * This module allows only those entries with module names that are stored in the filter
 * Or just the opposite, it displays all entries except those that have their names listed here.
 */
public class ModuleFilter implements ILogFilter {

    private Map modules;

    private boolean inverse;

    public ModuleFilter(boolean inverse, String[] selectedModules) {
        this.inverse = inverse;
        modules = new HashMap();
        for (int i = 0; i < selectedModules.length; i++) {
            modules.put(selectedModules[i], null);
        }
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogFilter#isAllowed(be.vanvlerken.bert.logmonitor.logging.ILogEntry)
     */
    public boolean isAllowed(ILogEntry entry) {
        boolean inModules = false;
        for (int i = 0; i < modules.size(); i++) {
            if (modules.containsKey(entry.getModuleName())) {
                inModules = true;
                break;
            }
        }
        if (inverse) {
            return !inModules;
        } else {
            return inModules;
        }
    }
}
