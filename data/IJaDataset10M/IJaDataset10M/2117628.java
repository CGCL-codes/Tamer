package mipt.math.sys.alt.impl.grade;

import mipt.math.Number;
import mipt.math.array.DefaultVector;
import mipt.math.array.Vector;
import mipt.math.sys.alt.GradeProblem.Delimiter;

/**
 * Limits are uniformely distributed through scales interval.
 * Assumes GradeProblem.CountDelimiter but other delimiters do not result
 *  in absence of groups in the solution (because each delimiter has getGradesCount() method). 
 * @author Evdokimov
 */
public class UniformLimitsGradingMethod extends LimitsGradingMethod {

    /**
	 * @see mipt.math.sys.alt.impl.grade.LimitsGradingMethod#getScoreLimits(mipt.math.sys.alt.GradeProblem.Delimiter, mipt.math.Number[])
	 */
    public Vector getScoreLimits(Delimiter delimiter, Object allScores) {
        Number minMax[] = getMinMaxScore(allScores), limit = minMax[1].copy();
        int nLimits = delimiter.getGradesCount() - 1;
        Number step = limit.copy().minus(minMax[0]);
        step = step.mult(-1. / (nLimits + 1));
        Vector result = new DefaultVector(nLimits);
        for (int i = 0; i < nLimits; i++) {
            limit = limit.add(step);
            result.set(i, limit.copy());
        }
        return result;
    }
}
