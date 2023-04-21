package ingenias.editor.entities;

import java.util.*;
import ingenias.editor.TypedVector;

public class DeploymentUnitByTypeWithInitMS extends DeploymentUnitByType {

    public ingenias.editor.entities.AgentModelModelEntity InitialState;

    public DeploymentUnitByTypeWithInitMS(String id) {
        super(id);
        this.setHelpDesc("<br>				A deploy unit is an entity that defines how many instances of agents will exist in the final system<br>			");
        this.setHelpRecom("");
    }

    public AgentModelModelEntity getInitialState() {
        return InitialState;
    }

    public void setInitialState(AgentModelModelEntity InitialState) {
        this.InitialState = InitialState;
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
