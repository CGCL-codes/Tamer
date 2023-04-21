package org.aplikator.client.descriptor;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QueryParameter implements Serializable {

    private String name;

    private String value;

    @SuppressWarnings("unused")
    private QueryParameter() {
    }

    public QueryParameter(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
