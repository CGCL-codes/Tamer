package org.squabble.web.admin;

import java.util.Map;
import org.squabble.domain.SystemProperty;

public abstract class AbstractSystemController {

    protected Object getPropertyValue(Map<String, SystemProperty> props, String key, Object defaultValue) {
        if (props.containsKey(key)) {
            return props.get(key).getValue();
        }
        return defaultValue;
    }

    protected Object getPropertyValue(Map<String, SystemProperty> props, String key) {
        return getPropertyValue(props, key, null);
    }
}
