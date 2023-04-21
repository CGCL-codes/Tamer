package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaFloat;

public class cg_float1 extends SchemaFloat {

    public cg_float1() {
        super();
    }

    public cg_float1(String newValue) {
        super(newValue);
        validate();
    }

    public cg_float1(SchemaFloat newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
