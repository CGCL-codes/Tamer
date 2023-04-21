package org.jdmp.jgroups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.ujmp.core.util.AbstractPlugin;

public class Plugin extends AbstractPlugin {

    private final List<Object> dependencies = new ArrayList<Object>();

    private final List<String> neededClasses = new ArrayList<String>();

    public Plugin() {
        dependencies.add("ujmp-core");
        dependencies.add("jdmp-core");
        dependencies.add("jgroups.jar");
        dependencies.add("commons-logging.jar");
        neededClasses.add("org.jgroups.Channel");
        neededClasses.add("org.apache.commons.logging.Log");
    }

    public String getDescription() {
        return "library for sharing data using JGroups";
    }

    public Collection<Object> getDependencies() {
        return dependencies;
    }

    public Collection<String> getNeededClasses() {
        return neededClasses;
    }
}
