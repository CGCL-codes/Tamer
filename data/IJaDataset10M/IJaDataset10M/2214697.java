package org.sablecc.objectmacro.codegeneration.java.macro;

public class MStringPart {

    private final String pString;

    private final MStringPart mStringPart = this;

    public MStringPart(String pString) {
        if (pString == null) {
            throw new NullPointerException();
        }
        this.pString = pString;
    }

    String pString() {
        return this.pString;
    }

    private String rString() {
        return this.mStringPart.pString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    sb.append(\"");
        sb.append(rString());
        sb.append("\");");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
