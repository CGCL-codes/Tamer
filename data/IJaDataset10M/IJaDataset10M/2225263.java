package blueprint4j.utils;

import java.util.*;

public class VectorBindableProperties {

    private Vector store = new Vector();

    public BindableProperties get(int pos) {
        return (BindableProperties) store.get(pos);
    }

    public void add(BindableProperties item) {
        store.add(item);
    }

    public boolean remove(BindableProperties item) {
        return store.remove(item);
    }

    public BindableProperties remove(int pos) {
        return (BindableProperties) store.remove(pos);
    }

    public int size() {
        return store.size();
    }
}
