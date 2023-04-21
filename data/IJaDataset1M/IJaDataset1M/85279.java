package gatech.mmpm.tools.expressionparser;

import gatech.mmpm.ActionParameterType;

/**
 * Class that represents the access to an identifier,
 * that could be a parameter or a "global variable"
 * (<tt>player</tt> or <tt>cycle</tt>). 
 *  
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class IdentifierAccessNode extends ExpressionNode {

    public IdentifierAccessNode(String name, ActionParameterType type) {
        _name = name;
        _type = type;
    }

    /**
	 * Return the identifier datatype, as specified in the
	 * constructor.
	 */
    public ActionParameterType getType() {
        return _type;
    }

    /**
	 * Accept a visitor, and invokes the <tt>visit</tt>
	 * method on it depending on the current node type.
	 * 
	 * @param visitor Visitor to accept.
	 * @return Visitor returned value.
	 */
    public Object accept(ExpressionNodeVisitor visitor) {
        return visitor.visit(this);
    }

    public String getName() {
        return _name;
    }

    public String toString() {
        return _name;
    }

    protected String _name;

    protected ActionParameterType _type;
}
