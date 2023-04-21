package org.ccpo.operators.objectives.constrainedtest;

import org.ccpo.common.api.FieldConstants;
import org.ccpo.common.api.IOperator;
import org.ccpo.common.api.annotations.*;

@IndividualOperator
public class FT_6_Monoconstrained implements IOperator {

    @Gene(name = "gene 1", min = 0, max = 400, round = 4, type = FieldConstants.DOUBLE)
    private Double gene1;

    @Gene(name = "gene 2", min = 0, max = 1000, round = 4, type = FieldConstants.DOUBLE)
    private Double gene2;

    @Gene(name = "gene 3", min = 340, max = 420, round = 4, type = FieldConstants.DOUBLE)
    private Double gene3;

    @Gene(name = "gene 4", min = 340, max = 420, round = 6, type = FieldConstants.DOUBLE)
    private Double gene4;

    @Gene(name = "gene 5", min = -1000, max = 1000, round = 6, type = FieldConstants.DOUBLE)
    private Double gene5;

    @Gene(name = "gene 6", min = 0, max = 0.5236, round = 6, type = FieldConstants.DOUBLE)
    private Double gene6;

    @Objective(name = "objective")
    private Double result;

    @GlobalField(name = "evals", method = "both")
    private Double evals;

    public void execute() {
        double[] x = new double[] { gene1, gene2, gene3, gene4, gene5, gene6 };
        double res[] = new double[1];
        double precizia = 1e-4;
        double pen = 0;
        double penaltyCoef = 1.e20;
        double a = 131.078;
        double b = 1.48477;
        double c = 0.90798;
        double d = Math.cos(1.47588);
        double e = Math.sin(1.47588);
        double f1, f2;
        if ((x[0] >= 0) && (x[0] < 300)) f1 = 30 * x[0]; else f1 = 31 * x[0];
        if ((x[1] >= 0) && (x[1] < 100)) f2 = 28 * x[1]; else if ((x[1] >= 100) && (x[1] < 200)) f2 = 29 * x[1]; else f2 = 30 * x[1];
        res[0] = f1 + f2;
        res[0] = res[0] / 12400.;
        double g[] = new double[4];
        g[0] = Math.abs(x[0] + Math.cos(b - x[5]) * x[2] * x[3] / a - c * d * x[2] * x[2] / a - 300) / precizia - 1.;
        g[1] = Math.abs(x[1] + Math.cos(b + x[5]) * x[2] * x[3] / a - c * d * x[3] * x[3] / a) / precizia - 1.;
        g[2] = Math.abs(x[4] + Math.sin(b + x[5]) * x[2] * x[3] / a - c * e * x[3] * x[3] / a) / precizia - 1.;
        g[3] = Math.abs(Math.sin(b - x[5]) * x[2] * x[3] / a - c * e * x[2] * x[2] / a - 200) / precizia - 1.;
        for (int i = 0; i < 4; i++) {
            if (g[i] < 0.) g[i] = 0.;
            pen += g[i];
        }
        result = res[0] + penaltyCoef * pen;
        evals += 1;
    }
}
