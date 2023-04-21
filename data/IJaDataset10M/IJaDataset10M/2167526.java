package ingenias.editor.entities;

import java.util.*;

public class AGORelationship2targetRole extends RoleEntity {

    static int idCounter = 0;

    public AGORelationship2targetRole() {
        super("AGORelationship2targetRole" + idCounter);
        idCounter++;
    }

    public AGORelationship2targetRole(String id) {
        super(id);
    }

    public void fromMap(Map ht) {
        super.fromMap(ht);
    }

    public void toMap(Map ht) {
        super.toMap(ht);
    }
}
