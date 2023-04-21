package org.apache.ws.jaxme.generator;

public class SchemaOption {

    private final String name;

    private final String target;

    private final String value;

    /** Creates a new instance of SchemaOption */
    public SchemaOption(String pName, String pValue, String pTarget) {
        name = pName;
        target = (pTarget == null || pTarget.length() == 0) ? null : pTarget;
        value = pValue;
    }

    public String getName() {
        return name;
    }

    public String getTarget() {
        return target;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        if (target == null) {
            return name + "=" + value;
        } else {
            return name + "=" + target + "=" + value;
        }
    }
}
