package com.evaserver.rof.script;

/**
 * ist eine JavaScript-Zeichenkette.
 *
 * @author Max Antoni
 * @version $Revision: 161 $
 */
final class TString extends TType implements Comparable {

    static final TString EMPTY = new TString();

    private String value;

    /**
     * standard constructor.
     */
    TString() {
        this("");
    }

    /**
     * @param inValue the value.
     */
    TString(String inValue) {
        if (inValue == null) {
            throw new NullPointerException();
        }
        value = inValue;
    }

    public int compareTo(Object inoutO) {
        return value.compareTo(((TString) inoutO).value);
    }

    public boolean equals(Object inObject) {
        if (this == inObject) {
            return true;
        }
        if (inObject instanceof TString) {
            return ((TString) inObject).value.equals(value);
        }
        return false;
    }

    public int hashCode() {
        return value.hashCode();
    }

    public String toString() {
        return value;
    }

    TBoolean toJSBoolean() throws ScriptException {
        return TBoolean.valueOf(value.length() != 0);
    }

    TNumber toJSNumber(ExecutionContext inContext) throws ScriptException {
        if (value.length() == 0) {
            return new TNumber(0);
        }
        if (value.equals(TNumber.NaN_STRING)) {
            return new TNumber(Float.NaN);
        }
        if (value.equals(TNumber.POSITIVE_INFINITY_STRING)) {
            return new TNumber(Float.POSITIVE_INFINITY);
        }
        return TWindow.parseFloat(value);
    }

    TObject toJSObject() {
        return new TObjectString(this);
    }

    TType toJSPrimitive(Class inHint, ExecutionContext inContext) throws ScriptException {
        return this;
    }

    TString toJSString(ExecutionContext inContext) throws ScriptException {
        return this;
    }

    /**
     * converts this string to lower case.
     *
     * @return the lower case string.
     */
    TString toLowerCase() {
        return new TString(value.toLowerCase());
    }

    Object toObject(TWindow inWindow) {
        return value;
    }

    /**
     * converts this string to upper case.
     *
     * @return the upper case string.
     */
    TString toUpperCase() {
        return new TString(value.toUpperCase());
    }

    String typeof() {
        return TType.STRING;
    }

    String toDebugString() {
        return '"' + value + '"';
    }

    Expression toExpression() {
        return LiteralString.valueOf(value);
    }
}
