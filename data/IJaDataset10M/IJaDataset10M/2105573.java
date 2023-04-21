package org.decomo.constants.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ssudhakar
 *
 */
public class ConstantsList {

    protected String name;

    protected String description;

    protected List<ConstantOption> options = new ArrayList<ConstantOption>();

    protected Map<String, ConstantOption> optionsMap = new HashMap<String, ConstantOption>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConstantOption> getOptions() {
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
        this.options.add(attribute);
        this.optionsMap.put(attribute.getId(), attribute);
    }

    public String toString() {
        return name;
    }

    public ConstantOption getOption(String id) {
        return optionsMap.get(id);
    }

    public String getOptionValue(String id) {
        return getOption(id).getValue();
    }
}
