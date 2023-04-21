package cn.vlabs.duckling.vwb.services.dlog.rdi.data;

import java.util.ArrayList;

public class QueryRecord {

    String object;

    int count;

    ArrayList<Object> elements;

    public void setObject(String event) {
        this.object = event;
    }

    public String getObject() {
        return this.object;
    }

    public void setCount(int num) {
        count = num;
    }

    public int getCount() {
        return count;
    }

    public void setElements(ArrayList<Object> values) {
        elements = values;
    }

    public ArrayList<Object> getElements() {
        return elements;
    }
}
