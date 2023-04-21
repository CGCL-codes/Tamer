package LONI.tree.module.metadata;

import LONI.tree.LoniNode;

public class MetadataValue extends LoniNode {

    public MetadataValue(String valueOption, String valueValue) {
        super();
        this.valueOption = valueOption;
        this.valueValue = valueValue;
    }

    public String getValueOption() {
        return valueOption;
    }

    public String getValueValue() {
        return valueValue;
    }

    private final String valueOption;

    private final String valueValue;
}
