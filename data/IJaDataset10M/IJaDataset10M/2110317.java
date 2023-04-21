package LONI.tree.workflow;

import LONI.tree.LoniNode;

public class Variable extends LoniNode {

    private String name;

    private String description;

    private boolean required;

    private boolean secret;

    private int order;

    private String value;

    public Variable() {
    }

    public Variable(String name, String description, boolean required, boolean secret, int order, String value) {
        super();
        this.name = name;
        this.description = description;
        this.required = required;
        this.secret = secret;
        this.order = order;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isSecret() {
        return secret;
    }

    public int getOrder() {
        return order;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
