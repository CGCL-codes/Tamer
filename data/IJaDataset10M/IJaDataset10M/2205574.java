package net.sourceforge.nattable.typeconfig.style;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes a ConfigType hierarchy.
 * 
 * @author Azubuko.Obele@rbccm.com
 */
public class ConfigType {

    private String id;

    private Map<String, IStyleConfig> displayModeOverrides = new HashMap<String, IStyleConfig>();

    private ConfigType parent;

    public ConfigType(String id) {
        if (id == null) throw new IllegalArgumentException("null");
        this.id = id;
    }

    public ConfigType(String id, ConfigType parent) {
        if (id == null || parent == null) throw new IllegalArgumentException("null");
        this.id = id;
        this.parent = parent;
    }

    public void apply(StyleConfigRegistry registry) {
        for (String displayMode : displayModeOverrides.keySet()) {
            registry.registerStyleConfig(displayMode, id, displayModeOverrides.get(displayMode));
        }
    }

    public String getId() {
        return id;
    }

    public Map<String, IStyleConfig> getDisplayModeOverrides() {
        return displayModeOverrides;
    }

    public ConfigType getParent() {
        return parent;
    }
}
