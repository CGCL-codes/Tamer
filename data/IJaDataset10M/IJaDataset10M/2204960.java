package org.j2eebuilder;

import org.jdom.*;
import java.io.*;
import java.util.*;
import java.net.URL;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)ModulesDefinition.java	1.350 01/12/03
 */
public class LayoutsDefinition implements Definition, java.io.Serializable {

    private static transient LogManager log = new LogManager(LayoutsDefinition.class);

    private String name;

    private String description;

    private String type;

    /**
	* Urls containing module definitions
	*/
    private Collection layoutDefinitions = new ArrayList();

    public LayoutsDefinition() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LayoutDefinition[] getLayoutDefinitions() {
        return (LayoutDefinition[]) this.layoutDefinitions.toArray(new LayoutDefinition[1]);
    }

    public void addLayoutDefinition(LayoutDefinition recordDefinition) {
        this.layoutDefinitions.add(recordDefinition);
    }

    public int size() {
        return layoutDefinitions.size();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("LayoutsDefinition[");
        buf.append(this.getName());
        buf.append(", ");
        buf.append(this.getDescription());
        buf.append(", ");
        buf.append(this.getType());
        buf.append(", ");
        buf.append("LayoutDefinitions[");
        buf.append(this.getLayoutDefinitions());
        buf.append("]");
        return buf.toString();
    }
}
