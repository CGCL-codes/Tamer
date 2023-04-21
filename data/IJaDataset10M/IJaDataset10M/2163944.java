package picoevo.gp.representation.mathoperators;

import java.util.ArrayList;
import picoevo.core.representation.Individual;
import picoevo.gp.representation.Element_Node_TreeGP;
import picoevo.gp.representation.Element_Node_TreeGP_Operator;
import picoevo.toolbox.Display;

public class Element_Node_TreeGP_Operator_sin extends Element_Node_TreeGP_Operator {

    public Element_Node_TreeGP_Operator_sin(Individual __individualOwner, ArrayList __elementLevelOperatorList) {
        super(__individualOwner, __elementLevelOperatorList);
        setArity(1);
        setDisplayValue("sin");
    }

    @Override
    public double evaluateDouble() {
        ArrayList list = this.getSuccessors();
        if (list.size() != 1) Display.error("" + getClass().getName() + "::evaluate() - incorrect number of successors (" + list.size() + ")");
        return (Math.sin(((Element_Node_TreeGP) list.get(0)).evaluateDouble()));
    }
}
