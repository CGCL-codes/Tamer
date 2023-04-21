package de.fzi.harmonia.commons.alignmentevaluators.ontologyevaluators;

import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import de.fzi.harmonia.commons.ConfigurationException;
import de.fzi.harmonia.commons.InfeasibleEvaluatorException;
import de.fzi.harmonia.commons.alignmentevaluators.AlignmentEvaluator;
import de.fzi.kadmos.api.Alignment;

/**
 * {@link AlignmentEvaluator} that evaluates an alignment
 * according to the number of unsatisfiable classes it
 * induces. The ontologies are merged and correspondences
 * are interpreted as equivalence axioms.
 * 
 * The {@link #getEvaluation(de.fzi.kadmos.api.Evaluable)} method
 * returns values from 0 to 1, where 1 means that the alignment induces
 * no additional unsatisfiable classes, and 0 means that all classes 
 * become unsatisfiable. Note that if one of the ontologies already contain
 * unsatisfiable classes, those are not considered to be caused by
 * the alignment. 
 * 
 * @author Juergen Bock (bock@fzi.de)
 * @author Matthias Stumpp (stumpp@fzi.de)
 *
 */
public class AlignmentCoherenceEvaluator extends AbstractOntologyMergerMeasuresEvaluator {

    private static final Log logger = LogFactory.getLog(AlignmentCoherenceEvaluator.class);

    /**
     * Creates a new {@link AlignmentCoherenceEvaluator}.
     * @param p Configuration parameters.
     * @param id Identifier.
     * @param align Alignment to be evaluated.
     * @throws ConfigurationException if there is a problem with the
     *                                configuration parameters.
     */
    public AlignmentCoherenceEvaluator(Properties p, String id, Alignment align) throws ConfigurationException {
        super(p, id, align);
    }

    @Override
    protected double computeEvaluation() throws InfeasibleEvaluatorException {
        if (ontMergerMeasures.getClassesInFreshMergedOntology().size() != ontMergerMeasures.getClassesInEnrichedMergedOntology().size()) {
            final String errMsg = "number of classes in fresh merged ontology: " + ontMergerMeasures.getClassesInFreshMergedOntology().size() + "number of classes in enriched merged ontology: " + ontMergerMeasures.getClassesInEnrichedMergedOntology().size();
            if (logger.isDebugEnabled()) {
                logger.debug(errMsg);
            }
            throw new InfeasibleEvaluatorException(errMsg);
        }
        double result, distance;
        try {
            if (ontMergerMeasures.getUnsatisfiableClassesInFreshMergedOntology().size() < ontMergerMeasures.getClassesInFreshMergedOntology().size()) {
                distance = (double) (ontMergerMeasures.getUnsatisfiableClassesInEnrichedMergedOntology().size() - ontMergerMeasures.getUnsatisfiableClassesInFreshMergedOntology().size()) / (double) (ontMergerMeasures.getClassesInFreshMergedOntology().size() - ontMergerMeasures.getUnsatisfiableClassesInFreshMergedOntology().size());
            } else {
                distance = 0.d;
            }
        } catch (InconsistentOntologyException e) {
            distance = 1.d;
        }
        result = 1.d - distance;
        if (logger.isDebugEnabled()) {
            logger.debug("result= " + result);
        }
        return result;
    }
}
