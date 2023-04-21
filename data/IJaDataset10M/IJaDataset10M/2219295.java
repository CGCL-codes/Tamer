package IO;

import java.util.ArrayList;

public class IOManager {

    private ArrayList<ListenerInterface> events = new ArrayList<ListenerInterface>();

    public IOManager() {
    }

    public void add(ListenerInterface e) {
        events.add(e);
    }

    public void remove(int index) {
        events.remove(index);
    }

    public void update() {
        for (ListenerInterface e : events) e.update();
    }
}
