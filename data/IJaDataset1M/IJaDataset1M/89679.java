package org.opt4j.benchmark.queens;

import static org.opt4j.core.Objective.RANK_ERROR;
import static org.opt4j.core.Objective.Sign.MIN;
import org.opt4j.core.Objective;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;
import org.opt4j.core.problem.Priority;
import com.google.inject.Inject;

/**
 * The evaluator for the {@link QueensProblem}.
 * 
 * @author lukasiewycz
 * 
 */
@Priority(RANK_ERROR)
public class QueensErrorEvaluator implements Evaluator<QueensBoard> {

    protected final QueensProblem problem;

    protected final Objective error = new Objective("error", MIN, RANK_ERROR);

    /**
	 * Constructs the evaluator for the {@link QueensProblem}.
	 * 
	 * @param problem
	 *            the problem to set
	 * 
	 */
    @Inject
    public QueensErrorEvaluator(QueensProblem problem) {
        this.problem = problem;
    }

    @Override
    public void evaluate(QueensBoard queensBoard, Objectives obj) {
        int errors = countErrors(queensBoard);
        obj.add(error, errors);
        if (errors > 0) {
            obj.setFeasible(false);
        }
    }

    /**
	 * Counts the number of errors for a given {@link QueensBoard}. That means
	 * the number of attacking queens.
	 * 
	 * @param queensBoard
	 *            the board
	 * @return the error count
	 */
    private int countErrors(QueensBoard queensBoard) {
        int errorcount = 0;
        int size = problem.size();
        for (int i = 0; i < size; i++) {
            int sum = 0;
            for (int j = 0; j < size; j++) {
                if (queensBoard.isQueen(i, j)) {
                    sum++;
                }
            }
            sum = Math.abs(1 - sum);
            errorcount += sum;
            sum = 0;
            for (int j = 0; j < size; j++) {
                if (queensBoard.isQueen(j, i)) {
                    sum++;
                }
            }
            sum = Math.abs(1 - sum);
            errorcount += sum;
        }
        for (int k = -size + 1; k < size; k++) {
            int sum = 0;
            for (int j = 0; j < size; j++) {
                int i = k + j;
                if (0 <= i && i < size && queensBoard.isQueen(j, i)) {
                    sum++;
                }
            }
            sum = Math.max(0, sum - 1);
            errorcount += sum;
        }
        for (int k = 0; k < 2 * size - 1; k++) {
            int sum = 0;
            for (int j = 0; j < size; j++) {
                int i = k - j;
                if (0 <= i && i < size && queensBoard.isQueen(j, i)) {
                    sum++;
                }
            }
            sum = Math.max(0, sum - 1);
            errorcount += sum;
        }
        return errorcount;
    }
}
