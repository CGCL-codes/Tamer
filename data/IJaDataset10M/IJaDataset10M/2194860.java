package org.xmatthew.spy2servers.util;

import org.apache.commons.lang.StringUtils;
import org.xmatthew.spy2servers.core.Component;

/**
 * @author Matthew Xie
 *
 */
public final class ComponentUtils {

    /**
     * 
     */
    private ComponentUtils() {
    }

    public static String getComponentName(Component component) {
        String name = component.getName();
        if (StringUtils.isBlank(name)) {
            name = component.getClass().getName();
        }
        return name;
    }
}
