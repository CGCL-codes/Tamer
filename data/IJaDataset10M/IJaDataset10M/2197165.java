package xml;

import java.util.Vector;

/**
 *
 * @author bi
 */
public class XMLRecord {

    private String name;

    private String value;

    private Vector<XMLRecord> child;

    public XMLRecord(String name, String value) {
        this.name = name;
        this.value = value;
        child = new Vector<XMLRecord>();
    }

    public int getChildSize() {
        return child.size();
    }

    public boolean hasChild() {
        return child.size() > 0;
    }

    public XMLRecord getChildAt(int index) {
        return child.get(index);
    }

    public XMLRecord[] getChild() {
        XMLRecord[] records = new XMLRecord[child.size()];
        for (int i = 0; i < child.size(); i++) {
            records[i] = child.get(i);
        }
        return records;
    }

    public boolean addChild(XMLRecord record) {
        return child.add(record);
    }

    public boolean removeChild(XMLRecord record) {
        for (int i = 0; i < child.size(); i++) {
            if (child.get(i).equals(record)) {
                child.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            return (obj.hashCode() == this.hashCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 23 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}
