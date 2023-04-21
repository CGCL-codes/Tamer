package jformulaengine.runtime.functions.operators;

import java.util.List;
import jformulaengine.runtime.Node;
import jformulaengine.runtime.Result;
import jformulaengine.runtime.functions.FunctionException;
import jformulaengine.runtime.functions.FunctionNode;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Add extends FunctionNode implements Node {

    protected Result execute(List args) throws FunctionException {
        if (args.size() != 2) {
            throw new FunctionException("Add requires two arguments");
        }
        Result lhs = (Result) args.get(0);
        Result rhs = (Result) args.get(1);
        if (lhs.type.equals("java.lang.Double") == false || rhs.type.equals("java.lang.Double") == false) {
            throw new FunctionException("Add requires two arguments of numeric type");
        }
        double dLeft = ((Double) lhs.obj).doubleValue();
        double dRight = ((Double) rhs.obj).doubleValue();
        return new Result(new Double(dLeft + dRight), "java.lang.Double");
    }
}
