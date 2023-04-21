package collaboration.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AddProject
* @author ontology bean generator
* @version 2009/06/29, 22:52:40
*/
public class AddProject implements AgentAction {

    /**
* Protege name: projectParameter
   */
    private Project projectParameter;

    public void setProjectParameter(Project value) {
        this.projectParameter = value;
    }

    public Project getProjectParameter() {
        return this.projectParameter;
    }
}
