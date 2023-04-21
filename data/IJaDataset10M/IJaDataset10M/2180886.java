package com.oat.stopcondition;

import com.oat.InvalidConfigurationException;
import com.oat.Solution;
import com.oat.SolutionEvaluationListener;

/**
 * Type: EvaluationsStopCondition<br/>
 * Date: 30/07/2007<br/>
 * <br/>
 * Description: Listens to solution evaluations, stop condition checks if
 * the number of evaluations equals or exceeds a predefined maximum number of evaluations
 * <br/>
 * @author Jason Brownlee
 *
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 *
 */
public class EvaluationsStopCondition extends GenericSolutionEvaluatedStopCondition implements SolutionEvaluationListener {

    /**
	 * Maximum number of evaluations supported
	 */
    protected long maxEvaluations = 1000L;

    /**
	 * Number of evaluations executed 
	 */
    protected long evaluationsCount;

    public EvaluationsStopCondition() {
    }

    public EvaluationsStopCondition(long aMaxEvaluations) {
        setMaxEvaluations(aMaxEvaluations);
    }

    @Override
    public String getName() {
        return "Total Evaluations";
    }

    @Override
    public boolean mustStopInternal() {
        if (evaluationsCount >= maxEvaluations) {
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        super.reset();
        evaluationsCount = 0;
    }

    @Override
    public void validateConfiguration() throws InvalidConfigurationException {
        if (maxEvaluations < 1) {
            throw new InvalidConfigurationException("MaxEvaluations must be >=1: " + maxEvaluations);
        }
    }

    public long getMaxEvaluations() {
        return maxEvaluations;
    }

    public void setMaxEvaluations(long maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public long getEvaluationsCount() {
        return evaluationsCount;
    }

    @Override
    public void solutionEvaluatedEvent(Solution evaluatedSolution) {
        evaluationsCount++;
    }
}
