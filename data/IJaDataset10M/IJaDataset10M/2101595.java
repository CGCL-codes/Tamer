package ingenias.jade.components;

import java.util.*;
import ingenias.jade.exception.*;
import ingenias.jade.comm.*;
import ingenias.jade.mental.*;
import ingenias.editor.entities.*;

public class UpdateTTask extends Task {

    public UpdateTTask(String id) {
        super(id, "UpdateT");
    }

    public void execute() throws TaskException {
        UpdateE eiUpdateE = (UpdateE) this.getFirstInputOfType("UpdateE");
        StateFF eiStateFF = (StateFF) this.getFirstInputOfType("StateFF");
        Vector<TaskOutput> outputs = this.getOutputs();
        TaskOutput defaultOutput = outputs.firstElement();
        TaskOutput outputsdefault = findOutputAlternative("default", outputs);
        YellowPages yp = null;
        eiStateFF.setSX(eiUpdateE.getX());
        eiStateFF.setSY(eiUpdateE.getY());
    }
}
