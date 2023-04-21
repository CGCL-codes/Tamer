package oext.model.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlAttribute {

    protected String name = "";

    protected List<String> values = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addValue(String e) {
        return values.add(e);
    }

    public void clearValue() {
        values.clear();
    }

    public boolean containsValue(Object o) {
        return values.contains(o);
    }

    public Iterator<String> iteratorValue() {
        return values.iterator();
    }

    public int sizeValue() {
        return values.size();
    }
}
