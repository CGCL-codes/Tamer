package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaString;

public class cg_ListOfFixed extends SchemaString {

    public cg_ListOfFixed() {
        super();
    }

    public cg_ListOfFixed(String newValue) {
        super(newValue);
        validate();
    }

    public cg_ListOfFixed(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
