package ch.kerbtier.malurus.coreimpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ch.kerbtier.malurus.ActionListener;
import ch.kerbtier.malurus.Event;

public class Listeners implements Serializable {

    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    public void add(ActionListener listener) {
        listeners.add(listener);
    }

    public void remove(ActionListener listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    public void execute(Event event) {
        for (ActionListener al : listeners) {
            al.execute(event);
        }
    }
}
