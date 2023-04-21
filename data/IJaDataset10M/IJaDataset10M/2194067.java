package org.zkoss.zktest.bind.issue;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.ListModelList;

/**
 * @author Dennis Chen
 * 
 */
public class B00821SelectedIndex {

    List<Item> items;

    int index = -1;

    public B00821SelectedIndex() {
        items = new ListModelList<Item>();
        items.add(new Item("A"));
        items.add(new Item("B"));
        items.add(new Item("C"));
        items.add(new Item("D"));
    }

    public List<Item> getItems() {
        return items;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static class Item {

        String name;

        List<Option> options = new ArrayList<Option>();

        public Item(String name) {
            this.name = name;
            options.add(new Option(name + " 0"));
            options.add(new Option(name + " 1"));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Option> getOptions() {
            return options;
        }
    }

    public static class Option {

        String name;

        public Option(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
