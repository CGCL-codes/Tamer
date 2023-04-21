package net.sourceforge.mords.docs.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

/**
 *
 * @author david
 */
public class IndexObj implements Index, Serializable {

    private String index;

    private Vector<String> categories;

    /** Creates a new instance of IndexObj */
    public IndexObj(String index) {
        this.index = index;
        categories = new Vector<String>();
    }

    public IndexObj(String index, Vector<String> categories) {
        this.index = index;
        this.categories = (Vector<String>) categories.clone();
    }

    public String getIndex() {
        return index;
    }

    public Collection<String> getCategories() {
        return categories;
    }

    public void addCategory(String cat) {
        categories.add(cat);
    }

    public String toString() {
        return index;
    }
}
