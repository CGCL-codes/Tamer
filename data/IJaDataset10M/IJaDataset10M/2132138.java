package org.isisproject.ice.conditionparser;

public class Value extends SimpleNode {

    private String value;

    public Value(int id) {
        super(id);
    }

    public Value(ConditionParser p, int id) {
        super(p, id);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
