package org.equanda.tool.shared.configuration;

import org.equanda.tool.shared.ToolException;

/**
 * Configuration interface (used in worker & main classes )
 *
 * @author NetRom team
 */
public interface ConfigurationExtra {

    public abstract void initSections() throws ToolException;

    public abstract String getPrefix();

    public abstract ConfigurationMain getMainConfiguration();

    public abstract boolean exists();
}
