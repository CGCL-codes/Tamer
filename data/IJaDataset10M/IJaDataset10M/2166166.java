package mipt.math.sys.alt.solve.prefer;

import mipt.math.Number;
import mipt.math.array.DefaultVector;
import mipt.math.sys.Problem;
import mipt.math.sys.alt.AlternativesProblem;
import mipt.math.sys.alt.DecisionSolution;
import mipt.math.sys.alt.DefaultDecisionSolution;
import mipt.math.sys.alt.ScoringSolution;
import mipt.math.sys.alt.prefer.PreferencesProblem;
import mipt.math.sys.alt.solve.AbstractAlternativesSolver;
import mipt.math.sys.alt.solve.ScoresGroupingAlgorithm;
import mipt.math.sys.num.Algorithm;
import mipt.math.sys.num.SolveException;

/**
 * Makes a decision based on alternatives' scores computed by PreferenceAlgorithm
 *  from PreferencesProblem (you may not use this class but call PreferenceAlgorithm
 *  directly - e.g. if you don't need group-grading of scores vector).
 * If the problem given to setProblem is not PreferencesProblems, converts it using
 *  Assessments2PreferencesPreprocessor (by default preprocessor does not
 *  produce normalized preferences and can't work with [0;X]-scale assessments). 
 * Both interface and implementation in this class.
 * @author Evdokimov
 */
public class PreferenceSolver extends AbstractAlternativesSolver {

    private PreferencesProblem problem;

    private PreferenceAlgorithm preferenceAlgorithm;

    private Assessments2PreferencesPreprocessor preprocessor;

    protected Number[] allScoresArray;

    public final Assessments2PreferencesPreprocessor getPreprocessor() {
        if (preprocessor == null) preprocessor = initPreprocessor();
        return preprocessor;
    }

    public void setPreprocessor(Assessments2PreferencesPreprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    /**
	 * Factory method
	 */
    protected Assessments2PreferencesPreprocessor initPreprocessor() {
        return new Assessments2PreferencesPreprocessor();
    }

    /**
	 * @see mipt.math.sys.num.Solver#setProblem(mipt.math.sys.Problem)
	 */
    public void setProblem(Problem problem) {
        PreferencesProblem pr;
        if (problem instanceof PreferencesProblem) pr = (PreferencesProblem) problem; else pr = (PreferencesProblem) getPreprocessor().transformProblem((AlternativesProblem) problem);
        setPreferencesProblem(pr);
    }

    /**
	 * @see mipt.math.sys.num.Solver#getProblem()
	 */
    public final Problem getProblem() {
        return getPreferencesProblem();
    }

    /**
	 * "Interface method"
	 */
    public final PreferencesProblem getPreferencesProblem() {
        return problem;
    }

    /**
	 * "Interface method"
	 */
    public void setPreferencesProblem(PreferencesProblem problem) {
        this.problem = problem;
    }

    /**
	 * "Interface method" (for convenience only)
	 */
    public DecisionSolution solve(PreferencesProblem problem) throws SolveException {
        setPreferencesProblem(problem);
        solve();
        return getDecisionSolution();
    }

    /**
	 * @see mipt.math.sys.num.Solver#solve()
	 */
    public void solve() throws SolveException {
        allScoresArray = getPreferenceAlgorithm().calcScores(getPreferencesProblem());
        calcSolution(allScoresArray);
    }

    /**
	 * @see mipt.math.sys.num.Solver#setAlgorithm(mipt.math.sys.num.Algorithm)
	 */
    public void setAlgorithm(Algorithm alg) {
        if (alg instanceof PreferenceAlgorithm) setPreferenceAlgorithm((PreferenceAlgorithm) alg); else if (alg instanceof ScoresGroupingAlgorithm) setGroupingAlgorithm((ScoresGroupingAlgorithm) alg);
    }

    /**
	 * @param preferenceAlgorithm
	 */
    public void setPreferenceAlgorithm(PreferenceAlgorithm preferenceAlgorithm) {
        this.preferenceAlgorithm = preferenceAlgorithm;
    }

    /**
	 * @return preferenceAlgorithm
	 */
    public final PreferenceAlgorithm getPreferenceAlgorithm() {
        if (preferenceAlgorithm == null) preferenceAlgorithm = initPreferenceAlgorithm();
        return preferenceAlgorithm;
    }

    /**
	 * Factory method
	 */
    protected PreferenceAlgorithm initPreferenceAlgorithm() {
        return new PreferenceAlgorithm();
    }

    /**
	 * Overrides alternative's methods not only of Virtual but of Default solution
	 *  because default implementation of these methods are slow and not always correct.
	 * @see mipt.math.sys.alt.solve.AbstractAlternativesSolver#initSolution(boolean, Object)
	 */
    protected DecisionSolution initSolution(boolean withGroups, Object scores) {
        if (withGroups) return new DefaultDecisionSolution() {

            public Number getScoreForAlternative(int alternativeIndex) {
                return allScoresArray[alternativeIndex];
            }

            public int getAlternativesCount() {
                return allScoresArray.length;
            }
        }; else return new ScoringSolution(new DefaultVector(allScoresArray));
    }
}
