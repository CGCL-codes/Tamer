package backend.event.type;

import org.apache.log4j.Level;

public class DeserialisationError extends EventType {

    public DeserialisationError(String message) {
        super(message);
        super.desc = "An error during ONDEX object deserialisation occurred.";
        this.setLog4jLevel(Level.ERROR);
    }
}
