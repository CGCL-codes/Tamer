package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaString;

public class float22 extends ListOfFloats {

    public float22() {
        super();
    }

    public float22(String newValue) {
        super(newValue);
        validate();
    }

    public float22(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
