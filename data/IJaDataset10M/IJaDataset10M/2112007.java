package org.cumt.model.der;

import org.cumt.model.ModelObject;

public class Constraint extends ModelObject {

    private static final long serialVersionUID = 4460334777000803804L;

    public enum Type {

        REFERENCE, CHECK, PRIMARY_KEY
    }

    ;

    private Type type = Type.REFERENCE;

    private Field targetField;

    public Field getTargetField() {
        return targetField;
    }

    public void setTargetField(Field targetField) {
        this.targetField = targetField;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
