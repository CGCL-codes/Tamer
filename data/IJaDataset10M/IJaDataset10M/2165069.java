package com.insose.gae.pager;

public class Parameter {

    private String name;

    private Class<?> type;

    private Object value;

    private boolean bookmark;

    Parameter(String name) {
        this.name = name;
    }

    public Parameter(String name, Object value) {
        this.name = name;
        this.value = value;
        if (value == null) throw new RuntimeException("cannot derive type from null " + "value, use other constructor");
        this.type = value.getClass();
    }

    public Parameter(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public Parameter(String name, Class<?> type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public Parameter clone() {
        Parameter parameter = new Parameter(name, type, value);
        parameter.bookmark = bookmark;
        return parameter;
    }

    void learnFrom(Parameter other) {
        if (other.type != null) this.type = other.type;
        if (other.value != null) this.value = other.value;
        if (other.bookmark) this.bookmark = true;
    }

    void setType(Class<?> type) {
        this.type = type;
    }

    public String toString() {
        return toString(true);
    }

    public String toString(boolean decl) {
        StringBuilder sb = new StringBuilder();
        if (decl) sb.append(type.getName()).append(' ');
        return sb.append(name).toString();
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }
}
