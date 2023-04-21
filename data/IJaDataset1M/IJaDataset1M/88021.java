package mipt.math.sys.alt.solve;

import java.util.HashMap;
import mipt.math.Number;
import mipt.math.sys.alt.DecisionSolution;
import mipt.math.sys.alt.DefaultDecisionSolution;
import mipt.math.sys.alt.IntDecisionProblem;
import mipt.math.sys.alt.VirtualDecisionSolution;
import mipt.math.sys.alt.impl.ArithmeticFunctionAlgorithm;
import mipt.math.sys.num.Algorithm;
import mipt.math.sys.num.SolveException;

/**
 * Makes a decision based on alternatives' scores computed by ScoreAlternativeAlgorithm  
 *  (scores themselves can be get too - by getAlternativeScore(int)).
 * By default uses arithmetic utility functions as scores
 *  but another scoreAlgorithm can be set or overridden.
 * Note on implementation: depending on problem (is it Int*Problem or not), stores alternatives'
 *  scores either in array (such is faster) or in Map (allows client not to make solid enumeration).
 *  The same structures are sent as Object to ScoresGroupingAlgorithm.calcGroups (and e.g. to 
 *  LimitsGradingMethod and to DecisionSolutionCreator and maybe to the solution itself).
 * @author Evdokimov
 */
public class ScoresDecisionSolver extends AbstractDecisionSolver {

    private ScoreAlternativeAlgorithm scoreAlgorithm;

    protected Number[] allScoresArray;

    protected HashMap<Integer, Number> allScores;

    /**
	 * @see mipt.math.sys.num.Solver#setAlgorithm(mipt.math.sys.num.Algorithm)
	 */
    public void setAlgorithm(Algorithm alg) {
        if (alg instanceof ScoreAlternativeAlgorithm) setScoreAlgorithm((ScoreAlternativeAlgorithm) alg); else if (alg instanceof ScoresGroupingAlgorithm) setGroupingAlgorithm((ScoresGroupingAlgorithm) alg);
    }

    /**
	 * @param scoreAlgorithm
	 */
    public void setScoreAlgorithm(ScoreAlternativeAlgorithm scoreAlgorithm) {
        this.scoreAlgorithm = scoreAlgorithm;
    }

    /**
	 * @return scoreAlgorithm
	 */
    public final ScoreAlternativeAlgorithm getScoreAlgorithm() {
        if (scoreAlgorithm == null) scoreAlgorithm = initScoreAlgorithm();
        return scoreAlgorithm;
    }

    /**
	 * Factory method
	 */
    protected ScoreAlternativeAlgorithm initScoreAlgorithm() {
        return new ArithmeticFunctionAlgorithm();
    }

    /**
	 * Overrides alternative's methods not only of Virtual but of Default solution
	 *  because default implementation of these methods are slow and not always correct.
	 * @see mipt.math.sys.alt.solve.AbstractAlternativesSolver#initSolution(boolean, Object)
	 */
    protected DecisionSolution initSolution(boolean withGroups, Object scores) {
        if (withGroups) return new DefaultDecisionSolution() {

            public Number getScoreForAlternative(int alternativeIndex) {
                return getAlternativeScore(alternativeIndex);
            }

            public int getAlternativesCount() {
                return getAlternativeCount();
            }
        }; else return new VirtualDecisionSolution() {

            public Number getScoreForAlternative(int alternativeIndex) {
                return getAlternativeScore(alternativeIndex);
            }

            public int getAlternativesCount() {
                return getAlternativeCount();
            }
        };
    }

    /**
	 * @see mipt.math.sys.num.Solver#solve()
	 */
    public void solve() throws SolveException {
        Object scores;
        if (getDecisionProblem() instanceof IntDecisionProblem) {
            allScoresArray = calcAllScores(null);
            allScores = null;
            scores = allScoresArray;
        } else {
            if (allScores == null) allScores = new HashMap(getDecisionProblem().getAlternativesCount() * 4 / 3 + 1); else allScores.clear();
            calcAllScores(allScores);
            scores = allScores;
        }
        calcSolution(scores);
    }

    /**
	 * @see mipt.math.sys.alt.solve.AbstractDecisionSolver#calcScore(int)
	 */
    protected Number calcScore(int alternativeIndex) {
        return getScoreAlgorithm().calcAlternativeScore(alternativeIndex, getDecisionProblem());
    }

    /**
	 * @return score - for an alternative
	 */
    public final Number getAlternativeScore(int alternativeIndex) {
        return allScores == null ? allScoresArray[alternativeIndex] : allScores.get(alternativeIndex);
    }

    /**
	 * 
	 */
    public final int getAlternativeCount() {
        return allScores == null ? allScoresArray.length : allScores.size();
    }
}
