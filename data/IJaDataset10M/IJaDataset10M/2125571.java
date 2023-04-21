package backend.event.type;

import org.apache.log4j.Level;

public class ObjectTypeMismatchError extends EventType {

    public ObjectTypeMismatchError(String message) {
        super(message);
        super.desc = "Object assigned was not of expected type";
        this.setLog4jLevel(Level.ERROR);
    }
}
