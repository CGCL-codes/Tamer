package atlantik.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UpdateSupport<T> {

    private final T obj;

    private final List<UpdateListener<T>> listeners = new LinkedList<UpdateListener<T>>();

    public UpdateSupport(T obj) {
        this.obj = obj;
    }

    public void addListener(UpdateListener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(UpdateListener<T> listener) {
        listeners.remove(listener);
    }

    public void update() {
        for (UpdateListener<T> listener : new ArrayList<UpdateListener<T>>(listeners)) listener.updated(obj);
    }
}
