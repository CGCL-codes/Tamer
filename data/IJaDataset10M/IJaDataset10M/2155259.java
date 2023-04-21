package com.pinae.mufasa.wsdl.resources;

import java.util.ArrayList;
import java.util.List;

public class Binding extends com.pinae.nala.xb.NalaObject {

    private String type;

    private Binding binding;

    private String name;

    private List<Operation> operation = new ArrayList<Operation>();

    private String style;

    private String transport;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public Binding() {
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setBinding(Binding binding) {
        this.binding = binding;
    }

    public Binding getBinding() {
        return binding;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOperation(Operation operation) {
        this.operation.add(operation);
    }

    public List<Operation> getOperation() {
        return operation;
    }
}
