package ingenias.editor.entities;

import java.util.*;

public class ApplicationBelongsTotargetRole extends RoleEntity {

    static int idCounter = 0;

    public ApplicationBelongsTotargetRole() {
        super("ApplicationBelongsTotargetRole" + idCounter);
        idCounter++;
    }

    public ApplicationBelongsTotargetRole(String id) {
        super(id);
    }

    public void fromMap(Map ht) {
        super.fromMap(ht);
    }

    public void toMap(Map ht) {
        super.toMap(ht);
    }
}
