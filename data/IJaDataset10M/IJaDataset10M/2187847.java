package ingenias.editor.entities;

import java.util.*;
import ingenias.editor.TypedVector;

public class AUMLAlternativeRow extends AUMLContainer {

    public java.lang.String Condition;

    public AUMLAlternativeRow(String id) {
        super(id);
        this.setHelpDesc("<br>It represents link of a column to another<br>");
        this.setHelpRecom("");
    }

    public java.lang.String getCondition() {
        return Condition;
    }

    public void setCondition(java.lang.String Condition) {
        this.Condition = Condition;
    }

    public void fromMap(Map ht) {
        super.fromMap(ht);
        if (ht.get("Condition") instanceof String) this.setCondition(ht.get("Condition").toString());
    }

    public void toMap(Map ht) {
        super.toMap(ht);
        if (this.getCondition() instanceof String) ht.put("Condition", this.getCondition().toString());
    }

    public String toString() {
        if (this.getId() == null || this.getId().toString().equals("")) return "Please, define the value of field Id"; else return this.getId().toString();
    }
}
