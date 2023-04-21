package org.openintents.safe;

/**
 * @author Randy McEoin
 */
public class SearchEntry extends Object {

    public long id = -1;

    public String name;

    public String category;

    public long categoryId = -1;

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public SearchEntry() {
        name = "";
        category = "";
    }

    public SearchEntry(String _name) {
        name = _name;
        category = "";
    }

    public SearchEntry(String _name, String _category) {
        name = _name;
        category = _category;
    }

    @Override
    public String toString() {
        return name + " " + category;
    }
}
