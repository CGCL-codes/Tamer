package odm.adapter.entities;

import java.util.Collections;
import odm.OWL;
import odm.OdmPackage;
import org.eclipse.emf.common.notify.Notification;
import org.semanticweb.kaon2.api.KAON2Exception;
import org.semanticweb.kaon2.api.KAON2Manager;
import org.semanticweb.kaon2.api.OntologyChangeEvent;
import org.semanticweb.kaon2.api.owl.elements.Individual;
import org.semanticweb.kaon2.api.owl.elements.OWLClass;
import org.semanticweb.kaon2.api.owl.elements.OWLEntity;
import com.ontoprise.ontostudio.owl.model.OWLModel;

public class IndividualAdapter extends EntityAdapter {

    @Override
    public void notification(Notification notification) {
        odm.Individual individual = (odm.Individual) notification.getNotifier();
        switch(notification.getFeatureID(odm.Individual.class)) {
            case OdmPackage.INDIVIDUAL__URI:
                System.out.println("individual uri changed " + notification.getOldValue() + " to: " + individual.getURI());
                changeOWLEntityUri((OWL) individual.eContainer(), notification.getOldStringValue(), notification.getNewStringValue());
                break;
        }
    }

    @Override
    protected void createNewOWLEntity(String toUri, OWLModel model) throws KAON2Exception {
        odm.Individual odmIndividual = (odm.Individual) getTarget();
        odm.ClassAssertion odmClassAssertion = (odm.ClassAssertion) odmIndividual.getClassMemberOf().get(0);
        odm.OWLClass odmClass = odmClassAssertion.getOwlClass();
        Individual individual = KAON2Manager.factory().individual(toUri);
        OWLClass owlClass = (OWLClass) getOWLModelEntityFromOdmEntity(odmClass, model);
        model.getOntology().applyChanges(Collections.singletonList(new OntologyChangeEvent(KAON2Manager.factory().classMember(owlClass, individual), OntologyChangeEvent.ChangeType.ADD)));
    }

    @Override
    protected OWLEntity newOWLEntity(String toUri) {
        return KAON2Manager.factory().individual(toUri);
    }
}
