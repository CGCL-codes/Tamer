package ingenias.editor.entities;

import java.util.*;

public class WFSpecifiesExecutionsourceRole extends RoleEntity {

    static int idCounter = 0;

    public WFSpecifiesExecutionsourceRole() {
        super("WFSpecifiesExecutionsourceRole" + idCounter);
        idCounter++;
    }

    public WFSpecifiesExecutionsourceRole(String id) {
        super(id);
    }

    public void fromMap(Map ht) {
        super.fromMap(ht);
    }

    public void toMap(Map ht) {
        super.toMap(ht);
    }
}
