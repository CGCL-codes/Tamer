package issrg.editor2.ontology;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;

public class ParameterInstance extends OntologyInstance {

    public static final String TYPE_PROPERTY = "Parameter_has_Type";

    protected ParameterInstance(OWLIndividual individual) {
        super(individual);
    }

    public String getType() {
        return (String) getDatatypePropertyValue(TYPE_PROPERTY);
    }
}
