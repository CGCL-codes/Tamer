package fr.upmf.animaths.client.mvp.MathML;

public class MMLOperator extends MMLGenericAtom {

    static String elementName = "mo";

    public MMLOperator(String n) {
        super(elementName, n);
    }

    public static MMLOperator equality() {
        return new MMLOperator("=");
    }

    public static MMLOperator times() {
        return new MMLOperator("×");
    }

    public static MMLOperator invisibleTimes() {
        return new MMLOperator("⋅");
    }

    public static MMLOperator lFence() {
        MMLOperator lFence = new MMLOperator("(");
        lFence.getElement().setAttribute("form", "prefix");
        lFence.getElement().setAttribute("fence", "true");
        lFence.getElement().setAttribute("stretchy", "true");
        lFence.getElement().setAttribute("symmetric", "true");
        lFence.getElement().setAttribute("lspace", "thinmathspace");
        return lFence;
    }

    public static MMLOperator rFence() {
        MMLOperator rFence = new MMLOperator(")");
        rFence.getElement().setAttribute("form", "postfix");
        rFence.getElement().setAttribute("fence", "true");
        rFence.getElement().setAttribute("stretchy", "true");
        rFence.getElement().setAttribute("symmetric", "true");
        rFence.getElement().setAttribute("rspace", "thinmathspace");
        return rFence;
    }

    @Override
    public MMLElement clone() {
        String text = getElement().getInnerText();
        if (text.equals("(")) return MMLOperator.lFence();
        if (text.equals(")")) return MMLOperator.rFence();
        return new MMLOperator(text);
    }
}
