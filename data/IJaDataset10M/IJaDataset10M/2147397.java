package de.fzi.harmonia.commons.basematchers.structuralbasematcher;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLProperty;
import de.fzi.harmonia.commons.ConfigurationException;
import de.fzi.harmonia.commons.InfeasibleEvaluatorException;
import de.fzi.kadmos.api.Alignment;

/**
 * A base matcher that computes a distance based on the hierarchical position of the entities
 * to be compared. In particular this takes into account sub-/super classes/properties of the
 * class/property under consideration and checks, whether the sub or super entities are already
 * corresponding. 
 * 
 * This basematcher computes a fitness by combining if the number of super/sub-classses for 
 * both entities match and whether the super/sub-classes are corresponding according to
 * the current alignment. 
 *  
 * @author Juergen Bock (bock@fzi.de)
 * @author Carsten Daenschel
 *
 */
public class HierarchyDistanceMatcher extends StructuralDistanceMatcher {

    private Log logger = LogFactory.getLog(HierarchyDistanceMatcher.class);

    /**
     * Creates a new instance.
     * @param p Configuration parameters.
     * @param id Identifier.
     * @param align Alignment context.
     * @throws ConfigurationException if a configuration problem occurs.
     */
    public HierarchyDistanceMatcher(Properties p, String id, Alignment align) throws ConfigurationException {
        super(p, id, align);
        if (logger.isInfoEnabled()) logger.info("Initialised " + this.getClass().getName() + " (ID=" + id + ").");
    }

    @Override
    protected <T extends OWLEntity> double computeDistance(T entity1, T entity2) throws InfeasibleEvaluatorException {
        Set<OWLEntity> super1 = null, super2 = null, sub1 = null, sub2 = null;
        if (entity1 instanceof OWLClass) {
            super1 = new HashSet<OWLEntity>(getNamedSuperClasses((OWLClass) entity1, alignment.getOntology1()).keySet());
            super2 = new HashSet<OWLEntity>(getNamedSuperClasses((OWLClass) entity2, alignment.getOntology2()).keySet());
            sub1 = new HashSet<OWLEntity>(getNamedSubClasses((OWLClass) entity1, alignment.getOntology1()).keySet());
            sub2 = new HashSet<OWLEntity>(getNamedSubClasses((OWLClass) entity2, alignment.getOntology2()).keySet());
        } else if (entity1 instanceof OWLProperty<?, ?>) {
            super1 = new HashSet<OWLEntity>(getNamedSuperProperties((OWLProperty<?, ?>) entity1, alignment.getOntology1()).keySet());
            super2 = new HashSet<OWLEntity>(getNamedSuperProperties((OWLProperty<?, ?>) entity2, alignment.getOntology2()).keySet());
            sub1 = new HashSet<OWLEntity>(getNamedSubProperties((OWLProperty<?, ?>) entity1, alignment.getOntology1()).keySet());
            sub2 = new HashSet<OWLEntity>(getNamedSubProperties((OWLProperty<?, ?>) entity2, alignment.getOntology2()).keySet());
        }
        final double superSubProportion = getSuperSubProportion(Math.max(super1.size(), super2.size()), Math.max(sub1.size(), sub2.size()));
        double alignmentFitness = 0;
        alignmentFitness += compareToAlignment(super1, super2) * superSubProportion;
        alignmentFitness += compareToAlignment(sub1, sub2) * (1 - superSubProportion);
        double result = 1. - alignmentFitness;
        return result;
    }

    /**
	 * Computes the proportion of super entities
	 * for the total number of sub- and super-entities.
     * Example if 3 super- and 1 subclass exist the factor should be 3/4 and 
     * the weighting should be used as superclass fitness * 3/4 + subclass fitness * 1/4
     */
    private double getSuperSubProportion(int superClasses, int subClasses) {
        if (superClasses == 0) return 0;
        if (subClasses == 0) return 1;
        return superClasses / (double) (superClasses + subClasses);
    }

    private double compareQuantity(Set<OWLEntity> set1, Set<OWLEntity> set2) {
        assert (set1 != null && set2 != null);
        if (set1.isEmpty() && set2.isEmpty()) return 1; else if (set1.isEmpty() || set2.isEmpty()) return 0;
        double result = Math.min(set1.size(), set2.size()) / (double) Math.max(set1.size(), set2.size());
        if (logger.isDebugEnabled()) {
            logger.debug("comparing " + set1.size() + ":" + set2.size() + "=" + result);
        }
        return result;
    }

    /**
     * For two sets of entities, this method checks how many
     * of those entities are corresponding compared to how many
     * could possible correspond.
     * For instance, if set1 contains 2 entities,
     * and set2 contains 3 entities, there is a maximum number of
     * 2 correspondences. If in fact there is only one correspondence,
     * the method returns 1 / 2 = 0.5 
     * @return A value between 0 and 1, where 0 means, no pair of entities
     *         of the two sets is corresponding, and 1 means, that the maximum
     *         number of pairs (the size of the smaller set) is actually corresponding.
     */
    private double compareToAlignment(Set<OWLEntity> set1, Set<OWLEntity> set2) {
        assert (set1 != null && set2 != null);
        double result;
        int corresponding = 0;
        if (set1.isEmpty() || set2.isEmpty()) result = 1.; else {
            for (OWLEntity a : set1) for (OWLEntity b : set2) if (alignment.contains(a, b)) corresponding++;
            result = (double) corresponding / (double) Math.min(set1.size(), set2.size());
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Size of set 1: " + set1.size());
            logger.trace("Size of set 2: " + set2.size());
            logger.trace("Number of corresponding entities from those sets: " + corresponding);
            logger.trace("Proportion: " + result);
        }
        return result;
    }
}
