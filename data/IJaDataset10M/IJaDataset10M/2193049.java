package org.codehaus.groovy.grails.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of <code>PluginFilter</code> which ensures that only the supplied
 * plugins (identified by name) as well as their dependencies are included in the filtered plugin list
 * @author Phil Zoio
 */
public class IncludingPluginFilter extends BasePluginFilter {

    public IncludingPluginFilter(Set included) {
        super(included);
    }

    public IncludingPluginFilter(String[] included) {
        super(included);
    }

    protected List getPluginList(List original, List pluginList) {
        List newList = new ArrayList();
        newList.addAll(pluginList);
        return newList;
    }

    protected void addPluginDependencies(List additionalList, GrailsPlugin plugin) {
        String[] dependencyNames = plugin.getDependencyNames();
        for (int j = 0; j < dependencyNames.length; j++) {
            String name = dependencyNames[j];
            GrailsPlugin p = getNamedPlugin(name);
            registerDependency(additionalList, p);
        }
    }
}
