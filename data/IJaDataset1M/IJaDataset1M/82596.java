package org.translator.java.code;

import java.util.SortedMap;

/**
 *
 * @author Joshua
 */
public class Parameter extends Value {

    private String type, identifier;

    private int index;

    public Parameter(String type, String identifier) {
        this.type = type;
        this.identifier = identifier;
        setValue(String.format("%s %s", type, identifier));
    }

    public Parameter(String type, String identifier, int index) {
        this.type = type;
        this.identifier = identifier;
        this.index = index;
        setValue(String.format("%s %s", type, identifier));
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    protected SortedMap<String, String> getDependencies() {
        SortedMap<String, String> dependencies = buildDependencies();
        addDependency(dependencies, type);
        return dependencies;
    }

    protected String getType() {
        return type;
    }

    protected String getIdentifier() {
        return identifier;
    }
}
