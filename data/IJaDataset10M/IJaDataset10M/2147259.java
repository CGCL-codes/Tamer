package de.fzi.herakles.anytime;

import org.semanticweb.owl.inference.OWLReasonerBase;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;

/**
 * Interface of anytime Instance reasoner 
 * @author xu
 *
 */
public interface AnytimeOWLIndividualReasoner extends OWLReasonerBase {

    public void getIndividuals(OWLDescription description, AnytimeListener<OWLIndividual> listener) throws OWLReasonerException;
}
