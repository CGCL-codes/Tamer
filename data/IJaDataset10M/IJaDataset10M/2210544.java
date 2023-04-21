package ingenias.editor.entities;

import java.util.*;
import ingenias.editor.TypedVector;

public class AutonomousEntityQuery extends INGENIASObject {

    public AutonomousEntityQuery(String id) {
        super(id);
        this.setHelpDesc("<br>Represent an instance of an agent or a set of agents in runtime. We can refer to that<br>running instance in different ways: with identifiers, with query-like expressions, etc.<br>There are concretions of this entity to represent these different ways of referring to<br>agents<br>");
        this.setHelpRecom("");
    }

    public void fromMap(Map ht) {
        super.fromMap(ht);
    }

    public void toMap(Map ht) {
        super.toMap(ht);
    }

    public String toString() {
        if (this.getId() == null || this.getId().toString().equals("")) return "Please, define the value of field Id"; else return this.getId().toString();
    }
}
