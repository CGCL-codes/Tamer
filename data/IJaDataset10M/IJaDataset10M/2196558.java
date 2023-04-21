package ingenias.jade.mental;

import java.util.*;
import ingenias.jade.components.*;
import ingenias.editor.entities.*;
import ingenias.editor.entities.ViewPreferences.ViewType;

public class FailOneParticipan2 extends ingenias.editor.entities.RuntimeFact {

    public FailOneParticipan2(String id) {
        super(id);
        this.getPrefs().setView(ViewType.UML);
        this.type = "FailOneParticipan2";
    }

    public FailOneParticipan2() {
        super(ingenias.jade.MentalStateManager.generateMentalEntityID());
        this.getPrefs().setView(ViewType.UML);
    }

    public String toString() {
        return this.getId() + ":" + this.getType();
    }

    public String getType() {
        return "FailOneParticipan2";
    }
}
