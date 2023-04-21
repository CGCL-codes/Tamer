package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaString;

public class cg_int2x4 extends cg_ListOfInt {

    public cg_int2x4() {
        super();
    }

    public cg_int2x4(String newValue) {
        super(newValue);
        validate();
    }

    public cg_int2x4(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
