package org.tolven.plugin.registry.xml;

import org.java.plugin.registry.MatchingRule;
import org.java.plugin.registry.Version;

/**
 * Class externalized from the ModelPluginManifest, since it is a useful interface to prerequisite information
 * 
 * @author Joseph Isaac
 *
 */
public class ModelPluginFragment extends ModelPluginManifest {

    private String pluginId;

    private Version pluginVersion;

    private MatchingRule matchingRule = MatchingRule.COMPATIBLE;

    public ModelPluginFragment() {
    }

    public MatchingRule getMatchingRule() {
        return matchingRule;
    }

    public void setMatchingRule(MatchingRule value) {
        matchingRule = value;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String value) {
        pluginId = value;
    }

    public Version getPluginVersion() {
        return pluginVersion;
    }

    public void setPluginVersion(String value) {
        pluginVersion = Version.parse(value);
    }
}
