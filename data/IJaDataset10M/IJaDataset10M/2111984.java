package systemStates;

import org.tzi.use.uml.ocl.value.Value;
import systemStates.exceptions.NotAllowedInHostException;
import systemStates.types.OCLExpression;
import systemStates.types.Variable;
import agg.xt_basis.TypeException;

/**
 * DOCUMENT ME!
 * 
 * @author hwaters To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SetAttributeProcessNode extends ProcessNode {

    protected Object value;

    protected Object attributeName;

    ObjectNode owner;

    public SetAttributeProcessNode(SystemState systemState) {
        super(systemState, systemState.systemStateGraGra.ntSetAttributeProcess);
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 */
    public void setAttributeName(String string) {
        attributeName = string;
        setAGGAttribute("attributeName", string);
    }

    public void setAttributeName(Variable var) throws NotAllowedInHostException {
        throw new NotAllowedInHostException();
    }

    public void setValue(ObjectNode ssObjectNode) {
        value = ssObjectNode;
        try {
            systemState.aggGraph.createArc(systemState.systemStateGraGra.atDefault, this.aggNode, ssObjectNode.aggNode);
        } catch (TypeException e) {
            e.printStackTrace();
        }
    }

    protected void setAGGValue(Value value) {
        this.value = value;
        getVM("value").setExprAsObject(Synchronizer.extractAGGValue(value));
    }

    public void setValue(String value) {
        getVM("value").setExprAsObject(value);
    }

    public void setValue(OCLExpression expr) throws NotAllowedInHostException {
        throw new NotAllowedInHostException();
    }

    public void setValue(Variable varName) throws NotAllowedInHostException {
        throw new NotAllowedInHostException();
    }

    public void setOwner(ObjectNode owner) {
        this.owner = owner;
        try {
            systemState.aggGraph.createArc(systemState.systemStateGraGra.atDefault, aggNode, owner.aggNode);
        } catch (TypeException e) {
            e.printStackTrace();
        }
    }

    public void setStatus(OCLExpression expr) throws NotAllowedInHostException {
        throw new NotAllowedInHostException();
    }
}
