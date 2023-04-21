package de.consolewars.api.event;

import java.util.ArrayList;
import java.util.EventObject;

public class ListEventObject<T> extends EventObject {

    private final ArrayList<T> list;

    public ListEventObject(final Object source, final ArrayList<T> list) {
        super(source);
        this.list = list;
    }

    public ArrayList<T> getList() {
        return list;
    }
}
