package ec.app.tutorial4;

import ec.util.*;
import ec.*;
import ec.gp.*;
import ec.gp.koza.*;
import ec.simple.*;

public class MultiValuedRegression extends GPProblem implements SimpleProblemForm {

    public double currentX;

    public double currentY;

    public DoubleData input;

    public Object clone() {
        MultiValuedRegression newobj = (MultiValuedRegression) (super.clone());
        newobj.input = (DoubleData) (input.clone());
        return newobj;
    }

    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);
        input = (DoubleData) state.parameters.getInstanceForParameterEq(base.push(P_DATA), null, DoubleData.class);
        input.setup(state, base.push(P_DATA));
    }

    public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation, final int threadnum) {
        if (!ind.evaluated) {
            int hits = 0;
            double sum = 0.0;
            double expectedResult;
            double result;
            for (int y = 0; y < 10; y++) {
                currentX = state.random[threadnum].nextDouble();
                currentY = state.random[threadnum].nextDouble();
                expectedResult = currentX * currentX * currentY + currentX * currentY + currentY;
                ((GPIndividual) ind).trees[0].child.eval(state, threadnum, input, stack, ((GPIndividual) ind), this);
                result = Math.abs(expectedResult - input.x);
                if (result <= 0.01) hits++;
                sum += result;
            }
            KozaFitness f = ((KozaFitness) ind.fitness);
            f.setStandardizedFitness(state, (float) sum);
            f.hits = hits;
            ind.evaluated = true;
        }
    }
}
