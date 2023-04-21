package org.squabble.web.account;

import java.util.Map;
import org.squabble.domain.AccountProperty;

public abstract class AbstractPropertyController {

    protected Object getPropertyValue(Map<String, AccountProperty> props, String key, Object defaultValue) {
        if (props.containsKey(key)) {
            return props.get(key).getValue();
        }
        return defaultValue;
    }

    protected Object getPropertyValue(Map<String, AccountProperty> props, String key) {
        return getPropertyValue(props, key, null);
    }
}
