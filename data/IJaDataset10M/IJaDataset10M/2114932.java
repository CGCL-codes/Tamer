package vademecum.rules.abstractions;

import org.jdom.Element;

/**
 * This Class represents a basic Operand
 * It is extended/implemented by IValue and AttributeOperand.
 * This way the Rules are flexible, allowing Conditions
 * (attr,val) , (attr,attr) and (val,val).
 */
public abstract class ICompOperand {

    public ICompOperand() {
    }

    ;

    public ICompOperand(Element element) {
    }

    ;

    /**
	 * Returns the String Representation for this Operand
	 * @return The String Representation for this Operand
	 */
    public abstract String getOutput();

    /**
	 * Get the Type of this Operand
	 * @return The Type of this Operand
	 */
    public abstract Class getType();

    public abstract Element toElement(Element element);
}
