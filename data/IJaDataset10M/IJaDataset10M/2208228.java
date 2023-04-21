package org.isisproject.ice.conditionparser;

public class Variable extends SimpleNode {

    private String name;

    public Variable(int id) {
        super(id);
    }

    public Variable(ConditionParser p, int id) {
        super(p, id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
