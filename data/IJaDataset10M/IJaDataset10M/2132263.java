package de.fzi.mappso.basematcher;

import java.util.Set;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * A base matcher that computes a distance of {@link OWLClass}es according to the
 * sets of individuals that are asserted to the respective classes. A correspondence
 * has a smaller distance, if there is a larger number of corresponding individuals
 * asserted to the respective classes.
 * 
 * @author bock
 *
 */
public class IndividualsOfClassDistanceMatcher extends AbstractClassBaseMatcher {

    @Override
    protected double computeDistance(Cell cell) throws InfeasibleBaseMatcherException {
        double result = 1.d;
        OWLClass ent1 = (OWLClass) cell.getObject1();
        OWLClass ent2 = (OWLClass) cell.getObject2();
        Set<OWLIndividual> individuals1 = ent1.getIndividuals(ontology1);
        Set<OWLIndividual> individuals2 = ent2.getIndividuals(ontology2);
        if (individuals1.isEmpty() || individuals2.isEmpty()) throw new InfeasibleBaseMatcherException("At least one class in the correspondence has no asserted individuals.");
        int maxMatches = Math.min(individuals1.size(), individuals2.size());
        int matches = 0;
        for (OWLIndividual ind1 : individuals1) if (corrsByObj1.containsKey(ind1)) for (Cell candCorr : corrsByObj1.get(ind1)) if (individuals2.contains(candCorr.getObject2())) matches++;
        result = 1. - ((double) matches / (double) maxMatches);
        return result;
    }
}
