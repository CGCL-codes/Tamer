package net.sf.orcc.df.transformations;

import net.sf.orcc.df.Argument;
import net.sf.orcc.df.Instance;
import net.sf.orcc.df.Network;
import net.sf.orcc.df.util.DfSwitch;
import net.sf.orcc.ir.Var;
import net.sf.orcc.ir.util.ExpressionEvaluator;
import net.sf.orcc.ir.util.ValueUtil;

/**
 * This class defines a transformation that evaluates all the arguments of all
 * instances of a network.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class ArgumentEvaluator extends DfSwitch<Void> {

    @Override
    public Void caseInstance(Instance instance) {
        for (Argument argument : instance.getArguments()) {
            Var var = argument.getVariable();
            Object value = new ExpressionEvaluator().doSwitch(argument.getValue());
            var.setValue(new ExpressionEvaluator().doSwitch(argument.getValue()));
            argument.setValue(ValueUtil.getExpression(value));
        }
        doSwitch(instance.getEntity());
        return null;
    }

    @Override
    public Void caseNetwork(Network network) {
        for (Var var : network.getVariables()) {
            var.setValue(new ExpressionEvaluator().doSwitch(var.getInitialValue()));
        }
        for (Instance instance : network.getInstances()) {
            doSwitch(instance);
        }
        return null;
    }
}
