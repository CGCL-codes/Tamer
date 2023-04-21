package org.animaths.client.MathML;

public abstract class MathMLGenericTwoChild extends MathMLElement {

    public MathMLGenericTwoChild(String elementName) {
        super(elementName);
    }

    public MathMLGenericTwoChild(String elementName, MathMLElement child1, MathMLElement child2) {
        super(elementName);
        setChildren(child1, child2);
    }

    public void setChildren(MathMLElement child1, MathMLElement child2) {
        clear();
        add(child1);
        add(child2);
    }
}
