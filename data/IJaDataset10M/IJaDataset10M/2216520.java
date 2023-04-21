package ingenias.jade.mental;

import java.util.*;
import ingenias.jade.components.*;
import ingenias.editor.entities.*;
import ingenias.editor.entities.ViewPreferences.ViewType;

public class Greetings2u2 extends ingenias.editor.entities.RuntimeFact {

    public Greetings2u2(String id) {
        super(id);
        this.getPrefs().setView(ViewType.UML);
        this.type = "Greetings2u2";
    }

    public Greetings2u2() {
        super(ingenias.jade.MentalStateManager.generateMentalEntityID());
        this.getPrefs().setView(ViewType.UML);
    }

    public String toString() {
        return this.getId() + ":" + this.getType();
    }

    public String getType() {
        return "Greetings2u2";
    }
}
