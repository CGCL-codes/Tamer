package ingenias.editor.entities;

import java.util.*;
import ingenias.editor.TypedVector;

public class SlotValueSpecification extends INGENIASObject {

    public java.lang.String Value;

    public ingenias.editor.entities.Slot Slot;

    public SlotValueSpecification(String id) {
        super(id);
        this.setHelpDesc("<br>		It is a common slot used only in Frame Fact entity. Each slot could be interpreted in the implementation as an<br>		attribute in an object or as a CLIPS slot in a CLIPS fact.<br>	");
        this.setHelpRecom("");
    }

    public java.lang.String getValue() {
        return Value;
    }

    public void setValue(java.lang.String Value) {
        this.Value = Value;
    }

    public ingenias.editor.entities.Slot getSlot() {
        return Slot;
    }

    public void setSlot(ingenias.editor.entities.Slot Slot) {
        this.Slot = Slot;
    }

    public void fromMap(Map ht) {
        super.fromMap(ht);
        if (ht.get("Value") instanceof String) this.setValue(ht.get("Value").toString());
    }

    public void toMap(Map ht) {
        super.toMap(ht);
        if (this.getValue() instanceof String) ht.put("Value", this.getValue().toString());
    }

    public String toString() {
        if (this.getSlot() == null || this.getSlot().toString().equals("")) return "Please, define the value of field Slot"; else return this.getSlot().toString();
    }
}
