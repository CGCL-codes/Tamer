package org.animaths.client.MathObject;

import org.animaths.client.MathML.MathMLElement;
import com.google.gwt.user.client.ui.ComplexPanel;

public abstract class MathObjectElement {

    public static final short MATH_OBJECT_WRAPPER = 0;

    public static final short MATH_OBJECT_EQUATION = 1;

    public static final short MATH_OBJECT_NUMBER = 2;

    public static final short MATH_OBJECT_IDENTIFIER = 3;

    public static final short MATH_OBJECT_SIGNED_ELEMENT = 4;

    public static final short MATH_OBJECT_ADDITION_CONTAINER = 5;

    public static final short MATH_OBJECT_MULTIPLY_ELEMENT = 6;

    public static final short MATH_OBJECT_MULTIPLY_CONTAINER = 7;

    public static final short MATH_OBJECT_POWER = 8;

    protected MathObjectElement mathObjectParent = null;

    protected MathMLElement mathMLParent = null;

    public abstract short getType();

    public MathObjectElement getMathObjectParent() {
        return mathObjectParent;
    }

    public MathMLElement getMathMLParent() {
        return mathMLParent;
    }

    public void setMathObjectParent(MathObjectElement mathObjectParent) {
        this.mathObjectParent = mathObjectParent;
    }

    public void setMathMLParent(MathMLElement mathMLParent) {
        this.mathMLParent = mathMLParent;
    }

    public boolean needsFence() {
        short typeP = mathObjectParent.getType();
        switch(this.getType()) {
            case MATH_OBJECT_SIGNED_ELEMENT:
                if (typeP == MATH_OBJECT_SIGNED_ELEMENT || typeP == MATH_OBJECT_MULTIPLY_ELEMENT || typeP == MATH_OBJECT_POWER) return true; else return false;
            case MATH_OBJECT_ADDITION_CONTAINER:
                if (typeP == MATH_OBJECT_SIGNED_ELEMENT || typeP == MATH_OBJECT_MULTIPLY_ELEMENT || typeP == MATH_OBJECT_POWER) return true; else return false;
            case MATH_OBJECT_MULTIPLY_CONTAINER:
                if (typeP == MATH_OBJECT_MULTIPLY_ELEMENT || typeP == MATH_OBJECT_POWER) return true; else return false;
            case MATH_OBJECT_POWER:
                if (typeP == MATH_OBJECT_POWER) return true; else return false;
            default:
                return false;
        }
    }

    public abstract void pack();
}
