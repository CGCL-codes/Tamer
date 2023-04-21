package systemStates;

import agg.xt_basis.TypeException;
import systemStates.exceptions.NotAllowedInHostException;
import systemStates.types.Variable;

/**
 * DOCUMENT ME!
 * 
 * @author hwaters To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AttributeNode extends Node {

    Object name;

    Object type;

    ClassNode ssClassNode;

    public AttributeNode(SystemState systemState) {
        super(systemState, systemState.systemStateGraGra.ntAttribute);
        this.name = null;
        this.type = null;
        ssClassNode = null;
    }

    public void setName(String name) {
        this.name = name;
        setAGGAttribute("name", name);
    }

    public void setName(Variable variable) throws NotAllowedInHostException {
        throw new NotAllowedInHostException();
    }

    public void setType(String type) {
        this.type = type;
        setAGGAttribute("type", type);
    }

    public void setClass(ClassNode clsNode) {
        ssClassNode = clsNode;
        try {
            systemState.aggGraph.createArc(systemState.systemStateGraGra.atDefault, aggNode, clsNode.aggNode);
        } catch (TypeException e) {
            e.printStackTrace();
        }
    }
}
