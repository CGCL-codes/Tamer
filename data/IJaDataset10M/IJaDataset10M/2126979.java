package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaString;

public class glsl_ListOfInt extends SchemaString {

    public glsl_ListOfInt() {
        super();
    }

    public glsl_ListOfInt(String newValue) {
        super(newValue);
        validate();
    }

    public glsl_ListOfInt(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
