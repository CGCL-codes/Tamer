package org.ccpo.operators.evaluators;

import org.ccpo.common.api.FieldConstants;
import org.ccpo.common.api.IOperator;
import org.ccpo.common.api.annotations.EvoField;
import org.ccpo.common.api.annotations.IndividualCondition;
import org.ccpo.common.api.annotations.Layer;
import org.ccpo.common.api.annotations.PopulationOperator;
import java.util.List;

@PopulationOperator
@IndividualCondition(value = "score == 0")
public class ParetoFronts implements IOperator {

    @Layer(layer = FieldConstants.LAYER_OBJECTIVE)
    List<List<Double>> objectives;

    @EvoField(name = "front", type = FieldConstants.INTEGER, direction = FieldConstants.INPUTOUTPUT, iniType = FieldConstants.VALUE, defaultValue = "0")
    private List<Integer> front;

    public void execute() {
        int noi = objectives.get(0).size();
        int noo = objectives.size();
        double objectiv[][] = new double[noi + 2][noo + 2];
        int n = 1;
        int i;
        int j;
        int k;
        int c1;
        int c2;
        int c3;
        int c4;
        int[][] d = new int[noi + 2][noi + 2];
        for (i = 0; i < noi; i++) {
            for (int u = 1; u <= noo; u++) {
                objectiv[n][u] = objectives.get(u - 1).get(i);
            }
            objectiv[n][noo + 1] = i;
            n++;
        }
        int nrfes = n;
        for (i = 1; i <= nrfes - 1; i++) {
            for (j = i + 1; j <= nrfes - 1; j++) {
                c1 = 0;
                c2 = 0;
                c3 = 0;
                c4 = 0;
                for (k = 1; k <= noo; k++) {
                    if (objectiv[i][k] <= objectiv[j][k]) c1++; else c4++;
                    if (objectiv[i][k] < objectiv[j][k]) c2++; else c3++;
                }
                if ((c1 == noo) && (c2 != 0)) {
                    d[i][j] = 1;
                    d[j][i] = -1;
                }
                if ((c3 == noo) && (c4 != 0)) {
                    d[i][j] = -1;
                    d[j][i] = 1;
                }
            }
        }
        int clasifed = 0;
        int front = 1;
        c1 = 0;
        while (clasifed < nrfes - 1) {
            for (i = 1; i <= nrfes; i++) {
                if (d[i][0] == 0) {
                    for (j = 1; j <= nrfes; j++) {
                        c1 = 0;
                        try {
                            if (d[i][j] == -1) {
                                c1++;
                                break;
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            System.out.println("Place individual condition score=0 on pareto component");
                        }
                    }
                    if (c1 == 0) {
                        d[i][0] = front;
                        clasifed++;
                    }
                }
            }
            for (i = 1; i <= nrfes; i++) {
                if (d[i][0] == front) {
                    for (j = 1; j <= nrfes; j++) {
                        d[i][j] = 0;
                        d[j][i] = 0;
                    }
                }
            }
            front++;
        }
        this.front.clear();
        for (i = 1; i <= nrfes; i++) {
            if (d[i][0] == 0) d[i][0] = front + 100;
            for (i = 1; i <= nrfes; i++) {
                if (Double.valueOf(d[i][0]) != 0) this.front.add(Math.round(d[i][0])); else this.front.add(999);
            }
        }
    }
}
