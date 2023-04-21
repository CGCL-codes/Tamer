package org.ccpo.operators.objectives.constrainedtest;

import org.ccpo.common.api.FieldConstants;
import org.ccpo.common.api.IOperator;
import org.ccpo.common.api.annotations.*;

@IndividualOperator
public class FT_4 implements IOperator {

    @Gene(name = "gene 1", min = -10000, max = 10000, round = 6, type = FieldConstants.DOUBLE)
    private Double gene1;

    @Gene(name = "gene 2", min = -10000, max = 10000, round = 6, type = FieldConstants.DOUBLE)
    private Double gene2;

    @Gene(name = "gene 3", min = -10000, max = 10000, round = 6, type = FieldConstants.DOUBLE)
    private Double gene3;

    @Gene(name = "gene 4", min = -10000, max = 10000, round = 6, type = FieldConstants.DOUBLE)
    private Double gene4;

    @Gene(name = "gene 5", min = -10000, max = 10000, round = 6, type = FieldConstants.DOUBLE)
    private Double gene5;

    @Gene(name = "gene 6", min = -10000, max = 10000, round = 6, type = FieldConstants.DOUBLE)
    private Double gene6;

    @Gene(name = "gene 7", min = -10000, max = 10000, round = 6, type = FieldConstants.DOUBLE)
    private Double gene7;

    @Objective(name = "objective")
    private Double result;

    @Constraint(name = "constraint 1")
    private Double cons1;

    @Constraint(name = "constraint 2")
    private Double cons2;

    @Constraint(name = "constraint 3")
    private Double cons3;

    @Constraint(name = "constraint 4")
    private Double cons4;

    @GlobalField(name = "evals", method = "both")
    private Double evals;

    public void execute() {
        double[] x = new double[] { gene1, gene2, gene3, gene4, gene5, gene6, gene7 };
        double res[] = new double[5];
        res[0] = (x[0] - 10) * (x[0] - 10) + 5 * (x[1] - 12) * (x[1] - 12) + x[2] * x[2] * x[2] * x[2] + 3 * (x[3] - 11) * (x[3] - 11) + 10 * x[4] * x[4] * x[4] * x[4] * x[4] * x[4] + 7 * x[5] * x[5] + x[6] * x[6] * x[6] * x[6] - 4 * x[5] * x[6] - 10 * x[5] - 8 * x[6];
        res[1] = 2 * x[0] * x[0] + 3 * x[1] * x[1] * x[1] * x[1] + x[2] + 4 * x[3] * x[3] + 5 * x[4] - 127;
        res[2] = 7 * x[0] + 3 * x[1] + 10 * x[2] * x[2] + x[3] - x[4] - 282;
        res[3] = 23 * x[0] + x[1] * x[1] + 6 * x[5] * x[5] - 8 * x[6] - 196;
        res[4] = 4 * x[0] * x[0] + x[1] * x[1] - 3 * x[0] * x[1] + 2 * x[2] * x[2] + 5 * x[5] - 11 * x[6];
        for (int j = 1; j <= 4; j++) {
            if (res[j] < 0) res[j] = 0;
        }
        result = res[0] / 10025263;
        cons1 = res[1];
        cons2 = res[2];
        cons3 = res[3];
        cons4 = res[4];
        evals += 1;
    }
}
