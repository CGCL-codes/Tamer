package fr.ecp.lgi.disaggregation.infer_shared_profiles;

import org.decisiondeck.jmcda.structure.sorting.problem.group_results.IGroupSortingResults;
import com.google.common.base.Preconditions;
import fr.ecp.lgi.disaggregation.DisaggregationProblemResultsForwarder;
import fr.ecp.lgi.disaggregation.IDisaggregationProblemResults;

public class InferSharedProfilesResults extends DisaggregationProblemResultsForwarder {

    private final InferSharedProfilesProblem m_problem;

    /**
     * @param problem
     *            not <code>null</code>.
     * @param results
     *            not <code>null</code>.
     * @param sortingResults
     *            may be <code>null</code>.
     * @param slackResult
     *            may be <code>null</code>.
     */
    public InferSharedProfilesResults(InferSharedProfilesProblem problem, IDisaggregationProblemResults results, IGroupSortingResults sortingResults, Double slackResult) {
        super(results);
        Preconditions.checkNotNull(problem);
        m_problem = problem;
        m_sortingResults = sortingResults;
        m_slackResult = slackResult;
    }

    /**
     * Retrieves the problem these results relate to.
     * 
     * @return not <code>null</code>.
     */
    public InferSharedProfilesProblem getProblem() {
        return m_problem;
    }

    private final Double m_slackResult;

    private final IGroupSortingResults m_sortingResults;

    /**
     * @return may be <code>null</code>.
     */
    public IGroupSortingResults getSortingResults() {
        return m_sortingResults;
    }

    /**
     * @return may be <code>null</code>.
     */
    public Double getSlackResult() {
        return m_slackResult;
    }
}
