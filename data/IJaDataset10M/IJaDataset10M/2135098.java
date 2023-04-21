package systemStates;

import systemStates.types.OCLExpression;
import systemStates.types.Variable;

/**
 * DOCUMENT ME!
 * 
 * @author hwaters To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
class RuleSetLocalVarProcessNode extends SetLocalVarProcessNode {

    protected RuleSetLocalVarProcessNode(SystemState systemState) {
        super(systemState);
    }

    public void setValue(Variable var) {
        value = var;
        setAGGVar("value", var.getVariableName());
    }

    public void setLocalVarName(Variable var) {
        localVarName = var;
        setAGGVar("localVarName", var.getVariableName());
    }

    public void setType(Variable var) {
        type = var;
        setAGGVar("type", var.getVariableName());
    }

    public void setValue(OCLExpression expr) {
        value = expr;
        this.setAGGAttribute("value", "OCL:" + expr.getExpression());
    }

    public void setStatus(OCLExpression expr) {
        status = expr;
        setAGGAttribute("status", "OCL:" + expr.getExpression());
    }
}
