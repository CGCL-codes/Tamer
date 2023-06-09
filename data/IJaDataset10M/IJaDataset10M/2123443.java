package br.ufal.ic.forbile.infra.emathema.ontology.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.*;
import br.ufal.ic.forbile.infra.emathema.ontology.*;

/**
 * Generated by Protege-OWL  (http://protege.stanford.edu/plugins/owl).
 * Source OWL Class: http://www.owl-ontologies.com/emathema.owl#OutcomeActivity
 *
 * @version generated on Wed Sep 27 02:26:49 GMT-03:00 2006
 */
public class DefaultOutcomeActivity extends DefaultTHINGCSCLDynamic implements OutcomeActivity {

    public DefaultOutcomeActivity(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }

    public DefaultOutcomeActivity() {
    }

    public Activity getIsComposedBy_Activity() {
        return (Activity) getPropertyValueAs(getIsComposedBy_ActivityProperty(), Activity.class);
    }

    public RDFProperty getIsComposedBy_ActivityProperty() {
        final String uri = "http://www.owl-ontologies.com/emathema.owl#isComposedBy_Activity";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasIsComposedBy_Activity() {
        return getPropertyValueCount(getIsComposedBy_ActivityProperty()) > 0;
    }

    public void setIsComposedBy_Activity(Activity newIsComposedBy_Activity) {
        setPropertyValue(getIsComposedBy_ActivityProperty(), newIsComposedBy_Activity);
    }

    public Community getIsComposedBy_Community() {
        return (Community) getPropertyValueAs(getIsComposedBy_CommunityProperty(), Community.class);
    }

    public RDFProperty getIsComposedBy_CommunityProperty() {
        final String uri = "http://www.owl-ontologies.com/emathema.owl#isComposedBy_Community";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasIsComposedBy_Community() {
        return getPropertyValueCount(getIsComposedBy_CommunityProperty()) > 0;
    }

    public void setIsComposedBy_Community(Community newIsComposedBy_Community) {
        setPropertyValue(getIsComposedBy_CommunityProperty(), newIsComposedBy_Community);
    }

    public String getOutcome() {
        return (String) getPropertyValue(getOutcomeProperty());
    }

    public RDFProperty getOutcomeProperty() {
        final String uri = "http://www.owl-ontologies.com/emathema.owl#outcome";
        final String name = getOWLModel().getResourceNameForURI(uri);
        return getOWLModel().getRDFProperty(name);
    }

    public boolean hasOutcome() {
        return getPropertyValueCount(getOutcomeProperty()) > 0;
    }

    public void setOutcome(String newOutcome) {
        setPropertyValue(getOutcomeProperty(), newOutcome);
    }
}
