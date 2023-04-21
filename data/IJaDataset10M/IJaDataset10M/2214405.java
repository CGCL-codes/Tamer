package eu.kolovos.minigen.model;

import java.util.ArrayList;

public class Row {

    protected ArrayList<String> fields = new ArrayList<String>();

    protected int index;

    protected String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return text;
    }

    public ArrayList<String> getFields() {
        return fields;
    }
}
