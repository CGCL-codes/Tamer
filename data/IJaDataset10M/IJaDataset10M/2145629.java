package org.sodeja.generator.uml;

public class UmlTagValue extends UmlElement {

    private UmlReference<UmlTagDefinition> tag;

    private String value;

    public UmlReference<UmlTagDefinition> getTag() {
        return tag;
    }

    public void setTag(UmlReference<UmlTagDefinition> tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
