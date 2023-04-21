package org.slasoi.businessManager.common.drools;

public class Global {

    private String name;

    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Global(String name, Object value) {
        super();
        this.name = name;
        this.value = value;
    }
}
