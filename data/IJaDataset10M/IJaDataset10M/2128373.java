package org.apache.myfaces.blank;

import java.io.Serializable;

public class SimpleColumn implements Serializable {

    private String name;

    private String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private SimpleColumn(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public static SimpleColumn valueOf(String name, String label) {
        return new SimpleColumn(name, label);
    }
}
