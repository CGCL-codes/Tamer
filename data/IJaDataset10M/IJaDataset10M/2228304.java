package net.grinder.plugininterface;

import net.grinder.util.GrinderProperties;

/**
 * @author Philip Aston
 * @version $Revision: 215 $
 */
public interface TestDefinition {

    /** Test number is returned as an Integer so that plugins can use
     * it as a Map key. */
    public Integer getTestNumber();

    public String getDescription();

    public GrinderProperties getParameters();
}
