package org.w2b.blog;

import java.util.Arrays;
import java.util.Vector;

public class Wordpress {

    private Vector<String> categories;

    private Vector<WPItem> items;

    public Wordpress() {
        categories = new Vector<String>();
        items = new Vector<WPItem>();
    }

    public String[] getCategories() {
        return categories.toArray(new String[categories.size()]);
    }

    public void setCategories(String[] categories) {
        this.categories = new Vector<String>(Arrays.asList(categories));
    }

    public boolean addCategory(String c) {
        return categories.add(c);
    }

    public WPItem[] getItems() {
        return items.toArray(new WPItem[items.size()]);
    }

    public void setItems(WPItem items) {
        this.items = new Vector<WPItem>(Arrays.asList(items));
    }

    public boolean addItem(WPItem item) {
        return items.add(item);
    }
}
