package com.oat.domains.bfo.problems.mahfoud;

import com.oat.Solution;
import com.oat.domains.bfo.BFOProblem;
import com.oat.domains.bfo.BFOSolution;
import com.oat.utils.ArrayUtils;
import com.oat.utils.BitStringUtils;

/**
 * Type: M9<br/>
 * Date: 05/12/2006<br/>
 * <br/>
 * Description: As described in: Niching Methods for Genetic Algorithms [PhD thesis] (1995)
 * 
 * Optima is 10
 * 
 * <br/>
 * @author Jason Brownlee
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class M9 extends BFOProblem {

    public static final int NUM_SUB_FUNC = 3;

    public static final int SUB_FUNC_LENGTH = 8;

    public static final boolean[][] SUB_FUNC_OPTIMA = { { false, false, false, false, false, false, false, false }, { true, false, false, false, true, true, false, false }, { false, true, false, false, true, false, true, false } };

    public M9() {
        length = NUM_SUB_FUNC * SUB_FUNC_LENGTH;
    }

    @Override
    protected double problemSpecificCost(Solution n) {
        BFOSolution s = (BFOSolution) n;
        boolean[] b = s.getBitString();
        double sum = 0.0;
        for (int i = 0; i < NUM_SUB_FUNC; i++) {
            sum += score(b, i * SUB_FUNC_LENGTH, SUB_FUNC_LENGTH);
        }
        return sum;
    }

    protected static final double score(boolean[] b, int start, int length) {
        double[] scores = new double[SUB_FUNC_OPTIMA.length];
        for (int i = 0; i < scores.length; i++) {
            scores[i] = BitStringUtils.hammingDistance(b, start, length, SUB_FUNC_OPTIMA[i], 0, SUB_FUNC_OPTIMA[i].length);
        }
        double min = ArrayUtils.min(scores);
        if (min == 0.0) {
            return 10.0;
        }
        return min;
    }

    @Override
    public String getName() {
        return "M9 (minimum distance)";
    }

    @Override
    public boolean isMinimization() {
        return false;
    }
}
