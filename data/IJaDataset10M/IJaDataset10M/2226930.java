package org.sablecc.objectmacro.codegeneration.scala.macro;

public class MParamRef {

    private final String pName;

    private final String pContext;

    private final MParamRef mParamRef = this;

    public MParamRef(String pName, String pContext) {
        if (pName == null) {
            throw new NullPointerException();
        }
        this.pName = pName;
        if (pContext == null) {
            throw new NullPointerException();
        }
        this.pContext = pContext;
    }

    String pName() {
        return this.pName;
    }

    String pContext() {
        return this.pContext;
    }

    private String rName() {
        return this.mParamRef.pName();
    }

    private String rContext() {
        return this.mParamRef.pContext();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  private def r");
        sb.append(rName());
        sb.append("() = {");
        sb.append(System.getProperty("line.separator"));
        sb.append("    m");
        sb.append(rContext());
        sb.append(" p");
        sb.append(rName());
        sb.append(System.getProperty("line.separator"));
        sb.append("  }");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
