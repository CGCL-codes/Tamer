package tm.clc.ast;

import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Integer constants.
  Can also be used for chars (in C/C++) and bools (in C/C++).
*/
public class ConstInt extends DefaultExpressionNode {

    public ConstInt(TypeNode t, String image, long value) {
        super("ConstInt");
        set_type(t);
        set_syntax(new String[] { image });
        set_selector(SelectorAlways.construct());
        set_stepper(new StepperConstInt(value));
        set_integral_constant_value(value);
        setUninteresting(true);
    }
}

class StepperConstInt extends StepperBasic {

    private long value;

    StepperConstInt(long value) {
        this.value = value;
    }

    public AbstractDatum inner_step(ExpressionNode nd, VMState vms) {
        Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities");
        AbstractIntDatum d = (AbstractIntDatum) util.scratchDatum(nd.get_type(), vms);
        d.putValue(this.value);
        return d;
    }
}
