package org.sablecc.sablecc.codegeneration.java.macro;

public class MEndParameter {

    MEndParameter() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("l$end");
        return sb.toString();
    }
}
