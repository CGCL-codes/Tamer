package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaString;

public class cg_int4 extends cg_ListOfInt {

    public cg_int4() {
        super();
    }

    public cg_int4(String newValue) {
        super(newValue);
        validate();
    }

    public cg_int4(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
