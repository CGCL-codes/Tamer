package org.unitils.core.util;

import java.util.Properties;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public interface Configurable {

    /**
     * Initializes the database operation class with the given {@link Properties}
     *
     * @param configuration The configuration, not null
     */
    public void init(Properties configuration);
}
