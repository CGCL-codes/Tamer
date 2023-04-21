package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaString;

public class float4x4 extends ListOfFloats {

    public float4x4() {
        super();
    }

    public float4x4(String newValue) {
        super(newValue);
        validate();
    }

    public float4x4(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
