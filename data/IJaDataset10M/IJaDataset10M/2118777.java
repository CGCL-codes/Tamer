package ingenias.editor.entities;

import java.util.*;

public class UIPrecedestargetRole extends RoleEntity {

    static int idCounter = 0;

    public UIPrecedestargetRole() {
        super("UIPrecedestargetRole" + idCounter);
        idCounter++;
    }

    public UIPrecedestargetRole(String id) {
        super(id);
    }

    public void fromMap(Map ht) {
        super.fromMap(ht);
    }

    public void toMap(Map ht) {
        super.toMap(ht);
    }
}
