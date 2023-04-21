package de.ui.sushi.metadata.simpletypes;

import de.ui.sushi.metadata.Schema;
import de.ui.sushi.metadata.SimpleType;
import de.ui.sushi.metadata.SimpleTypeException;

public class DoubleType extends SimpleType {

    public DoubleType(Schema schema) {
        super(schema, Double.class, "double");
    }

    @Override
    public Object newInstance() {
        return 0;
    }

    @Override
    public String valueToString(Object obj) {
        return ((Double) obj).toString();
    }

    @Override
    public Object stringToValue(String str) throws SimpleTypeException {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw new SimpleTypeException("number expected, got '" + str + "'");
        }
    }
}
