package tm.clc.ast;

import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Integer operations */
public class OpInt extends DefaultExpressionNode {

    int operator;

    /** Binary operations */
    public OpInt(TypeNode t, int operator, String operator_image, ExpressionNode left_operand, ExpressionNode right_operand) {
        super("OpInt", left_operand, right_operand);
        set_syntax(new String[] { "", " " + operator_image + " ", "" });
        this.operator = operator;
        set_type(t);
        set_selector(SelectorLeftToRight.construct());
        set_stepper(StepperOpInt2.construct());
        if (left_operand.is_integral_constant() && right_operand.is_integral_constant()) {
            int numberOfBytes = t.getNumBytes();
            long val = Arithmetic.do_arith(numberOfBytes, operator, left_operand.get_integral_constant_value(), right_operand.get_integral_constant_value());
            set_integral_constant_value(val);
        }
    }

    /** Unary operations */
    public OpInt(TypeNode t, int operator, String operator_image, ExpressionNode operand) {
        super("int op", operand);
        set_syntax(new String[] { operator_image, "" });
        this.operator = operator;
        set_type(t);
        set_selector(SelectorLeftToRight.construct());
        set_stepper(StepperOpInt1.construct());
        if (operand.is_integral_constant()) {
            int numberOfBytes = t.getNumBytes();
            long val = Arithmetic.do_arith(numberOfBytes, operator, operand.get_integral_constant_value());
            set_integral_constant_value(val);
        }
    }

    public String formatNodeData() {
        return super.formatNodeData() + " #" + operator;
    }

    ;
}

class StepperOpInt2 extends StepperBasic {

    private static StepperOpInt2 singleton;

    static StepperOpInt2 construct() {
        if (singleton == null) singleton = new StepperOpInt2();
        return singleton;
    }

    public AbstractDatum inner_step(ExpressionNode nd, VMState vms) {
        Object xd = vms.top().at(nd.child_exp(0));
        Assert.check(xd instanceof AbstractIntDatum);
        AbstractIntDatum xid = (AbstractIntDatum) xd;
        long x = xid.getValue();
        Object yd = vms.top().at(nd.child_exp(1));
        Assert.check(yd instanceof AbstractIntDatum);
        AbstractIntDatum yid = (AbstractIntDatum) yd;
        long y = yid.getValue();
        int operator = ((OpInt) nd).operator;
        int numberOfBytes = nd.get_type().getNumBytes();
        long value = Arithmetic.do_arith(numberOfBytes, operator, x, y);
        Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities");
        AbstractIntDatum d = (AbstractIntDatum) util.scratchDatum(nd.get_type(), vms);
        d.putValue(value);
        return d;
    }
}

class StepperOpInt1 extends StepperBasic {

    private static StepperOpInt1 singleton;

    static StepperOpInt1 construct() {
        if (singleton == null) singleton = new StepperOpInt1();
        return singleton;
    }

    public AbstractDatum inner_step(ExpressionNode nd, VMState vms) {
        Object xd = vms.top().at(nd.child_exp(0));
        Assert.check(xd instanceof AbstractIntDatum);
        AbstractIntDatum xid = (AbstractIntDatum) xd;
        long x = xid.getValue();
        int operator = ((OpInt) nd).operator;
        int numberOfBytes = nd.get_type().getNumBytes();
        long value = Arithmetic.do_arith(numberOfBytes, operator, x);
        Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities");
        AbstractIntDatum d = (AbstractIntDatum) util.scratchDatum(nd.get_type(), vms);
        d.putValue(value);
        return d;
    }
}
