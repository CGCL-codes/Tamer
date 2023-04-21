package org.jdmp.mallet;

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
        dependencies.add("mallet.jar");
        dependencies.add("mallet-deps.jar");
        neededClasses.add("cc.mallet.classify.Classifier");
    }

    public String getDescription() {
        return "interface to Mallet";
    }

    public Collection<Object> getDependencies() {
        return dependencies;
    }

    public Collection<String> getNeededClasses() {
        return neededClasses;
    }
}
