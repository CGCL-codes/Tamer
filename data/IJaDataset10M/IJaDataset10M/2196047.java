package br.ufal.ic.forbile.infra.emathema.ontology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.*;
import br.ufal.ic.forbile.infra.emathema.ontology.*;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/emathema.owl#Role
 *
 * @version generated on Wed Sep 27 02:26:49 GMT-03:00 2006
 */
public class DefaultRole extends DefaultTHINGLearnerStatic implements Role {

    public DefaultRole(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }

    public DefaultRole() {
    }

    public String getCharacteristic() {
        return (String) getPropertyValue(getCharacteristicProperty());
    }

    public RDFProperty getCharacteristicProperty() {
        final String uri = "http://www.owl-ontologies.com/emathema.owl#characteristic";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasCharacteristic() {
        return getPropertyValueCount(getCharacteristicProperty()) > 0;
    }

    public void setCharacteristic(String newCharacteristic) {
        setPropertyValue(getCharacteristicProperty(), newCharacteristic);
    }
}