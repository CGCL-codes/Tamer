package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaString;

public class cg_int3x2 extends cg_ListOfInt {

    public cg_int3x2() {
        super();
    }

    public cg_int3x2(String newValue) {
        super(newValue);
        validate();
    }

    public cg_int3x2(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
