package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaString;

public class cg_int1x3 extends cg_ListOfInt {

    public cg_int1x3() {
        super();
    }

    public cg_int1x3(String newValue) {
        super(newValue);
        validate();
    }

    public cg_int1x3(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
