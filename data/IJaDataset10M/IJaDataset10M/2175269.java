package org.renjin.sexp;

/**
 * This was the internal storage format for Strings in the
 * C-implementation; it doesn't appear that these are visible
 * from the R-language; if that's the case this may be removed
 * at some point.
 */
public class CHARSEXP extends AbstractSEXP {

    private String value;

    public static final String TYPE_NAME = "char";

    public CHARSEXP(String value) {
        this.value = value;
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public int length() {
        return value.length();
    }

    public String getValue() {
        return value;
    }

    @Override
    public void accept(SexpVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return value;
    }
}
