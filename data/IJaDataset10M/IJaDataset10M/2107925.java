package eu.planets_project.pp.plato.services.characterisation.jhove;

import java.util.Vector;

public class Property {

    String name;

    Vector<Object> values;

    String type;

    public Property() {
        super();
        values = new Vector<Object>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector<Object> getValues() {
        return values;
    }

    public void setValues(Vector<Object> values) {
        this.values = values;
    }

    public void addValue(Object o) {
        values.add(o);
    }

    @Override
    public String toString() {
        if (type.compareTo("String") == 0) return name + " " + values.toString(); else if (type.compareTo("Integer") == 0) return name + " " + values.toString(); else if (type.compareTo("XMP") == 0) return values.toString(); else return name + ":type:" + type + "/\n" + values;
    }
}
