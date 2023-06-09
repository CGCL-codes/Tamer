package org.sablecc.sablecc.codegeneration.java.macro;

import java.util.*;

public class MNormalGroup {

    private final List<Object> eNormalCondition_EndCondition = new LinkedList<Object>();

    MNormalGroup() {
    }

    public MNormalCondition newNormalCondition(String pAhead, String pTokenType) {
        MNormalCondition lNormalCondition = new MNormalCondition(pAhead, pTokenType);
        this.eNormalCondition_EndCondition.add(lNormalCondition);
        return lNormalCondition;
    }

    public MEndCondition newEndCondition(String pAhead) {
        MEndCondition lEndCondition = new MEndCondition(pAhead);
        this.eNormalCondition_EndCondition.add(lEndCondition);
        return lEndCondition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.eNormalCondition_EndCondition.size() > 1) {
            sb.append("(");
        }
        {
            boolean first = true;
            for (Object oNormalCondition_EndCondition : this.eNormalCondition_EndCondition) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" || ");
                }
                sb.append(oNormalCondition_EndCondition.toString());
            }
        }
        if (this.eNormalCondition_EndCondition.size() > 1) {
            sb.append(")");
        }
        return sb.toString();
    }
}
