package org.decomo.constants.eclipse.registry.model;

import java.util.ArrayList;

/**
 * @author ssudhakar
 *
 */
public class ConstantsList {

    public String name;

    public String description;

    public ArrayList<ConstantOption> options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ConstantOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<ConstantOption> options) {
        this.options = options;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addOption(ConstantOption attribute) {
        if (this.options == null) options = new ArrayList<ConstantOption>();
        this.options.add(attribute);
    }

    public String toString() {
        return name;
    }
}
