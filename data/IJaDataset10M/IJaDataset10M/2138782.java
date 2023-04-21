package ingenias.editor.entities;

import java.util.*;

public class OHasGroup extends NAryEdgeEntity {

    public OHasGroup(String id) {
        super(id);
        ModelEntity em = null;
    }

    public void fromMap(Map ht) {
        super.fromMap(ht);
    }

    public void toMap(Map ht) {
        super.toMap(ht);
    }

    public String toString() {
        return getId() + ":" + getType();
    }
}
