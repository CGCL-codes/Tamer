package ingenias.jade.mental;

import java.util.*;
import ingenias.jade.components.*;
import ingenias.editor.entities.*;
import ingenias.editor.entities.ViewPreferences.ViewType;

public class SearchTimeExceeded extends ingenias.editor.entities.RuntimeFact {

    public SearchTimeExceeded(String id) {
        super(id);
        this.getPrefs().setView(ViewType.UML);
        this.type = "SearchTimeExceeded";
    }

    public SearchTimeExceeded() {
        super(ingenias.jade.MentalStateManager.generateMentalEntityID());
        this.getPrefs().setView(ViewType.UML);
    }

    public String toString() {
        return this.getId() + ":" + this.getType();
    }

    public String getType() {
        return "SearchTimeExceeded";
    }

    public String getParentType() {
        return "RuntimeFact";
    }
}
