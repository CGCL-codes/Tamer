package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaString;

public class cg_half2x4 extends cg_ListOfHalf {

    public cg_half2x4() {
        super();
    }

    public cg_half2x4(String newValue) {
        super(newValue);
        validate();
    }

    public cg_half2x4(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
