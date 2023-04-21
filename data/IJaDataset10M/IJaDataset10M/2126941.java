package org.fao.fenix.web.client.vo;

import java.io.Serializable;
import java.util.List;

public class Field implements Serializable {

    public String name;

    public List values;

    public Field() {
    }

    public Field(String name, List values) {
        this.setName(name);
        this.setValues(values);
    }

    public List getValues() {
        return values;
    }

    public void setValues(List values) {
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
