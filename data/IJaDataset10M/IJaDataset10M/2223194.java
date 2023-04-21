package backend.event.type;

import org.apache.log4j.Level;

public class RelationTypeSetMissing extends EventType {

    public RelationTypeSetMissing(String message) {
        this(message, "");
    }

    public RelationTypeSetMissing(String message, String extension) {
        super(message, extension);
        super.desc = "One or more RelationTypeSets are missing in the metadata.";
        this.setLog4jLevel(Level.ERROR);
    }
}
