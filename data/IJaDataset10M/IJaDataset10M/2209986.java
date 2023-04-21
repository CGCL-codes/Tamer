package org.hemera;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.hemera.utils.HemeraUtils;

/**
 * Hemera - Intelligent System
 * Web Service config model.
 * 
 * @author Bertrand Benoit <projettwk@users.sourceforge.net>
 * @since 0.2
 */
public final class ConfigurationModel {

    /****************************************************************************************/
    private final String installDir;

    private final String changeLog;

    private final Properties properties;

    /****************************************************************************************/
    public ConfigurationModel() {
        installDir = HemeraUtils.getInstallDir();
        changeLog = HemeraUtils.getInstallDir();
        properties = HemeraUtils.getConfiguration();
    }

    /**
     * @return the installDir
     */
    public final String getInstallDir() {
        return installDir;
    }

    /**
     * @return the changeLog
     */
    public final String getChangeLog() {
        return changeLog;
    }

    public final Set<Entry<Object, Object>> getPropertySet() {
        return properties.entrySet();
    }
}
