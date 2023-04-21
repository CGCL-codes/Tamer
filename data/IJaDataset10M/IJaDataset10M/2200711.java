package net.sf.beezle.sushi.metadata.simpletypes;

import net.sf.beezle.sushi.metadata.Schema;
import net.sf.beezle.sushi.metadata.SimpleType;

public class StringType extends SimpleType {

    public StringType(Schema schema) {
        super(schema, String.class, "string");
    }

    @Override
    public Object newInstance() {
        return "";
    }

    @Override
    public String valueToString(Object obj) {
        return (String) obj;
    }

    @Override
    public Object stringToValue(String str) {
        return str;
    }
}
