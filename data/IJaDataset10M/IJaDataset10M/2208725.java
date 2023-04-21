package de.fzi.harmonia.commons.alignmentevaluators.ontologyevaluators;

import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.fzi.harmonia.commons.ConfigurationException;
import de.fzi.harmonia.commons.InfeasibleEvaluatorException;
import de.fzi.harmonia.commons.alignmentevaluators.AlignmentEvaluator;
import de.fzi.kadmos.api.Alignment;

/**
 * {@link AlignmentEvaluator} that checks whether an alignment
 * induces logical inconsistency when the ontologies are merged
 * and correspondences are interpreted as equivalence axioms.
 * 
 * The {@link #getEvaluation(de.fzi.kadmos.api.Evaluable)} method
 * returns only the discrete values 0 or 1, where
 * 1 means that no logical inconsistency is caused by the alignment
 * and 0 otherwise. Note that if the ontology created by merging
 * only axioms from the two input ontologies without interpreting
 * the alignment is already inconsistent, this evaluator will
 * return 1.
 * 
 * @author Juergen Bock (bock@fzi.de)
 * @author Matthias Stumpp (stumpp@fzi.de)
 *
 */
public class AlignmentConsistencyEvaluator extends AbstractOntologyMergerMeasuresEvaluator {

    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(AlignmentConsistencyEvaluator.class);

    /**
     * Creates a new {@link AlignmentConsistencyEvaluator}.
     * @param p Configuration parameters.
     * @param id Identifier.
     * @param align Alignment to be evaluated.
     * @throws ConfigurationException if there is a problem with the
     *                                configuration parameters.
     */
    public AlignmentConsistencyEvaluator(Properties p, String id, Alignment align) throws ConfigurationException {
        super(p, id, align);
    }

    @Override
    protected double computeEvaluation() throws InfeasibleEvaluatorException {
        if (!ontMergerMeasures.isFreshMergedOntologyConsistent()) return 1.d;
        if (!ontMergerMeasures.isEnrichedMergedOntologyConsistent()) return 0.d;
        return 1.d;
    }
}
