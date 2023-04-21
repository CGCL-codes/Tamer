package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaString;

public class cg_half3x2 extends cg_ListOfHalf {

    public cg_half3x2() {
        super();
    }

    public cg_half3x2(String newValue) {
        super(newValue);
        validate();
    }

    public cg_half3x2(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
