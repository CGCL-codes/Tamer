package org.macaca.database.classification;

import org.macaca.data.Type;

public class TypeClassification extends Classification {

    private Type type;

    public TypeClassification(String name) {
        super(name);
    }

    public TypeClassification(String name, Type type) {
        super(name);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
