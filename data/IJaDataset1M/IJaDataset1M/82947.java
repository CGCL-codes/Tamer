package net.sf.resultsetmapper.jelly;

import net.sf.resultsetmapper.MapToData;

public class JellyAttribute {

    @MapToData
    String name;

    @MapToData
    String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
