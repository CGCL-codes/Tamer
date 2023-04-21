package uk.co.ordnancesurvey.confluence.ui.listener.ontologychange;

import org.semanticweb.owl.model.OWLClass;
import uk.co.ordnancesurvey.confluence.model.ConfluenceModelManager;

/**
 * Base ontology listener class which leaves subclasses to implement those
 * methods for handling changes in an OWLObjectProperty.
 * 
 * @author rdenaux
 * 
 */
public abstract class BaseOWLObjectPropertyAddAndRemoveListener extends BaseOWLEntityAddAndRemoveListener {

    public BaseOWLObjectPropertyAddAndRemoveListener(ConfluenceModelManager modelManager) {
        super(modelManager);
    }

    @Override
    protected void handleClassAdded(OWLClass class1) {
    }

    @Override
    protected void handleClassDeleted(OWLClass class1) {
    }
}
