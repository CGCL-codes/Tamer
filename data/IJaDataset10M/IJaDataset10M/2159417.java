package gov.sns.tools.solver.solutionjudge;

import gov.sns.tools.solver.*;
import java.util.*;

/**
 * This judge weights the least satisfied objective most and each subsequent one half less than the prior one.
 * Each objective satisfaction must be based on a range of 0 to 1.
 * @author t6p
 */
public class WorstObjectiveBiasedJudge extends SolutionJudge {

    /** the bias weight */
    protected final double BIAS_WEIGHT;

    /** the current best satisfaction */
    protected double _bestSatisfaction;

    /** used to normalize the total satisfaction to a scale of 0 to 1 */
    protected double _totalWeight;

    /** the current list of the most optimal solutions */
    protected List<Trial> _optimalSolutions;

    /** Constructor */
    public WorstObjectiveBiasedJudge() {
        this(0.25);
    }

    /** Constructor */
    public WorstObjectiveBiasedJudge(final double biasWeight) {
        BIAS_WEIGHT = biasWeight;
        _bestSatisfaction = 0.0;
        _totalWeight = 0.0;
        _optimalSolutions = new ArrayList<Trial>();
    }

    /**
	 * Reset the satisfaction sum judge.
	 */
    @Override
    public void reset() {
        _bestSatisfaction = 0.0;
        _totalWeight = 0.0;
        _optimalSolutions = new ArrayList<Trial>();
    }

    /**
	 * Get the optimal solutions.
	 * @return a list of solutions
	 */
    @Override
    public List<Trial> getOptimalSolutions() {
        return _optimalSolutions;
    }

    /**
	 * Judge the trial.
	 * @param trial The trial with which to update the solution judge.
	 */
    @Override
    public void judge(final Trial trial) {
        final List<Objective> objectives = trial.getProblem().getObjectives();
        final int numObjectives = objectives.size();
        final List<Double> satisfactions = new ArrayList<Double>(numObjectives);
        final Iterator<Objective> objectiveIter = objectives.iterator();
        while (objectiveIter.hasNext()) {
            final Objective objective = objectiveIter.next();
            final double satisfaction = trial.getScore(objective).getSatisfaction();
            satisfactions.add(new Double(satisfaction));
        }
        Collections.sort(satisfactions);
        double weightedSum = 0.0;
        double weight = 1.0;
        final Iterator<Double> satisfactionIter = satisfactions.iterator();
        while (satisfactionIter.hasNext()) {
            final double satisfaction = satisfactionIter.next().doubleValue();
            weightedSum += weight * satisfaction;
            weight *= BIAS_WEIGHT;
        }
        if (_totalWeight == 0.0) {
            _totalWeight = (1.0 - Math.pow(BIAS_WEIGHT, numObjectives)) / (1.0 - BIAS_WEIGHT);
        }
        final double totalSatisfaction = weightedSum / _totalWeight;
        trial.setSatisfaction(totalSatisfaction);
        if (totalSatisfaction == _bestSatisfaction) {
            _optimalSolutions.add(trial);
            _eventProxy.foundNewOptimalSolution(this, _optimalSolutions, trial);
        } else if (totalSatisfaction > _bestSatisfaction) {
            _bestSatisfaction = totalSatisfaction;
            _optimalSolutions.clear();
            _optimalSolutions.add(trial);
            _eventProxy.foundNewOptimalSolution(this, _optimalSolutions, trial);
        }
    }
}
