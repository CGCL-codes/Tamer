package muse.external.java.processor;

import java.util.Map;
import mipt.math.Number;
import muse.external.BridgeParameter;
import muse.external.ExternalException;
import muse.external.java.JavaModuleProcessor;
import muse.external.solver.SolverBuilder;

/**
 * Quotient calculation of 2 parameters value.
 *
 * @author Korchak Anton
 */
public class DivisionJavaModule implements JavaModuleProcessor {

    /**
	 * Do nothing.
	 * @see muse.external.java.JavaModuleProcessor#prepare(double)
	 */
    public void prepare(double timeStep) {
    }

    /**
	 * Do nothing.
	 * @see muse.external.java.JavaModuleProcessor#prepare(double, int)
	 */
    public void prepare(double timeStep, int blockID) {
    }

    /**
	 * Calculation of quotient between 2 parameter values.
	 * At first place of input-array should be ID of dividend and at second place
	 * should be ID of divisor. Resault is stored at fiest place in output-array. 
	 * @see muse.external.java.JavaModuleProcessor#processing(java.lang.Object[], java.lang.Object[], muse.external.solver.SolverBuilder)
	 */
    public void processing(Object[] input, Object[] output, SolverBuilder solverBuilder) throws ExternalException {
        if (input == null || input[0] == null || input[1] == null) return;
        if (output == null || output[0] == null) return;
        if (solverBuilder == null) return;
        Map parameterMap = solverBuilder.getParameterStorage().getParameterMap();
        Number dividend = ((BridgeParameter) parameterMap.get(input[0])).getValue();
        Number divisor = ((BridgeParameter) parameterMap.get(input[1])).getValue();
        Number differance = Number.divide(dividend, divisor);
        BridgeParameter resault = (BridgeParameter) parameterMap.get(output[0]);
        resault.setValue(differance);
    }
}
