package event.changeHoja;

import java.util.EventObject;

public class EventChangeHoja extends EventObject {

    private static final long serialVersionUID = 1L;

    private String id;

    public EventChangeHoja(Object obj, String id) {
        super(obj);
        this.id = id;
    }

    String getEventoID() {
        return (id);
    }
}
