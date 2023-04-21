package edu.mit.lcs.haystack.adenine.instructions;

import edu.mit.lcs.haystack.adenine.AdenineException;
import edu.mit.lcs.haystack.adenine.interpreter.ConstantTable;
import edu.mit.lcs.haystack.adenine.interpreter.DynamicEnvironment;
import edu.mit.lcs.haystack.adenine.interpreter.Environment;
import edu.mit.lcs.haystack.adenine.interpreter.IExpression;
import edu.mit.lcs.haystack.adenine.interpreter.IInstructionHandler;
import edu.mit.lcs.haystack.adenine.interpreter.Interpreter;
import edu.mit.lcs.haystack.adenine.interpreter.VariableFrame;
import edu.mit.lcs.haystack.rdf.Resource;

/**
 * @version 	1.0
 * @author		Dennis Quan
 */
public class BreakInstruction implements IInstructionHandler {

    /**
	 * @see IInstructionHandler#initialize(Interpreter)
	 */
    public void initialize(Interpreter interpreter) {
    }

    /**
	 * @see IInstructionHandler#isConstantExpression()
	 */
    public boolean isConstantExpression() {
        return false;
    }

    /**
	 * @see IInstructionHandler#evaluate(Resource, Environment, DynamicEnvironment)
	 */
    public Object evaluate(Resource res, Environment env, DynamicEnvironment denv) throws AdenineException {
        throw new BreakException();
    }

    /**
	 * @see IInstructionHandler#generateExpression(Resource)
	 */
    public IExpression generateExpression(Resource res) throws AdenineException {
        return new IExpression() {

            public void generateJava(String target, StringBuffer buffer, VariableFrame frame, ConstantTable ct) {
                buffer.append(target);
                buffer.append(" = null;\nbreak;\n");
            }

            public Object evaluate(Environment env, DynamicEnvironment denv) throws AdenineException {
                throw new BreakException();
            }
        };
    }
}
