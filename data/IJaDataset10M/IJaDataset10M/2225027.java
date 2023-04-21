package com.icteam.fiji.persistence.template.handlers;

import com.icteam.fiji.persistence.template.IParameterHandler;

public abstract class AbstractParameterHandler<T> implements IParameterHandler<T> {

    protected String name;

    protected T value;

    protected AbstractParameterHandler(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("name: [");
        strBuilder.append(this.getName());
        strBuilder.append("], ");
        strBuilder.append("value: [");
        strBuilder.append(this.getValue());
        strBuilder.append("]");
        return strBuilder.toString();
    }
}
