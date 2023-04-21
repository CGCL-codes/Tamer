package issrg.editor2.ontology;

import java.util.*;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;

public class ResourceInstance extends OntologyInstance {

    public static final String ACTION_PROPERTY = "Resource_has_Action";

    protected ResourceInstance(OWLIndividual individual) {
        super(individual);
    }

    public String[] getIncludeDNs() {
        return new String[] { "" };
    }

    public String[] getExcludeDNs() {
        return null;
    }

    public Collection<ActionInstance> getActions() {
        HashSet<ActionInstance> actions = null;
        Collection<OntologyInstance> instances = getObjectPropertyValues(ACTION_PROPERTY);
        if (instances != null && !instances.isEmpty()) {
            actions = new HashSet<ActionInstance>();
            for (OntologyInstance inst : instances) {
                actions.add((ActionInstance) inst);
            }
        }
        if (!isGenericInstance()) {
            ResourceInstance genericInstance = (ResourceInstance) getOntologyClass().getGenericInstance();
            if (genericInstance.getActions() != null && !genericInstance.getActions().isEmpty()) {
                if (actions == null) actions = new HashSet<ActionInstance>();
                actions.addAll(genericInstance.getActions());
            }
        } else {
            if (!getOntologyClass().getName().equals(PolicyOntology.RESOURCE_CLASS)) {
                OntologyClass superClass = getOntologyClass().getSuperClasses().iterator().next();
                ResourceInstance superGenericInstance = ((ResourceInstance) superClass.getGenericInstance());
                if (superGenericInstance.getActions() != null && !superGenericInstance.getActions().isEmpty()) {
                    if (actions == null) actions = new HashSet<ActionInstance>();
                    actions.addAll(superGenericInstance.getActions());
                }
            }
        }
        return actions;
    }
}
