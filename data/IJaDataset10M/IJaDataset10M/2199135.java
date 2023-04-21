package org.happy.commons.patterns.version.formula.ver3x0;

import java.util.HashMap;
import org.happy.commons.patterns.version.Version_1x0;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.core.exception.ParsingException;

/**
 * this is adopted 2x0 version which wraps the functionality to the 3x0 version
 * 
 * @author Eugen Lofing, Andreas Hollmann, Wjatscheslaw Stoljarski
 * 
 */
public class MathSolver_2x0 implements Version_1x0<Float> {

    /**
	 * wrap method to that field
	 */
    MathSolver_3x0 mathSolver3x0 = new MathSolver_3x0();

    public double solvePower(double value, int power) {
        HashMap<String, Float> parameters = new HashMap<String, Float>();
        parameters.put("x", (float) value);
        String formula = "f(x)=x^2";
        double result = Double.NaN;
        try {
            result = mathSolver3x0.solveFormula(formula, parameters);
        } catch (ParsingException e) {
            e.printStackTrace();
            throw new InternalError("parsing error in expression4J");
        } catch (EvalException e) {
            e.printStackTrace();
            throw new InternalError("evaluation error in expression4J");
        }
        return result;
    }

    public Float getVersion() {
        return 2.0F;
    }
}
