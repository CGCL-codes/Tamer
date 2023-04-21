package apollo.gui.event;

import apollo.datamodel.*;
import java.util.EventObject;

public class OrientationEvent extends EventObject {

    public OrientationEvent(Object source) {
        super(source);
    }

    public Object getSource() {
        return source;
    }
}
