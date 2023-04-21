package ingenias.editor.entities;

import java.util.*;
import ingenias.editor.TypedVector;

public class GRASIAMentalStatePattern extends SymbolicMentalStatePattern {

    public ingenias.editor.entities.AgentModelModelEntity DescriptionWithAgentModel;

    public GRASIAMentalStatePattern(String id) {
        super(id);
        this.setHelpDesc("<br>Describes an agent mental state using agent models. In these models you are expected to have only an instance of AutonomousEntityQuery associated with a mental state, and this mental state with required mental entities. Another alternative is to have a conditional mental state entity that allows to express conditions over identified mental entities<br>");
        this.setHelpRecom("");
    }

    public AgentModelModelEntity getDescriptionWithAgentModel() {
        return DescriptionWithAgentModel;
    }

    public void setDescriptionWithAgentModel(AgentModelModelEntity DescriptionWithAgentModel) {
        this.DescriptionWithAgentModel = DescriptionWithAgentModel;
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
