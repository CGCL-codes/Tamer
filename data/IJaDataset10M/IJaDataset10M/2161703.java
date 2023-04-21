package mySBML.primitiveTypes;

import mySBML.SBase;

public final class BooleanSbml extends TypeSbml {

    public BooleanSbml(SBase parent) {
        super(parent);
    }

    public BooleanSbml(SBase parent, String input) {
        super(parent, input);
    }

    public boolean equalTo(Boolean bool) {
        if (bool == null) return false;
        return bool.equals(getValue());
    }

    public Boolean getValue() {
        if (content.equalsIgnoreCase("true") || content.equals("1")) return true;
        if (content.equalsIgnoreCase("false") || content.equals("0")) return false;
        return null;
    }

    public boolean isValid() {
        return isEmpty() || content.equalsIgnoreCase("true") || content.equalsIgnoreCase("false") || content.equals("1") || content.equals("0");
    }
}
