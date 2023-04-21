package com.hp.hpl.jena.reasoner.rulesys.builtins;

import com.hp.hpl.jena.reasoner.rulesys.*;
import com.hp.hpl.jena.graph.*;

/**
 * Bind the second argument to 1+ the first argument. Just used for testing builtins.
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.8 $ on $Date: 2006/03/22 13:52:34 $
 */
public class AddOne extends BaseBuiltin {

    /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    public String getName() {
        return "addOne";
    }

    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    public int getArgLength() {
        return 2;
    }

    /**
     * This method is invoked when the builtin is called in a rule body.
     * @param args the array of argument values for the builtin, this is an array 
     * of Nodes, some of which may be Node_RuleVariables.
     * @param context an execution context giving access to other relevant data
     * @param length the length of the argument list, may be less than the length of the args array
     * for some rule engines
     * @return return true if the buildin predicate is deemed to have succeeded in
     * the current environment
     */
    public boolean bodyCall(Node[] args, int length, RuleContext context) {
        checkArgs(length, context);
        BindingEnvironment env = context.getEnv();
        boolean ok = false;
        Node a0 = getArg(0, args, context);
        Node a1 = getArg(1, args, context);
        if (Util.isNumeric(a0)) {
            Node newVal = Util.makeIntNode(Util.getIntValue(a0) + 1);
            ok = env.bind(args[1], newVal);
        } else if (Util.isNumeric(a1)) {
            Node newVal = Util.makeIntNode(Util.getIntValue(a1) - 1);
            ok = env.bind(args[0], newVal);
        }
        return ok;
    }
}
