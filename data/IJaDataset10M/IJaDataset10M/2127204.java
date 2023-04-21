package ingenias.editor.entities;

import java.util.*;

public class AGORelationship3sourceRole extends RoleEntity {

    static int idCounter = 0;

    public AGORelationship3sourceRole() {
        super("AGORelationship3sourceRole" + idCounter);
        idCounter++;
    }

    public AGORelationship3sourceRole(String id) {
        super(id);
    }

    public void fromMap(Map ht) {
        super.fromMap(ht);
    }

    public void toMap(Map ht) {
        super.toMap(ht);
    }
}
