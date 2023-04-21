package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaString;

public class bool4 extends ListOfBools {

    public bool4() {
        super();
    }

    public bool4(String newValue) {
        super(newValue);
        validate();
    }

    public bool4(SchemaString newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
    }
}
