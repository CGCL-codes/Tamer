package org.animaths.client.MathML;

public class MathMLNumber extends MathMLGenericAtom {

    static String elementName = "mn";

    public MathMLNumber(String n) {
        super(elementName, n);
    }

    public MathMLNumber(Number n) {
        super(elementName, n.toString());
    }
}
