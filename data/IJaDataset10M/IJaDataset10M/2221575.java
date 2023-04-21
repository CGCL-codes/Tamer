package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaString;

public class glsl_ListOfBool extends SchemaString {

    public glsl_ListOfBool() {
        super();
    }

    public glsl_ListOfBool(String newValue) {
        super(newValue);
        validate();
    }

    public glsl_ListOfBool(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
