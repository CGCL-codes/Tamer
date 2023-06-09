package de.uniwue.tm.textruler.lp2;

import java.util.ArrayList;
import java.util.List;
import org.apache.uima.cas.Type;
import de.uniwue.tm.textruler.core.TextRulerAnnotation;
import de.uniwue.tm.textruler.core.TextRulerRule;
import de.uniwue.tm.textruler.core.TextRulerRuleItem;
import de.uniwue.tm.textruler.core.TextRulerSingleSlotRule;
import de.uniwue.tm.textruler.core.TextRulerToolkit;
import de.uniwue.tm.textruler.core.TextRulerWordConstraint;
import de.uniwue.tm.textruler.core.TextRulerTarget.MLTargetType;

/**
 * 
 * @author Tobias Hermann
 * 
 */
public class LP2RuleItem implements TextRulerRuleItem {

    protected TextRulerWordConstraint wordConstraint;

    protected MLLP2ContextConstraint contextConstraint;

    protected List<MLLP2OtherConstraint> otherConstraints = new ArrayList<MLLP2OtherConstraint>();

    public static class MLLP2ContextConstraint {

        private String contextBoundaryName;

        private int contextSize;

        private boolean direction;

        public MLLP2ContextConstraint(int contextSize, LP2Rule parentRule) {
            super();
            this.contextSize = contextSize;
            contextBoundaryName = TextRulerToolkit.getTypeShortName(parentRule.getTarget().getCounterPartBoundaryTarget().getSingleSlotTypeName());
            direction = parentRule.getTarget().type == MLTargetType.SINGLE_LEFT_BOUNDARY ? true : false;
        }

        public MLLP2ContextConstraint(MLLP2ContextConstraint copyFrom) {
            super();
            contextBoundaryName = copyFrom.contextBoundaryName;
            contextSize = copyFrom.contextSize;
            direction = copyFrom.direction;
        }

        public MLLP2ContextConstraint copy() {
            return new MLLP2ContextConstraint(this);
        }

        @Override
        public String toString() {
            return "NEAR(" + contextBoundaryName + ", 0," + contextSize + "," + (direction ? "true" : "false") + ",true)";
        }

        @Override
        public boolean equals(Object o) {
            return toString().equals(((MLLP2ContextConstraint) o).toString());
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }
    }

    public static class MLLP2OtherConstraint {

        TextRulerAnnotation tokenAnnotation;

        TextRulerAnnotation constraintAnnotation;

        boolean canBeAnchor;

        Type type;

        public MLLP2OtherConstraint(TextRulerAnnotation tokenAnnotation, TextRulerAnnotation constraintAnnotation) {
            this.tokenAnnotation = tokenAnnotation;
            this.constraintAnnotation = constraintAnnotation;
            this.type = constraintAnnotation.getType();
            canBeAnchor = (tokenAnnotation.getBegin() == constraintAnnotation.getBegin()) && (tokenAnnotation.getEnd() == constraintAnnotation.getEnd());
        }

        public boolean isTMBasicTypeTokenConstraint() {
            return tokenAnnotation == constraintAnnotation;
        }

        public boolean canBeAnchorConstraint() {
            return canBeAnchor;
        }

        @Override
        public boolean equals(Object o) {
            MLLP2OtherConstraint co = (MLLP2OtherConstraint) o;
            return toString().equals(co.toString()) && (canBeAnchor == co.canBeAnchor);
        }

        @Override
        public int hashCode() {
            return toString().hashCode() * (canBeAnchor ? 2 : 1);
        }

        @Override
        public String toString() {
            return type.getShortName();
        }

        public MLLP2OtherConstraint copy() {
            return new MLLP2OtherConstraint(tokenAnnotation, constraintAnnotation);
        }
    }

    public LP2RuleItem(LP2RuleItem copyFrom) {
        super();
        if (copyFrom.wordConstraint != null) wordConstraint = copyFrom.wordConstraint.copy();
        if (copyFrom.contextConstraint != null) contextConstraint = copyFrom.contextConstraint.copy();
        for (MLLP2OtherConstraint c : copyFrom.otherConstraints) otherConstraints.add(c.copy());
    }

    public LP2RuleItem() {
        super();
    }

    public LP2RuleItem copy() {
        return new LP2RuleItem(this);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(TextRulerRuleItem o) {
        return toString().equals(((LP2RuleItem) o).toString());
    }

    @Override
    public String toString() {
        return getStringForRuleString(null, null, 0, 0, 0, 0, 0);
    }

    public MLLP2OtherConstraint getTMBasicTypeTokenConstraint() {
        for (MLLP2OtherConstraint c : otherConstraints) if (c.isTMBasicTypeTokenConstraint()) return c;
        return null;
    }

    public String getStringForRuleString(TextRulerRule rule, MLRuleItemType type, int numberInPattern, int patternSize, int numberInRule, int ruleSize, int slotIndex) {
        String result = "";
        LP2Rule lp2Rule = (LP2Rule) rule;
        boolean isMarkingItem = (rule != null) && (((rule.getTarget().type == MLTargetType.SINGLE_RIGHT_BOUNDARY) && (type == MLRuleItemType.PREFILLER) && (numberInPattern == patternSize - 1)) || ((rule.getTarget().type == MLTargetType.SINGLE_LEFT_BOUNDARY) && (type == MLRuleItemType.POSTFILLER) && (numberInPattern == 0)));
        ArrayList<String> constraints = new ArrayList<String>();
        String anchor = null;
        if (wordConstraint == null) anchor = "ANY"; else {
            if (wordConstraint.isRegExpConstraint()) {
                anchor = wordConstraint.typeShortName();
                constraints.add("REGEXP(\"" + wordConstraint + "\")");
            } else anchor = wordConstraint.toString();
        }
        if (isMarkingItem && lp2Rule.isContextualRule()) constraints.add("-IS(" + ((TextRulerSingleSlotRule) rule).getMarkName() + ")");
        if (contextConstraint != null) constraints.add(contextConstraint.toString());
        MLLP2OtherConstraint anchorConstraint = null;
        if (wordConstraint == null) {
            anchorConstraint = getTMBasicTypeTokenConstraint();
            if (anchorConstraint == null) {
                for (MLLP2OtherConstraint c : otherConstraints) if (c.canBeAnchorConstraint()) {
                    anchorConstraint = c;
                    break;
                }
            }
            for (MLLP2OtherConstraint oc : otherConstraints) {
                if (oc != anchorConstraint) {
                    if (oc.canBeAnchorConstraint()) constraints.add("IS(" + oc + ")"); else constraints.add("PARTOF(" + oc + ")");
                }
            }
            if (anchorConstraint != null) anchor = anchorConstraint.toString();
        }
        if (constraints.size() > 0) {
            String cStr = "";
            for (String constraintStr : constraints) {
                if (cStr.length() > 0) cStr += ", ";
                cStr += constraintStr;
            }
            result += "{" + cStr;
        }
        if ((rule != null) && (((rule.getTarget().type == MLTargetType.SINGLE_RIGHT_BOUNDARY) && (type == MLRuleItemType.PREFILLER) && (numberInPattern == patternSize - 1)) || ((rule.getTarget().type == MLTargetType.SINGLE_LEFT_BOUNDARY) && (type == MLRuleItemType.POSTFILLER) && (numberInPattern == 0)))) {
            if (constraints.size() == 0) result += "{";
            result += "->MARKONCE(" + ((TextRulerSingleSlotRule) rule).getMarkName() + ")";
            if (lp2Rule.isContextualRule()) result += ", ASSIGN(redoContextualRules, true)";
            result += "}";
        } else {
            if (constraints.size() != 0) result += "}";
        }
        return anchor + result;
    }

    public void addOtherConstraint(MLLP2OtherConstraint c) {
        if (!otherConstraints.contains(c)) otherConstraints.add(c);
    }

    public List<MLLP2OtherConstraint> getOtherConstraints() {
        return otherConstraints;
    }

    public void setWordConstraint(TextRulerAnnotation tokenAnnotation) {
        setWordConstraint(new TextRulerWordConstraint(tokenAnnotation));
    }

    public void setContextConstraint(MLLP2ContextConstraint c) {
        contextConstraint = c;
    }

    public MLLP2ContextConstraint getContextConstraint() {
        return contextConstraint;
    }

    public void setWordConstraint(TextRulerWordConstraint c) {
        wordConstraint = c;
    }

    public TextRulerWordConstraint getWordConstraint() {
        return wordConstraint;
    }

    public void removeConstraintWithName(String name) {
        otherConstraints.remove(name);
    }

    public int totalConstraintCount() {
        return otherConstraints.size() + (wordConstraint != null ? 1 : 0) + (contextConstraint != null ? 1 : 0);
    }
}
