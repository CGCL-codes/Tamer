package diamante.extension.graph.property;

import diamante.core.graph.property.Property;

public class ID extends Property {

    int id;

    public ID(int id) {
        super("ID");
        this.id = id;
    }

    public boolean equals(Property prop2) {
        return id == ((ID) prop2).getValue();
    }

    public int getValue() {
        return id;
    }

    public void setValue(int i) {
        id = i;
    }

    public void fromString(String value) throws IllegalArgumentException {
        id = (new Integer(value)).intValue();
    }

    public String toString() {
        return new Integer(id).toString();
    }
}
