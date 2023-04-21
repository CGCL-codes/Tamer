package ingenias.jade.components;

import java.util.*;
import ingenias.jade.exception.*;
import ingenias.jade.comm.*;
import ingenias.jade.mental.*;
import ingenias.editor.entities.*;

public class AnotherTaskTask extends Task {

    public AnotherTaskTask(String id) {
        super(id, "AnotherTask");
    }

    public void execute() throws TaskException {
        DemoFact eiDemoFact = (DemoFact) this.getFirstInputOfType("DemoFact");
        Vector<TaskOutput> outputs = this.getOutputs();
        TaskOutput defaultOutput = outputs.firstElement();
        TaskOutput outputsdefault = findOutputAlternative("default", outputs);
        YellowPages yp = null;
    }
}
