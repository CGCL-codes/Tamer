package hu.csq.dyneta.configuration;

import java.util.LinkedHashMap;

/**
 *
 * @author Tamás Cséri
 */
public class ExperimentRunConfigurationPluginWrapper {

    public String shortName;

    public String name;

    public LinkedHashMap<String, String> params;

    /**
     * Clones this object. Also creates a new parameter LinkedHashMap
     *
     * @return
     */
    public ExperimentRunConfigurationPluginWrapper deepClone() {
        ExperimentRunConfigurationPluginWrapper rv = new ExperimentRunConfigurationPluginWrapper();
        rv.name = name;
        rv.shortName = shortName;
        if (params != null) {
            rv.params = new LinkedHashMap<String, String>(params);
        }
        return rv;
    }
}
