package fr.ecp.lgi.disaggregation;

import org.decisiondeck.jlp.LpResultStatus;
import org.decisiondeck.jlp.parameters.LpParameters;
import org.decisiondeck.jlp.solution.LpSolverDuration;
import com.google.common.base.Preconditions;

public class DisaggregationProblemResultsForwarder implements IDisaggregationProblemResults {

    private final IDisaggregationProblemResults m_delegate;

    @Override
    public LpSolverDuration getDuration() {
        return m_delegate.getDuration();
    }

    @Override
    public LpParameters getParameters() {
        return m_delegate.getParameters();
    }

    @Override
    public LpResultStatus getStatus() {
        return m_delegate.getStatus();
    }

    /**
     * @param delegate
     *            not <code>null</code>, must not contain a <code>null</code> value for duration, parameters and status.
     *            Some parameters may have a <code>null</code> value, however.
     */
    public DisaggregationProblemResultsForwarder(IDisaggregationProblemResults delegate) {
        Preconditions.checkNotNull(delegate);
        m_delegate = delegate;
    }
}
