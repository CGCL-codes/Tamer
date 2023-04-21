package sgf.property;

import java.util.ArrayList;
import sgf.value.Value;

public class Property {

    private PropertyId id;

    private String ident;

    private Value value;

    public Property(String ident, ArrayList<String> valueList) {
        this.id = PropertyId.get(ident);
        this.ident = ident;
        this.value = (Value) id.newValueInstance();
        for (String s : valueList) {
            value.setSgfString(s);
        }
    }

    public Property(PropertyId id) {
        this.id = id;
        this.ident = id.getIdent();
        this.value = (Value) id.newValueInstance();
    }

    public Property(PropertyId id, Value value) {
        this.id = id;
        this.ident = id.getIdent();
        this.value = value;
    }

    public PropertyId getId() {
        return id;
    }

    public PropertyType getType() {
        return id.getPropertyType();
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    public String toSgfString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident).append(value.getValueString());
        return sb.toString();
    }
}
