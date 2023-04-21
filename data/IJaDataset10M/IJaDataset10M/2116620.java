package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaFloat;

public class gl_alpha_value_type extends SchemaFloat {

    public gl_alpha_value_type() {
        super();
    }

    public gl_alpha_value_type(String newValue) {
        super(newValue);
        validate();
    }

    public gl_alpha_value_type(SchemaFloat newValue) {
        super(newValue);
        validate();
    }

    public void validate() {
        if (compareTo(getMaxInclusive()) > 0) throw new homura.hde.util.xml.xml.XmlException("Value of gl_alpha_value_type is out of range.");
        if (compareTo(getMinInclusive()) < 0) throw new homura.hde.util.xml.xml.XmlException("Value of gl_alpha_value_type is out of range.");
    }

    public SchemaFloat getMaxInclusive() {
        return new SchemaFloat("1.0");
    }

    public SchemaFloat getMinInclusive() {
        return new SchemaFloat("0.0");
    }
}
