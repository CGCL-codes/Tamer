package org.animaths.client.MathML;

public class MathMLFenced extends MathMLGenericOneChild {

    static String elementName = "mfenced";

    public MathMLFenced() {
        super(elementName);
        getElement().setAttribute("open", "(");
        getElement().setAttribute("close", ")");
        getElement().setAttribute("separators", "");
    }

    public MathMLFenced(MathMLElement child) {
        this();
        setChild(child);
    }
}
