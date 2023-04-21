package de.fzi.harmonia.commons.basematchers.classbasematcher;

import java.util.Properties;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import de.fzi.harmonia.commons.ConfigurationException;
import de.fzi.harmonia.commons.InfeasibleEvaluatorException;
import de.fzi.kadmos.api.Alignment;

/**
 * This basematcher compares the disjoint classes of the given entities.
 * The result is composed of two parts:
 * the first 0.5 points of the distance depend on the quantity of disjointclassexpressions 
 *  (have both entites the same quantity of those they get 0 if it differs they get the proportion)
 * the second part compares how many of those disjointclassexpressions are backed by a correspondence in the current alignment.
 * @author Carsten Daenschel
 */
public class ClassDisjointHierarchyDistanceMatcher extends AbstractClassBaseMatcher {

    public ClassDisjointHierarchyDistanceMatcher(Properties p, String id, Alignment align) throws ConfigurationException {
        super(p, id, align);
    }

    private Log logger = LogFactory.getLog(ClassDisjointHierarchyDistanceMatcher.class);

    @Override
    protected <T extends OWLEntity> double computeDistance(T entity1, T entity2) throws InfeasibleEvaluatorException {
        OWLClass class1 = (OWLClass) entity1;
        OWLClass class2 = (OWLClass) entity2;
        Set<OWLClassExpression> disjoint1 = class1.getDisjointClasses(alignment.getOntology1());
        Set<OWLClassExpression> disjoint2 = class2.getDisjointClasses(alignment.getOntology2());
        double result = 0;
        result = compareAligned(disjoint1, disjoint2) / 2;
        if (logger.isDebugEnabled()) logger.debug("Alignedscore: " + result);
        result += compareQuantity(disjoint1, disjoint2) / 2;
        if (logger.isDebugEnabled()) logger.debug("final score: " + result);
        return 1 - result;
    }

    private double compareAligned(Set<OWLClassExpression> disjoint1, Set<OWLClassExpression> disjoint2) {
        if (logger.isDebugEnabled()) logger.debug("disjoint1Set: " + disjoint1.size() + " disjoint2Set: " + disjoint2.size());
        if (disjoint1.isEmpty() && disjoint2.isEmpty()) return 1;
        if (disjoint1.isEmpty()) return 0;
        if (disjoint2.isEmpty()) return 0;
        int score = 0;
        for (OWLClassExpression a : disjoint1) {
            for (OWLClassExpression b : disjoint2) {
                OWLClass class1 = a.asOWLClass();
                OWLClass class2 = b.asOWLClass();
                if (alignment.contains(class1, class2)) score++;
            }
        }
        if (logger.isDebugEnabled()) logger.debug(score + ":" + Math.max(disjoint1.size(), disjoint2.size()));
        return (double) score / (double) Math.max(disjoint1.size(), disjoint2.size());
    }

    /**
	 * Returns min(A,B) / max(A,B)
	 * with A = number of disjointclass expressions of entity1
	 *      B = number of disjointclass expressions of entity2
	 * This should serve as a measure of similarity. 
	 * 
	 * @param entity1
	 * @param entity2
	 * @param disjoint1
	 * @param disjoint2
	 * @return
	 */
    private double compareQuantity(Set<OWLClassExpression> disjoint1, Set<OWLClassExpression> disjoint2) {
        int quantity1 = disjoint1.size();
        int quantity2 = disjoint2.size();
        if (quantity1 == 0 && quantity2 == 0) return 1;
        return (double) Math.min(quantity1, quantity2) / (double) Math.max(quantity1, quantity2);
    }
}
