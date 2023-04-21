package ingenias.jade.mental;

import java.util.*;
import ingenias.jade.components.*;
import ingenias.editor.entities.*;
import ingenias.editor.entities.ViewPreferences.ViewType;

public class Answer extends ingenias.editor.entities.RuntimeFact {

    public Answer(String id) {
        super(id);
        this.getPrefs().setView(ViewType.UML);
        this.type = "Answer";
    }

    public Answer() {
        super(ingenias.jade.MentalStateManager.generateMentalEntityID());
        this.getPrefs().setView(ViewType.UML);
    }

    public String toString() {
        return this.getId() + ":" + this.getType();
    }

    public String getType() {
        return "Answer";
    }

    public String getParentType() {
        return "RuntimeFact";
    }
}
