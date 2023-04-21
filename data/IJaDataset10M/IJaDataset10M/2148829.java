package slickxml.data;

import java.util.Collection;

public class Parser extends DataObject {

    private String m_name;

    private Collection<DataObject> m_allObjects;

    public Parser(String name) {
        super(null, name);
    }

    public Collection<DataObject> getAllObjects() {
        return (m_allObjects);
    }

    public void setAllObjects(Collection<DataObject> objects) {
        m_allObjects = objects;
    }
}
